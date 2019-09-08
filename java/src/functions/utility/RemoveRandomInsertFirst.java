package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleOrderCostSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveRandomInsertFirst extends RemoveAndReinsert{
    public RemoveRandomInsertFirst(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.description = "remove and reinsert operator that removes between "+lowerLimit+" to "+upperLimit+" random elements from solution and reinserts them in randomly selected vehicles";
    }

    @Override
    public int[] apply(int[] solution) {
        int amountOfElements = lowerLimit + random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> ordersToRemove = getElementsToRemove(amountOfElements,solution);
        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);

        return insertOrdersInFirstFit(ordersToRemove, solutionWithoutOrders);

    }

    private int[] insertOrdersInFirstFit(HashSet<Integer> orders, int[] solution) {

        int[] result = solution.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);

        Iterator<Integer> iterator = orders.iterator();

        while(iterator.hasNext()){
            int order = iterator.next();

            List<Integer> newSchedule = new ArrayList<>();
            int idx = 0; //TODO: change to 0 when I am certain code works.
            int vehicle = 0;
            int tries = 0;
            boolean foundNew = false;
            List<Integer> vehicles = Stream.iterate(1,n->n+1).limit(vehicleAmount-1).collect(Collectors.toList());
            while(vehicles.size()>0){
                idx = random.nextInt(vehicles.size());
                vehicle = vehicles.remove(idx);
                newSchedule = insertOrderInVehicleFirstRandomFit(order,vehicle,vehicleSchedules.get(vehicle));
                tries++;
                if (newSchedule.size()>0){
                    foundNew=true;
                    break;
                }
            }
            if(foundNew) {
                vehicleSchedules.set(vehicle, newSchedule);
                result = createNewSolution(new VehicleOrderCostSchedule(vehicle, order, 0.0, newSchedule), result);
            }
        }
        return result;
    }

    private List<Integer> insertOrderInVehicleFirstRandomFit(int order, int vehicle, List<Integer> vehicleSchedule) {
        List<Integer> failResult = new ArrayList<>();
        int possiblePositions = vehicleSchedule.size()+1;//can insert order on all positions in schedule apart from last
        List<Integer> possiblePositionsList = Stream.iterate(0,n->n+1).limit(possiblePositions).collect(Collectors.toList());
        while(possiblePositionsList.size()>0){
            int startPosition = possiblePositionsList.remove(random.nextInt(possiblePositionsList.size()));
            for (int i = startPosition; i < possiblePositions; i++) {
                for (int j = i+1; j <possiblePositions+1 ; j++) {
                    List<Integer> newVehicleSchedule = createNewSchedule(order,i,j,vehicleSchedule);
                    if (feasibility.checkSchedule(vehicle,newVehicleSchedule)){
                        return newVehicleSchedule;
                    }
                }
            }
        }
        return failResult;
    }

    protected HashSet<Integer> getElementsToRemove(int amountOfElements, int[] solution){
        HashSet<Integer> result = new HashSet<>(amountOfElements);
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);

        while (amountOfElements>0){
            int choice;
            int vehicle;
            List<Integer> vehicleSchedule=new ArrayList<>();
            do {
                choice = random.nextInt(orderAmount) + 1;
                vehicle = getVehicle(choice,vehicleSchedules);
                if(vehicle!=vehicleAmount) {
                    vehicleSchedule = vehicleSchedules.get(vehicle);
                } else if(!result.contains(choice)){
                    break;
                }
            }while(result.contains(choice)||!feasibility.checkScheduleWithoutOrder(choice,vehicle,vehicleSchedule));
            result.add(choice);
            if (vehicle!=vehicleAmount) {
                vehicleSchedules.set(vehicle, getScheduleWithoutOrder(choice,vehicleSchedule));
            }
            amountOfElements--;
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "removes random orders and reinserts them in the first fitting position in random vehicle";
    }
}
