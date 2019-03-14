package dataObjects;

import java.util.Collection;
import java.util.List;

public interface IData {
    int getVehicleAmount();
    int getOrderAmount();
    int getFactoryAmount();
    int getStopAmount();
    int getWeightDimension();
    int getDistanceDimension();

    void setFactories(int[] factoryList);
    int[] getFactories();

    void setFactoryStopCapacities(int[] factoryStopCapacities);
    int[] getFactoryStopCapacities();

    void setLocations(int[] locationCollection);
    int[] getLocations();

    void setVehicleNodes(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehicleNodes();

    void setVehiclePickupNodes(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehiclePickupNodes();

    void setVehicleStartingLocations(int[] vehicleStartingLocations);
    int[] getVehicleStartingLocations();

    void setVehicleDestinationLocations(int[] vehicleDestinationLocations);
    int[] getVehicleDestinationLocations();


    void setVehicleCapacities(int[] vehicleCapacities);
    int[] getVehicleCapacities();

    void setOrderWeights(int[] orderWeights);
    int[] getOrderWeights();

    void setOrderPenalties(int[] orderPenalties);
    int[] getOrderPenalties();

    void setDistanceIntervals(int[] distanceIntervals);
    int[] getDistanceIntervals();

    void setWeightIntervals(int[] weightIntervals);
    int[] getWeightIntervals();

    void setKmCostMatrix(int[][][] kmCostMatrix);
    int[][][] getKmCostMatrix();

    void setKgCostMatrix(int[][][] kgCostMatrix);
    int[][][] getKgCostMatrix();

    void setFixCostMatrix(int[][][] fixCostMatrix);
    int[][][] getFixCostMatrix();


    void setStopCostMatrix(int[][] stopCosts);
    int[][] getStopCostMatrix();

    void setTimeWindowAmounts(int[] timeWindowAmounts);
    int[] getTimeWindowAmounts();

    void setLowerTimeWindows(int[][] lowerTimeWindows);
    int[][] getLowerTimeWindows();

    void setUpperTimeWindows(int[][] upperTimeWindows);
    int[][] getUpperTimeWindows();

    void setTravelTime(int[][][] travelTime);
    int[][][] getTravelTime();

    void setTravelDistance(int[][] travelDistance);
    int[][] getTravelDistance();
}
