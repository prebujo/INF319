package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.*;

/**
 * Operator that removes randomly and tries to reinsert given amount of orders in a given solution.The removal operator selects random orders to getElementsToRemove
 * and the insertion operator reinserts the removed orders into a randomly selected vehicle if possible.
 */
public class RemoveAndReinsert implements IOperator {

    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibilityCheck;
    private Random random;
    private Throwable IllegalArgumentException;
    private int lowerLimit;
    private int upperLimit;
    private String name;

    public RemoveAndReinsert(IDataSet dataSet, Random random, IFeasibility feasibilityCheck, int lowerLimit, int upperLimit, String name){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = feasibilityCheck;
        this.IllegalArgumentException = new IllegalArgumentException("Trying to reinsert more orders than contained in solution..");
        this.random = random;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.name = name;
    }
    @Override
    public int[] apply(int[] solution) throws Throwable {
        int[] result;
        int amount = lowerLimit + random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> elementsToRemove = getElementsToRemove(amount,solution);

        result = insert(solution, elementsToRemove);

        if (feasibilityCheck.check(result)) {
            return result;
        } else {
            return solution;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private int[] insert(int[] solution, HashSet<Integer> elementsToRemove) {
        int[] result = new int[solution.length];

        //arranges which vehicle should recieve which order
        List<List<Integer>> orderAssignment = new ArrayList<>(vehicleAmount);

        for (int i = 0;i<vehicleAmount+1;i++){
            orderAssignment.add(new ArrayList<>());
        }
        for(Integer element:elementsToRemove){
            int vehicleChoice = random.nextInt(vehicleAmount);
            orderAssignment.get(vehicleChoice).add(element);
        }


        int resultIndex = 0;
        int solutionElement;
        int vehicle = 0;

        for (int i = 0; i<solution.length;i++){
            solutionElement = solution[i];
            //TODO: Continue here and add all choices for each vehicle in lists, then select either new element or old element.
            int assignedElementsSize;
            if(vehicle<vehicleAmount){
                assignedElementsSize = orderAssignment.get(vehicle).size();
            } else{
                assignedElementsSize =0;
            }
            if(solutionElement==0&&assignedElementsSize==0){
                resultIndex++;
                vehicle++;
                continue;
            }
            if(assignedElementsSize>0) {
                List<Integer> possibilities = new ArrayList<>();
                if((!elementsToRemove.contains(solutionElement)&&!elementsToRemove.contains(solutionElement-orderAmount))&&solutionElement!=0) {
                    possibilities.add(solutionElement);
                }
                int lastAssignedOrder = removeRandomElement(orderAssignment, vehicle);
                possibilities.add(lastAssignedOrder);
                while(possibilities.size()>0) {
                    int size = possibilities.size();
                    int choiceIndex = random.nextInt(size);
                    int elementChoice = possibilities.get(choiceIndex);
                    //If I choose the solution element and solution element is not an element to remove
                    if (elementChoice==solutionElement){
                        i++;
                        solutionElement = solution[i];
                        while (elementsToRemove.contains(solutionElement)||elementsToRemove.contains(solutionElement-orderAmount)) {
                            i++;
                            solutionElement = solution[i];
                        }
                        if(solutionElement!=0){
                            possibilities.add(solutionElement);
                        }
                    }
                    //if I am picking up an assignedorder I have to be able to deliver it after
                    if (elementChoice<orderAmount&&elementsToRemove.contains(elementChoice)){
                        possibilities.add(elementChoice+orderAmount);
                    }
                    //removing chosen order from possibilities
                    int temp = possibilities.remove(possibilities.size()-1);
                    if (choiceIndex!=possibilities.size()){
                        possibilities.set(choiceIndex,temp);
                    }
                    //if I still have orders to assign and I chose an Assigned order as element to insert I have to add
                    //the next orderAssignment.
                    if (orderAssignment.get(vehicle).size()>0&&elementChoice==lastAssignedOrder){
                        possibilities.add(removeRandomElement(orderAssignment, vehicle));
                        lastAssignedOrder = elementChoice;
                    }
                    result[resultIndex] = elementChoice;
                    resultIndex++;
                }
                if(solutionElement==0) {
                    vehicle++;
                    resultIndex++;
                }
            } else if(!elementsToRemove.contains(solutionElement)&&!elementsToRemove.contains(solutionElement-orderAmount)){
                result[resultIndex] = solutionElement;
                resultIndex++;
            }
        }
    return result;
    }

    private int removeRandomElement(List<List<Integer>> orderAssignment, int vehicle) {
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

    private HashSet<Integer> getElementsToRemove(int n, int[] solution) throws Throwable {
        if(n>(solution.length-vehicleAmount)/2){
            throw (Throwable) IllegalArgumentException;
        }
        HashSet<Integer> result = new HashSet<>(n);

        int vehicle = 0;
        List<Integer> possibilities = new ArrayList<>();

        for (int i = 0; i<solution.length;i++){
            if(vehicle>=vehicleAmount&&possibilities.size()>=n){
                break;
            }
            int solutionElement = solution[i];
            if (solutionElement == 0){
                vehicle++;
                continue;
            }
            if (solutionElement<orderAmount){
                possibilities.add(solutionElement);
            }
        }

        Integer temp;
        for (int i = 0; i<n;i++){
            int choiceIndex = random.nextInt(possibilities.size());
            result.add(possibilities.get(choiceIndex));
            temp = possibilities.remove(possibilities.size() - 1);
            if(choiceIndex!=possibilities.size()) {
                possibilities.set(choiceIndex, temp);
            }
        }
        return result;
    }
}
