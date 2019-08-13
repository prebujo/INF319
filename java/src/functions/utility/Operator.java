package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleOrderCostSchedule;
import dataObjects.VehicleAndSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;

public class Operator implements IOperator {

    protected String name;
    protected String description = "super class for all Operators not working standalone as operator";
    protected Random random;
    protected final IFeasibility feasibility;
    protected final int vehicleAmount;
    protected final int orderAmount;
    protected final double[][][] kgCostMatrix;
    protected final double[][][] kmCostMatrix;
    protected final double[][][] fixCostMatrix;
    protected final double[][] distanceIntervals;
    protected final double[][] weightIntervals;
    protected final double[] orderWeights;
    protected final int[] orderPickupLocations;
    protected final int[] orderDeliveryLocations;
    protected final double[][] travelDistance;
    protected final double[][] stopCosts;
    private final double[] orderPenalties;

    public Operator(String name, Random random, IFeasibility feasibility, IDataSet dataSet){
        this.name = name;
        this.random = random;
        this.feasibility = feasibility;
        this.orderAmount = dataSet.getOrderAmount();
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderWeights = dataSet.getOrderWeights();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
        this.travelDistance=dataSet.getTravelDistances();
        this.kgCostMatrix = dataSet.getKgCostMatrices();
        this.kmCostMatrix = dataSet.getKmCostMatrices();
        this.fixCostMatrix = dataSet.getFixCostMatrices();
        this.distanceIntervals = dataSet.getDistanceIntervals();
        this.weightIntervals = dataSet.getWeightIntervals();
        this.stopCosts = dataSet.getStopCosts();
        this.orderPenalties = dataSet.getOrderPenalties();
    }
    @Override
    public int[] apply(int[] solution) throws Throwable {
        return new int[0];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    protected double calculateVehicleCost(int vehicleChoice, List<Integer> vehicleSchedule) {
        if (vehicleSchedule.size()==0){
            return 0.0;
        } else if (vehicleChoice==vehicleAmount){
            return getPenaltyCostForOrdersInSchedule(vehicleSchedule);
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

    private double getPenaltyCostForOrdersInSchedule(List<Integer> vehicleSchedule) {
        double result = 0.0;
        boolean[] calculated = new boolean[orderAmount+1];
        for (Integer order :
                vehicleSchedule) {
            if (!calculated[order]) {
                result += orderPenalties[order-1];
                calculated[order]=true;
            }
        }
        return result;
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
        boolean usedVehicle = false;
        for (int i = 0; i < vehicleSchedule.size(); i++) {
            int scheduleElement = vehicleSchedule.get(i);
            if (scheduleElement==order){
                continue;
            }
            usedVehicle=true;
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
        if (usedVehicle) {
            result += getVehicleCosts(vehicleChoice, vehicleTotalDistance, vehicleMaxWeight);
        }
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

    protected List<Integer> getVehicleSchedule(int vehicleChoice, int[] solution) {
        List<Integer> result = new ArrayList<>();
        int vehicle=0;
        boolean done = false;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement=solution[i];
            if (vehicle == vehicleChoice&&solutionElement!=0){
                result.add(solutionElement);
                done = true;
            }else if (solutionElement==0){
                if (done){
                    break;
                }
                vehicle++;
            }
        }
        return result;
    }

    public List<List<Integer>> getVehicleSchedules(int[] solution) {
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



    protected int[] createNewSolution(int vehicleChoice, List<Integer> vehicleSchedule, int[] solution) {
        int[] result = solution.clone();
        int vehicle = 0;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if (solutionElement==0){
                vehicle++;
                continue;
            }else if (vehicle==vehicleChoice){
                for (int j = 0; j < vehicleSchedule.size(); j++) {
                    result[i++] = vehicleSchedule.get(j);
                }
                break;
            }
        }
        return result;
    }

    protected int[] createNewSolution(VehicleOrderCostSchedule scheduleAndCost, int[] solution) {
        int[] result = new int[solution.length];
        int vehicle = 0;
        int resultIdx = 0;
        for (int i = 0; i < solution.length; i++) {
            if (vehicle==scheduleAndCost.vehicle) {
                List<Integer> schedule = scheduleAndCost.schedule;
                for (int j = 0; j < schedule.size(); j++) {
                    result[resultIdx++] = schedule.get(j);
                }
                while (solution[i] != 0) {
                    i++;
                }
            }
            int solutionElement = solution[i];
            if (solutionElement==0){
                vehicle++;
                resultIdx++;
            } else if(solutionElement!=scheduleAndCost.order){
                result[resultIdx++]=solutionElement;
            }
        }
        return result;
    }

    protected List<Integer> createNewSchedule(Integer order, int i, int j, List<Integer> schedule) {
        List<Integer> result= new ArrayList<>();
        int scheduleIdx = 0;
        for (int k = 0; k < schedule.size()+2; k++) {
            if (k==i||k==j){
                result.add(order);
            }else {
                result.add(schedule.get(scheduleIdx++));
            }
        }
        return result;
    }

    protected List<Integer> getVehicleScheduleWithoutOrder(int vehicle, int order, List<List<Integer>> vehicleSchedules) {
        return getScheduleWithoutOrder(order,vehicleSchedules.get(vehicle));
    }

    protected List<Integer> getScheduleWithoutOrder(int order, List<Integer> schedule) {
        List<Integer> newSchedule = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            if(schedule.get(i)==order){
                continue;
            } else {
                newSchedule.add(schedule.get(i));
            }
        }
        return newSchedule;
    }

    protected int getVehicle(int order, List<List<Integer>> vehicleSchedules) {
        int vehicle = vehicleSchedules.size();
        for (int i = 0; i < vehicleSchedules.size(); i++) {
            List<Integer> schedule = vehicleSchedules.get(i);
            for (int j = 0; j < schedule.size(); j++) {
                if (schedule.get(j)==order){
                    return i;
                }
            }
        }
        return vehicle;
    }

    protected VehicleAndSchedule getVehicleScheduleOfOrder(int order, int[] solution) {
        List<Integer> schedule = new ArrayList<>();
        int vehicle = 0;
        boolean correctSchedule = false;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if(solutionElement==0){
                if (correctSchedule){
                    return new VehicleAndSchedule(vehicle,schedule);
                }
                schedule=new ArrayList<>();
                vehicle++;
            } else if (solutionElement==order){
                schedule.add(solutionElement);
                correctSchedule = true;
            } else{
                schedule.add(solutionElement);
            }
        }
        return new VehicleAndSchedule(vehicle,schedule);
    }
}
