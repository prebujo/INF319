package Heuristic;

import dataObjects.IData;
import functions.ObjectiveFunction;
import functions.feasibility.CollectiveCheck;
import functions.feasibility.Feasible;
import functions.feasibility.IFeasibility;
import functions.utility.ISolutionGenerator;
import functions.utility.SolutionGenerator;

public class RandomHeuristic implements IHeuristic{

    private final IData dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibilityCheck;

    public RandomHeuristic(IData dataSet){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = new Feasible(dataSet);
    }

    @Override
    public int[] optimize(int[] startSolution) {


        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        int currentObjective = objectiveFunction.calculateSolution(startSolution);
        int bestObjective = currentObjective;
        int[] bestSolution = startSolution;
        int[] currentSolution;
        ISolutionGenerator solutionGenerator = new SolutionGenerator(1);

        int i = 10000;
        while (i>0){
            currentSolution=solutionGenerator.randomlyAssignOrders(vehicleAmount,orderAmount);
            if(!feasibilityCheck.check(currentSolution)){
                continue;
            }
            currentObjective = objectiveFunction.calculateSolution(currentSolution);
            if(currentObjective<bestObjective){
                bestSolution = currentSolution;
                bestObjective = currentObjective;
            }
            i--;
        }
        return bestSolution;
    }
}
