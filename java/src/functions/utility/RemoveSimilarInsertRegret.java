package functions.utility;

import dataObjects.*;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveSimilarInsertRegret extends RemoveAndReinsert {
    private final int regretK;
    IDataSet dataSet;
    public RemoveSimilarInsertRegret(String name, int lowerLimit, int upperLimit, int regretK, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.dataSet = dataSet;
        this.regretK = regretK;
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        //remove orders that are similar. first run Shaw, then remove those similar
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        int randomOrder = 1 + random.nextInt(orderAmount);
        List<OrderAndSimilarity> orderSimilarities = getOrderSimilarities(randomOrder);
        orderSimilarities.sort(Comparator.comparing(OrderAndSimilarity::getSimilarity));
        HashSet<Integer> ordersToRemove = getMostSimilarOrders(amount,orderSimilarities, solution);
        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertRegret(ordersToRemove,solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;



        //and if orders can be picked up from the same vehicles or not, add also if orders are deliverd in the same factories
        //test if similar weight will work or test if better with small weight (wi + wj), the smaller the more likely they will fit together
        //experiment with the size of the greek constants to be multiplied.

        //reinsert similar orders based on regret-k.
    }

    private int[] insertRegret(HashSet<Integer> orders, int[] solution) {
        int[] result = solution.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);

        List<OrderRegretKVOCS> orderRegretKs = new ArrayList<>();
        List<OrderRegretKVOCS> firstPriority = new ArrayList<>();
            Iterator<Integer> ordersIterator = orders.iterator();
            while (ordersIterator.hasNext()) {
                int order = ordersIterator.next();
                List<VehicleOrderCostSchedule> kBestSchedules = getKBestSchedules(order, regretK, vehicleSchedules);
                OrderRegretKVOCS regretKAndVOCS = getRegretKValue(kBestSchedules);
                if (regretKAndVOCS.vehicleOrderCostScheduleList.size() < regretK) {
                    if (firstPriority.size() == 0) {
                        firstPriority.add(regretKAndVOCS);
                    } else if (firstPriority.get(0).vehicleOrderCostScheduleList.size() > regretKAndVOCS.vehicleOrderCostScheduleList.size()) {
                        firstPriority.add(regretKAndVOCS);
                    } else if (firstPriority.get(0).vehicleOrderCostScheduleList.size() == regretKAndVOCS.vehicleOrderCostScheduleList.size()) {
                        if (firstPriority.get(0).regretValue < regretKAndVOCS.regretValue) {
                            firstPriority.add(0, regretKAndVOCS);
                        } else if (firstPriority.get(0).regretValue == regretKAndVOCS.regretValue && firstPriority.get(0).vehicleOrderCostScheduleList.get(0).cost > regretKAndVOCS.vehicleOrderCostScheduleList.get(0).cost) {
                            firstPriority.add(0, regretKAndVOCS);
                        } else {
                            firstPriority.add(regretKAndVOCS);
                        }
                    } else {
                        firstPriority.add(regretKAndVOCS);
                    }
                } else if (orderRegretKs.size() == 0) {
                    orderRegretKs.add(regretKAndVOCS);
                } else if (regretKAndVOCS.regretValue > orderRegretKs.get(0).regretValue) {
                    orderRegretKs.add(0, regretKAndVOCS);
                } else if (regretKAndVOCS.regretValue == orderRegretKs.get(0).regretValue) {
                    if (regretKAndVOCS.vehicleOrderCostScheduleList.get(0).cost < orderRegretKs.get(0).vehicleOrderCostScheduleList.get(0).cost) {
                        orderRegretKs.add(0, regretKAndVOCS);
                    } else {
                        orderRegretKs.add(regretKAndVOCS);
                    }
                } else {
                    orderRegretKs.add(regretKAndVOCS);
                }
            }

            VehicleOrderCostSchedule chosenOrderSchedule;
            if (firstPriority.size() > 0) {
                chosenOrderSchedule = firstPriority.remove(0).vehicleOrderCostScheduleList.get(0);
            } else {
                chosenOrderSchedule = orderRegretKs.remove(0).vehicleOrderCostScheduleList.get(0);
            }

            result = createNewSolution(chosenOrderSchedule,result);
            vehicleSchedules.set(chosenOrderSchedule.vehicle,chosenOrderSchedule.schedule);
            orders.remove(chosenOrderSchedule.order);

        while (orders.size()>0){
            if (firstPriority.size()>0){
                for (int i = 0; i < firstPriority.size(); i++) {
                    OrderRegretKVOCS orderRegretKVOCSnew = updateOrderRegret(chosenOrderSchedule, firstPriority.get(i));
                    OrderRegretKVOCS orderRegretKVOCS = firstPriority.get(i);


                }

            }

            orders.remove(chosenOrderSchedule.order);
        }

        return new int[0];
    }

    private OrderRegretKVOCS updateOrderRegret(VehicleOrderCostSchedule chosenOrderSchedule, OrderRegretKVOCS orderRegretKVOCS) {
        int order = orderRegretKVOCS.vehicleOrderCostScheduleList.get(0).order;
        int vehicle = chosenOrderSchedule.vehicle;
        List<Integer> schedule = chosenOrderSchedule.schedule;
        VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
        if (bestVOCS.schedule.size()>0) {
            boolean updated = false;
            for (int vocsIdx = 0; vocsIdx <orderRegretKVOCS.vehicleOrderCostScheduleList.size();vocsIdx++) {
                if (vehicle == vehicleOrderCostSchedule.vehicle&&) {
                    //TODO: continue here... either update (quick) or create new...
                }
            }
        }
        return orderRegretKVOCS;
    }

    private OrderRegretKVOCS getRegretKValue(List<VehicleOrderCostSchedule> kBestSchedules) {

        double regretKValue = 0.0;

        for (int i = 1; i < kBestSchedules.size(); i++) {
            regretKValue += kBestSchedules.get(i).cost-kBestSchedules.get(0).cost;
        }
        return new OrderRegretKVOCS(regretKValue,kBestSchedules);
    }

    private List<VehicleOrderCostSchedule> getKBestSchedules(int order, int regretK, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>();
        int vehicle = 0;
        boolean notEnoughVehicles = false;
        double worstCost = 0.0;
        double bestCost = Double.MAX_VALUE;
        while(result.size()<regretK&&vehicle<vehicleSchedules.size()){
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
            if (result.size()==0){
                worstCost=bestCost=bestVOCS.cost;
                result.add(bestVOCS);
            }else if (bestVOCS.cost<bestCost){
                bestCost=bestVOCS.cost;
                result.add(0,bestVOCS);
            }else if (bestVOCS.cost>worstCost){
                worstCost=bestVOCS.cost;
                result.add(result.size()-1,bestVOCS);
            }
            vehicle++;
        }
        Comparator<VehicleOrderCostSchedule> comparing = Comparator.comparing(VehicleOrderCostSchedule::getCost);
        result.sort(comparing);

        if(vehicle==vehicleSchedules.size()&&result.size()<regretK){
            return result;
        }

        while (vehicle<vehicleSchedules.size()) {
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
            if (bestVOCS.cost<bestCost){
                bestCost=bestVOCS.cost;
                result.add(0,bestVOCS);
                result.remove(result.size()-1);
            }else if (bestVOCS.cost<worstCost){
                result.add(result.size()-1,bestVOCS);
                result.remove(result.size()-1);
                result.sort(comparing);
                worstCost=result.get(result.size()-1).cost;
            }
            vehicle++;
        }

        return result;
    }

    private List<OrderAndSimilarity> getOrderSimilarities(int randomOrder) {
        List<OrderAndSimilarity> result = new ArrayList<>();
        for (int order = 0; order<dataSet.getOrderAmount();order++){
            result.add(new OrderAndSimilarity(order+1, dataSet.getOrderSimilarity(randomOrder-1,order)));
        }
        return result;
    }



    private HashSet<Integer> getMostSimilarOrders(int amount, List<OrderAndSimilarity> orderSimilarities, int[] solution) {
        HashSet<Integer> result = new HashSet<>();
        int idx = 0;
        while (result.size()<amount){
            int order = orderSimilarities.get(idx).order;
            VehicleAndSchedule vehicleScheduleOfOrder = getVehicleScheduleOfOrder(order, solution);
            if (feasibility.checkScheduleWithoutOrder(order,vehicleScheduleOfOrder)){
                result.add(order);
            }
            idx++;
        }
        return result;
    }


}
