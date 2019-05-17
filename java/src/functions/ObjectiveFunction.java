package functions;

import dataObjects.IDataSet;

public class ObjectiveFunction {
    private final int vehicles;
    private final IDataSet dataSet;
    private final int[] orderPickupLocations;
    private final int[] orderDeliveryLocations;
    private int[] solution;
    private final int orderAmount;
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
        this.orderAmount = dataSet.getOrderAmount();
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
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
    }


    public double calculateSolution(int[] solution) {
        this.solution = solution;
        return calculateSolution();
    }

    private double calculateSolution() {
        return getTravelCosts() + getPenaltyCosts();
    }

    private double getTravelCosts() {
        int result = 0;
        int i = 0;
        for (int v = 0; v < vehicles; v++) {
            int vehicleCost = 0;
            int vehicleTotalDistance = 0;
            int vehicleWeight = 0;
            int vehicleMaxWeight = 0;
            int vehiclePosition = 0;
            int solutionElement = solution[i];



            int previousStop = vehiclePosition;
            boolean[] pickedUp = new boolean[orderAmount];
            while (solutionElement!=0) {
                if(vehiclePosition!=0) {
                    vehicleTotalDistance += travelDistance[vehiclePosition - 1][solutionElement - 1];
                }
                vehicleWeight += weightDifference(pickedUp[solutionElement-1],orderWeights, solutionElement);
                if(vehicleWeight > vehicleMaxWeight) {
                    vehicleMaxWeight = vehicleWeight;
                }
                int solutionElementPosition;
                if(!pickedUp[solutionElement-1]) {
                    solutionElementPosition = orderPickupLocations[solutionElement-1];
                    pickedUp[solutionElement-1] = true;
                } else {
                    solutionElementPosition = orderDeliveryLocations[solutionElement-1];
                }
                vehicleCost += getStopCost(vehiclePosition,solutionElementPosition, v );
                vehiclePosition = solutionElementPosition;
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

    private double weightDifference(boolean pickedUp, double[] orderWeights, int solutionElement) {
        if(pickedUp) {
            return -orderWeights[solutionElement-1];
        }
        else{
            return orderWeights[solutionElement-1];
        }
    }

    private double getStopCost(int previousStop, int solutionElementPosition, int v ) {
        double result=0;
        if(previousStop!=solutionElementPosition){
            result = stopCostMatrix[v][solutionElementPosition-1];
        }
        return result;
    }

    private double getVehicleCosts(int v, int vehicleTotalDistance, int vehicleMaxWeight) {
        int distanceDimension = determineDistanceDimension(vehicleTotalDistance, v);
        int weightDimension = determineWeightDimension(vehicleMaxWeight, v);
        double result = vehicleMaxWeight*kgCostMatrix[v][weightDimension][distanceDimension] + vehicleTotalDistance*kmCostMatrix[v][weightDimension][distanceDimension] +fixCostMatrix[v][weightDimension][distanceDimension];
        return result;
    }

    private double getPenaltyCosts() {
        double result = 0;
        int i = solution.length-1;
        int solutionElement = solution[i];
        boolean[] calculatedOrder = new boolean[orderAmount];
        while(solutionElement!=0){
            if (!calculatedOrder[solutionElement-1]) {
                result += orderPenalties[solutionElement - 1];
                calculatedOrder[solutionElement - 1] = true;
            }
            i--;
            solutionElement = solution[i];
        }
        return result;
    }

    private int determineWeightDimension(int vehicleMaxWeight, int v) {
        double[][] weightIntervals = dataSet.getWeightIntervals();
        return findDimension(vehicleMaxWeight, weightIntervals, v);
    }

    private int determineDistanceDimension(int vehicleTotalDistance, int v) {
        double[][] distanceIntervals = dataSet.getDistanceIntervals();
        return findDimension(vehicleTotalDistance, distanceIntervals, v);
    }

    private int findDimension(int element, double[][] intervals, int vehicle) {
        for (int i = 0; i < intervals.length - 1; i++) {
            if (element >= intervals[vehicle][i] && element < intervals[vehicle][i + 1]) {
                return i;
            }
        }
        return 0;
    }
}
