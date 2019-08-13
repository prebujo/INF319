package functions.utility;

import dataObjects.IDataSet;
import dataObjects.VehicleOrderCostSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveExpensiveInsertGreedy extends RemoveAndReinsert{


    private final int randomReduction;
    protected double[] orderPenalties;

    public RemoveExpensiveInsertGreedy(String name, int randomReduction, int lowerLimit, int upperLimit, Random random, IFeasibility feasibility, IDataSet dataSet){
        super(name,lowerLimit,upperLimit,random,feasibility,dataSet);
        this.orderPenalties = dataSet.getOrderPenalties();
        this.randomReduction = randomReduction;
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {

        int amountOfElements = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> ordersToRemove = getWorstElements(amountOfElements,randomReduction, solution);
        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertGreedy(ordersToRemove, solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;
    }

    protected HashSet<Integer> getWorstElements(int amount, double randomReduction, int[] solution) {
            HashSet<Integer> result = new HashSet<>();
            List<VehicleOrderCostSchedule> orderCost = getOrderCosts(solution);
            while(amount>0){
                orderCost.sort(Comparator.comparing(VehicleOrderCostSchedule::getNegativeCost));
                double position = random.nextDouble();
                position=Math.pow(position,randomReduction);
                int idx = (int) Math.round(position*(orderCost.size()-1));
                VehicleOrderCostSchedule element = orderCost.remove(idx);
                if (!feasibility.checkScheduleWithoutOrder(element.order,element.vehicle,element.schedule)){
                    continue;
                }
                result.add(element.order);
                List<Integer> newSchedule = getScheduleWithoutOrder(element.order,element.schedule);
                Double newVehicleCost = calculateVehicleCost(element.vehicle,newSchedule);
                boolean[] done = new boolean[orderAmount+1];
                for (int i = 0; i < newSchedule.size(); i++) {
                    int scheduleElement = newSchedule.get(i);
                    if(!done[scheduleElement]) {
                        for (int j = 0; j < orderCost.size(); j++) {
                            VehicleOrderCostSchedule costElement = orderCost.get(j);
                            if (costElement.order == scheduleElement) {
                                orderCost.set(j, new VehicleOrderCostSchedule(costElement.vehicle, costElement.order, newVehicleCost - calculateVehicleCostWithoutOrder(costElement.order, costElement.vehicle, newSchedule), newSchedule));
                            }
                        }
                        done[scheduleElement] = true;
                    }
                }
                amount--;
            }
            return result;
    }

    private List<VehicleOrderCostSchedule> getOrderCosts(int[] solution) {
        List<VehicleOrderCostSchedule> orderCost = new ArrayList<>();
        HashSet<Integer> orders = new HashSet<>();
        List<Integer> ordersList = new ArrayList<>();
        List<Integer> schedule = new ArrayList<>();
        VehicleOrderCostSchedule worstSchedule;
        int vehicle = 0;

        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if (solutionElement==0){
                if (!schedule.isEmpty()){
                    double vehicleTotalCost = calculateVehicleCost(vehicle,schedule);
                    while(!orders.isEmpty()){
                        Integer removed = ordersList.remove(0);
                        orderCost.add(new VehicleOrderCostSchedule(vehicle, removed, vehicleTotalCost - calculateVehicleCostWithoutOrder(removed, vehicle, schedule),schedule));
                        orders.remove(removed);
                    }
                }
                vehicle++;
                assert orders.isEmpty();
                assert ordersList.isEmpty();
                schedule = new ArrayList<>();
                continue;
            }
            if(!orders.contains(solutionElement)){
                orders.add(solutionElement);
                ordersList.add(solutionElement);
            }
            schedule.add(solutionElement);
        }
        while (!orders.isEmpty()){
            Integer removed = ordersList.remove(0);
            orderCost.add(new VehicleOrderCostSchedule(vehicleAmount,removed,orderPenalties[removed-1],new ArrayList<>()));
            orders.remove(removed);
        }
        return orderCost;
    }

//    protected int[] insertGreedy(HashSet<Integer> ordersToInsert, int[] solutionWithoutOrders) {
//        int[] result = solutionWithoutOrders.clone();
//        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solutionWithoutOrders);
//
//        while(ordersToInsert.size()>0) {
//            VehicleOrderScheduleCost bestOverallSchedule = getBestOverallScheduleAndCost(ordersToInsert.iterator(), vehicleSchedules);
//            ordersToInsert.remove(bestOverallSchedule.order);
//            if(bestOverallSchedule.vehicle!=vehicleAmount||!bestOverallSchedule.schedule.isEmpty()) {
//                vehicleSchedules.set(bestOverallSchedule.vehicle, bestOverallSchedule.schedule);
//                result = createNewSolution(bestOverallSchedule, result);
//            }
//        }
//        return result;
//    }

    private VehicleOrderCostSchedule getBestOverallScheduleAndCost(Iterator<Integer> iterator, List<List<Integer>> vehicleSchedules) {
        VehicleOrderCostSchedule bestOverallSchedule = findBestScheduleForOrder(iterator.next(), vehicleSchedules);
        while (iterator.hasNext()) {
            int o = iterator.next();
            VehicleOrderCostSchedule bestScheduleForOrder = findBestScheduleForOrder(o, vehicleSchedules);
            if (bestScheduleForOrder.cost < bestOverallSchedule.cost) {
                bestOverallSchedule = bestScheduleForOrder;
            }
        }
        return bestOverallSchedule;
    }

    protected int[] insertGreedy(HashSet<Integer> ordersToInsert, int[] solutionWithoutOrders) {
        int[] result = solutionWithoutOrders.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solutionWithoutOrders);
        List<VehicleOrderCostSchedule> bestSchedules = getBestSchedules(ordersToInsert.iterator(), vehicleSchedules);
        VehicleOrderCostSchedule bestOverallSchedule = bestSchedules.remove(0);
        ordersToInsert.remove(bestOverallSchedule.order);
        if(bestOverallSchedule.vehicle!=vehicleAmount||!bestOverallSchedule.schedule.isEmpty()) {
            vehicleSchedules.set(bestOverallSchedule.vehicle, bestOverallSchedule.schedule);
            result = createNewSolution(bestOverallSchedule, result);
        }

        while (ordersToInsert.size()>0){
            //update bestschedules here
            bestSchedules = updateBestSchedules(ordersToInsert.iterator(),bestOverallSchedule,bestSchedules, vehicleSchedules);
            bestOverallSchedule = bestSchedules.remove(0);
            ordersToInsert.remove(bestOverallSchedule.order);
            if(bestOverallSchedule.vehicle!=vehicleAmount&&!bestOverallSchedule.schedule.isEmpty()) {
                vehicleSchedules.set(bestOverallSchedule.vehicle, bestOverallSchedule.schedule);
                result = createNewSolution(bestOverallSchedule, result);
            }
        }

        return result;
    }

    private List<VehicleOrderCostSchedule> updateBestSchedules(Iterator<Integer> iterator, VehicleOrderCostSchedule updatedSchedule, List<VehicleOrderCostSchedule> bestSchedules, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>();
        while(iterator.hasNext()) {
            int orderToCheck = iterator.next();
            VehicleOrderCostSchedule newBestVOSC;
            for (int i = 0; i < bestSchedules.size(); i++) {
                VehicleOrderCostSchedule schedule = bestSchedules.get(i);
                if (schedule.order==orderToCheck){
                    newBestVOSC = schedule;
                    VehicleOrderCostSchedule bestSolutionInSchedule = findBestScheduleCostForOrderInVehicle(orderToCheck,updatedSchedule.vehicle,updatedSchedule.schedule);
                    if (schedule.vehicle==updatedSchedule.vehicle&&bestSolutionInSchedule.cost>=schedule.cost){
                        newBestVOSC = findBestScheduleForOrder(orderToCheck,vehicleSchedules);
                    } else if (bestSolutionInSchedule.cost<schedule.cost){
                        newBestVOSC = bestSolutionInSchedule;
                    }
                    if (result.size()==0){
                        result.add(newBestVOSC);
                    } else if (newBestVOSC.cost<result.get(0).cost){
                        result.add(0,newBestVOSC);
                    } else {
                        result.add(newBestVOSC);
                    }
                    break;
                }
            }
        }
        return result;
    }




    @Override
    public String getDescription() {
        return "Removes the k most expensive orders and reinserts them in the cheapest possible place.";
    }

}
