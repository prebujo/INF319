package dataObjects;

import java.util.Collection;
import java.util.List;

public interface IDataSet {
    int getVehicleAmount();
    int getOrderAmount();
    int getFactoryAmount();
    int getStopAmount();

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

    void setVehicleCanVisitNode(boolean[][] vehicleNodes);
    boolean[][] getVehicleCanVisitNode();

    void setVehicleCanPickupOrder(boolean[][] vehicleCanPickupOrder);
    boolean[][] getVehicleCanPickupOrder();

    void setVehicleStartingLocations(int[] vehicleStartingLocations);
    int[] getVehicleStartingLocations();

    void setVehicleDestinationLocations(int[] vehicleDestinationLocations);
    int[] getVehicleDestinationLocations();


    void setVehicleWeightCapacities(int[] vehicleCapacities);
    int[] getVehicleWeightCapacities();

    void setOrderWeights(int[] orderWeights);
    int[] getOrderWeights();

    void setVehicleVolumeCapacities(int[] vehicleVolumeCapacities);

    int[] getVehicleVolumeCapacities();

    void setOrderVolumes(int[] orderVolumes);

    int[] getOrderVolumes();

    void setOrderPenalties(int[] orderPenalties);
    int[] getOrderPenalties();

    void setDistanceIntervals(int[][] distanceIntervals);
    int[][] getDistanceIntervals();

    void setWeightIntervals(int[][] weightIntervals);
    int[][] getWeightIntervals();

    void setKmCostMatrices(int[][][] kmCostMatrices);
    int[][][] getKmCostMatrices();

    void setKgCostMatrices(int[][][] kgCostMatrices);
    int[][][] getKgCostMatrices();

    void setFixCostMatrices(int[][][] fixCostMatrices);
    int[][][] getFixCostMatrices();


    void setStopCosts(int[][] stopCosts);
    int[][] getStopCosts();

    void setTimeWindowAmounts(int[] timeWindowAmounts);
    int[] getTimeWindowAmounts();

    void setLowerTimeWindows(int[][] lowerTimeWindow);
    int[][] getLowerTimeWindows();

    void setUpperTimeWindows(int[][] upperTimeWindow);
    int[][] getUpperTimeWindows();

    void setTravelTimes(int[][][] travelTime);
    int[][][] getTravelTimes();

    void setTravelDistances(int[][] travelDistance);
    int[][] getTravelDistances();
}
