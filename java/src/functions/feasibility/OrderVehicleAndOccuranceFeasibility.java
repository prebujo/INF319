package functions.feasibility;

import dataObjects.IDataSet;

public class OrderVehicleAndOccuranceFeasibility implements IFeasibility {

    private final int vehicleAmount;
    private final boolean[][] vehicleCanVisitNode;
    private final boolean[][] vehicleCanPickupNode;
    private final int orderAmount;

    public OrderVehicleAndOccuranceFeasibility(IDataSet dataset) {
        this.vehicleAmount = dataset.getVehicleAmount();
        this.vehicleCanVisitNode = dataset.getVehicleCanVisitNode();
        this.vehicleCanPickupNode = dataset.getVehicleCanPickupOrder();
        this.orderAmount = dataset.getOrderAmount();

    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        int solutionElement;
        int[] pickedUpOrDeliveredBy = new int[2*orderAmount+1];
        for (int v = 0; v < vehicleAmount; v++) {
            solutionElement = solution[i];
            while (solutionElement != 0) {
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
                i++;
                solutionElement = solution[i];
            }
            i++;
        }
        return true;
    }
}
