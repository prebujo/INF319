package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleOrderCostSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;

/**
 * Operator that removes randomly and tries to reinsert given amount of orders in a given solution.The removal operator selects random orders to getElementsToRemove
 * and the insertion operator reinserts the removed orders into a randomly selected vehicle if possible.
 */
public class RemoveAndReinsert extends Operator {

    protected int lowerLimit;
    protected int upperLimit;

    public RemoveAndReinsert(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet){
        super(name,random,feasibilityCheck,dataSet);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    protected VehicleOrderCostSchedule findBestScheduleForOrder(Integer order, List<List<Integer>> vehicleSchedules) {
        List<Integer> bestSchedule = new ArrayList<>();
        int bestVehicle = 0;
        Double bestCost = Double.MAX_VALUE;
        for (int vehicle = 0; vehicle<vehicleSchedules.size();vehicle++) {
            Double solutionIncreasedCost;
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            Double vehicleCost = calculateVehicleCost(vehicle,schedule);
            for (int i = 0; i < schedule.size() +1; i++) {
                for (int j = i+1; j < schedule.size() + 2; j++) {
                    List<Integer> newSchedule = createNewSchedule(order, i, j, schedule);
                    if(feasibility.checkSchedule(vehicle, newSchedule)){
                        solutionIncreasedCost = calculateVehicleCost(vehicle,newSchedule)-vehicleCost;
                        if (solutionIncreasedCost<bestCost){
                            bestCost=solutionIncreasedCost;
                            bestSchedule=newSchedule;
                            bestVehicle=vehicle;
                        }
                    }
                }
            }
        }
        if (bestSchedule.size()==0){
            bestVehicle=vehicleAmount;
        }
        return new VehicleOrderCostSchedule(bestVehicle,order,bestCost,bestSchedule);
    }

    protected VehicleOrderCostSchedule findBestScheduleCostForOrderInVehicle(Integer order, int vehicle, List<Integer> vehicleSchedule) {
        List<Integer> bestSchedule = new ArrayList<>();
        int bestVehicle = 0;
        Double bestCost = Double.MAX_VALUE;
        Double solutionIncreasedCost;
        Double vehicleCost = calculateVehicleCost(vehicle, vehicleSchedule);
        for (int i = 0; i < vehicleSchedule.size() + 1; i++) {
            for (int j = i + 1; j < vehicleSchedule.size() + 2; j++) {
                List<Integer> newSchedule = createNewSchedule(order, i, j, vehicleSchedule);
                if (feasibility.checkSchedule(vehicle, newSchedule)) {
                    solutionIncreasedCost = calculateVehicleCost(vehicle, newSchedule) - vehicleCost;
                    if (solutionIncreasedCost < bestCost) {
                        bestCost = solutionIncreasedCost;
                        bestSchedule = newSchedule;
                    }
                }
            }
        }
        return new VehicleOrderCostSchedule(vehicle, order, bestCost, bestSchedule);
    }
}
