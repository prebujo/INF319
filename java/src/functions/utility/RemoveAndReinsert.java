package functions.utility;

import dataObjects.IDataSet;
import dataObjects.ScheduleAndCost;
import functions.feasibility.IFeasibility;

import java.util.*;

/**
 * Operator that removes randomly and tries to reinsert given amount of orders in a given solution.The removal operator selects random orders to getElementsToRemove
 * and the insertion operator reinserts the removed orders into a randomly selected vehicle if possible.
 */
public class RemoveAndReinsert implements IOperator {

    protected String name;
    protected String description = "remove and reinsert operator that removes between 1-1 random elements from solution and reinserts them in randomly selected vehicles";
    protected Random random;
    protected final IFeasibility feasibilityCheck;
    protected final int vehicleAmount;
    protected final int orderAmount;
    private final int[] locations;
    private final double[][][] kgCostMatrix;
    private final double[][][] kmCostMatrix;
    private final double[][][] fixCostMatrix;
    private final double[][] distanceIntervals;
    private final double[][] weightIntervals;
    protected int lowerLimit;
    protected int upperLimit;
    protected final double[] orderWeights;
    protected final int[] orderPickupLocations;
    protected final int[] orderDeliveryLocations;
    protected final double[][] travelDistance;
    protected final double[][] stopCosts;
    private Throwable IllegalArgumentException;

