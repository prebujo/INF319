package dataObjects;

import java.util.List;

public interface IDataSet {
    int getVehicleAmount();
    int getOrderAmount();
    int getFactoryAmount();
    int getLocationsAmount();

    void setWeightDimensions(int[] weightDimensions);
    int[] getWeightDimensions();

    void setDistanceDimensions(int[] distanceDimensions);
    int[] getDistanceDimensions();

    void setFactories(int[] factoryList);
    int[] getFactories();

    void setFactoryStopCapacities(int[] factoryStopCapacities);
    int[] getFactoryStopCapacities();

    void setLocations(int[] locationCollection);
    int[] getLocations();

    void setVehicleCanVisitLocation(boolean[][] vehicleNodes);
    boolean[][] getVehicleCanVisitLocation();

    void setVehicleCanPickupOrder(boolean[][] vehicleCanPickupOrder);
    boolean[][] getVehicleCanPickupOrder();

    void setVehicleStartingLocations(int[] vehicleStartingLocations);
    int[] getVehicleStartingLocations();

    void setVehicleDestinationLocations(int[] vehicleDestinationLocations);
    int[] getVehicleDestinationLocations();


    void setVehicleWeightCapacities(double[] vehicleCapacities);
    double[] getVehicleWeightCapacities();

    void setOrderWeights(double[] orderWeights);
    double[] getOrderWeights();

    void setVehicleVolumeCapacities(double[] vehicleVolumeCapacities);

    double[] getVehicleVolumeCapacities();

    void setOrderVolumes(double[] orderVolumes);

    double[] getOrderVolumes();

    void setOrderPenalties(double[] orderPenalties);
    double[] getOrderPenalties();

    void setDistanceIntervals(double[][] distanceIntervals);
    double[][] getDistanceIntervals();

    void setWeightIntervals(double[][] weightIntervals);
    double[][] getWeightIntervals();

    void setKmCostMatrices(double[][][] kmCostMatrices);
    double[][][] getKmCostMatrices();

    void setKgCostMatrices(double[][][] kgCostMatrices);
    double[][][] getKgCostMatrices();

    void setFixCostMatrices(double[][][] fixCostMatrices);
    double[][][] getFixCostMatrices();


    void setStopCosts(double[][] stopCosts);
    double[][] getStopCosts();

    void setTimeWindowAmounts(int[] timeWindowAmounts);
    int[] getTimeWindowAmounts();

    void setLowerTimeWindows(double[][] lowerTimeWindow);
    double[][] getLowerTimeWindows();

    void setUpperTimeWindows(double[][] upperTimeWindow);
    double[][] getUpperTimeWindows();

    void setTravelTimes(double[][][] travelTime);
    double[][][] getTravelTimes();

    void setTravelDistances(double[][] travelDistance);
    double[][] getTravelDistances();

    int[] getOrderPickupLocations();

    void setOrderPickupLocations(int[] orderPickupLocations);

    int[] getOrderDeliveryLocations();

    void setOrderDeliveryLocations(int[] orderDeliveryLocations);


    void setOrderDeliveryDocks(int[] orderDeliveryDocks);

    double getMaxDistance();
    void setMaxDistance(double maxDistance);

    double getLatestPickupTimeWindow();
    void setLatestPickupTimeWindow(double latestPickupTimeWindow);
    double getLatestDeliveryTimeWindow();
    void setLatestDeliveryTimeWindow(double latestDeliveryTimeWindow);

    double getOrderSimilarity(int order1, int order2);

    void setOrderSimilarities();

    void setMaxWeight(double maxWeight);

    double getMaxWeight();

    void setLocationClusters();

    List<List<Integer>> getLocationClusters();

    int getCluster(int location);
}
