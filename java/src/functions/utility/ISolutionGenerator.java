package functions.utility;

public interface ISolutionGenerator {
    int[] randomize (int[] solution);
    int[] randomlyAssignOrders(int vehicles, int orders);
    int[] createDummySolution(int vehicles, int orders);

    int[] createDummyStartSolution(int vehicleAmount, int orderAmount);
}
