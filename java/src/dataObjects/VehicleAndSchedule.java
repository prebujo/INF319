package dataObjects;

import java.util.List;

public class VehicleAndSchedule {
    public int vehicle;
    public List<Integer> schedule;

    public VehicleAndSchedule(int vehicle,List<Integer> schedule){
        this.vehicle = vehicle;
        this.schedule = schedule;
    }
}
