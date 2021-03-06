package functions.feasibility;

import dataObjects.VehicleAndSchedule;

import java.util.List;

public interface IFeasibility {
    /**
     * checking if a solution is feasible
     * @param solution
     * @return true if feasible solution
     */
    boolean check(int[] solution);

    boolean checkSchedule(int vehicle, List<Integer> schedule);

    boolean checkScheduleWithOrderReplacement(int vehicle, int orderToReplace, int replacement, List<Integer> schedule);

    boolean checkScheduleWithoutOrder(int order, VehicleAndSchedule vehicleScheduleOfOrder);

    boolean checkScheduleWithoutOrder(int order, int vehicle, List<Integer> schedule);
}
