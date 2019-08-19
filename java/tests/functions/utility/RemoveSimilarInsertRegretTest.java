package functions.utility;

import dataObjects.IDataSet;
import dataObjects.OrderAndSimilarity;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import generators.ISolutionGenerator;
import generators.SolutionGenerator;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoveSimilarInsertRegretTest {

    @Test
    void solutions_generated_are_feasible_large() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("larger_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator removeAndReinsert = new RemoveSimilarInsertRegret("",4,1,7,3,random,feasibility,dataSet);
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
    void solutions_generated_are_feasible_medium() throws Throwable {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("medium_test_file");
        IFeasibility feasibility = new Feasibility(dataSet);

        Random random = new Random(11);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        IOperator removeAndReinsert = new RemoveSimilarInsertRegret("",4,1,5,3,random,feasibility,dataSet);
        int vehicleAmount = dataSet.getVehicleAmount();
        int orderAmount = dataSet.getOrderAmount();
        int[] solution = solutionGenerator.createDummyStartSolution(vehicleAmount,orderAmount);
        solution = removeAndReinsert.apply(solution);
        int iterations = 100140;
        while(iterations>0) {
            solution = removeAndReinsert.apply(solution);
            assertTrue(feasibility.check(solution));
            iterations--;
        }
    }

    @Test
    void returns_correct_similarities() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        RemoveSimilarInsertRegret removeAndReinsert = new RemoveSimilarInsertRegret("",4,1,3,3,new Random(1),feasibility,dataSet);

        int order = 3;
        List<OrderAndSimilarity> orderSimilarityList = removeAndReinsert.getOrderSimilarities(order);

        assertTrue(orderSimilarityList.size()==4);
        assertEquals(orderSimilarityList.get(0).order, 1);
        assertEquals(0.19166763397082842, orderSimilarityList.get(0).similarity);
        assertEquals(orderSimilarityList.get(1).order, 2);
        assertEquals(0.8096243556540229, orderSimilarityList.get(1).similarity);
        assertEquals(orderSimilarityList.get(2).order, 3);
        assertEquals(0.0, orderSimilarityList.get(2).similarity);
        assertEquals(orderSimilarityList.get(3).order, 4);
        assertEquals(1.629758113997644, orderSimilarityList.get(3).similarity);

    }

}