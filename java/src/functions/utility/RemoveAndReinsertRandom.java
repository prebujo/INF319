package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RemoveAndReinsertRandom extends RemoveAndReinsert {


    public RemoveAndReinsertRandom(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.description = "remove and reinsert operator that removes between "+lowerLimit+" to "+upperLimit+" random elements from solution and reinserts them in randomly selected vehicles";
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        int[] result;
        int amountOfElements = lowerLimit + random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> elementsToRemove = getElementsToRemove(amountOfElements,solution);

        result = reinsert(solution, elementsToRemove);

        if (feasibility.check(result)) {
            return result;
        } else {
            return solution.clone();
        }
    }
    protected HashSet<Integer> getElementsToRemove(int amountOfElements, int[] solution){
        HashSet<Integer> result = new HashSet<>(amountOfElements);

        while (amountOfElements>0){
            int choice = random.nextInt(orderAmount)+1;
            while(result.contains(choice)){
                choice = random.nextInt(orderAmount)+1;
            }
            result.add(choice);
            amountOfElements--;
        }

        return result;
    }
    protected int[] reinsert(int[] solution, HashSet<Integer> elementsToRemove) {
        int[] result = new int[solution.length];

        //arranges which vehicle should recieve which order
        List<List<Integer>> orderAssignment = new ArrayList<>(vehicleAmount);

        for (int i = 0; i < vehicleAmount + 1; i++) {
            orderAssignment.add(new ArrayList<>());
        }
        for (Integer element : elementsToRemove) {
            int vehicleChoice = random.nextInt(vehicleAmount);
            orderAssignment.get(vehicleChoice).add(element);
        }


        int resultIndex = 0;
        int solutionElement;
        int vehicle = 0;
        boolean[] pickedUp = new boolean[orderAmount + 1];

        for (int i = 0; i < solution.length; i++) {
            solutionElement = solution[i];
            if (pickedUp[solutionElement]) {
                continue;
            }
            int assignedElementsSize;
            if (vehicle < vehicleAmount) {
                assignedElementsSize = orderAssignment.get(vehicle).size();
            } else {
                assignedElementsSize = 0;
            }
            if (solutionElement == 0 && assignedElementsSize == 0) {
                resultIndex++;
                vehicle++;
                continue;
            }
            if (assignedElementsSize > 0) {
                List<Integer> possibilities = new ArrayList<>();
                if ((!elementsToRemove.contains(solutionElement)) && solutionElement != 0) {
                    possibilities.add(solutionElement);
                }
                int lastAssignedOrder = removeRandomElement(orderAssignment, vehicle);
                possibilities.add(lastAssignedOrder);
                while (possibilities.size() > 0) {
                    int choiceIndex = random.nextInt(possibilities.size());
                    int elementChoice = possibilities.get(choiceIndex);
                    //If I choose the solution element and solution element is not an element to remove
                    if (elementChoice == solutionElement) {
                        i++;
                        solutionElement = solution[i];
                        while (elementsToRemove.contains(solutionElement)) {
                            i++;
                            solutionElement = solution[i];
                        }
                        if (solutionElement != 0 && !pickedUp[solutionElement]) {
                            if (!possibilities.contains(solutionElement)) {
                                possibilities.add(solutionElement);
                            }
                        }
                    }
                    //if I have already picked up the chosen element I remove it as an option
                    if (pickedUp[elementChoice]) {
                        int temp = possibilities.remove(possibilities.size() - 1);
                        if (choiceIndex != possibilities.size()) {
                            possibilities.set(choiceIndex, temp);
                        }
                    } else {
                        pickedUp[elementChoice] = true;
                    }
                    //if I still have orders to assign and I chose an Assigned order as element to reinsert I have to add
                    //the next orderAssignment.
                    if (orderAssignment.get(vehicle).size() > 0 && elementChoice == lastAssignedOrder) {
                        possibilities.add(removeRandomElement(orderAssignment, vehicle));
                        lastAssignedOrder = elementChoice;
                    }
                    result[resultIndex] = elementChoice;
                    resultIndex++;
                }
                if (solutionElement == 0) {
                    vehicle++;
                    resultIndex++;
                }
            } else if (!elementsToRemove.contains(solutionElement)) {
                result[resultIndex] = solutionElement;
                resultIndex++;
            }
        }
        if (feasibility.check(result)) {
            return result;
        } else {
            return solution;
        }
    }
    protected int removeRandomElement(List<List<Integer>> orderAssignment, int vehicle) {
        int result;
        int assignedElementsSize = orderAssignment.get(vehicle).size();
        int j = random.nextInt(assignedElementsSize);
        result = orderAssignment.get(vehicle).get(j);
        int temp = orderAssignment.get(vehicle).remove(orderAssignment.get(vehicle).size()-1);
        if(j!=orderAssignment.get(vehicle).size()){
            orderAssignment.get(vehicle).set(j,temp);
        }
        return result;
    }
}
