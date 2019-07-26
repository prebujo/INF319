package functions;

import dataObjects.IDataSet;

public class ObjectiveFunction {
    private final int vehicles;
    private final IDataSet dataSet;
    private final int[] orderPickupLocations;
    private final int[] orderDeliveryLocations;
    private final double[][] distanceIntervals;
    private final double[][] weightIntervals;
    private int[] solution;
    private final int orderAmount;
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
        this.vehicleStartingLocations = dataSet.getVehicleStartingLocations();
        this.vehicleDestinationLocations = dataSet.getVehicleDestinationLocations();
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
        this.orderPenalties = dataSet.getOrderPenalties();
    }


    public double calculateSolution(int[] solution) {
        this.solution = solution;
        return calculateSolution();
    }

    private double calculateSolution() {
        return getTravelCosts() + getPenaltyCosts();
    }

    private double getTravelCosts() {
        double result = 0;
        int i = 0;
        for (int v = 0; v < vehicles; v++) {
            double vehicleCost = 0;
            double vehicleTotalDistance = 0;
            double vehicleWeight = 0;
            double vehicleMaxWeight = 0;
            int vehicleLocation = 0;
            int solutionElement = solution[i];



            boolean[] pickedUp = new boolean[orderAmount];
            while (solutionElement!=0) {
                vehicleWeight += weightDifference(pickedUp[solutionElement-1],orderWeights, solutionElement);
                if(vehicleWeight > vehicleMaxWeight) {
                    vehicleMaxWeight = vehicleWeight;
                }
                int solutionElementLocation;
                if(!pickedUp[solutionElement-1]) {
                    solutionElementLocation = orderPickupLocations[solutionElement-1];
                    pickedUp[solutionElement-1] = true;
                } else {
                    solutionElementLocation = orderDeliveryLocations[solutionElement-1];
                }
                if(vehicleLocation!=0) {
                    vehicleTotalDistance += travelDistance[vehicleLocation - 1][solutionElementLocation - 1];
                }
                vehicleCost += getStopCost(vehicleLocation,solutionElementLocation, v );
                vehicleLocation = solutionElementLocation;

                solutionElement = solution[++i];
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
        return pickedUp ? -orderWeights[solutionElement-1]: orderWeights[solutionElement-1];
    }

    private double getStopCost(int previousStop, int solutionElementPosition, int v ) {
        return previousStop!=solutionElementPosition ? stopCostMatrix[v][solutionElementPosition-1]:0.0;
    }

    private double getVehicleCosts(int v, double vehicleTotalDistance, double vehicleMaxWeight) {
        int distanceDimension = findDimension(vehicleTotalDistance, distanceIntervals, v);
        int weightDimension = findDimension(vehicleMaxWeight, weightIntervals, v);
        double result = vehicleMaxWeight*kgCostMatrix[v][distanceDimension][weightDimension] + vehicleTotalDistance*kmCostMatrix[v][distanceDimension][weightDimension] +fixCostMatrix[v][distanceDimension][weightDimension];
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
}
