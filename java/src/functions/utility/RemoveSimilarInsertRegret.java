package functions.utility;

import dataObjects.*;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveSimilarInsertRegret extends RemoveAndReinsert {
    private final int regretK;
    private final int randomReduction;
    IDataSet dataSet;
    public RemoveSimilarInsertRegret(String name, int randomReduction,int lowerLimit, int upperLimit, int regretK, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.dataSet = dataSet;
        this.regretK = regretK;
        this.randomReduction = randomReduction;
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        //remove orders that are similar. first run Shaw, then remove those similar
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);
        int randomOrder = 1 + random.nextInt(orderAmount);
        int vehicle = getVehicle(randomOrder,vehicleSchedules);
        if (vehicle!=vehicleAmount) {
            while (!feasibility.checkScheduleWithoutOrder(randomOrder, vehicle, vehicleSchedules.get(vehicle))) {
                randomOrder = 1 + random.nextInt(orderAmount);
                vehicle = getVehicle(randomOrder, vehicleSchedules);
                if (vehicle==vehicleAmount){
                    break;
                }
            }
        }
        if (vehicle!=vehicleAmount) {
            vehicleSchedules.set(vehicle, getScheduleWithoutOrder(randomOrder, vehicleSchedules.get(vehicle)));
        }

        List<OrderAndSimilarity> orderSimilarities = getOrderSimilarities(randomOrder);
        orderSimilarities.sort(Comparator.comparing(OrderAndSimilarity::getSimilarity));

        HashSet<Integer> ordersToRemove = getMostSimilarOrders(amount,orderSimilarities, vehicleSchedules);
        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertRegret(ordersToRemove,solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;
    }

    protected int[] insertRegret(HashSet<Integer> orders, int[] solution) {
        int[] result = solution.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);

        List<OrderRegretKVOCS> orderRegretKs = new ArrayList<>();
        List<OrderRegretKVOCS> firstPriorityList = new ArrayList<>();
        Iterator<Integer> ordersIterator = orders.iterator();
        List<Integer> ordersToRemove = new ArrayList<>();
        while (ordersIterator.hasNext()) {
            int order = ordersIterator.next();
            List<VehicleOrderCostSchedule> kBestSchedules = getKBestSchedules(order, regretK, vehicleSchedules);
            OrderRegretKVOCS regretKAndVOCS = getRegretKValue(order,kBestSchedules);
            if (regretKAndVOCS.vehicleOrderCostScheduleList.size()==0){
                ordersToRemove.add(order);
                continue;
            }
            addRegretAndVOCSInCorrectPosition(orders.size()-firstPriorityList.size(), orderRegretKs,firstPriorityList, regretKAndVOCS);
        }
        while (ordersToRemove.size()>0){
            orders.remove(ordersToRemove.remove(0));
        }

        VehicleOrderCostSchedule chosenOrderSchedule;
        if (firstPriorityList.size() > 0) {
            chosenOrderSchedule = firstPriorityList.remove(0).vehicleOrderCostScheduleList.get(0);
        } else if (orderRegretKs.size()>0){
            chosenOrderSchedule = orderRegretKs.remove(0).vehicleOrderCostScheduleList.get(0);
        } else {
            return result;
        }

        result = createNewSolution(chosenOrderSchedule,result);
        vehicleSchedules.set(chosenOrderSchedule.vehicle,chosenOrderSchedule.schedule);
        orders.remove(chosenOrderSchedule.order);
        List<OrderRegretKVOCS> fromFirstPriorityToNormal = new ArrayList<>();
        while (orders.size()>0){
            if (firstPriorityList.size()>0){
                ArrayList<OrderRegretKVOCS> oldFirstPriority = new ArrayList<>(firstPriorityList);
                for (int i = 0; i < oldFirstPriority.size(); i++) {
                    OrderRegretKVOCS orderRegretKVOCSnew = updateOrderRegretFirstPriority(chosenOrderSchedule, oldFirstPriority.get(i), vehicleSchedules);
                    if (orderRegretKVOCSnew.vehicleOrderCostScheduleList.size()==0){
                        removeOrderRegretFromPossibilitiesAndList(orderRegretKVOCSnew,orders, firstPriorityList);
                    }else if (orderRegretKVOCSnew.vehicleOrderCostScheduleList.size()==regretK) {
                        fromFirstPriorityToNormal.add(orderRegretKVOCSnew);
                        removeOrderRegretFromList(orderRegretKVOCSnew,firstPriorityList);
                    }else{
                        addFirstPriorityInCorrectPosition(orders.size()-orderRegretKs.size()-firstPriorityList.size(),firstPriorityList, orderRegretKVOCSnew);
                    }
                }
            }
            int size = orderRegretKs.size();
            ArrayList<OrderRegretKVOCS> oldOrderRegretKs = new ArrayList<>(orderRegretKs);
            if (size >0){
                for (int i = 0; i < size; i++) {
                    OrderRegretKVOCS orderRegretKVOCSnew = updateOrderRegret(chosenOrderSchedule, oldOrderRegretKs.get(i), vehicleSchedules);
                    addRegretAndVOCSInCorrectPosition(orders.size()-firstPriorityList.size()-fromFirstPriorityToNormal.size(),orderRegretKs,firstPriorityList,orderRegretKVOCSnew);
                }
            }
            if (fromFirstPriorityToNormal.size()>0){
                while(fromFirstPriorityToNormal.size()>0) {
                    addRegretAndVOCSInCorrectPosition(orders.size()-firstPriorityList.size(),orderRegretKs,firstPriorityList,fromFirstPriorityToNormal.remove(0));
                }
            }
            if (firstPriorityList.size() > 0) {
                chosenOrderSchedule = firstPriorityList.remove(0).vehicleOrderCostScheduleList.get(0);
            } else if (orderRegretKs.size()>0){
                chosenOrderSchedule = orderRegretKs.remove(0).vehicleOrderCostScheduleList.get(0);
            }else{
                break;
            }

            result = createNewSolution(chosenOrderSchedule,result);
            vehicleSchedules.set(chosenOrderSchedule.vehicle,chosenOrderSchedule.schedule);
            orders.remove(chosenOrderSchedule.order);
        }

        return result;
    }

    private void removeOrderRegretFromPossibilitiesAndList(OrderRegretKVOCS orderRegretKVOCSnew, HashSet<Integer> orders, List<OrderRegretKVOCS> firstPriority) {
        for (int orderRegretIdx = 0; orderRegretIdx<firstPriority.size();orderRegretIdx++){
            if (firstPriority.get(orderRegretIdx).order==orderRegretKVOCSnew.order){
                firstPriority.remove(orderRegretIdx);
                orders.remove(orderRegretKVOCSnew.order);
                break;
            }
        }
    }

    private void removeOrderRegretFromList(OrderRegretKVOCS orderRegretKVOCSnew, List<OrderRegretKVOCS> firstPriority) {
        for (int orderRegretIdx = 0; orderRegretIdx<firstPriority.size();orderRegretIdx++){
            if (firstPriority.get(orderRegretIdx).order==orderRegretKVOCSnew.order){
                firstPriority.remove(orderRegretIdx);
                break;
            }
        }
    }

    protected void addRegretAndVOCSInCorrectPosition(int size,List<OrderRegretKVOCS> orderRegretKs, List<OrderRegretKVOCS> firstPriority, OrderRegretKVOCS regretKAndVOCS) {
        int idx = 0;
        while (orderRegretKs.size()==size&&idx<orderRegretKs.size()){
            if(orderRegretKs.get(idx).order==regretKAndVOCS.order){
                orderRegretKs.remove(idx);
            }
            idx++;
        }
        if (regretKAndVOCS.vehicleOrderCostScheduleList.size() < regretK) {
            addFirstPriorityInCorrectPosition(size-orderRegretKs.size(),firstPriority, regretKAndVOCS);
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

    protected void addFirstPriorityInCorrectPosition(int size, List<OrderRegretKVOCS> firstPriority, OrderRegretKVOCS regretKAndVOCS) {
        int idx = 0;
        while(firstPriority.size()>=size&&idx<firstPriority.size()){
            if(firstPriority.get(idx).order==regretKAndVOCS.order){
                firstPriority.remove(idx);
            }
            idx++;
        }
        if (firstPriority.size() == 0) {
            firstPriority.add(regretKAndVOCS);
        } else if (regretKAndVOCS.vehicleOrderCostScheduleList.size() < firstPriority.get(0).vehicleOrderCostScheduleList.size()) {
            firstPriority.add(0,regretKAndVOCS);
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

    protected OrderRegretKVOCS updateOrderRegret(VehicleOrderCostSchedule chosenOrderSchedule, OrderRegretKVOCS orderRegretKVOCS, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList = orderRegretKVOCS.vehicleOrderCostScheduleList;
        int order = vehicleOrderCostScheduleList.get(0).order;
        int chosenVehicle = chosenOrderSchedule.vehicle;
        List<Integer> chosenSchedule = chosenOrderSchedule.schedule;
        VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,chosenVehicle,chosenSchedule);
        int size = vehicleOrderCostScheduleList.size();
        for (int vocsIdx = 0; vocsIdx < size; vocsIdx++) {
            if (chosenVehicle == vehicleOrderCostScheduleList.get(vocsIdx).vehicle) {
                if (bestVOCS.cost<vehicleOrderCostScheduleList.get(vocsIdx).cost){ //either I have a new better schedule
                    vehicleOrderCostScheduleList.set(vocsIdx,bestVOCS);
                    vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
                } else { //or I have to search again to find them, could potentially only search for one schedule here... the best schedule apart from the selected ones
                    vehicleOrderCostScheduleList.remove(vocsIdx);//remove this since we cant be sure its among the best
                    vehicleOrderCostScheduleList = addKBestSchedule(order,regretK,vehicleSchedules,vehicleOrderCostScheduleList);
                }
                return getRegretKValue(order,vehicleOrderCostScheduleList);
            }
        }
        if (bestVOCS.cost<vehicleOrderCostScheduleList.get(size -1).cost){
            vehicleOrderCostScheduleList.set(size-1,bestVOCS);
            vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
            return getRegretKValue(order,vehicleOrderCostScheduleList);
        }
        return orderRegretKVOCS;
    }

    protected OrderRegretKVOCS updateOrderRegretFirstPriority(VehicleOrderCostSchedule chosenOrderSchedule, OrderRegretKVOCS orderRegretKVOCS, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList = orderRegretKVOCS.vehicleOrderCostScheduleList;
        int order = orderRegretKVOCS.order;
        int chosenVehicle = chosenOrderSchedule.vehicle;
        List<Integer> chosenSchedule = chosenOrderSchedule.schedule;
        VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,chosenVehicle,chosenSchedule);
        int size = vehicleOrderCostScheduleList.size();
        for (int vocsIdx = 0; vocsIdx < size; vocsIdx++) {
            if (chosenVehicle == vehicleOrderCostScheduleList.get(vocsIdx).vehicle) {
                if (bestVOCS.schedule.size()>0){ //either I have a new better schedule
                    vehicleOrderCostScheduleList.set(vocsIdx,bestVOCS);
                }else {
                    vehicleOrderCostScheduleList.remove(vocsIdx);
                }
                vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
                //or there will be no other schedule to insert into since we have first priority, ie. return
                return getRegretKValue(order,vehicleOrderCostScheduleList);
            }
        }
        //if I get through without hitting the same vehicle it means I have found a new potential schedule in the chosen vehicle
        if (bestVOCS.schedule.size()>0) {
            vehicleOrderCostScheduleList.add(bestVOCS);
            vehicleOrderCostScheduleList.sort(Comparator.comparing(VehicleOrderCostSchedule::getCost));
        }
        return getRegretKValue(order,vehicleOrderCostScheduleList);
        //if its not possible to insert Order in updated schedule, return same object
    }

    protected List<VehicleOrderCostSchedule> addKBestSchedule(int order, int regretK, List<List<Integer>> vehicleSchedules, List<VehicleOrderCostSchedule> vehicleOrderCostScheduleList) {
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
                for (int i = 0; i < schedule.size()+1; i++) {
                    for (int j = i+1; j < schedule.size()+2; j++) {
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

    protected OrderRegretKVOCS getRegretKValue(int order, List<VehicleOrderCostSchedule> kBestSchedules) {

        double regretKValue = 0.0;

        for (int i = 1; i < kBestSchedules.size(); i++) {
            regretKValue += kBestSchedules.get(i).cost-kBestSchedules.get(0).cost;
        }
        return new OrderRegretKVOCS(order,regretKValue,kBestSchedules);
    }

    protected List<VehicleOrderCostSchedule> getKBestSchedules(int order, int regretK, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>();
        int vehicle = 0;
        double worstCost = 0.0;
        double bestCost = Double.MAX_VALUE;
        while(result.size()<regretK&&vehicle<vehicleSchedules.size()){
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
            if (bestVOCS.schedule.size()==0){
                vehicle++;
                continue;
            }
            if (result.size()==0){
                worstCost=bestCost=bestVOCS.cost;
                result.add(bestVOCS);
            }else if (bestVOCS.cost<bestCost){
                bestCost=bestVOCS.cost;
                result.add(bestVOCS);
            }else if (bestVOCS.cost>=worstCost){
                worstCost=bestVOCS.cost;
                result.add(result.size(),bestVOCS);
            }else {
                result.add(bestVOCS);
            }
            vehicle++;
        }
        Comparator<VehicleOrderCostSchedule> comparing = Comparator.comparing(VehicleOrderCostSchedule::getCost);
        result.sort(comparing);

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

    protected List<OrderAndSimilarity> getOrderSimilarities(int randomOrder) {
        List<OrderAndSimilarity> result = new ArrayList<>();
        for (int order = 0; order<dataSet.getOrderAmount();order++){
            result.add(new OrderAndSimilarity(order+1, dataSet.getOrderSimilarity(randomOrder-1,order)));
        }
        return result;
    }



    protected HashSet<Integer> getMostSimilarOrders(int amount, List<OrderAndSimilarity> orderSimilarities, List<List<Integer>> vehicleSchedules) {
        HashSet<Integer> result = new HashSet<>();
        result.add(orderSimilarities.remove(0).order);//adding randomly selected order to selection
        while (result.size()<amount&&orderSimilarities.size()>0){
            double position = random.nextDouble();
            position=Math.pow(position,randomReduction);
            int idx = (int) Math.round(position*(orderSimilarities.size()-1));
            OrderAndSimilarity element = orderSimilarities.remove(idx);
            int order = element.order;
            int vehicle = getVehicle(order,vehicleSchedules);
            List<Integer> vehicleSchedule = new ArrayList<>();
            if(vehicle<vehicleAmount) {
                vehicleSchedule = vehicleSchedules.get(vehicle);

                if (!feasibility.checkScheduleWithoutOrder(order, vehicle, vehicleSchedule)) {
                    continue;
                }

                vehicleSchedules.set(vehicle, getScheduleWithoutOrder(order, vehicleSchedule));
            }
            result.add(order);
        }
        return result;
    }


}
