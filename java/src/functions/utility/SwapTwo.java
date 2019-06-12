package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.Random;

public class SwapTwo implements IOperator {


    private final IDataSet dataSet;
    private final int orderAmount;
    private final int vehicleAmount;
    private final Random random;
    private final IFeasibility feasibility;
    private final String description;
    private String name;


    public SwapTwo(IDataSet dataSet, Random random, IFeasibility feasibility, String name, String description){
        this.dataSet = dataSet;
        this.orderAmount = dataSet.getOrderAmount();
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.random = random;
        this.feasibility = feasibility;
        this.name = name;
        this.description = description;
    }

    @Override
    public int[] apply(int[] solution) {

        int[] result = solution;

        int choice1 = 1+random.nextInt(orderAmount );
        int choice2 = choice1;

        while (choice1 == choice2) {
            choice2 = 1+random.nextInt(orderAmount);
        }

        for (int i = 0; i < solution.length; i++) {
            if (result[i] == choice1) {
                result[i] = choice2;
            } else if (result[i] == choice2) {
                result[i] = choice1;
            }
        }

        if (feasibility.check(result)) {
            return result;
        } else {
            return solution;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
