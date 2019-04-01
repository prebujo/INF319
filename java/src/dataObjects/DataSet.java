package dataObjects;

public class DataSet implements IData{
    private int vehicleAmount;
    private int orderAmount;
    private int factoryAmount;
    private int stopAmount;
    private int[] weightDimensions;
    private int[] distanceDimensions;
    private int[] factories;
    private int[] factoryStopCapacities;
    private int[] locations;
    private boolean[][] vehicleCanVisitNode;
    private boolean[][] vehicleCanPickupNode;
    private int[] vehicleStartingLocations;
    private int[] vehicleDestinationLocations;
    private int[] vehicleWeightCapacities;
    private int[] vehicleVolumeCapacities;
    private int[] orderWeights;
    private int[] orderVolumes;
    private int[] orderPenalties;
    private int[][] distanceIntervals;
    private int[][] weightIntervals;
    private int[][][] kmCostMatrices;
    private int[][][] kgCostMatrices;
    private int[][][] fixCostMatrices;
    private int[][] stopCosts;
    private int[] timeWindowAmounts;
    private int[][] lowerTimeWindows;
    private int[][] upperTimeWindows;
    private int[][][] travelTimes;
    private int[][] travelDistances;


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
    public void setWeightDimensions(int[] weightDimensions) {
        this.weightDimensions = weightDimensions;
    }

    @Override
    public int[] getWeightDimensions() {
        return weightDimensions;
    }

    @Override
    public void setDistanceDimensions(int[] distanceDimensions) {
        this.distanceDimensions = distanceDimensions;
    }

    @Override
    public int[] getDistanceDimensions() { return distanceDimensions; }

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
    public void setVehicleCanVisitNode(boolean[][] vehicleCanVisitNode) {
        this.vehicleCanVisitNode = vehicleCanVisitNode;
    }

    @Override
    public boolean[][] getVehicleCanVisitNode() {
        return vehicleCanVisitNode;
    }

    @Override
    public void setVehicleCanPickupNode(boolean[][] vehicleCanPickupNode) {
        this.vehicleCanPickupNode = vehicleCanPickupNode;
    }

    @Override
    public boolean[][] getVehicleCanPickupNode() {
        return vehicleCanPickupNode;
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
    public void setVehicleWeightCapacities(int[] vehicleWeightCapacities) {
        this.vehicleWeightCapacities = vehicleWeightCapacities;
    }

    @Override
    public int[] getVehicleWeightCapacities() {
        return vehicleWeightCapacities;
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
    public void setVehicleVolumeCapacities(int[] vehicleVolumeCapacities) {
        this.vehicleVolumeCapacities = vehicleVolumeCapacities;
    }

    @Override
    public int[] getVehicleVolumeCapacities() {
        return vehicleVolumeCapacities;
    }

    @Override
    public void setOrderVolumes(int[] orderVolumes) {
        this.orderVolumes = orderVolumes;
    }

    @Override
    public int[] getOrderVolumes() {
        return orderVolumes;
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
    public void setDistanceIntervals(int[][] distanceIntervals) {
        this.distanceIntervals = distanceIntervals;
    }

    @Override
    public int[][] getDistanceIntervals() {
        return distanceIntervals;
    }

    @Override
    public void setWeightIntervals(int[][] weightIntervals) {
        this.weightIntervals = weightIntervals;
    }

    @Override
    public int[][] getWeightIntervals() {
        return weightIntervals;
    }

    @Override
    public void setKmCostMatrices(int[][][] kmCostMatrices) {
        this.kmCostMatrices = kmCostMatrices;
    }

    @Override
    public int[][][] getKmCostMatrices() {
        return kmCostMatrices;
    }

    @Override
    public void setKgCostMatrices(int[][][] kgCostMatrices) {
        this.kgCostMatrices = kgCostMatrices;
    }

    @Override
    public int[][][] getKgCostMatrices() {
        return kgCostMatrices;
    }

    @Override
    public void setFixCostMatrices(int[][][] fixCostMatrices) {
        this.fixCostMatrices = fixCostMatrices;
    }

    @Override
    public int[][][] getFixCostMatrices() {
        return fixCostMatrices;
    }

    @Override
    public void setStopCosts(int[][] stopCosts) {
        this.stopCosts = stopCosts;
    }

    @Override
    public int[][] getStopCosts() {
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
    public void setTravelTimes(int[][][] travelTimes) {
        this.travelTimes = travelTimes;
    }

    @Override
    public int[][][] getTravelTimes() {
        return travelTimes;
    }

    @Override
    public void setTravelDistances(int[][] travelDistances) {
        this.travelDistances = travelDistances;
    }

    @Override
    public int[][] getTravelDistances() {
        return travelDistances;
    }

}
