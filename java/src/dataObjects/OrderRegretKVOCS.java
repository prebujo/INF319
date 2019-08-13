package dataObjects;

import java.util.List;

public class OrderRegretKVOCS {
    public double regretValue;
    public List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList;
    public OrderRegretKVOCS(double regretValue,List<VehicleOrderCostSchedule> vehicleOrderCostSchedule){
        this.regretValue=regretValue;
        this.vehicleOrderCostScheduleList =vehicleOrderCostSchedule;
    }
    public double getRegretValue(){
        return regretValue;
    }
}
