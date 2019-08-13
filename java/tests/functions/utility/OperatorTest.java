package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleOrderCostSchedule;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperatorTest {

    @Test
    public void vehicle_Schedule_Test() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("Inst1_Ord_4_Veh_3_Loc_7");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,2,1,2,0,0,3,3,0,4,4};

        List<Integer> vehicleSchedules = operator.getVehicleSchedule(0,solution);
        assertEquals(4,vehicleSchedules.size());
        assertEquals(1,vehicleSchedules.get(0));
        assertEquals(2,vehicleSchedules.get(1));
        assertEquals(1,vehicleSchedules.get(2));
        assertEquals(2,vehicleSchedules.get(3));
    }

    @Test
    public void create_i_j_schedule() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("Inst1_Ord_4_Veh_3_Loc_7");
        Operator operator = new Operator(null,null,null,dataSet);

        List<Integer> schedule = Arrays.asList(1,2,1,2);
        List<Integer> expected = Arrays.asList(1,3,2,1,2,3);

        List<Integer> vehicleSchedule = operator.createNewSchedule(3,1,5,schedule);
        assertEquals(6,vehicleSchedule.size());
        assertEquals(1,vehicleSchedule.get(0));
        assertEquals(3,vehicleSchedule.get(1));
        assertEquals(2,vehicleSchedule.get(2));
        assertEquals(3,vehicleSchedule.get(5));
    }

    @Test
    public void returns_correct_schedules1() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("Inst1_Ord_4_Veh_3_Loc_7");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,2,1,2,0,0,3,3,0,4,4};

        List<List<Integer>> vehicleSchedules = operator.getVehicleSchedules(solution);
        assertEquals(3,vehicleSchedules.size());
        assertTrue(vehicleSchedules.get(1).isEmpty());
        assertEquals(2,vehicleSchedules.get(0).get(1));
        assertEquals(1,vehicleSchedules.get(0).get(0));
        assertEquals(3,vehicleSchedules.get(2).get(0));
    }

    @Test
    public void returns_correct_schedules2() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,2,1,2,0,3,3,0,4,4,0};

        List<Integer> schedule1 = Arrays.asList(1,2,1,2);
        List<Integer> schedule2 = Arrays.asList(3,3);
        List<Integer> schedule3 = Arrays.asList(4,4);

        List<List<Integer>> testedSolution = operator.getVehicleSchedules(solution);

        assertTrue(testedSolution.size()==3);
        assertEquals(schedule1,testedSolution.get(0));
        assertEquals(schedule2,testedSolution.get(1));
        assertEquals(schedule3,testedSolution.get(2));
    }

    @Test
    public void calculates_Correct_Vehicle_Cost() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        Operator operator = new Operator(null,null,null,dataSet);

        List<Integer> schedule = Arrays.asList(1,2,3,1,2,3);

        Double resultWithOrder = operator.calculateVehicleCost(1,schedule);

        Double testedResult = operator.calculateVehicleCostWithoutOrder(2,1,schedule);

        assertTrue(resultWithOrder>testedResult);
        assertEquals(9908.244255854934,resultWithOrder);
        assertEquals(2590.2635167145245, testedResult);
    }

    @Test
    public void removes_Correct_Orders() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,2,1,2,0,3,3,0,4,4,0};
        HashSet<Integer> ordersToRemove = new HashSet<>();
        ordersToRemove.add(1);
        ordersToRemove.add(2);

        int[] expectedSolution = new int[]{0,3,3,0,4,4,0,1,1,2,2};

        int[] testedSolution = operator.removeOrdersFromSolution(ordersToRemove,solution);

        assertTrue(testedSolution.length==solution.length);
        assertEquals(expectedSolution[0],testedSolution[0]);
        assertEquals(expectedSolution[4],testedSolution[4]);
        assertEquals(expectedSolution[9],testedSolution[9]);
    }

    @Test
    public void returns_correct_solution() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,1,0,3,3,0,0,2,2,4,4};
        int[] expected = new int[]{1,1,0,3,4,3,4,0,0,2,2};
        VehicleOrderCostSchedule scheduleAndCost = new VehicleOrderCostSchedule(1,4,1.0,Arrays.asList(3,4,3,4));

        int[] testedObject = operator.createNewSolution(scheduleAndCost,solution);
        assertEquals(expected.length,testedObject.length);
        assertEquals(expected[3],testedObject[3]);
        assertEquals(expected[4],testedObject[4]);
        assertEquals(expected[5],testedObject[5]);
        assertEquals(expected[6],testedObject[6]);
        assertEquals(expected[7],testedObject[7]);
        assertEquals(expected[8],testedObject[8]);
        assertEquals(expected[9],testedObject[9]);
        assertEquals(expected[10],testedObject[10]);
        assertEquals(expected[0],testedObject[0]);
        assertEquals(expected[1],testedObject[1]);
    }

    @Test
    public void returns_correct_solution2() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("test_file");
        Operator operator = new Operator(null,null,null,dataSet);

        int[] solution = new int[]{1,1,0,3,3,4,4,0,0,2,2};
        int[] expected = new int[]{1,1,0,3,4,3,4,0,0,2,2};
        List<Integer> schedule = Arrays.asList(3,4,3,4);

        int[] testedObject = operator.createNewSolution(1,schedule,solution);
        assertEquals(expected.length,testedObject.length);
        assertEquals(expected[0],testedObject[0]);
        assertEquals(expected[1],testedObject[1]);
        assertEquals(expected[2],testedObject[2]);
        assertEquals(expected[3],testedObject[3]);
        assertEquals(expected[4],testedObject[4]);
        assertEquals(expected[5],testedObject[5]);
        assertEquals(expected[6],testedObject[6]);
        assertEquals(expected[7],testedObject[7]);
        assertEquals(expected[8],testedObject[8]);
        assertEquals(expected[9],testedObject[9]);
        assertEquals(expected[10],testedObject[10]);
    }

}