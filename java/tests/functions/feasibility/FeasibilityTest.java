package functions.feasibility;

import dataObjects.IDataSet;
import functions.utility.*;
import generators.ISolutionGenerator;
import generators.SolutionGenerator;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeasibilityTest {

    @Test
    void if_solution_is_feasible_all_feasibilities_true() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        IFeasibility collectiveCheck = new CollectiveCheck(dataSet);

        Random random = new Random(13);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator removeAndReinsert = new RemoveRandomInsertFirst("",2,5,random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        int iterations = 100;
        while(iterations>0) {
            while (!feasibility.check(solution)) {
                solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            }
            assertTrue(collectiveCheck.check(solution));
            iterations--;
            solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        }
    }

    @Test
    void if_all_schedules_are_feasible_solution_is_feasible() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        Operator operator = new Operator("",random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();

        int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        int iterations = 100;
        while(iterations>0) {
            while (!checkAllSchedules(solution, operator,feasibility)) {
                solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            }
            assertTrue(feasibility.check(solution));
            iterations--;
            solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        }
    }

    @Test
    void solution_is_feasible_all_schedules_are_feasible() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        Operator operator = new Operator("",random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();

        int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        int iterations = 100;
        while(iterations>0) {
            while (!feasibility.check(solution)) {
                solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            }
            assertTrue(checkAllSchedules(solution, operator,feasibility));
            iterations--;
            solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        }
    }

    @Test
    void checkSchedule_returns_same_result_as_feasible() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        Operator operator = new Operator("",random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();

        int[] solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
        int iterations = 1000000;
        while(iterations>0) {
            solution = solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            assertEquals(checkAllSchedules(solution, operator,feasibility),feasibility.check(solution));
            iterations--;
        }
    }


    private boolean checkAllSchedules(int[] solution, Operator operator, IFeasibility feasibility) {

        List<List<Integer>> vehicleSchedules = operator.getVehicleSchedules(solution);
        for (int i = 0; i < vehicleSchedules.size() ; i++) {
            if(!feasibility.checkSchedule(i,vehicleSchedules.get(i))){
                return false;
            }
        }
        return true;
    }
}