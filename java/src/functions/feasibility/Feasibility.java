package functions.feasibility;

import dataObjects.IDataSet;

public class Feasibility implements IFeasibility{


    private final int vehicleAmount;
    private final int[][][] travelTime;
    private final int[] vehicleStartingLocation;
    private final int[][] lowerTimeWindows;
    private final int[][] upperTimeWindows;
    private final int[] timeWindowAmounts;
    private final int orderAmount;
    private final int factoryAmount;
    private final int[] factoryStopCapacity;
    private final int[] orderVolume;
    private final int[] orderWeights;
    private final int[] vehicleWeightCapacities;
    private final int[] vehicleVolumeCapacities;
    private final int[] factory;
    private final boolean[][] vehicleCanVisitNode;
    private boolean[][] vehicleCanPickupNode;

    public Feasibility(IDataSet dataSet){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.travelTime = dataSet.getTravelTimes();
        this.vehicleStartingLocation = dataSet.getVehicleStartingLocations();
        this.lowerTimeWindows = dataSet.getLowerTimeWindows();
        this.upperTimeWindows = dataSet.getUpperTimeWindows();
        this.timeWindowAmounts = dataSet.getTimeWindowAmounts();
        this.orderVolume = dataSet.getOrderVolumes();
        this.orderWeights = dataSet.getOrderWeights();
        this.vehicleWeightCapacities = dataSet.getVehicleWeightCapacities();
        this.vehicleVolumeCapacities = dataSet.getVehicleVolumeCapacities();
        this.factory = dataSet.getFactories();
        this.factoryAmount = dataSet.getFactoryAmount();
        this.factoryStopCapacity = dataSet.getFactoryStopCapacities();
        this.vehicleCanVisitNode = dataSet.getVehicleCanVisitNode();
        this.vehicleCanPickupNode = dataSet.getVehicleCanPickupOrder();
    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        int[] pickedUpOrDeliveredBy = new int[2*orderAmount+1];
        for (int v = 0; v<vehicleAmount;v++) {
            int weightOnVehicle = 0;
            int volumeOnVehicle = 0;
            int vehicleLocation = vehicleStartingLocation[v];
            solutionElement = solution[i];
            int currentVehicleTime = 0;
            int factoryStopCounter = 1;
            while(solutionElement!=0){

                if (!vehicleCanVisitNode[v][solutionElement]) {
                    return false;
                }

                if(solutionElement>orderAmount){
                    if(pickedUpOrDeliveredBy[solutionElement]>0||pickedUpOrDeliveredBy[solutionElement-orderAmount]!=(v+1)){
                        return false;
                    }
                }
                else {
                    if (!vehicleCanPickupNode[v][solutionElement]){
                        return false;
                    }
                    if(pickedUpOrDeliveredBy[solutionElement]>0){
                        return false;
                    }
                }
                pickedUpOrDeliveredBy[solutionElement] = v+1;

                currentVehicleTime +=travelTime[v][vehicleLocation-1][solutionElement-1];
                for (int timewindow = 0;timewindow<timeWindowAmounts[solutionElement-1];timewindow++){
                    if(currentVehicleTime<lowerTimeWindows[timewindow][solutionElement-1]){
                        currentVehicleTime = lowerTimeWindows[timewindow][solutionElement-1];
                    }
                    if(currentVehicleTime>=lowerTimeWindows[timewindow][solutionElement-1] && currentVehicleTime<=upperTimeWindows[timewindow][solutionElement-1]) {
                        break;
                    }
                    if (timewindow==timeWindowAmounts[solutionElement-1]-1){
                        return false;
                    }
                }

                weightOnVehicle += getWeightDifference(solutionElement);
                volumeOnVehicle += getVolumeDifference(solutionElement);
                if (weightOnVehicle > vehicleWeightCapacities[v]||volumeOnVehicle> vehicleVolumeCapacities[v]) {
                    return false;
                }

                if(i>0) {
                    int previousSolutionElement = solution[i - 1];
                    if (previousSolutionElement != 0) {

                        if (factory[solutionElement - 1] != factory[previousSolutionElement - 1] || factory[solutionElement - 1] == 0) {
                            factoryStopCounter = 1;
                        } else {
                            factoryStopCounter++;
                            if (factoryStopCounter > factoryStopCapacity[factory[solutionElement - 1] - 1]) {
                                return false;
                            }
                        }
                    }
                }
                vehicleLocation = solutionElement;
                i++;
                solutionElement = solution[i];
            }
            i++;
        }
        return true;
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