    public RemoveAndReinsert(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet){
        this.name = name;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.random = random;
        this.feasibilityCheck = feasibilityCheck;
        this.orderAmount = dataSet.getOrderAmount();
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderWeights = dataSet.getOrderWeights();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
        this.travelDistance=dataSet.getTravelDistances();
        this.locations = dataSet.getLocations();
        this.kgCostMatrix = dataSet.getKgCostMatrices();
        this.kmCostMatrix = dataSet.getKmCostMatrices();
        this.fixCostMatrix = dataSet.getFixCostMatrices();
        this.distanceIntervals = dataSet.getDistanceIntervals();
        this.weightIntervals = dataSet.getWeightIntervals();
        this.stopCosts = dataSet.getStopCosts();
        this.IllegalArgumentException = new IllegalArgumentException("Trying to reinsert more orders than contained in solution..");
    }
    @Override
    public int[] apply(int[] solution) throws Throwable {
        int[] result;
        int amountOfElements = lowerLimit + random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> elementsToRemove = getElementsToRemove(amountOfElements,solution);

        result = reinsert(solution, elementsToRemove);

        if (feasibilityCheck.check(result)) {
            return result;
        } else {
            return solution.clone();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    private int[] reinsert(int[] solution, HashSet<Integer> elementsToRemove) {
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
        if (feasibilityCheck.check(result)) {
            return result;
        } else {
            return solution;
        }
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

    protected HashSet<Integer> getElementsToRemove(int amountOfElements, int[] solution) throws Throwable {
        if(amountOfElements>(solution.length-vehicleAmount)/2){
            throw (Throwable) IllegalArgumentException;
        }

        List<Integer> possibilities = getPossibilities(amountOfElements, solution);

        return pickRandomElements(amountOfElements, possibilities);
    }

    protected List<Integer> getPossibilities(int amountOfElements, int[] solution) {
        int vehicle = 0;
        List<Integer> possibilities = new ArrayList<>();

        for (int i = 0; i<solution.length;i++){
            if(vehicle>=vehicleAmount&&possibilities.size()>=amountOfElements){
                break;
            }
            int solutionElement = solution[i];
            if (solutionElement == 0){
                vehicle++;
                continue;
            }
            if (!possibilities.contains(solutionElement)){
                possibilities.add(solutionElement);
            }
        }
        return possibilities;
    }

    private HashSet<Integer> pickRandomElements(int amountOfElements, List<Integer> possibilities) {
        HashSet<Integer> result = new HashSet<>(amountOfElements);
        Integer temp;
        for (int i = 0; i<amountOfElements;i++){
            int choiceIndex = random.nextInt(possibilities.size());
            result.add(possibilities.get(choiceIndex));
            temp = possibilities.remove(possibilities.size() - 1);
            if(choiceIndex!=possibilities.size()) {
                possibilities.set(choiceIndex, temp);
            }
        }
        return result;
    }

    protected double calculateVehicleCost(int vehicleChoice, List<Integer> vehicleSchedule) {
        if (vehicleSchedule.size()==0){
            return 0.0;
        }
        double result = 0.0;
        double vehicleTotalDistance = 0.0;
        double vehicleWeight = 0.0;
        double vehicleMaxWeight = 0.0;
        int vehicleLocation = 0;
        boolean[] pickedUp = new boolean[orderAmount];
        for (int i = 0; i < vehicleSchedule.size(); i++) {
            int scheduleElement = vehicleSchedule.get(i);
            vehicleWeight += weightDifference(pickedUp[scheduleElement-1],orderWeights, scheduleElement);
            if(vehicleWeight > vehicleMaxWeight) {
                vehicleMaxWeight = vehicleWeight;
            }
            int solutionElementLocation;
            if(!pickedUp[scheduleElement-1]) {
                solutionElementLocation = orderPickupLocations[scheduleElement-1];
                pickedUp[scheduleElement-1] = true;
            } else {
                solutionElementLocation = orderDeliveryLocations[scheduleElement-1];
            }
            if(vehicleLocation!=0) {
                vehicleTotalDistance += travelDistance[vehicleLocation - 1][solutionElementLocation - 1];
            }
            result += getStopCost(vehicleLocation,solutionElementLocation, vehicleChoice );
            vehicleLocation = solutionElementLocation;
        }
        result += getVehicleCosts(vehicleChoice, vehicleTotalDistance, vehicleMaxWeight);
        return result;

    }

    protected double getVehicleCosts(int v, double vehicleTotalDistance, double vehicleMaxWeight) {
        int distanceDimension = findDimension(vehicleTotalDistance, distanceIntervals, v);
        int weightDimension = findDimension(vehicleMaxWeight, weightIntervals, v);
        double result = vehicleMaxWeight*kgCostMatrix[v][distanceDimension][weightDimension] + vehicleTotalDistance*kmCostMatrix[v][distanceDimension][weightDimension] +fixCostMatrix[v][distanceDimension][weightDimension];
        return result;
    }

    protected int findDimension(double element, double[][] intervals, int vehicle) {
        double previousInterval = 0;
        for (int i = 0; i < intervals[vehicle].length; i++) {
            if (element <= intervals[vehicle][i] && element >= previousInterval) {
                return i;
            }
            previousInterval = intervals[vehicle][i];
        }
        return 0;
    }

    protected double weightDifference(boolean pickedUp, double[] orderWeights, int solutionElement) {
        return pickedUp ? -orderWeights[solutionElement-1]: orderWeights[solutionElement-1];
    }

    protected double getStopCost(int previousStop, int solutionElementPosition, int v ) {
        return previousStop!=solutionElementPosition ? stopCosts[v][solutionElementPosition-1]:0.0;
    }

    protected Double calculateVehicleCostWithoutOrder(Integer order, int vehicleChoice, List<Integer> vehicleSchedule) {
        if (vehicleSchedule.size()==0){
            return 0.0;
        }
        double result = 0.0;
        double vehicleTotalDistance = 0.0;
        double vehicleWeight = 0.0;
        double vehicleMaxWeight = 0.0;
        int vehicleLocation = 0;
        boolean[] pickedUp = new boolean[orderAmount];
        for (int i = 0; i < vehicleSchedule.size(); i++) {
            int scheduleElement = vehicleSchedule.get(i);
            if (scheduleElement==order){
                continue;
            }
            vehicleWeight += weightDifference(pickedUp[scheduleElement-1],orderWeights, scheduleElement);
            if(vehicleWeight > vehicleMaxWeight) {
                vehicleMaxWeight = vehicleWeight;
            }
            int solutionElementLocation;
            if(!pickedUp[scheduleElement-1]) {
                solutionElementLocation = orderPickupLocations[scheduleElement-1];
                pickedUp[scheduleElement-1] = true;
            } else {
                solutionElementLocation = orderDeliveryLocations[scheduleElement-1];
            }
            if(vehicleLocation!=0) {
                vehicleTotalDistance += travelDistance[vehicleLocation - 1][solutionElementLocation - 1];
            }
            result += getStopCost(vehicleLocation,solutionElementLocation, vehicleChoice );
            vehicleLocation = solutionElementLocation;
        }
        result += getVehicleCosts(vehicleChoice, vehicleTotalDistance, vehicleMaxWeight);
        return result;
    }

    protected List<List<Integer>> getVehicleSchedules(int[] solution) {
        List<List<Integer>> result = new ArrayList<>();
        int vehicle = 0;
        List<Integer> schedule = new ArrayList<>();
        for (int i = 0; i < solution.length; i++) {
            if (vehicle==vehicleAmount){
                break;
            }
            int solutionElement = solution[i];
            if (solutionElement==0){
                result.add(schedule);
                vehicle++;
                schedule=new ArrayList<>();
            } else{
                schedule.add(solutionElement);
            }
        }
        return result;
    }

    protected int[] removeOrdersFromSolution(HashSet<Integer> ordersToRemove, int[] solution) {
        int[] result = new int[solution.length];
        int resultIdx = 0;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if(ordersToRemove.contains(solutionElement)) {
                continue;
            }else{
                result[resultIdx++]=solutionElement;
            }
        }
        Iterator<Integer> iterator = ordersToRemove.iterator();
        while(iterator.hasNext()){
            int nextOrder = iterator.next();
            result[resultIdx++]=nextOrder;
            result[resultIdx++]=nextOrder;
        }
        return result;
    }

    protected int[] createNewSolution(ScheduleAndCost scheduleAndCost, int[] solution) {
        int[] result = new int[solution.length];
        int vehicle = 0;
        int resultIdx = 0;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if (solutionElement==0){
                vehicle++;
                resultIdx++;
                continue;
            }else if (vehicle==scheduleAndCost.vehicle){
                List<Integer> schedule = scheduleAndCost.schedule;
                for (int j = 0; j < schedule.size(); j++) {
                    result[resultIdx++] = schedule.get(j);
                }
                while (solution[i]!=0){
                    i++;
                }
                vehicle++;
                resultIdx++;
            } else if(solutionElement!=scheduleAndCost.order){
                result[resultIdx++]=solutionElement;
            }
        }
        return result;
    }
}
