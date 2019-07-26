package functions.feasibility;

import java.util.List;

public interface IFeasibility {
    /**
     * checking if a solution is feasible
     * @param solution
     * @return true if feasible solution
     */
    boolean check(int[] solution);

    boolean checkSchedule(int vehicle, List<Integer> schedule);
}
