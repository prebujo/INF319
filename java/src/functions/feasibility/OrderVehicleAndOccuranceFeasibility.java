package functions.feasibility;

import dataObjects.IDataSet;

import java.util.List;

public class OrderVehicleAndOccuranceFeasibility implements IFeasibility {

    private final int vehicleAmount;
    private final boolean[][] vehicleCanVisitLocation;
    private final boolean[][] vehicleCanPickupOrder;
    private final int orderAmount;
    private final int[] orderDeliveryLocations;
    private final int[] orderPickupLocations;

    public OrderVehicleAndOccuranceFeasibility(IDataSet dataset) {
        this.vehicleAmount = dataset.getVehicleAmount();
        this.vehicleCanVisitLocation = dataset.getVehicleCanVisitLocation();
        this.vehicleCanPickupOrder = dataset.getVehicleCanPickupOrder();
        this.orderAmount = dataset.getOrderAmount();
        this.orderDeliveryLocations = dataset.getOrderDeliveryLocations();
        this.orderPickupLocations = dataset.getOrderPickupLocations();

    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        int[] pickedUpBy = new int[orderAmount+1];
        int[] deliveredBy = new int[orderAmount+1];
        for (int v = 0; v < vehicleAmount; v++) {
            solutionElement = solution[i];
            while (solutionElement != 0) {
                int solutionLocation;
                if (pickedUpBy[solutionElement]>0){
                    if(deliveredBy[solutionElement]>0||pickedUpBy[solutionElement]!=(v+1)){
                        return false;
                    }
                    solutionLocation=orderDeliveryLocations[solutionElement-1];
                    deliveredBy[solutionElement]=v+1;
                } else{
                    if (!vehicleCanPickupOrder[v][solutionElement-1]) {
                        return false;
                    }
                    solutionLocation=orderPickupLocations[solutionElement-1];
                    pickedUpBy[solutionElement] = v+1;
                }
                if (!vehicleCanVisitLocation[v][solutionLocation-1]) {
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
}
