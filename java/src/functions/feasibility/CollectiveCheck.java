package functions.feasibility;

import dataObjects.IDataSet;

import java.util.List;

public class CollectiveCheck implements IFeasibility {

    private final TimeFeasible timeFeasible;
    private final OrderVehicleAndOccuranceFeasibility orderVehicleAndOccuranceFeasibility;
    private final WeightAndVolumeFeasible weightAndVolumeFeasible;
    private final FactoryDockFeasible factoryDockFeasible;

    public CollectiveCheck(IDataSet dataSet){
        this.factoryDockFeasible = new FactoryDockFeasible(dataSet);
        this.weightAndVolumeFeasible = new WeightAndVolumeFeasible(dataSet);
        this.orderVehicleAndOccuranceFeasibility = new OrderVehicleAndOccuranceFeasibility(dataSet);
        this.timeFeasible = new TimeFeasible(dataSet);
    }
    @Override
    public boolean check(int[] solution) {
        if (!factoryDockFeasible.check(solution)){
            return false;
        }
        if(!weightAndVolumeFeasible.check(solution)){
            return false;
        }
        if(!orderVehicleAndOccuranceFeasibility.check(solution)){
            return false;
        }
        if(!timeFeasible.check(solution)){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkSchedule(int vehicle, List<Integer> schedule) {
        return false;
    }
}
