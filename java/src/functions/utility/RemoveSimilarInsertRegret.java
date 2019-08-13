package functions.utility;

import dataObjects.IDataSet;
import dataObjects.OrderAndSimilarity;
import dataObjects.VehicleAndSchedule;
import dataObjects.VehicleOrderCostSchedule;
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
        Iterator<Integer> ordersIterator = orders.iterator();
        List<VehicleOrderScheduleRegretKList> highestRegretKs = new ArrayList<>();
        highestRegretKs.add(new VehicleOrderCostSchedule(0,0,0.0,new ArrayList<>()));

        while(ordersIterator.hasNext()) {
            int order = ordersIterator.next();
            //calculate the regret-k values for each order
            double regretKValue = getRegretKValue(order, regretK, solution);
            if (regretKValue>highestRegretKs.get(0).cost) {
                VehicleAndSchedule vehicleScheduleOfOrder = getVehicleScheduleOfOrder(order, solution);
                highestRegretKs.add(0, new VehicleOrderCostSchedule(vehicleScheduleOfOrder.vehicle, order, regretKValue, vehicleScheduleOfOrder.schedule));
            }
        }
        //choose the highest regret value

        //insert order in its cheapest position.

        orders.remove(chosenOrder);
        double highestRegretValue =0.0;
        Integer chosenOrder = 0;
        return new int[0];
    }

    private double getRegretKValue(int order, int regretK, int[] solution) {
        double result;
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);
        List<VehicleOrderCostSchedule> kBestSchedules = getKBestSchedules(order,regretK,vehicleSchedules);
        VehicleOrderCostSchedule bestSchedule = findBestScheduleForOrder(order,vehicleSchedules);
        while(regretKList.size()<regretK){


        }





        return result;
    }

    private List<VehicleOrderCostSchedule> getKBestSchedules(int order, int regretK, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>();
        int vehicle = 0;
        boolean notEnoughVehicles = false;
        while(result.size()<regretK&&vehicle<vehicleSchedules.size()){
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            VehicleOrderCostSchedule bestVOCS = findBestScheduleCostForOrderInVehicle(order,vehicle,schedule);
            if (bestVOCS.schedule.size()>0){
                result.add(bestVOCS);
            }
            vehicle++;
        }

        if(vehicle==vehicleSchedules.size()&&result.size()<regretK){
            //TODO: deal with situation when 
        }


        while (vehicle<vehicleSchedules.size()) {
            Double solutionIncreasedCost;
            List<Integer> schedule = vehicleSchedules.get(vehicle);
            Double vehicleCost = calculateVehicleCost(vehicle,schedule);
            for (int i = 0; i < schedule.size() +1; i++) {
                for (int j = i+1; j < schedule.size() + 2; j++) {
                    List<Integer> newSchedule = createNewSchedule(order, i, j, schedule);
                    if(feasibility.checkSchedule(vehicle, newSchedule)){
                        solutionIncreasedCost = calculateVehicleCost(vehicle,newSchedule)-vehicleCost;
                        if (solutionIncreasedCost<bestCost){
                            bestCost=solutionIncreasedCost;
                            bestSchedule=newSchedule;
                            bestVehicle=vehicle;
                        }
                    }
                }
            }
            vehicle++;
        }
        if (bestSchedule.size()==0){
            bestVehicle=vehicleAmount;
        }

        return null;
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
