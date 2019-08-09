package dataObjects;

import java.util.List;

public class VehicleCostSchedule {
    public List<Integer> schedule;
    public Double cost;
    public int vehicle;

    public VehicleCostSchedule(int vehicle,int order, Double cost, List<Integer> schedule){
        this.vehicle = vehicle;
        this.cost = cost;
        this.schedule = schedule;
    }
    public Double getCost(){
        return cost;
    }
    public Double getNegativeCost(){return -cost;}
}
