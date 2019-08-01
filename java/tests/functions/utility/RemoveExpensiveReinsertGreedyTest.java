package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoveExpensiveReinsertGreedyTest {

    @Test
    void most_expensive_elements_are_in_dummy() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        RemoveExpensiveReinsertGreedy removeAndReinsert = new RemoveExpensiveReinsertGreedy(null,1,1,null,null,dataSet);

        int[] solution = new int[]{0,3,3,0,4,4,0,1,1,2,2};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getMostExpensiveElements(2, solution);

        assertTrue(mostExpensiveElements.size()==2);
        assertTrue(mostExpensiveElements.contains(1));
        assertTrue(mostExpensiveElements.contains(2));
    }
    @Test
    void returns_correctly_the_most_expensive_elements() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        RemoveExpensiveReinsertGreedy removeAndReinsert = new RemoveExpensiveReinsertGreedy(null,1,1,null,null,dataSet);

        int[] solution = new int[]{1,2,1,2,0,3,3,0,4,4,0};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getMostExpensiveElements(2, solution);

        assertTrue(mostExpensiveElements.size()==2);
        assertTrue(mostExpensiveElements.contains(3));
        assertTrue(mostExpensiveElements.contains(4));
    }

    @Test
    void inserts_correctly() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        IFeasibility feasibility = new Feasibility(dataSet);
        RemoveExpensiveReinsertGreedy removeAndReinsert = new RemoveExpensiveReinsertGreedy(null,1,1,null,feasibility,dataSet);

        int[] solution = new int[]{2,2,0,3,3,0,4,4,0,1,1};
        int[] expected = new int[]{2,2,0,1,3,3,1,0,4,4,0};

        HashSet<Integer> mostExpensiveElements = removeAndReinsert.getMostExpensiveElements(1, solution);

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