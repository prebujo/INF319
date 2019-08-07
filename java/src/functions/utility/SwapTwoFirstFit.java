package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.Random;

public class SwapTwoFirstFit extends Operator {
    public SwapTwoFirstFit(String name, Random random, IFeasibility feasibility, IDataSet dataSet) {
        super(name, random, feasibility, dataSet);
    }
    @Override
    public int[] apply(int[] solution) {

        int[] result = solution.clone();

        boolean feasible = false;
        int tries =0;
        while(!feasible&&tries<100) {
            int choice1 = 1 + random.nextInt(orderAmount);
            int choice2 = choice1;

            while (choice1 == choice2) {
                choice2 = 1 + random.nextInt(orderAmount);
            }

            for (int i = 0; i < solution.length; i++) {
                if (result[i] == choice1) {
                    result[i] = choice2;
                } else if (result[i] == choice2) {
                    result[i] = choice1;
                }
            }
            feasible = feasibility.check(result);
            tries++;
        }
        if (tries==100){
            return solution;
        }
        return result;
    }
}
