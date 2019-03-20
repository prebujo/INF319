package dataObjects;

import java.util.Collection;
import java.util.List;

public interface IData {
    int getVehicleAmount();
    int getOrderAmount();
    int getFactoryAmount();
    int getStopAmount();

    void setWeightDimension(int[] weightDimension);
    int[] getWeightDimension();

    void setDistanceDimension(int[] distanceDimension);
    int[] getDistanceDimension();

    void setFactory(int[] factoryList);
    int[] getFactory();

    void setFactoryStopCapacity(int[] factoryStopCapacity);
    int[] getFactoryStopCapacity();

    void setLocation(int[] locationCollection);
    int[] getLocation();

    void setVehicleNode(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehicleNode();

    void setVehiclePickupNode(List<Collection<Integer>> locationCollection);
    List<Collection<Integer>> getVehiclePickupNode();

    void setVehicleStartingLocation(int[] vehicleStartingLocation);
    int[] getVehicleStartingLocation();

    void setVehicleDestinationLocation(int[] vehicleDestinationLocation);
    int[] getVehicleDestinationLocation();


    void setVehicleWeightCapacity(int[] vehicleCapacities);
    int[] getVehicleWeightCapacity();

    void setOrderWeight(int[] orderWeight);
    int[] getOrderWeight();

    void setVehicleVolumeCapacity(int[] vehicleVolumeCapacity);

    int[] getVehicleVolumeCapacity();

    void setOrderVolume(int[] orderVolume);

    int[] getOrderVolume();

    void setOrderPenalty(int[] orderPenalty);
    int[] getOrderPenalty();

    void setDistanceInterval(int[][] distanceInterval);
    int[][] getDistanceInterval();

    void setWeightInterval(int[][] weightInterval);
    int[][] getWeightInterval();

    void setKmCostMatrix(int[][][] kmCostMatrix);
    int[][][] getKmCostMatrix();

    void setKgCostMatrix(int[][][] kgCostMatrix);
    int[][][] getKgCostMatrix();

    void setFixCostMatrix(int[][][] fixCostMatrix);
    int[][][] getFixCostMatrix();


    void setStopCostMatrix(int[][] stopCosts);
    int[][] getStopCostMatrix();

    void setTimeWindowAmount(int[] timeWindowAmount);
    int[] getTimeWindowAmount();

    void setLowerTimeWindow(int[][] lowerTimeWindow);
    int[][] getLowerTimeWindow();

    void setUpperTimeWindow(int[][] upperTimeWindow);
    int[][] getUpperTimeWindow();

    void setTravelTime(int[][][] travelTime);
    int[][][] getTravelTime();

    void setTravelDistance(int[][] travelDistance);
    int[][] getTravelDistance();
}
