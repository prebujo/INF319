package functions.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SolutionGenerator implements ISolutionGenerator {

    private final Random random;

    public SolutionGenerator(Random random){
        this.random = random;
    }

    @Override
    public int[] randomize(int[] solution) {
        int index, temporary;
        for (int i = solution.length-1; i>0;i--){
            index = random.nextInt(i+1);
            if(index != 1){
                temporary = solution[index];
                solution[index] = solution[i];
                solution[i] = temporary;
            }
        }
        return solution;
    }

    @Override
    public int[] randomlyAssignOrders(int vehicles, int orders) {
        orders -= vehicles;
        List<List<Integer>> assignedOrders = new ArrayList<>(vehicles+1);

        for (int i = 0; i<vehicles+1;i++){
            assignedOrders.add(new ArrayList<>());
        }

        int chosenVehicle;

        for (int i = 1; i<=orders;i++){
            chosenVehicle = random.nextInt(vehicles+1);
            assignedOrders.get(chosenVehicle).add(i);
        }



        int[] result = new int[vehicles+2*orders];
        int index = 0;

        List<Integer> possibilities = new ArrayList<>();
        List<Integer> vehicleOrders;
        for (int i = 0; i < vehicles+1;i++){
            vehicleOrders = assignedOrders.get(i);

            if(vehicleOrders.size()>0) {
                addPossibilityFromAssignedOrders(possibilities, vehicleOrders);
                Integer temp;
                while (possibilities.size() > 0) {
                    int choice = random.nextInt(possibilities.size());
                    result[index] = possibilities.get(choice);
                    if(result[index]<=orders){
                        possibilities.add(result[index]+orders+vehicles);
                        if(vehicleOrders.size()>0){
                            addPossibilityFromAssignedOrders(possibilities,vehicleOrders);
                        }
                    }
                    temp = possibilities.remove(possibilities.size() - 1);
                    if(choice!=possibilities.size()) {
                        possibilities.set(choice, temp);
                    }
                    index++;
                }
            }

            if(i != vehicles) {
                result[index] = 0;
                index++;
            }
        }
        return result;
    }

    private void addPossibilityFromAssignedOrders(List<Integer> possibilities, List<Integer> vehicleOrders) {
        int randomOrder = random.nextInt(vehicleOrders.size());
        possibilities.add(vehicleOrders.get(randomOrder));
        Integer temp = vehicleOrders.remove(vehicleOrders.size() - 1);
        if(randomOrder!=vehicleOrders.size()) {
            vehicleOrders.set(randomOrder, temp);
        }
    }

    @Override
    public int[] createDummySolution(int vehicles, int orders) {
        orders -= vehicles;
        int[] result = new int[vehicles+2*orders];
        int index = vehicles;
        for (int i = 1;i<=orders;i++){
            result[index] = i;
            index++;
        }
        for (int i = orders+vehicles+1;i<=2*orders+vehicles;i++){
            result[index] = i;
            index++;
        }
        return result;
    }
}
