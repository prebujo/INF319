package dataObjects;

import java.util.List;

public class ScheduleAndCost {
    public List<Integer> schedule;
    public Double cost;
    public int vehicle;
    public int order;

    public ScheduleAndCost(int vehicle,int order, Double cost, List<Integer> schedule){
        this.order=order;
        this.vehicle = vehicle;
        this.cost = cost;
        this.schedule = schedule;
    }
    public Double getCost(){
        return cost;
    }
}
