package dataObjects;

import java.util.Collection;
import java.util.List;

public class DataSet implements IData{
    private int vehicleAmount;
    private int orderAmount;
    private int factoryAmount;
    private int stopAmount;
    private int[] weightDimension;
    private int[] distanceDimension;
    private int[] factory;
    private int[] factoryStopCapacity;
    private int[] location;
    private List<Collection<Integer>> vehicleNode;
    private List<Collection<Integer>> vehiclePickupNode;
    private int[] vehicleStartingLocation;
    private int[] vehicleDestinationLocation;
    private int[] vehicleWeightCapacity;
    private int[] vehicleVolumeCapacity;
    private int[] orderWeight;
    private int[] orderVolume;
    private int[] orderPenalty;
    private int[][] distanceInterval;
    private int[][] weightInterval;
    private int[][][] kmCostMatrix;
    private int[][][] kgCostMatrix;
    private int[][][] fixCostMatrix;
    private int[][] stopCost;
    private int[] timeWindowAmount;
    private int[][] lowerTimeWindow;
    private int[][] upperTimeWindow;
    private int[][][] travelTime;
    private int[][] travelDistance;


    public DataSet(int vehicleAmount, int orderAmount, int factoryAmount, int stopAmount){
        this.vehicleAmount = vehicleAmount;
        this.orderAmount = orderAmount;
        this.factoryAmount = factoryAmount;
        this.stopAmount = stopAmount;
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
    public void setWeightDimension(int[] weightDimension) {
        this.weightDimension = weightDimension;
    }

    @Override
    public int[] getWeightDimension() {
        return weightDimension;
    }

    @Override
    public void setDistanceDimension(int[] distanceDimension) {
        this.distanceDimension = distanceDimension;
    }

    @Override
    public int[] getDistanceDimension() { return distanceDimension; }

    @Override
    public void setFactory(int[] factory){
        this.factory = factory;
    }

    @Override
    public int[] getFactory() {
        return factory;
    }

    @Override
    public void setFactoryStopCapacity(int[] factoryStopCapacity) {
        this.factoryStopCapacity = factoryStopCapacity;
    }

    @Override
    public int[] getFactoryStopCapacity() {
        return factoryStopCapacity;
    }

    @Override
    public void setLocation(int[] location) {
        this.location = location;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public void setVehicleNode(List<Collection<Integer>> vehicleNodeCollection) {
        this.vehicleNode = vehicleNodeCollection;
    }

    @Override
    public List<Collection<Integer>> getVehicleNode() {
        return vehicleNode;
    }

    @Override
    public void setVehiclePickupNode(List<Collection<Integer>> vehiclePickupNode) {
        this.vehiclePickupNode = vehiclePickupNode;
    }

    @Override
    public List<Collection<Integer>> getVehiclePickupNode() {
        return vehiclePickupNode;
    }

    @Override
    public void setVehicleStartingLocation(int[] vehicleStartingLocation) {
        this.vehicleStartingLocation = vehicleStartingLocation;
    }

    @Override
    public int[] getVehicleStartingLocation() {
        return vehicleStartingLocation;
    }

    @Override
    public void setVehicleDestinationLocation(int[] vehicleDestinationLocation) {
        this.vehicleDestinationLocation = vehicleDestinationLocation;
    }

    @Override
    public int[] getVehicleDestinationLocation() {
        return vehicleDestinationLocation;
    }

    @Override
    public void setVehicleWeightCapacity(int[] vehicleWeightCapacity) {
        this.vehicleWeightCapacity = vehicleWeightCapacity;
    }

    @Override
    public int[] getVehicleWeightCapacity() {
        return vehicleWeightCapacity;
    }

    @Override
    public void setOrderWeight(int[] orderWeight) {
        this.orderWeight = orderWeight;
    }

    @Override
    public int[] getOrderWeight() {
        return orderWeight;
    }

    @Override
    public void setVehicleVolumeCapacity(int[] vehicleVolumeCapacity) {
        this.vehicleVolumeCapacity = vehicleVolumeCapacity;
    }

    @Override
    public int[] getVehicleVolumeCapacity() {
        return vehicleVolumeCapacity;
    }

    @Override
    public void setOrderVolume(int[] orderVolume) {
        this.orderVolume = orderVolume;
    }

    @Override
    public int[] getOrderVolume() {
        return orderVolume;
    }

    @Override
    public void setOrderPenalty(int[] orderPenalty) {
        this.orderPenalty = orderPenalty;
    }

    @Override
    public int[] getOrderPenalty() {
        return orderPenalty;
    }

    @Override
    public void setDistanceInterval(int[][] distanceInterval) {
        this.distanceInterval = distanceInterval;
    }

    @Override
    public int[][] getDistanceInterval() {
        return distanceInterval;
    }

    @Override
    public void setWeightInterval(int[][] weightInterval) {
        this.weightInterval = weightInterval;
    }

    @Override
    public int[][] getWeightInterval() {
        return weightInterval;
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
        this.stopCost = stopCosts;
    }

    @Override
    public int[][] getStopCostMatrix() {
        return stopCost;
    }

    @Override
    public void setTimeWindowAmount(int[] timeWindowAmount) {
        this.timeWindowAmount = timeWindowAmount;
    }

    @Override
    public int[] getTimeWindowAmount() {
        return timeWindowAmount;
    }

    @Override
    public void setLowerTimeWindow(int[][] lowerTimeWindow) {
        this.lowerTimeWindow = lowerTimeWindow;
    }

    @Override
    public int[][] getLowerTimeWindow() {
        return lowerTimeWindow;
    }

    @Override
    public void setUpperTimeWindow(int[][] upperTimeWindow) {
        this.upperTimeWindow = upperTimeWindow;
    }

    @Override
    public int[][] getUpperTimeWindow() {
        return upperTimeWindow;
    }

    @Override
    public void setTravelTime(int[][][] travelTimes) {
        this.travelTime = travelTimes;
    }

    @Override
    public int[][][] getTravelTime() {
        return travelTime;
    }

    @Override
    public void setTravelDistance(int[][] travelDistances) {
        this.travelDistance = travelDistances;
    }

    @Override
    public int[][] getTravelDistance() {
        return travelDistance;
    }

}
