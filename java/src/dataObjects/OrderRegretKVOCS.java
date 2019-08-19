package dataObjects;

import java.util.List;

public class OrderRegretKVOCS {
    public int order;
    public double regretValue;
    public List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList;
    public OrderRegretKVOCS(int order,double regretValue,List<VehicleOrderCostSchedule> vehicleOrderCostSchedule){
        this.order = order;
        this.regretValue=regretValue;
        this.vehicleOrderCostScheduleList =vehicleOrderCostSchedule;
    }
    public double getRegretValue(){
        return regretValue;
    }
}
