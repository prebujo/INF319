package functions.utility;

import dataObjects.IDataSet;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.HashSet;

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
}