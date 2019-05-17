package functions;

import dataObjects.IDataSet;

public class ObjectiveFunction {
    private final int vehicles;
    private final IDataSet dataSet;
    private int[] solution;
    private final int orders;
    private final int stops;
    private final int[] vehicleStartingLocations;
    private final int[] vehicleDestinationLocations;
    private final double[][] stopCostMatrix;
    private final double[][] travelDistance;
    private final double[] orderWeights;
    private final int[] locations;
    private double[][][] kgCostMatrix;
    private double[][][] kmCostMatrix;
    private double[] orderPenalties;
    private final double[][][] fixCostMatrix;

    public ObjectiveFunction(IDataSet dataSet){
        this.dataSet = dataSet;
        this.vehicles = dataSet.getVehicleAmount();
        this.orders = dataSet.getOrderAmount();
        this.stops = dataSet.getStopAmount();
        this.vehicleStartingLocations = dataSet.getVehicleStartingLocations();
        this.vehicleDestinationLocations = dataSet.getVehicleDestinationLocations();
        this.stopCostMatrix = dataSet.getStopCosts();
        this.travelDistance = dataSet.getTravelDistances();
        this.orderWeights = dataSet.getOrderWeights();
        this.locations = dataSet.getLocations();
        this.kgCostMatrix = dataSet.getKgCostMatrices();
        this.kmCostMatrix = dataSet.getKmCostMatrices();
        this.orderPenalties = dataSet.getOrderPenalties();
        this.fixCostMatrix = dataSet.getFixCostMatrices();
    }


    public int calculateSolution(int[] solution) {
        this.solution = solution;
        return calculateSolution();
    }

    private int calculateSolution() {
        return getTravelCosts() + getPenaltyCosts();
    }

    private int getTravelCosts() {
        int result = 0;
        int i = 0;
        for (int v = 0; v < vehicles; v++) {
            int vehicleCost = 0;
            int vehicleTotalDistance = 0;
            int vehicleWeight = 0;
            int vehicleMaxWeight = 0;
            int vehiclePosition = vehicleStartingLocations[v];
            int solutionElement = solution[i];



            boolean[] visitedStop = new boolean[stops+1];
            while (solutionElement!=0) {
                vehicleCost += getStopCost(visitedStop, v, solutionElement);
                vehicleTotalDistance += travelDistance[vehiclePosition-1][solutionElement-1];
                vehicleWeight += weightDifference(orders, orderWeights, solutionElement);
                if(vehicleWeight > vehicleMaxWeight) {
                        vehicleMaxWeight = vehicleWeight;
                }
                vehiclePosition = solutionElement;
                i++;
                solutionElement = solution[i];
            }
            if(vehicleTotalDistance>0) {
                vehicleCost += getVehicleCosts(v, vehicleTotalDistance, vehicleMaxWeight);
            }
            result += vehicleCost;
            i++;
        }
        return result;
    }

    private double weightDifference(int orders, double[] orderWeights, int solutionElement) {
        if(solutionElement<=orders) {
            return orderWeights[solutionElement-1];
        }
        else{
            return -orderWeights[solutionElement-orders-1];
        }
    }

    private double getStopCost(boolean[] visitedStop, int v, int solutionElement) {
        double result=0;
        int currentStop= locations[solutionElement-1];
        if(!visitedStop[currentStop]){
            visitedStop = new boolean[stops+1];
            visitedStop[currentStop] = true;
            result = stopCostMatrix[v][solutionElement-1];
        }
        return result;
    }

    private double getVehicleCosts(int v, int vehicleTotalDistance, int vehicleMaxWeight) {
        int distanceDimension = determineDistanceDimension(vehicleTotalDistance, v);
        int weightDimension = determineWeightDimension(vehicleMaxWeight, v);
        double result = vehicleMaxWeight*kgCostMatrix[v][weightDimension][distanceDimension] + vehicleTotalDistance*kmCostMatrix[v][weightDimension][distanceDimension] +fixCostMatrix[v][weightDimension][distanceDimension];
        return result;
    }

    private int getPenaltyCosts() {
        int result = 0;
        int i = solution.length-1;
        int solutionElement = solution[i];
        while(solutionElement!=0){
            if (solutionElement<=dataSet.getOrderAmount()){
                result += orderPenalties[solutionElement-1];
            }
            i--;
            solutionElement = solution[i];
        }
        return result;
    }

    private int determineWeightDimension(int vehicleMaxWeight, int v) {
        int[][] weightIntervals = dataSet.getWeightIntervals();
        return findDimension(vehicleMaxWeight, weightIntervals, v);
    }

    private int determineDistanceDimension(int vehicleTotalDistance, int v) {
        int[][] distanceIntervals = dataSet.getDistanceIntervals();
        return findDimension(vehicleTotalDistance, distanceIntervals, v);
    }

    private int findDimension(int element, int[][] intervals, int vehicle) {
        for (int i = 0; i < intervals.length - 1; i++) {
            if (element >= intervals[vehicle][i] && element < intervals[vehicle][i + 1]) {
                return i;
            }
        }
        return 0;
    }
}
