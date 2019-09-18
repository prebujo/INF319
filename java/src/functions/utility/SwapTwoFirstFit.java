package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleAndSchedule;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwapTwoFirstFit extends Operator {
    public SwapTwoFirstFit(String name, Random random, IFeasibility feasibility, IDataSet dataSet) {
        super(name, random, feasibility, dataSet);
    }

    @Override
    public int[] apply(int[] solution) {

        int tries = 0;
        while (tries<100) {
            int choice1 = 1 + random.nextInt(orderAmount);
            int choice2 = choice1;
            while (choice1 == choice2) {
                choice2 = 1 + random.nextInt(orderAmount);
            }
            VehicleAndSchedule vehicleSchedule1 = getVehicleScheduleOfOrder(choice1,solution);
            VehicleAndSchedule vehicleSchedule2 = getVehicleScheduleOfOrder(choice2,solution);
            if(vehicleSchedule1.vehicle==vehicleSchedule2.vehicle){
                List<Integer> vehicleScheduleOrderReplacements = getVehicleScheduleOrderReplacements(choice1, choice2, vehicleSchedule1.schedule);
                if(feasibility.checkSchedule(vehicleSchedule1.vehicle, vehicleScheduleOrderReplacements)){
                    return createNewSolution(vehicleSchedule1.vehicle,vehicleScheduleOrderReplacements,solution);
                }
            }else if(feasibility.checkScheduleWithOrderReplacement(vehicleSchedule1.vehicle,choice1, choice2,vehicleSchedule1.schedule)&&
            feasibility.checkScheduleWithOrderReplacement(vehicleSchedule2.vehicle, choice2, choice1, vehicleSchedule2.schedule)) {
                return createNewSolution(choice1,choice2,vehicleSchedule1,vehicleSchedule2,solution);
            }
            tries++;
        }
        return solution;
    }

    private int[] createNewSolution(int choice1, int choice2, VehicleAndSchedule vehicleSchedule1, VehicleAndSchedule vehicleSchedule2, int[] solution) {
        int[] result = new int[solution.length];
        int resultIdx = 0;
        int vehicle = 0;
        int vehicle1 = vehicleSchedule1.vehicle;
        int vehicle2 = vehicleSchedule2.vehicle;
        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if(vehicle==vehicle1){
                for (Integer k: vehicleSchedule1.schedule) {
                    if(k==choice1){
                        result[resultIdx++]=choice2;
                    }else {
                        result[resultIdx++] = k;
                    }
                }
                resultIdx++;
                while(solution[i]!=0){
                    i++;
                    if(i==solution.length){
                        break;
                    }
                }
                vehicle++;
            }else if (vehicle==vehicle2){
                for (Integer k: vehicleSchedule2.schedule) {
                    if(k==choice2){
                        result[resultIdx++]=choice1;
                    }else {
                        result[resultIdx++] = k;
                    }
                }
                resultIdx++;
                while(solution[i]!=0){
                    i++;
                    if(i==solution.length){
                        break;
                    }
                }
                vehicle++;
            }else if(solutionElement==0){
                vehicle++;
                resultIdx++;
            }else{
                result[resultIdx++]=solutionElement;
            }
        }

        return result;
    }

    private List<Integer> getVehicleScheduleOrderReplacements(int choice1, int choice2, List<Integer> schedule) {
        List<Integer> newSchedule = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            int scheduleElement = schedule.get(i);
            if (scheduleElement==choice1){
                newSchedule.add(choice2);
            }else if(scheduleElement==choice2){
                newSchedule.add(choice1);
            }else{
                newSchedule.add(scheduleElement);
            }
        }
        return newSchedule;
    }
    @Override
    public String getDescription() {
        return "swaps the pickup and delivery of two random orders until it finds a feasible solution";
    }

}