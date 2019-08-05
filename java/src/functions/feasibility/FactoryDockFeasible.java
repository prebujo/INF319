package functions.feasibility;

import dataObjects.IDataSet;

import java.util.List;

public class FactoryDockFeasible implements IFeasibility {

    private final int[] factory;
    private final int[] factoryStopCapacity;
    private final int vehicleAmount;
    private final int factoryAmount;
    private final int[] orderDeliveryLocations;
    private final int[] orderPickupLocations;
    private final int orderAmount;

    public FactoryDockFeasible(IDataSet dataSet) {
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.factory = dataSet.getFactories();
        this.factoryAmount = dataSet.getFactoryAmount();
        this.factoryStopCapacity = dataSet.getFactoryStopCapacities();
        this.orderAmount = dataSet.getOrderAmount();
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        boolean[] pickedUp=new boolean[orderAmount];
        for(int v = 0;v<vehicleAmount;v++) {
            int solutionElement = solution[i];
            int vehicleLocation = 0;
            int counter = 1;
            while(solutionElement!=0) {
                int solutionLocation;
                if(!pickedUp[solutionElement-1]) {
                    solutionLocation = orderPickupLocations[solutionElement-1];
                    pickedUp[solutionElement-1] = true;
                } else{
                    solutionLocation = orderDeliveryLocations[solutionElement-1];
                }
                if(vehicleLocation!=0) {
                    if (factory[solutionLocation-1] != factory[vehicleLocation-1] || factory[solutionLocation-1] == 0) {
                        counter = 1;
                    } else {
                        counter++;
                        if (counter > factoryStopCapacity[factory[solutionLocation-1] - 1]) {
                            return false;
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

    @Override
    public boolean checkSchedule(int vehicle, List<Integer> schedule) {
        return false;
    }

    @Override
    public boolean checkScheduleWithOrderReplacement(int vehicle, int orderToReplace, int replacement, List<Integer> schedule) {
        return false;
    }
}
