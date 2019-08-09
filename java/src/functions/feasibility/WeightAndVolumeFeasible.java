package functions.feasibility;

import dataObjects.IDataSet;
import dataObjects.VehicleAndSchedule;

import java.util.List;

public class WeightAndVolumeFeasible implements IFeasibility {
    private final IDataSet dataset;
    private final int vehicleAmount;
    private final double[] vehicleWeightCapacity;
    private final int orderAmount;
    private final double[] orderWeights;
    private final double[] orderVolume;
    private final double[] vehicleVolumeCapacity;

    public WeightAndVolumeFeasible(IDataSet dataSet) {
        this.dataset = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.vehicleWeightCapacity = dataSet.getVehicleWeightCapacities();
        this.vehicleVolumeCapacity = dataSet.getVehicleVolumeCapacities();
        this.orderAmount = dataSet.getOrderAmount();
        this.orderWeights = dataSet.getOrderWeights();
        this.orderVolume = dataSet.getOrderVolumes();
    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        boolean[] pickedUp = new boolean[orderAmount];
        for (int v = 0; v < vehicleAmount; v++) {
            double weightOnVehicle = 0;
            double volumeOnVehicle = 0;
            int solutionElement = solution[i];
            while (solutionElement != 0) {
                weightOnVehicle += getWeightDifference(solutionElement, pickedUp[solutionElement-1]);
                volumeOnVehicle += getVolumeDifference(solutionElement, pickedUp[solutionElement-1]);
                pickedUp[solutionElement-1]=true;
                if (weightOnVehicle > vehicleWeightCapacity[v]||volumeOnVehicle>vehicleVolumeCapacity[v]) {
                    return false;
                }
                i++;
                solutionElement = solution[i];
            }
            i++;
        }
        return true;
    }

    @Override
    public boolean checkSchedule(int vehicle, List<Integer> schedule) {
        return false;
    }

    @Override
    public boolean checkScheduleWithOrderReplacement(int vehicle, int orderToReplace, int replacement, List<Integer> schedule) {
        return false;
    }

    @Override
    public boolean checkScheduleWithoutOrder(int order, VehicleAndSchedule vehicleScheduleOfOrder) {
        return false;
    }

    @Override
    public boolean checkScheduleWithoutOrder(int order, int vehicle, List<Integer> schedule) {
        return false;
    }

    private double getVolumeDifference(int solutionElement, boolean pickedup) {
        if(!pickedup){
            return orderVolume[solutionElement-1];
        }
        else {
            return -orderVolume[solutionElement-1];
        }
    }

    private double getWeightDifference(int solutionElement, boolean pickedup) {
        if(!pickedup){
            return orderWeights[solutionElement-1];
        }
        else {
            return -orderWeights[solutionElement-1];
        }
    }
}
