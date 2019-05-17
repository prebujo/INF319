package Heuristic;

import dataObjects.DataResult;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.ISolutionGenerator;
import functions.utility.SolutionGenerator;

import java.util.ArrayList;
import java.util.Random;

public class RandomHeuristic implements IHeuristic{

    private final IDataSet dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibilityCheck;
    private final Random random;

    public RandomHeuristic(IDataSet dataSet, Random random){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = new Feasibility(dataSet);
        this.random = random;
    }

    @Override
    public IDataResult optimize() {

        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);
        int[] startSolution = solutionGenerator.createDummySolution(dataSet.getVehicleAmount(),dataSet.getOrderAmount());

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        double currentObjective = objectiveFunction.calculateSolution(startSolution);
        double bestObjective = currentObjective;
        int[] bestSolution = startSolution;
        int[] currentSolution;

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
        IDataResult dataResult = new DataResult(new ArrayList<>(), 1000,10000,0  );
        dataResult.setBestSolution(bestSolution);
        return dataResult;
    }
}
