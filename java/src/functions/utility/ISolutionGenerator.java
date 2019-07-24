package functions.utility;

public interface ISolutionGenerator {
    int[] randomize (int[] solution);
    int[] randomlyAssignOrders(int vehicles, int orders);
    int[] createDummySolution(int vehicles, int orders);

    int[] createStartSolution(int vehicleAmount, int orderAmount);
}
