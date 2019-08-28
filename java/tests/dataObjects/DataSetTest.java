package dataObjects;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSetTest {

    @Test
    public void merge_column_and_row_returns_correct_matrix(){
        DataSet dataSet = new DataSet(3,5,3,2);

        double[][] initialMatrix = new double[][]{{0,7,3,8},{7,0,4,8},{3,4,0,8},{8,8,8,0}};
        double[][] expectedObject =new double[][]{{0,4,8},{4,0,8},{8,8,0}};
        List<List<Integer>> clusterList = new ArrayList<>();
        for (int i = 0; i<4;i++){
            clusterList.add(new ArrayList<>());
        }
        double[][] testedObject = dataSet.mergeRowAndColumn(0,2,initialMatrix, clusterList);
        assertEquals(3,testedObject.length);
        assertEquals(3,testedObject[0].length);
        assertEquals(3,clusterList.size());
        assertEquals(expectedObject[0][0],testedObject[0][0]);
        assertEquals(expectedObject[1][0],testedObject[1][0]);
        assertEquals(expectedObject[2][0],testedObject[2][0]);
        assertEquals(expectedObject[0][1],testedObject[0][1]);
        assertEquals(expectedObject[1][1],testedObject[1][1]);
        assertEquals(expectedObject[2][1],testedObject[2][1]);
        assertEquals(expectedObject[0][2],testedObject[0][2]);
        assertEquals(expectedObject[1][2],testedObject[1][2]);
        assertEquals(expectedObject[2][2],testedObject[2][2]);
    }

    @Test
    public void calculate_correct_Siluette_Coefficient(){
        DataSet dataSet = new DataSet(3,5,4,2);

        double[][] travelDistances = new double[][]{{0, 7, 3, 8}, {7, 0, 4, 8}, {3, 4, 0, 8}, {8, 8, 8, 0}};
        dataSet.setTravelDistances(travelDistances);

        double[][] initialMatrix = new double[][]{{0,7,3,8},{7,0,4,8},{3,4,0,8},{8,8,8,0}};
        List<List<Integer>> clusterList = new ArrayList<>();
        for (int i = 0; i<4;i++){
            clusterList.add(Arrays.asList(i));
        }
        double[][] mergedMatrix = dataSet.mergeRowAndColumn(0,2,initialMatrix, clusterList);

        double testedObject = dataSet.calculateSiluetteCoefficient(mergedMatrix,clusterList);

        assertEquals(0.7053571428571428,testedObject);
    }

    @Test
    public void set_location_cluster_returns_correct_cluster() {
        DataSet dataSet = new DataSet(3, 5, 5, 2);

        double[][] initialMatrix =new double[][]{{0,1,9,9,2},{1,0,8,8,2},{9,8,0,1,8},{9,8,1,0,8},{2,2,8,8,0}};
        dataSet.setTravelDistances(initialMatrix);

        dataSet.setLocationClusters();
        List<List<Integer>> testedObject = dataSet.getLocationClusters();
        assertEquals(testedObject.size(), 3);
        assertEquals(0,testedObject.get(0).get(0));
        assertEquals(1,testedObject.get(0).get(1));
        assertEquals(2,testedObject.get(1).get(0));
        assertEquals(3,testedObject.get(1).get(1));
        assertEquals(4,testedObject.get(2).get(0));

        //TODO: find best way of representing the clusters. possibly List of lists like methods..
    }

    @Test
    public void set_location_cluster_returns_correct_cluster_2() {
        DataSet dataSet = new DataSet(3, 5, 5, 2);

        double[][] initialMatrix =new double[][]{{0,8,3,10,9},{8,0,9,1,2},{3,9,0,10,8},{10,1,10,0,1},{9,2,8,1,0}};
        dataSet.setTravelDistances(initialMatrix);

        dataSet.setLocationClusters();
        List<List<Integer>> testedObject = dataSet.getLocationClusters();
        assertEquals(testedObject.size(), 3);
        assertEquals(0,testedObject.get(0).get(0));
        assertEquals(1,testedObject.get(1).get(0));
        assertEquals(3,testedObject.get(1).get(1));
        assertEquals(4,testedObject.get(1).get(2));
        assertEquals(2,testedObject.get(2).get(0));
    }
}