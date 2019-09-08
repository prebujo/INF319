package generators;

import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SolutionGeneratorTest {

    @Test
    void all_solutions_generated_are_feasible() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("larger_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random,feasibility);

        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int iterations = 10000;
        while(iterations>0) {
            int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

    @Test
    void all_solutions_generated_are_feasible_xlarge() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("xlarge_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random,feasibility);

        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int iterations = 100;
        while(iterations>0) {
            int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

    @Test
    void all_solutions_generated_are_feasible_xxlarge() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("xxlarge_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random,feasibility);

        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int iterations = 20;
        while(iterations>0) {
            int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

}