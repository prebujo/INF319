package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExchangeThree extends Operator {

    private final String name;
    private final IDataSet dataSet;
    private final Random random;
    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibility;
    private final String description= "exchanges the order of 3 scheduled pickups/deliveries for a vehicle";

    public ExchangeThree(String name, Random random, IFeasibility feasibility, IDataSet dataSet){
        super(name,random,feasibility,dataSet);
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.random = random;
        this.feasibility = feasibility;
        this.name = name;
    }

    @Override
    public int[] apply(int[] solution) {
        int[] result = solution.clone();
        int tries = 10;
        boolean fits = false;
        while(tries>0){
            int vehicleChoice = random.nextInt(vehicleAmount);
            List<Integer> vehicleSchedule = getVehicleSchedule(vehicleChoice,solution);
            if (vehicleSchedule.size()<4){
                tries--;
                continue;
            }
            int exchangetries = 10;
            while(exchangetries>0){
                int choice1 = random.nextInt(vehicleSchedule.size());
                int choice2 = choice1;
                while(choice1==choice2){
                    choice2 = random.nextInt(vehicleSchedule.size());
                }
                int choice3 = choice2;
                while (choice3==choice2||choice3==choice1){
                    choice3 = random.nextInt(vehicleSchedule.size());
                }
                List<Integer> newVehicleSchedule=exchangeThreePositions(choice1,choice2,choice3, vehicleSchedule);
                if (feasibility.checkSchedule(vehicleChoice,newVehicleSchedule)){
                    fits=true;
                    result=createNewSolution(vehicleChoice,newVehicleSchedule,solution);
                }
                exchangetries--;
            }
            if (fits){
                break;
            }
            tries--;
        }

        return result;
    }

    private List<Integer> exchangeThreePositions(int choice1, int choice2, int choice3, List<Integer> vehicleSchedule) {
        List<Integer> result = new ArrayList<>(vehicleSchedule);
        int temp = result.get(choice1);
        result.set(choice1,result.get(choice2));
        result.set(choice2,result.get(choice3));
        result.set(choice3,temp);
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "exchanges 3 positions in a random vehicles schedule, returns first feasible exchange";
    }
}

