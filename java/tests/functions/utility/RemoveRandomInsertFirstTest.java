package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.CollectiveCheck;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import generators.ISolutionGenerator;
import generators.SolutionGenerator;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RemoveRandomInsertFirstTest {

    @Test
    void all_solutions_generated_are_feasible() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        IFeasibility collectiveCheck = new CollectiveCheck(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random, feasibility);
        IOperator removeAndReinsert = new RemoveRandomInsertFirst("",2,5,random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int[] solution = solutionGenerator.createDummyStartSolution(vehicleAmount,orderAmount);
        solution = removeAndReinsert.apply(solution);
        int iterations = 50000;
        while(iterations>0) {
            solution = removeAndReinsert.apply(solution);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

}