package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TwoOpt implements IOperator {

    private final IFeasibility feasibility;
    private final Random random;
    private final IDataSet dataSet;
    private final double[][] stopCostMatrix;
    private final double[][] travelDistance;
    private final double[] orderWeights;
    private final int[] locations;
    private final double[][][] kgCostMatrix;
    private final double[][][] kmCostMatrix;
    private final double[][][] fixCostMatrix;
    private final int[] orderPickupLocations;
    private final int[] orderDeliveryLocations;
    private final double[][] distanceIntervals;
    private final double[][] weightIntervals;
    private final int orderAmount;
    private String name;

    public TwoOpt(String name, Random random, IFeasibility feasibility, IDataSet dataSet){
        this.name =name;
        this.feasibility = feasibility;
        this.random = random;
        this.dataSet = dataSet;
        this.orderAmount = dataSet.getOrderAmount();
        this.stopCostMatrix = dataSet.getStopCosts();
        this.travelDistance = dataSet.getTravelDistances();
        this.orderWeights = dataSet.getOrderWeights();
        this.locations = dataSet.getLocations();
        this.kgCostMatrix = dataSet.getKgCostMatrices();
        this.kmCostMatrix = dataSet.getKmCostMatrices();
        this.fixCostMatrix = dataSet.getFixCostMatrices();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
        this.distanceIntervals = dataSet.getDistanceIntervals();
        this.weightIntervals = dataSet.getWeightIntervals();
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        List<Integer> vehicleSchedule = Arrays.asList(0);
        int vehicleChoice=0;
        int tries = 0;
        while(vehicleSchedule.get(0)==0){
            if(tries>100){
                return solution.clone();
            }
            vehicleChoice = random.nextInt(dataSet.getVehicleAmount());
            vehicleSchedule = findVehicleSchedule(vehicleChoice,solution);
            tries++;
        }

        return perform2optOnSchedule(vehicleChoice,vehicleSchedule,solution);
    }

    private List<Integer> findVehicleSchedule(int vehicleChoice, int[] solution) {
        List<Integer> result = new ArrayList<>();
        int vehicle=0;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement=solution[i];
            if (vehicle == vehicleChoice&&solutionElement!=0){
                result.add(solutionElement);
            }
            if (solutionElement==0){
                vehicle++;
            }
        }
        return (result.size()>2) ? result: Arrays.asList(0);
    }

    private int[] perform2optOnSchedule(int vehicleChoice, List<Integer> vehicleSchedule, int[] solution) {
        double vehicleCost = calculateVehicleCost(vehicleChoice, vehicleSchedule);
        double bestCost = vehicleCost;
        List<Integer> bestSchedule = vehicleSchedule;


        for (int i = 0; i < vehicleSchedule.size()-1; i++) {
            for (int j = i+1; j < vehicleSchedule.size(); j++) {
                List<Integer> newSchedule = twoOptSwap(i,j,vehicleSchedule);
                double newCost = bestCost;
                if(feasibility.checkSchedule(vehicleChoice,newSchedule)) {
                    newCost = calculateVehicleCost(vehicleChoice,newSchedule);
                }
                if (newCost<bestCost){
                    bestCost=newCost;
                    bestSchedule=newSchedule;
                }
            }
        }

        return bestCost<vehicleCost ? solution.clone():createNewSolution(vehicleChoice,bestSchedule,solution);
    }

    private List<Integer> twoOptSwap(int i, int j, List<Integer> vehicleSchedule) {
        List<Integer> result = new ArrayList<>();
        int idx = 0;
        while(idx<i){
            result.add(vehicleSchedule.get(idx++));
        }
        idx = j;
        while(idx>=i){
            result.add(vehicleSchedule.get(idx--));
        }
        idx=j+1;
        while(idx<vehicleSchedule.size()){
            result.add(vehicleSchedule.get(idx++));
        }
        return result;
    }

    private int[] createNewSolution(int vehicleChoice, List<Integer> bestSchedule, int[] solution) {
        int[] result = solution.clone();
        int vehicle = 0;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if (solutionElement==0){
                vehicle++;
                continue;
            }else if (vehicle==vehicleChoice){
                for (int j = 0; j < bestSchedule.size(); j++) {
                    result[i++] = bestSchedule.get(j);
                }
                break;
            }
        }
        return result;
    }

    private double calculateVehicleCost(int vehicleChoice, List<Integer> vehicleSchedule) {
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

    private double getVehicleCosts(int v, double vehicleTotalDistance, double vehicleMaxWeight) {
        int distanceDimension = findDimension(vehicleTotalDistance, distanceIntervals, v);
        int weightDimension = findDimension(vehicleMaxWeight, weightIntervals, v);
        double result = vehicleMaxWeight*kgCostMatrix[v][distanceDimension][weightDimension] + vehicleTotalDistance*kmCostMatrix[v][distanceDimension][weightDimension] +fixCostMatrix[v][distanceDimension][weightDimension];
        return result;
    }

    private int findDimension(double element, double[][] intervals, int vehicle) {
        double previousInterval = 0;
        for (int i = 0; i < intervals[vehicle].length; i++) {
            if (element <= intervals[vehicle][i] && element >= previousInterval) {
                return i;
            }
            previousInterval = intervals[vehicle][i];
        }
        return 0;
    }

    private double weightDifference(boolean pickedUp, double[] orderWeights, int solutionElement) {
            return pickedUp ? -orderWeights[solutionElement-1]: orderWeights[solutionElement-1];
    }

    private double getStopCost(int previousStop, int solutionElementPosition, int v ) {
        return previousStop!=solutionElementPosition ? stopCostMatrix[v][solutionElementPosition-1]:0.0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Classical 2-opt operator that picks a random vehicle and switches tries to find the best switch possible for this vehicle";
    }
}
