package Heuristic;

import dataObjects.IData;
import functions.ObjectiveFunction;
import functions.feasibility.Feasible;
import functions.utility.ISolutionGenerator;
import functions.utility.RemoveAndReinsert;
import functions.utility.SolutionGenerator;

public class AdaptiveLargeNeighbourhoodSearch implements IHeuristic{

    private final IData dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final Feasible feasibilityCheck;

    public AdaptiveLargeNeighbourhoodSearch(IData dataSet){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = new Feasible(dataSet);
    }

    @Override
    public int[] optimize(int[] startSolution) {

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        int[] bestSolution = startSolution;
        int[] currentSolution = startSolution;
        int currentObjective = objectiveFunction.calculateSolution(currentSolution);
        int bestObjective = currentObjective;


        ISolutionGenerator solutionGenerator = new SolutionGenerator(1);
        RemoveAndReinsert removeAndReinsert = new RemoveAndReinsert(dataSet,1, feasibilityCheck);
        int iteration = 0;
        int i = 10000;
        while (i>0){
            int j = 100;

            //segment
            while(j>0) {



                currentSolution = removeAndReinsert.apply(currentSolution);

                currentObjective = objectiveFunction.calculateSolution(currentSolution);
                if (currentObjective < bestObjective) {
                    iteration=i;
                    bestSolution = currentSolution;
                    bestObjective = currentObjective;
                }
                i--;
                j--;
            }
        }
        System.out.println("Found best solution after: "+iteration);


        return bestSolution;
    }
}
