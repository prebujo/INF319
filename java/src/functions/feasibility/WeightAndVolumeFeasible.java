package functions.feasibility;

import dataObjects.IDataSet;

public class WeightAndVolumeFeasible implements IFeasibility {
    private final IDataSet dataset;
    private final int vehicleAmount;
    private final int[] vehicleWeightCapacity;
    private final int orderAmount;
    private final int[] orderWeights;
    private final int[] orderVolume;
    private final int[] vehicleVolumeCapacity;

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
        boolean result = true;

        int i = 0;
        for (int v = 0; v < vehicleAmount; v++) {
            int weightOnVehicle = 0;
            int volumeOnVehicle = 0;
            int solutionElement = solution[i];
            while (solutionElement != 0) {
                weightOnVehicle += getWeightDifference(solutionElement);
                volumeOnVehicle += getVolumeDifference(solutionElement);
                if (weightOnVehicle > vehicleWeightCapacity[v]||volumeOnVehicle>vehicleVolumeCapacity[v]) {
                    result = false;
                    break;
                }
                i++;
                solutionElement = solution[i];
            }
            if (weightOnVehicle > vehicleWeightCapacity[v]) {
                break;
            }
            i++;
        }
        return result;
    }

    private int getVolumeDifference(int solutionElement) {
        if(solutionElement<=orderAmount){
            return orderVolume[solutionElement-1];
        }
        else {
            return -orderVolume[solutionElement-orderAmount-1];
        }
    }

    private int getWeightDifference(int solutionElement) {
        if(solutionElement<=orderAmount){
            return orderWeights[solutionElement-1];
        }
        else {
            return -orderWeights[solutionElement-orderAmount-1];
        }
    }
}
