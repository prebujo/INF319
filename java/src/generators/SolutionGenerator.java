package generators;

import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SolutionGenerator implements ISolutionGenerator {

    private final Random random;
    private IFeasibility feasibility;

    public SolutionGenerator(Random random, IFeasibility feasibility){
        this.random = random;
        this.feasibility = feasibility;
    }

    @Override
    public int[] randomize(int[] solution) {
        int index, temporary;
        for (int i = solution.length-1; i>0;i--){
            index = random.nextInt(i+1);
            if(index != i){
                temporary = solution[index];
                solution[index] = solution[i];
                solution[i] = temporary;
            }
        }
        return solution;
    }

//    @Override
//    public int[] randomlyAssignOrders(int vehicles, int orders) {
//        List<List<Integer>> assignedOrders = new ArrayList<>(vehicles+1);
//
//        for (int i = 0; i<vehicles+1;i++){
//            assignedOrders.add(new ArrayList<>());
//        }
//
//        int chosenVehicle;
//
//        for (int i = 1; i<=orders;i++){
//            chosenVehicle = random.nextInt(vehicles+1);
//            assignedOrders.get(chosenVehicle).add(i);
//        }
//
//
//
//        int[] result = new int[vehicles+2*orders];
//        int index = 0;
//
//        List<Integer> possibilities = new ArrayList<>();
//        List<Integer> vehicleOrders;
//        for (int i = 0; i < vehicles+1;i++){
//            vehicleOrders = assignedOrders.get(i);
//
//            if(vehicleOrders.size()>0) {
//                addPossibilityFromAssignedOrders(possibilities, vehicleOrders);
//                Integer temp;
//                boolean[] pickedUp = new boolean[orders];
//                while (possibilities.size() > 0) {
//                    int choice = random.nextInt(possibilities.size());
//                    result[index] = possibilities.get(choice);
//                    if(!pickedUp[result[index]-1]){
//                        pickedUp[result[index]-1] = true;
//                        if(vehicleOrders.size()>0){
//                            addPossibilityFromAssignedOrders(possibilities,vehicleOrders);
//                        }
//                    } else {
//                        temp = possibilities.remove(possibilities.size() - 1);
//                        if (choice != possibilities.size()) {
//                            possibilities.set(choice, temp);
//                        }
//                    }
//                    index++;
//
//                }
//            }
//
//            if(i != vehicles) {
//                result[index] = 0;
//                index++;
//            }
//        }
//        return result;
//    }

    @Override
    public int[] randomlyAssignOrders(int vehicles, int orders) {
        while(true) {
            List<List<Integer>> assignedOrders = new ArrayList<>(vehicles + 1);
            for (int i = 0; i < vehicles + 1; i++) {
                assignedOrders.add(new ArrayList<>());
            }

            int chosenVehicle;

            for (int i = 1; i <= orders; i++) {
                chosenVehicle = random.nextInt(vehicles + 1);
                assignedOrders.get(chosenVehicle).add(i);
            }
            boolean failed = false;
            List<List<Integer>> vehicleSchedules = new ArrayList<>();
            for (int i = 0; i < assignedOrders.size()-1; i++) {
                if (assignedOrders.get(i).size() == 0) {
                    vehicleSchedules.add(new ArrayList<>());
                } else {
                    List<Integer> randomFeasibleSchedule = createRandomFeasibleSchedule(i, assignedOrders.get(i), assignedOrders.get(assignedOrders.size()-1));
                    if (randomFeasibleSchedule.size() != 0) {
                        vehicleSchedules.add(randomFeasibleSchedule);
                    } else {
                        failed = true;
                        break;
                    }
                }
            }
            if(!failed){
                return createSolutionFromVehicleSchedules(vehicles, orders,assignedOrders.get(vehicles),vehicleSchedules);
            }
        }
    }

    private int[] createSolutionFromVehicleSchedules(int vehicles, int orders,List<Integer> dummyList, List<List<Integer>> vehicleSchedules) {
        int[] result = new int[vehicles+2*orders];
        int index = 0;
        for (List<Integer> schedule : vehicleSchedules) {
            for (Integer integer :schedule) {
                result[index++] = integer;
            }
            result[index++] = 0;
        }
        for (Integer integer:dummyList ) {
            result[index++] = integer;
            result[index++] = integer;
        }
        return result;
    }

    private List<Integer> createRandomFeasibleSchedule(int vehicle, List<Integer> assignedOrders, List<Integer> dummyOrders) {
        boolean feasible = false;
        List<Integer> result;
        while(true) {
            int tries = 20;
            List<Integer> schedule = createStraightSchedule(assignedOrders);
            while (tries > 0) {
                Collections.shuffle(schedule);
                if (feasibility.checkSchedule(vehicle, schedule)) {
                    feasible = true;
                    break;
                }
                tries--;
            }
            if (feasible){
                result=new ArrayList<>(schedule);
                break;
            } else {
                dummyOrders.add(assignedOrders.remove(random.nextInt(assignedOrders.size())));
            }
        }
        return feasible ? result:new ArrayList<>();
    }

    private List<Integer> createStraightSchedule(List<Integer> integers) {
        List<Integer> result = new ArrayList<>();
        for (Integer number : integers) {
            result.add(number);
            result.add(number);
        }
        return result;
    }

    private List<Integer> shuffleSchedule(List<Integer> schedule) {

        List<Integer> result = new ArrayList<>(schedule);

        for (int i = 0; i < result.size(); i++) {
            int newPosition = random.nextInt(result.size());

        }

        return null;
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
        int[] result = new int[vehicles+2*orders];
        int index = vehicles;
        for (int i = 1;i<=orders;i++){
            result[index] = i;
            index++;
            result[index] = i;
            index++;
        }
        return result;
    }

    @Override
    public int[] createDummyStartSolution(int vehicles, int orders){
        return createDummySolution(vehicles,orders);
    }

    @Override
    public List<int[]> getRandomSolutions(int i, int vehicles, int orders, IFeasibility feasibility) {
        List<int[]> result = new ArrayList<>();
        while(result.size()<i){
            int[] solution = randomlyAssignOrders(vehicles,orders);
            while(!feasibility.check(solution)){
                solution = randomlyAssignOrders(vehicles,orders);
            }
            result.add(solution);
        }
        return result;
    }
}
