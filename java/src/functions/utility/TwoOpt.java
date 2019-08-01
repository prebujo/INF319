package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TwoOpt extends Operator {

    public TwoOpt(String name, Random random, IFeasibility feasibility, IDataSet dataSet){
        super(name,random,feasibility,dataSet);
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        List<Integer> vehicleSchedule = Arrays.asList(0);
        int vehicleChoice=0;
        int tries = 0;
        while(vehicleSchedule.get(0)==0){
            if(tries>100){
                return solution.clone();
            }
            vehicleChoice = random.nextInt(vehicleAmount);
            vehicleSchedule = getVehicleSchedule(vehicleChoice,solution);
            tries++;
        }

        return perform2optOnSchedule(vehicleChoice,vehicleSchedule,solution);
    }

    @Override
    public String getDescription() {
        return "2-opt operator that picks a random vehicle and performs reverses on parts of the schedule until no more improvement is possible for this vehicle";
    }



    protected int[] perform2optOnSchedule(int vehicleChoice, List<Integer> vehicleSchedule, int[] solution) {
        double vehicleCost = calculateVehicleCost(vehicleChoice, vehicleSchedule);
        double bestCost = vehicleCost;
        List<Integer> bestSchedule=vehicleSchedule;
        boolean improved=true;
        while(improved) {
            improved=false;
            for (int i = 0; i < vehicleSchedule.size() - 1; i++) {
                for (int j = i + 1; j < vehicleSchedule.size(); j++) {
                    List<Integer> newSchedule = twoOptSwap(i, j, vehicleSchedule);
                    double newCost = bestCost;
                    if (feasibility.checkSchedule(vehicleChoice, newSchedule)&&!newSchedule.equals(vehicleSchedule)) {
                        newCost = calculateVehicleCost(vehicleChoice, newSchedule);
                    }
                    if (newCost < bestCost) {
                        bestCost = newCost;
                        bestSchedule = newSchedule;
                        improved=true;
                    }
                }
            }
            vehicleSchedule=bestSchedule;
        }


        return bestCost<vehicleCost ? createNewSolution(vehicleChoice,vehicleSchedule,solution):solution.clone();
    }

    protected List<Integer> twoOptSwap(int i, int j, List<Integer> vehicleSchedule) {
        List<Integer> result = new ArrayList<>();
        int idx = 0;
        while(idx<i){
            result.add(vehicleSchedule.get(idx++));
        }
        idx = j;
        while(idx>=i){
            result.add(vehicleSchedule.get(idx--));
        }
        idx=j+1;
        while(idx<vehicleSchedule.size()){
            result.add(vehicleSchedule.get(idx++));
        }
        return result;
    }
}
