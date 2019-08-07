package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import generators.ISolutionGenerator;
import generators.SolutionGenerator;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SwapTwoFirstFit2Test {

    @Test
    void all_solutions_generated_are_feasible() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("larger_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator swapFirst = new SwapTwoFirstFit2("",random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        int iterations = 10000;
        while(iterations>0) {
            while(!feasibility.check(solution)){
                solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            }

            solution = swapFirst.apply(solution);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

}