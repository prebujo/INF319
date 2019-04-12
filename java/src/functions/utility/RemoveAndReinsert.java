package functions.utility;

import dataObjects.IData;
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

    public RemoveAndReinsert(IData dataSet, int seed, IFeasibility feasibilityCheck){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = feasibilityCheck;

        random = new Random(seed);

    }
    @Override
    public int[] apply(int[] solution) {
        int[] result;
        HashSet<Integer> elementsToRemove = getElementsToRemove(1,solution);

        result = insert(1,solution, elementsToRemove);

        if (feasibilityCheck.check(result)) {
            return result;
        } else {
            return solution;
        }
    }

    private int[] insert(int n, int[] solution, HashSet<Integer> elementsToRemove) {
        int[] result = new int[solution.length];

        //arranges which vehicle should recieve which order
        List<List<Integer>> orderAssignment = new ArrayList<>(vehicleAmount);

        for (int i = 0;i<orderAssignment.size();i++){
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
            if (elementsToRemove.contains(solutionElement)){
                continue;
            }
            if(solutionElement==0){
                resultIndex++;
                vehicle++;
                continue;
            }
            //TODO: Continue here and add all choices for each vehicle in lists, then select either new element or old element.
            int assignedElementsSize = orderAssignment.get(vehicle).size();
            if(assignedElementsSize>0) {
                List<Integer> possibilities = new ArrayList<>();
                possibilities.add(solutionElement);
                int lastAssignedOrder = removeRandomElement(orderAssignment, vehicle, assignedElementsSize);
                possibilities.add(lastAssignedOrder);
                while(possibilities.size()>0) {
                    int size = possibilities.size();
                    int choiceIndex = random.nextInt(size);
                    int elementChoice = possibilities.get(choiceIndex);
                    if (elementChoice<orderAmount){
                        possibilities.add(elementChoice+orderAmount);
                    }
                    int temp = possibilities.remove(size-1);
                    if (choiceIndex!=size-1){
                        possibilities.set(choiceIndex,temp);
                    }
                    if (orderAssignment.get(vehicle).size()>0&&elementChoice==lastAssignedOrder){
                        possibilities.add(removeRandomElement(orderAssignment, vehicle, assignedElementsSize));
                        lastAssignedOrder = elementChoice;
                    }
                    if (elementChoice==solutionElement){
                        i++;
                        solutionElement = solution[i];
                        if (solutionElement!=0){
                            possibilities.add(solutionElement);
                        }
                    }
                    result[resultIndex] = elementChoice;
                    resultIndex++;
                }
            } else {
                result[resultIndex] = solutionElement;
                resultIndex++;
            }
        }
    return result;
    }

    private int removeRandomElement(List<List<Integer>> orderAssignment, int vehicle, int assignedElementsSize) {
        int result;
        int j = random.nextInt(assignedElementsSize);
        result = orderAssignment.get(vehicle).get(j);
        int temp = orderAssignment.get(vehicle).remove(orderAssignment.get(vehicle).size()-1);
        if(j!=orderAssignment.get(vehicle).size()){
            orderAssignment.get(vehicle).set(j,temp);
        }
        return result;
    }

    private HashSet<Integer> getElementsToRemove(int n, int[] solution) {
        HashSet<Integer> result = new HashSet<>(n);

        int vehicle = 0;
        List<Integer> possibilities = new ArrayList<>();
        for (int i = 0; i<solution.length;i++){
            if(vehicle>vehicleAmount&&possibilities.size()>=n){
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
