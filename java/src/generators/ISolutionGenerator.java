package generators;

import functions.feasibility.IFeasibility;

import java.util.List;

public interface ISolutionGenerator {
    int[] randomize (int[] solution);
    int[] randomlyAssignOrders(int vehicles, int orders);
    int[] createDummySolution(int vehicles, int orders);

    int[] createDummyStartSolution(int vehicleAmount, int orderAmount);

    List<int[]> getRandomSolutions(int i, int vehicles,int orders, IFeasibility feasibility);
}
