package functions.feasibility;

import dataObjects.IDataSet;

public class TimeFeasible implements IFeasibility {

    private final int vehicleAmount;
    private final double[][][] travelTime;
    private final int[] vehicleStartingLocation;
    private final double[][] lowerTimeWindows;
    private final double[][] upperTimeWindows;
    private final int[] timeWindowAmounts;

    public TimeFeasible(IDataSet dataSet){
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.travelTime = dataSet.getTravelTimes();
        this.vehicleStartingLocation = dataSet.getVehicleStartingLocations();
        this.lowerTimeWindows = dataSet.getLowerTimeWindows();
        this.upperTimeWindows = dataSet.getUpperTimeWindows();
        this.timeWindowAmounts = dataSet.getTimeWindowAmounts();

    }
    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        for (int v = 0; v<vehicleAmount;v++) {
            int vehicleLocation = vehicleStartingLocation[v];
            solutionElement = solution[i];
            double currentVehicleTime = 0;
            while(solutionElement!=0){
                currentVehicleTime +=travelTime[v][vehicleLocation-1][solutionElement-1];
                for (int tw = 0;tw<timeWindowAmounts[solutionElement-1];tw++){
                    if(currentVehicleTime<lowerTimeWindows[solutionElement-1][tw]){
                        currentVehicleTime = lowerTimeWindows[solutionElement-1][tw];
                    }
                    if(currentVehicleTime>=lowerTimeWindows[solutionElement-1][tw] && currentVehicleTime<=upperTimeWindows[solutionElement-1][tw]) {
                        break;
                    }
                    if (tw==timeWindowAmounts[solutionElement-1]-1){
                        return false;
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
}
