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

    void addFactories(int[] factoryList);
    int[] getFactories();

    void addFactoryStopCapacities(int[] factoryStopCapacities);
    int[] getFactoryStopCapacities();

    void addLocations(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getLocations();

    void addVehicleNodes(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehicleNodes();

    void addVehiclePickupNodes(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehiclePickupNodes();

    void addVehicleStartingLocations(int[] vehicleStartingLocations);
    int[] getVehicleStartingLocations();

    void addVehicleDestinationLocations(int[] vehicleDestinationLocations);
    int[] getVehicleDestinationLocations();


    void addVehicleCapacities(int[] vehicleCapacities);
    int[] getVehicleCapacities();

    void addOrderWeights(int[] orderWeights);
    int[] getOrderWeights();

    void addOrderPenalties(int[] orderPenalties);
    int[] getOrderPenalties();

    void addDistanceIntervals(int[] distanceIntervals);
    int[] getDistanceIntervals();

    void addWeightIntervals(int[] weightIntervals);
    int[] getWeightIntervals();

    void addKmCostMatrix(int[][][] kmCostMatrix);
    int[][][] getKmCostMatrix();

    void addKgCostMatrix(int[][][] kgCostMatrix);
    int[][][] getKgCostMatrix();

    void addFixCostMatrix(int[][][] fixCostMatrix);
    int[][][] getFixCostMatrix();


    void addStopCostMatrix(int[][] stopCosts);
    int[][] getStopCostMatrix();

    void addTimeWindowAmounts(int[] timeWindowAmounts);
    int[] getTimeWindowAmounts();

    void addLowerTimeWindows(int[][] lowerTimeWindows);
    int[][] getLowerTimeWindows();

    void addUpperTimeWindows(int[][] upperTimeWindows);
    int[][] getUpperTimeWindows();

    void addTravelTime(int[][][] travelTime);
    int[][][] getTravelTime();

    void addTravelDistance(int[][] travelDistance);
    int[][] getTravelDistance();
}
