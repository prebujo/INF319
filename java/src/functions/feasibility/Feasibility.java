package functions.feasibility;

import dataObjects.IDataSet;

public class Feasibility implements IFeasibility{


    private final int vehicleAmount;
    private final double[][][] travelTime;
    private final double[][] lowerTimeWindows;
    private final double[][] upperTimeWindows;
    private final int[] timeWindowAmounts;
    private final int orderAmount;
    private final int factoryAmount;
    private final int[] factoryStopCapacity;
    private final double[] orderVolume;
    private final double[] orderWeights;
    private final double[] vehicleWeightCapacities;
    private final double[] vehicleVolumeCapacities;
    private final int[] factory;
    private final boolean[][] vehicleCanVisitNode;
    private final int[] orderPickupLocations;
    private final int[] orderDeliveryLocations;
    private boolean[][] vehicleCanPickupOrder;

    public Feasibility(IDataSet dataSet){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.travelTime = dataSet.getTravelTimes();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
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
        this.vehicleCanPickupOrder = dataSet.getVehicleCanPickupOrder();
    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        int[] pickedUpBy = new int[orderAmount+1];
        int[] deliveredBy = new int[orderAmount+1];
        for (int v = 0; v<vehicleAmount;v++) {
            double weightOnVehicle = 0;
            double volumeOnVehicle = 0;
            int vehicleLocation = 0;
            int solutionLocation;
            solutionElement = solution[i];
            double currentVehicleTime = 0;
            int factoryStopCounter = 1;
            while(solutionElement!=0){


                weightOnVehicle += getWeightDifference(solutionElement, pickedUpBy[solutionElement]>0);
                volumeOnVehicle += getVolumeDifference(solutionElement, pickedUpBy[solutionElement]>0);
                if (weightOnVehicle > vehicleWeightCapacities[v]||volumeOnVehicle> vehicleVolumeCapacities[v]) {
                    return false;
                }
                if(pickedUpBy[solutionElement]>0){
                    if(deliveredBy[solutionElement]>0||pickedUpBy[solutionElement]!=(v+1)){
                        return false;
                    }
                    deliveredBy[solutionElement] = v+1;
                    solutionLocation = orderDeliveryLocations[solutionElement-1];
                } else {
                    if (!vehicleCanPickupOrder[v][solutionElement-1]){
                        return false;
                    }
                    if(pickedUpBy[solutionElement]>0){
                        return false;
                    }
                    pickedUpBy[solutionElement] = v+1;
                    solutionLocation = orderPickupLocations[solutionElement-1];
                }
                if (!vehicleCanVisitNode[v][solutionLocation-1]) {
                    return false;
                }
                if(vehicleLocation!= 0) {
                    currentVehicleTime += travelTime[v][vehicleLocation - 1][solutionLocation - 1];
                }
                for (int timewindow = 0; timewindow < timeWindowAmounts[solutionLocation-1]; timewindow++) {
                    if (currentVehicleTime < lowerTimeWindows[solutionLocation - 1][timewindow]) {
                        currentVehicleTime = lowerTimeWindows[solutionLocation - 1][timewindow];
                    }
                    if (currentVehicleTime >= lowerTimeWindows[solutionLocation - 1][timewindow] && currentVehicleTime <= upperTimeWindows[solutionLocation - 1][timewindow]) {
                        break;
                    }
                    if (timewindow == timeWindowAmounts[solutionLocation- 1] - 1) {
                        return false;
                    }
                }


                if(i>0) {
                    if (vehicleLocation != 0) {

                        if (factory[solutionLocation - 1] != factory[vehicleLocation - 1] || factory[solutionLocation - 1] == 0) {
                            factoryStopCounter = 1;
                        } else {
                            factoryStopCounter++;
                            if (factoryStopCounter > factoryStopCapacity[factory[solutionLocation - 1] - 1]) {
                                return false;
                            }
                        }
                    }
                }
                vehicleLocation = solutionLocation;
                i++;
                solutionElement = solution[i];
            }
            i++;
        }
        return true;
    }
    private double getVolumeDifference(int solutionElement, boolean pickedUp) {
        if(!pickedUp){
            return orderVolume[solutionElement-1];
        }
        else {
            return -orderVolume[solutionElement-1];
        }
    }

    private double getWeightDifference(int solutionElement, boolean pickedUp) {
        if(!pickedUp){
            return orderWeights[solutionElement-1];
        }
        else {
            return -orderWeights[solutionElement-1];
        }
    }
}
