package functions.utility;

import dataObjects.IDataSet;
import org.junit.jupiter.api.Test;
import reader.IReader;
import reader.Reader;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoOptTest {

    @Test
    public void findVehicleSchedule_returns_correct_schedule() throws Exception {
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile("Inst1_Ord_4_Veh_3_Loc_7");
        TwoOpt twoOpt = new TwoOpt("test",null,null,dataSet);
        int[] solution = new int[]{0,1,1,2,2,0};
        List<Integer> testedObject = twoOpt.getVehicleSchedule(1,solution);
        List<Integer> actual = Arrays.asList(1,1,2,2);
        assertTrue(testedObject.equals(actual));
    }
}