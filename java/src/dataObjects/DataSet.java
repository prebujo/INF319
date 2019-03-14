package dataObjects;

import java.util.Collection;
import java.util.List;

public class DataSet implements IData{
    private int vehicleAmount;
    private int orderAmount;
    private int factoryAmount;
    private int stopAmount;
    private int weightDimension;
    private int distanceDimension;
    private int[] factories;
    private int[] factoryStopCapacities;
    private int[] locations;
    private List<Collection<Integer>> vehicleNodes;
    private List<Collection<Integer>> vehiclePickupNodes;
    private int[] vehicleStartingLocations;
    private int[] vehicleDestinationLocations;
    private int[] vehicleCapacties;
    private int[] orderWeights;
    private int[] orderPenalties;
    private int[] distanceIntervals;
    private int[] weightIntervals;
    private int[][][] kmCostMatrix;
    private int[][][] kgCostMatrix;
    private int[][][] fixCostMatrix;
    private int[][] stopCosts;
    private int[] timeWindowAmounts;
    private int[][] lowerTimeWindows;
    private int[][] upperTimeWindows;
    private int[][][] travelTimes;
    private int[][] travelDistances;


    public DataSet(int vehicleAmount, int orderAmount, int factoryAmount, int stopAmount, int distanceDimension, int weightDimension){
        this.vehicleAmount = vehicleAmount;
        this.orderAmount = orderAmount;
        this.factoryAmount = factoryAmount;
        this.stopAmount = stopAmount;
        this.distanceDimension = distanceDimension;
        this.weightDimension = weightDimension;
    }

    @Override
    public int getOrderAmount() {
        return orderAmount;
    }

    @Override
    public int getFactoryAmount() {
        return factoryAmount;
    }

    @Override
    public int getStopAmount() { return stopAmount; }

    @Override
    public int getVehicleAmount() {
        return vehicleAmount;
    }

    @Override
    public int getWeightDimension() {
        return weightDimension;
    }

    @Override
    public int getDistanceDimension() { return distanceDimension; }

    @Override
    public void setFactories(int[] factories){
        this.factories = factories;
    }

    @Override
    public int[] getFactories() {
        return factories;
    }

    @Override
    public void setFactoryStopCapacities(int[] factoryStopCapacities) {
        this.factoryStopCapacities = factoryStopCapacities;
    }

    @Override
    public int[] getFactoryStopCapacities() {
        return factoryStopCapacities;
    }

    @Override
    public void setLocations(int[] locations) {
        this.locations = locations;
    }

    @Override
    public int[] getLocations() {
        return locations;
    }

    @Override
    public void setVehicleNodes(List<Collection<Integer>> vehicleNodeCollection) {
        this.vehicleNodes = vehicleNodeCollection;
    }

    @Override
    public List<Collection<Integer>> getVehicleNodes() {
        return vehicleNodes;
    }

    @Override
    public void setVehiclePickupNodes(List<Collection<Integer>> vehiclePickupNodes) {
        this.vehiclePickupNodes = vehiclePickupNodes;
    }

    @Override
    public List<Collection<Integer>> getVehiclePickupNodes() {
        return vehiclePickupNodes;
    }

    @Override
    public void setVehicleStartingLocations(int[] vehicleStartingLocations) {
        this.vehicleStartingLocations = vehicleStartingLocations;
    }

    @Override
    public int[] getVehicleStartingLocations() {
        return vehicleStartingLocations;
    }

    @Override
    public void setVehicleDestinationLocations(int[] vehicleDestinationLocations) {
        this.vehicleDestinationLocations = vehicleDestinationLocations;
    }

    @Override
    public int[] getVehicleDestinationLocations() {
        return vehicleDestinationLocations;
    }

    @Override
    public void setVehicleCapacities(int[] vehicleCapacities) {
        this.vehicleCapacties = vehicleCapacities;
    }

    @Override
    public int[] getVehicleCapacities() {
        return vehicleCapacties;
    }

    @Override
    public void setOrderWeights(int[] orderWeights) {
        this.orderWeights = orderWeights;
    }

    @Override
    public int[] getOrderWeights() {
        return orderWeights;
    }

    @Override
    public void setOrderPenalties(int[] orderPenalties) {
        this.orderPenalties = orderPenalties;
    }

    @Override
    public int[] getOrderPenalties() {
        return orderPenalties;
    }

    @Override
    public void setDistanceIntervals(int[] distanceIntervals) {
        this.distanceIntervals = distanceIntervals;
    }

    @Override
    public int[] getDistanceIntervals() {
        return distanceIntervals;
    }

    @Override
    public void setWeightIntervals(int[] weightIntervals) {
        this.weightIntervals = weightIntervals;
    }

    @Override
    public int[] getWeightIntervals() {
        return weightIntervals;
    }

    @Override
    public void setKmCostMatrix(int[][][] kmCostMatrix) {
        this.kmCostMatrix = kmCostMatrix;

    }

    @Override
    public int[][][] getKmCostMatrix() {
        return kmCostMatrix;
    }

    @Override
    public void setKgCostMatrix(int[][][] kgCostMatrix) {
        this.kgCostMatrix = kgCostMatrix;
    }

    @Override
    public int[][][] getKgCostMatrix() {
        return kgCostMatrix;
    }

    @Override
    public void setFixCostMatrix(int[][][] fixCostMatrix) {
        this.fixCostMatrix = fixCostMatrix;
    }

    @Override
    public int[][][] getFixCostMatrix() {
        return fixCostMatrix;
    }

    @Override
    public void setStopCostMatrix(int[][] stopCosts) {
        this.stopCosts = stopCosts;
    }

    @Override
    public int[][] getStopCostMatrix() {
        return stopCosts;
    }

    @Override
    public void setTimeWindowAmounts(int[] timeWindowAmounts) {
        this.timeWindowAmounts = timeWindowAmounts;
    }

    @Override
    public int[] getTimeWindowAmounts() {
        return timeWindowAmounts;
    }

    @Override
    public void setLowerTimeWindows(int[][] lowerTimeWindows) {
        this.lowerTimeWindows = lowerTimeWindows;
    }

    @Override
    public int[][] getLowerTimeWindows() {
        return lowerTimeWindows;
    }

    @Override
    public void setUpperTimeWindows(int[][] upperTimeWindows) {
        this.upperTimeWindows = upperTimeWindows;
    }

    @Override
    public int[][] getUpperTimeWindows() {
        return upperTimeWindows;
    }

    @Override
    public void setTravelTime(int[][][] travelTimes) {
        this.travelTimes = travelTimes;
    }

    @Override
    public int[][][] getTravelTime() {
        return travelTimes;
    }

    @Override
    public void setTravelDistance(int[][] travelDistances) {
        this.travelDistances = travelDistances;
    }

    @Override
    public int[][] getTravelDistance() {
        return travelDistances;
    }

}
