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

import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RemoveExpensiveInsertGreedyTest {

    @Test
    void solutions_generated_are_feasible_large() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("larger_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        IFeasibility collectiveCheck = new CollectiveCheck(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator removeAndReinsert = new RemoveExpensiveInsertGreedy("",4,1,5,random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int[] solution = solutionGenerator.createDummyStartSolution(vehicleAmount,orderAmount);
        solution = removeAndReinsert.apply(solution);
        int iterations = 10000;
        while(iterations>0) {
            solution = removeAndReinsert.apply(solution);
            assertTrue(feasibility.check(solution)); //TODO: continue analysing here. Not giving feasible solution always
            iterations--;
        }
    }

    @Test
    void solutions_generated_are_feasible_medium() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        IFeasibility collectiveCheck = new CollectiveCheck(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator removeAndReinsert = new RemoveExpensiveInsertGreedy("",4,1,5,random,feasibility,dataSet);
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

    @Test
    void most_expensive_elements_are_in_dummy() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        RemoveExpensiveInsertGreedy removeAndReinsert = new RemoveExpensiveInsertGreedy(null,4,1,1,new Random(1),feasibility,dataSet);

        int[] solution = new int[]{0,3,3,0,4,4,0,1,1,2,2};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getWorstElements(2, 4, solution);

        assertTrue(mostExpensiveElements.size()==2);
        assertTrue(mostExpensiveElements.contains(1));
        assertTrue(mostExpensiveElements.contains(2));
    }
    @Test
    void returns_correctly_the_most_expensive_elements() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        RemoveExpensiveInsertGreedy removeAndReinsert = new RemoveExpensiveInsertGreedy(null,4,1,1,new Random(1),feasibility,dataSet);

        int[] solution = new int[]{1,2,1,2,0,3,3,0,4,4,0};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getWorstElements(2,4, solution);

        assertTrue(mostExpensiveElements.size()==2);
        assertTrue(mostExpensiveElements.contains(4));
        assertTrue(mostExpensiveElements.contains(3));
    }

    @Test
    void inserts_correctly() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        RemoveExpensiveInsertGreedy removeAndReinsert = new RemoveExpensiveInsertGreedy(null,4,1,1,new Random(1),feasibility,dataSet);

        int[] solution = new int[]{2,2,0,3,3,0,4,4,0,1,1};
        int[] expected = new int[]{2,2,0,1,3,3,1,0,4,4,0};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getWorstElements(1,4, solution);

        int[] testedObject = removeAndReinsert.insertGreedy(mostExpensiveElements,solution);
        assertEquals(expected[0],testedObject[0]);
        assertEquals(expected[1],testedObject[1]);
        assertEquals(expected[2],testedObject[2]);
        assertEquals(expected[3],testedObject[3]);
        assertEquals(expected[4],testedObject[4]);
        assertEquals(expected[5],testedObject[5]);
        assertEquals(expected[6],testedObject[6]);
        assertEquals(expected[7],testedObject[7]);
    }
}