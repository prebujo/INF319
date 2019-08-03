package functions.feasibility;

import dataObjects.IDataSet;

import java.util.List;

public class TimeFeasible implements IFeasibility {

    private final int vehicleAmount;
    private final double[][][] travelTime;
    private final int[] vehicleStartingLocation;
    private final double[][] lowerTimeWindows;
    private final double[][] upperTimeWindows;
    private final int[] timeWindowAmounts;
    private final int[] orderPickupLocations;
    private int orderAmount;
    private int[] orderDeliveryLocations;

    public TimeFeasible(IDataSet dataSet){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.travelTime = dataSet.getTravelTimes();
        this.vehicleStartingLocation = dataSet.getVehicleStartingLocations();
        this.lowerTimeWindows = dataSet.getLowerTimeWindows();
        this.upperTimeWindows = dataSet.getUpperTimeWindows();
        this.timeWindowAmounts = dataSet.getTimeWindowAmounts();
        this.orderAmount = dataSet.getOrderAmount();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
    }
    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        boolean[] pickedUp = new boolean[orderAmount+1];
        for (int v = 0; v<vehicleAmount;v++) {
            int vehicleLocation = 0;
            solutionElement = solution[i];
            double currentVehicleTime = 0;
            while(solutionElement!=0){
                int solutionLocation;
                if (pickedUp[solutionElement]){
                    solutionLocation=orderDeliveryLocations[solutionElement-1];
                }else{
                    solutionLocation=orderPickupLocations[solutionElement-1];
                    pickedUp[solutionElement] = true;
                }
                if(vehicleLocation!=0) {
                    currentVehicleTime += travelTime[v][vehicleLocation - 1][solutionLocation - 1];
                }
                for (int tw = 0;tw<timeWindowAmounts[solutionLocation-1];tw++){
                    if(currentVehicleTime<lowerTimeWindows[solutionLocation-1][tw]){
                        currentVehicleTime = lowerTimeWindows[solutionLocation-1][tw];
                    }
                    if(currentVehicleTime>=lowerTimeWindows[solutionLocation-1][tw] && currentVehicleTime<=upperTimeWindows[solutionLocation-1][tw]) {
                        break;
                    }
                    if (tw==timeWindowAmounts[solutionLocation-1]-1){
                        return false;
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

    @Override
    public boolean checkSchedule(int vehicle, List<Integer> schedule) {
        return false;
    }
}
