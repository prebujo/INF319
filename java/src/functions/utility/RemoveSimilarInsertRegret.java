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
            addRegretAndVOCSInCorrectPosition(orderRegretKs, firstPriority, regretKAndVOCS);
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
        List<OrderRegretKVOCS> fromFirstPriorityToNormal = new ArrayList<>();
        List<OrderRegretKVOCS> fromNormalToFirstPriority = new ArrayList<>();
        while (orders.size()>0){
            if (firstPriority.size()>0){
                for (int i = 0; i < firstPriority.size(); i++) {
                    OrderRegretKVOCS orderRegretKVOCSnew = updateOrderRegretFirstPriority(chosenOrderSchedule, firstPriority.remove(i), vehicleSchedules);
                    if (orderRegretKVOCSnew.vehicleOrderCostScheduleList.size()==regretK) {
                        fromFirstPriorityToNormal.add(orderRegretKVOCSnew);
                    }else{
                        addFirstPriorityInCorrectPosition(firstPriority, orderRegretKVOCSnew);
                    }
                }
            }
            if (orderRegretKs.size()>0){
                for (int i = 0; i < orderRegretKs.size(); i++) {
                    OrderRegretKVOCS orderRegretKVOCSnew = updateOrderRegret(chosenOrderSchedule, orderRegretKs.remove(i), vehicleSchedules);
                    addRegretAndVOCSInCorrectPosition(orderRegretKs,firstPriority,orderRegretKVOCSnew);
                }
            }

            if (fromFirstPriorityToNormal.size()>0){
                while(fromFirstPriorityToNormal.size()>0) {
                    addRegretAndVOCSInCorrectPosition(orderRegretKs,firstPriority,fromFirstPriorityToNormal.remove(0));
                }
            }
            if (firstPriority.size() > 0) {
                chosenOrderSchedule = firstPriority.remove(0).vehicleOrderCostScheduleList.get(0);
            } else {
                chosenOrderSchedule = orderRegretKs.remove(0).vehicleOrderCostScheduleList.get(0);
            }

            result = createNewSolution(chosenOrderSchedule,result);
            vehicleSchedules.set(chosenOrderSchedule.vehicle,chosenOrderSchedule.schedule);
            orders.remove(chosenOrderSchedule.order);
        }

        return result;
    }

    private void addRegretAndVOCSInCorrectPosition(List<OrderRegretKVOCS> orderRegretKs, List<OrderRegretKVOCS> firstPriority, OrderRegretKVOCS regretKAndVOCS) {
        if (regretKAndVOCS.vehicleOrderCostScheduleList.size() < regretK) {
            addFirstPriorityInCorrectPosition(firstPriority, regretKAndVOCS);
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

    private void addFirstPriorityInCorrectPosition(List<OrderRegretKVOCS> firstPriority, OrderRegretKVOCS regretKAndVOCS) {
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
    }

    private OrderRegretKVOCS updateOrderRegret(VehicleOrderCostSchedule chosenOrderSchedule, OrderRegretKVOCS orderRegretKVOCS, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList = orderRegretKVOCS.vehicleOrderCostScheduleList;
        int order = vehicleOrderCostScheduleList.get(0).order;
        int vehicle = chosenOrderSchedule.vehicle;
        List<Integer> schedule = chosenOrderSchedule.schedule;
        VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
        if (bestVOCS.schedule.size()>0) {
            int size = vehicleOrderCostScheduleList.size();
            for (int vocsIdx = 0; vocsIdx < size; vocsIdx++) {
                if (vehicle == vehicleOrderCostScheduleList.get(vocsIdx).vehicle) {
                    if (bestVOCS.cost<vehicleOrderCostScheduleList.get(vocsIdx).cost){ //either I have a new better schedule
                        vehicleOrderCostScheduleList.set(vocsIdx,bestVOCS);
                    } else { //or I have to search again to find them, could potentially only search for one schedule here... the best schedule apart from the selected ones
                        vehicleOrderCostScheduleList.remove(vocsIdx);//remove this since we cant be sure its among the best
                        vehicleOrderCostScheduleList = addKBestSchedule(order,regretK,vehicleSchedules,vehicleOrderCostScheduleList);
                    }
                    return getRegretKValue(vehicleOrderCostScheduleList);
                }
            }
            if (bestVOCS.cost<vehicleOrderCostScheduleList.get(size -1).cost){
                vehicleOrderCostScheduleList.set(size-1,bestVOCS);
                vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
                return getRegretKValue(vehicleOrderCostScheduleList);
            }
        }
        return orderRegretKVOCS;
    }

    private OrderRegretKVOCS updateOrderRegretFirstPriority(VehicleOrderCostSchedule chosenOrderSchedule, OrderRegretKVOCS orderRegretKVOCS, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList = orderRegretKVOCS.vehicleOrderCostScheduleList;
        int order = vehicleOrderCostScheduleList.get(0).order;
        int vehicle = chosenOrderSchedule.vehicle;
        List<Integer> schedule = chosenOrderSchedule.schedule;
        VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
        if (bestVOCS.schedule.size()>0) {
            boolean updated = false;
            VehicleOrderCostSchedule replaceableSchedule;
            for (int vocsIdx = 0; vocsIdx < vehicleOrderCostScheduleList.size(); vocsIdx++) {
                if (vehicle == vehicleOrderCostScheduleList.get(vocsIdx).vehicle) {
                    if (bestVOCS.cost<vehicleOrderCostScheduleList.get(vocsIdx).cost){ //either I have a new better schedule
                        vehicleOrderCostScheduleList.set(vocsIdx,bestVOCS);
                    }
                    //or there will be no other schedule to insert into since we have first priority, ie. return
                    return getRegretKValue(vehicleOrderCostScheduleList);
                }
            }
            //if I get through without hitting the same vehicle it means I have found a new potential schedule in the chosen vehicle
            vehicleOrderCostScheduleList.add(bestVOCS);
            vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
            return getRegretKValue(vehicleOrderCostScheduleList);
        }
        //if its not possible to insert Order in updated schedule, return same object
        return orderRegretKVOCS;
    }

    private List<VehicleOrderCostSchedule> addKBestSchedule(int order, int regretK, List<List<Integer>> vehicleSchedules, List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>(vehicleOrderCostScheduleList);
        boolean[] includedVehicle = new boolean[vehicleAmount];
        for (VehicleOrderCostSchedule costSchedule :
                vehicleOrderCostScheduleList) {
            includedVehicle[costSchedule.vehicle] = true;
        }

        int bestVehicle = 0;
        double bestCost = Double.MAX_VALUE;
        List<Integer> newBestSchedule = new ArrayList<>();
        for (int vehicle = 0; vehicle < vehicleSchedules.size(); vehicle++) {
            if (!includedVehicle[vehicle]){
                List<Integer> schedule = vehicleSchedules.get(vehicle);
                double costWithoutOrder = calculateVehicleCost(vehicle,schedule);
                for (int i = 0; i < schedule.size(); i++) {
                    for (int j = i+1; j < schedule.size(); j++) {
                        List<Integer> newSchedule = createNewSchedule(order,i,j,schedule);
                        if (feasibility.checkSchedule(vehicle,newSchedule)){
                            double cost = calculateVehicleCost(vehicle,newSchedule)-costWithoutOrder;
                            if (cost<bestCost){
                                bestCost=cost;
                                bestVehicle=vehicle;
                                newBestSchedule=newSchedule;
                            }
                        }
                    }
                }
            }
        }
        if (newBestSchedule.size()>0) {
            result.add(new VehicleOrderCostSchedule(bestVehicle, order, bestCost, newBestSchedule));
        }
        return result;
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
