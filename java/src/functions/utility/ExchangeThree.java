package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.Random;

public class ExchangeThree implements IOperator {

    private final String name;
    private final IDataSet dataSet;
    private final Random random;
    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibility;

    public ExchangeThree(IDataSet dataSet, Random random, IFeasibility feasibility, String name  ){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.random = random;
        this.feasibility = feasibility;
        this.name = name;
    }

    @Override
    public int[] apply(int[] solution) {
        int vehicleChoice = random.nextInt(vehicleAmount);
        int vehicle = 0;

        int iteration = 0;

        int[] newSolution = solution.clone();

        while (iteration<2*orderAmount + vehicleAmount){
            int solutionElement = solution[iteration];
            if (vehicle==vehicleChoice){
                int startIndex = iteration;
                int counter = 0;
                while(solutionElement!=0){
                    counter++;
                    iteration++;
                    if(iteration>2*orderAmount + vehicleAmount){
                        break;
                    }
                    solutionElement=solution[iteration];
                }
                if(counter>2){
                    int choice1 = startIndex+random.nextInt(counter);
                    int choice2 = choice1;
                    while(choice1==choice2){
                        choice2 = startIndex+random.nextInt(counter);
                    }
                    int choice3 = choice2;
                    while (choice3==choice2||choice3==choice1){
                        choice3 = startIndex+random.nextInt(counter);
                    }

                    newSolution[choice1] = solution[choice2];
                    newSolution[choice2] = solution[choice3];
                    newSolution[choice3] = solution[choice1];
                }
                break;
            } else if(solutionElement==0){
                vehicle = vehicle+1;
            }
            iteration++;
        }
        if(feasibility.check(newSolution)) {
            return newSolution;
        }else{
            return solution;
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
