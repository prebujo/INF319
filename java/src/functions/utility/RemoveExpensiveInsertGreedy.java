package functions.utility;

import dataObjects.IDataSet;
import dataObjects.ScheduleAndCost;
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
            List<ScheduleAndCost> orderCost = getOrderCosts(solution);
            while(amount>0){
                orderCost.sort(Comparator.comparing(ScheduleAndCost::getNegativeCost));
                double position = random.nextDouble();
                position=Math.pow(position,randomReduction);
                int idx = (int) Math.round(position*(orderCost.size()-1));
                ScheduleAndCost element = orderCost.remove(idx);
                result.add(element.order);
                List<Integer> newSchedule = getScheduleWithoutOrder(element.order,element.schedule);
                Double newVehicleCost = calculateVehicleCost(element.vehicle,newSchedule);
                boolean[] done = new boolean[orderAmount+1];
                for (int i = 0; i < newSchedule.size(); i++) {
                    int scheduleElement = newSchedule.get(i);
                    if(!done[scheduleElement]) {
                        for (int j = 0; j < orderCost.size(); j++) {
                            ScheduleAndCost costElement = orderCost.get(j);
                            if (costElement.order == scheduleElement) {
                                orderCost.set(j, new ScheduleAndCost(costElement.vehicle, costElement.order, newVehicleCost - calculateVehicleCostWithoutOrder(costElement.order, costElement.vehicle, newSchedule), newSchedule));
                            }
                        }
                        done[scheduleElement] = true;
                    }
                }
                amount--;
            }
            return result;
    }

    private List<ScheduleAndCost> getOrderCosts(int[] solution) {
        List<ScheduleAndCost> orderCost = new ArrayList<>();
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
                        if(feasibility.checkScheduleWithoutOrder(removed, vehicle, schedule)) {
                            orderCost.add(new ScheduleAndCost(vehicle, removed, vehicleTotalCost - calculateVehicleCostWithoutOrder(removed, vehicle, schedule),schedule));
                        }else{
                            orderCost.add(new ScheduleAndCost(vehicle,removed,0.0, schedule));
                        }
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
            orderCost.add(new ScheduleAndCost(vehicleAmount,removed,orderPenalties[removed-1],new ArrayList<>()));
            orders.remove(removed);
        }
        return orderCost;
    }

    protected int[] insertGreedy(HashSet<Integer> ordersToInsert, int[] solutionWithoutOrders) {
        int[] result = solutionWithoutOrders.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solutionWithoutOrders);

        while(ordersToInsert.size()>0) {
            ScheduleAndCost bestOverallSchedule = getBestOverallScheduleAndCost(ordersToInsert.iterator(), vehicleSchedules);
            ordersToInsert.remove(bestOverallSchedule.order);
            if(bestOverallSchedule.vehicle!=vehicleAmount||!bestOverallSchedule.schedule.isEmpty()) {
                vehicleSchedules.set(bestOverallSchedule.vehicle, bestOverallSchedule.schedule);
                result = createNewSolution(bestOverallSchedule, result);
            }
        }
        return result;
    }

    private ScheduleAndCost getBestOverallScheduleAndCost(Iterator<Integer> iterator, List<List<Integer>> vehicleSchedules) {
        ScheduleAndCost bestOverallSchedule = findBestScheduleForOrder(iterator.next(), vehicleSchedules);
        while (iterator.hasNext()) {
            int o = iterator.next();
            ScheduleAndCost bestScheduleForOrder = findBestScheduleForOrder(o, vehicleSchedules);
            if (bestScheduleForOrder.cost < bestOverallSchedule.cost) {
                bestOverallSchedule = bestScheduleForOrder;
            }
        }
        return bestOverallSchedule;
    }

    protected ScheduleAndCost findBestScheduleForOrder(Integer order, List<List<Integer>> vehicleSchedules) {
        List<Integer> bestSchedule = new ArrayList<>();
        int bestVehicle = 0;
        Double bestCost = Double.MAX_VALUE;
        for (int vehicle = 0; vehicle<vehicleSchedules.size();vehicle++) {
            Double cost;
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            for (int i = 0; i < schedule.size() +1; i++) {
                for (int j = i+1; j < schedule.size() + 2; j++) {
                    List<Integer> newSchedule = createNewSchedule(order, i, j, schedule);
                    if(feasibility.checkSchedule(vehicle, newSchedule)){
                        cost = calculateVehicleCost(vehicle,newSchedule);
                        if (cost<bestCost){
                            bestCost=cost;
                            bestSchedule=newSchedule;
                            bestVehicle=vehicle;
                        }
                    }
                }
            }
        }
        if (bestSchedule.size()==0){
            bestVehicle=vehicleAmount;
        }
        return new ScheduleAndCost(bestVehicle,order,bestCost,bestSchedule);
    }

    @Override
    public String getDescription() {
        return "Removes the k most expensive orders and reinserts them in the cheapest possible place.";
    }

}
