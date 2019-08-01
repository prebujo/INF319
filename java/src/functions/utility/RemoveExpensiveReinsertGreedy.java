package functions.utility;

import dataObjects.IDataSet;
import dataObjects.OrderAndCost;
import dataObjects.ScheduleAndCost;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveExpensiveReinsertGreedy extends RemoveAndReinsert{


    protected double[] orderPenalties;

    public RemoveExpensiveReinsertGreedy(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibility, IDataSet dataSet){
        super(name,lowerLimit,upperLimit,random,feasibility,dataSet);
        this.orderPenalties = dataSet.getOrderPenalties();
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        int[] result = solution.clone();

        int amountOfElements = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        HashSet<Integer> ordersToRemove = getMostExpensiveElements(amountOfElements, solution);
        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertGreedy(ordersToRemove, solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;
    }

    protected int[] insertGreedy(HashSet<Integer> ordersToRemove, int[] solutionWithoutOrders) {
        int[] result = solutionWithoutOrders.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solutionWithoutOrders);

        List<ScheduleAndCost> bestOrderSchedules = new ArrayList<>();
        Iterator<Integer> iterator = ordersToRemove.iterator();
        while(iterator.hasNext()){
            bestOrderSchedules.add(findBestScheduleForOrder(iterator.next(),vehicleSchedules));
        }
        //sort list of schedules after cost
        bestOrderSchedules.sort(Comparator.comparing(ScheduleAndCost::getCost));
        //insert each order, cheapest first, and keep track of vehicle used to then update schedule for that order
        boolean[] touchedVehicle = new boolean[vehicleAmount];
        for (ScheduleAndCost scheduleAndCost:bestOrderSchedules) {
            if(!touchedVehicle[scheduleAndCost.vehicle]) {
                result = createNewSolution(scheduleAndCost, result);
                touchedVehicle[scheduleAndCost.vehicle]=true;
            }else {
                vehicleSchedules = getVehicleSchedules(result);
                ScheduleAndCost newSchedule = findBestScheduleForOrder(scheduleAndCost.order,vehicleSchedules);
                result = createNewSolution(newSchedule,result);
            }
        }
        return result;
    }

    private ScheduleAndCost findBestScheduleForOrder(Integer next, List<List<Integer>> vehicleSchedules) {
        return null;
    }

    protected HashSet<Integer> getMostExpensiveElements(int amount, int[] solution) {
        List<OrderAndCost> orderCost = getOrderCosts(solution);
        return getMostExpensiveOrders(amount,orderCost);
    }



    private List<OrderAndCost> getOrderCosts(int[] solution) {
        List<OrderAndCost> orderCost = new ArrayList<>();
        HashSet<Integer> orders = new HashSet<>();
        List<Integer> ordersList = new ArrayList<>();
        List<Integer> schedule = new ArrayList<>();


        int vehicle = 0;

        for (int i = 0; i < solution.length; i++) {
            int solutionElement = solution[i];
            if (solutionElement==0){
                if (!schedule.isEmpty()){
                    double vehicleTotalCost = calculateVehicleCost(vehicle,schedule);
                    while(!orders.isEmpty()){
                        Integer removed = ordersList.remove(0);
                        orderCost.add(new OrderAndCost(removed,vehicleTotalCost-calculateVehicleCostWithoutOrder(removed,vehicle,schedule)));
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
            orderCost.add(new OrderAndCost(removed,orderPenalties[removed]));
            orders.remove(removed);
        }
        return orderCost;
    }

    private HashSet<Integer> getMostExpensiveOrders(int amount, List<OrderAndCost> orderCost) {
        assert orderCost.size()>amount;
        HashSet<Integer> result = new HashSet<>();
        List<OrderAndCost> mostExpensiveOrders = new ArrayList<>();
        int cheapestIndex = 0;
        double cheapestCost = orderCost.get(0).cost;
        while(mostExpensiveOrders.size()<amount){
            OrderAndCost removed = orderCost.remove(0);
            mostExpensiveOrders.add(removed);
            if (removed.cost<cheapestCost){
                cheapestIndex = mostExpensiveOrders.size()-1;
                cheapestCost = removed.cost;
            }
        }
        for (OrderAndCost order:orderCost) {
            if (order.cost>cheapestCost){
                mostExpensiveOrders.set(cheapestIndex,order);
                cheapestCost = order.cost;
                for (int i = 0; i < mostExpensiveOrders.size(); i++) {
                    OrderAndCost orderAndCost = mostExpensiveOrders.get(i);
                    if (orderAndCost.cost<cheapestCost){
                        cheapestCost=orderAndCost.cost;
                        cheapestIndex=i;
                    }
                }
            }
        }
        for (OrderAndCost orderAndCost:mostExpensiveOrders) {
            result.add(orderAndCost.order);
        }
        return result;
    }


    @Override
    public String getDescription() {
        return "Removes the k most expensive orders and reinserts them in the cheapest possible place.";
    }

}
