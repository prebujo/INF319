package functions;

import dataObjects.IData;

public class WeightFeasible implements IFeasibility {
    private final IData dataset;
    private final int vehicleAmount;
    private final int[] vehicleCapacities;
    private final int orderAmount;

    public WeightFeasible(IData dataSet){
        this.dataset = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.vehicleCapacities = dataSet.getVehicleCapacities();
        this.orderAmount = dataSet.getOrderAmount();
    }
    @Override
    public boolean check(int[] solution) {
        boolean result = true;

        int i = 0;
        for (int v = 0; v<vehicleAmount;v++){
            int weightOnVehicle = 0;
            int solutionElement = solution[i];
            while(solutionElement!=0){
                if(solutionElement<=orderAmount){
                    weightOnVehicle +=
                }
                else {
                    weightOnVehicle
                }

                if (weightOnVehicle>vehicleCapacities[v]){
                    result = false;
                    break;
                }
            }
            if(weightOnVehicle>vehicleCapacities[v]){
                break;
            }
            i++;
        }
        return result;
    }
}
