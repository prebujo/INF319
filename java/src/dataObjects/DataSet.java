package dataObjects;

public class DataSet implements IDataSet {
    private int vehicleAmount;
    private int orderAmount;
    private int locationsAmount;
    private int factoryAmount;
    private int stopAmount;
    private int[] orderPickupLocations;
    private int[] orderDeliveryLocations;
    private int[] weightDimensions;
    private int[] distanceDimensions;
    private int[] factories;
    private int[] factoryStopCapacities;
    private int[] locations;
    private boolean[][] vehicleCanVisitNode;
    private boolean[][] vehicleCanPickupOrder;
    private int[] vehicleStartingLocations;
    private int[] vehicleDestinationLocations;
    private double[] vehicleWeightCapacities;
    private double[] vehicleVolumeCapacities;
    private double[] orderWeights;
    private double[] orderVolumes;
    private double[] orderPenalties;
    private double[][] distanceIntervals;
    private double[][] weightIntervals;
    private double[][][] kmCostMatrices;
    private double[][][] kgCostMatrices;
    private double[][][] fixCostMatrices;
    private double[][] stopCosts;
    private int[] timeWindowAmounts;
    private double[][] lowerTimeWindows;
    private double[][] upperTimeWindows;
    private double[][][] travelTimes;
    private double[][] travelDistances;
    private int[] orderDeliveryDock;


    public DataSet(int vehicleAmount, int orderAmount, int locationsAmount, int factoryAmount){
        this.vehicleAmount = vehicleAmount;
        this.orderAmount = orderAmount;
        this.locationsAmount = locationsAmount;
        this.factoryAmount = factoryAmount;
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
    public void setVehicleCanPickupOrder(boolean[][] vehicleCanPickupOrder) {
        this.vehicleCanPickupOrder = vehicleCanPickupOrder;
    }

    @Override
    public boolean[][] getVehicleCanPickupOrder() {
        return vehicleCanPickupOrder;
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
    public void setVehicleWeightCapacities(double[] vehicleWeightCapacities) {
        this.vehicleWeightCapacities = vehicleWeightCapacities;
    }

    @Override
    public double[] getVehicleWeightCapacities() {
        return vehicleWeightCapacities;
    }

    @Override
    public void setOrderWeights(double[] orderWeights) {
        this.orderWeights = orderWeights;
    }

    @Override
    public double[] getOrderWeights() {
        return orderWeights;
    }

    @Override
    public void setVehicleVolumeCapacities(double[] vehicleVolumeCapacities) {
        this.vehicleVolumeCapacities = vehicleVolumeCapacities;
    }

    @Override
    public double[] getVehicleVolumeCapacities() {
        return vehicleVolumeCapacities;
    }

    @Override
    public void setOrderVolumes(double[] orderVolumes) {
        this.orderVolumes = orderVolumes;
    }

    @Override
    public double[] getOrderVolumes() {
        return orderVolumes;
    }

    @Override
    public void setOrderPenalties(double[] orderPenalties) {
        this.orderPenalties = orderPenalties;
    }

    @Override
    public double[] getOrderPenalties() {
        return orderPenalties;
    }

    @Override
    public void setDistanceIntervals(double[][] distanceIntervals) {
        this.distanceIntervals = distanceIntervals;
    }

    @Override
    public double[][] getDistanceIntervals() {
        return distanceIntervals;
    }

    @Override
    public void setWeightIntervals(double[][] weightIntervals) {
        this.weightIntervals = weightIntervals;
    }

    @Override
    public double[][] getWeightIntervals() {
        return weightIntervals;
    }

    @Override
    public void setKmCostMatrices(double[][][] kmCostMatrices) {
        this.kmCostMatrices = kmCostMatrices;
    }

    @Override
    public double[][][] getKmCostMatrices() {
        return kmCostMatrices;
    }

    @Override
    public void setKgCostMatrices(double[][][] kgCostMatrices) {
        this.kgCostMatrices = kgCostMatrices;
    }

    @Override
    public double[][][] getKgCostMatrices() {
        return kgCostMatrices;
    }

    @Override
    public void setFixCostMatrices(double[][][] fixCostMatrices) {
        this.fixCostMatrices = fixCostMatrices;
    }

    @Override
    public double[][][] getFixCostMatrices() {
        return fixCostMatrices;
    }

    @Override
    public void setStopCosts(double[][] stopCosts) {
        this.stopCosts = stopCosts;
    }

    @Override
    public double[][] getStopCosts() {
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
    public void setLowerTimeWindows(double[][] lowerTimeWindows) {
        this.lowerTimeWindows = lowerTimeWindows;
    }

    @Override
    public double[][] getLowerTimeWindows() {
        return lowerTimeWindows;
    }

    @Override
    public void setUpperTimeWindows(double[][] upperTimeWindows) {
        this.upperTimeWindows = upperTimeWindows;
    }

    @Override
    public double[][] getUpperTimeWindows() {
        return upperTimeWindows;
    }

    @Override
    public void setTravelTimes(double[][][] travelTimes) {
        this.travelTimes = travelTimes;
    }

    @Override
    public double[][][] getTravelTimes() {
        return travelTimes;
    }

    @Override
    public void setTravelDistances(double[][] travelDistances) {
        this.travelDistances = travelDistances;
    }

    @Override
    public double[][] getTravelDistances() {
        return travelDistances;
    }

    @Override
    public int[] getOrderPickupLocations() {
        return orderPickupLocations;
    }

    @Override
    public void setOrderPickupLocations(int[] orderPickupLocations) {
        this.orderPickupLocations = orderPickupLocations;
    }

    @Override
    public int[] getOrderDeliveryLocations() {
        return orderDeliveryLocations;
    }

    @Override
    public void setOrderDeliveryLocations(int[] orderDeliveryLocations) {
        this.orderDeliveryLocations = orderDeliveryLocations;
    }

    @Override
    public void setOrderDeliveryDocks(int[] orderDeliveryDock) {
        this.orderDeliveryDock = orderDeliveryDock;
    }

    @Override
    public int getLocationsAmount() {
        return locationsAmount;
    }

}
