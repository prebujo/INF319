package functions.utility;

import dataObjects.IDataSet;
import dataObjects.IntegerDouble;
import dataObjects.VehicleOrderCostSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveNonClusteredInsertClustered extends RemoveAndReinsert{

    private final int randomReduction;
    private final IDataSet dataSet;
    private int[] orderPickupLocations;
    private int[] orderDeliveryLocations;

    public RemoveNonClusteredInsertClustered(String name, int randomReduction, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.dataSet = dataSet;
        this.orderPickupLocations = dataSet.getOrderPickupLocations();
        this.orderDeliveryLocations = dataSet.getOrderDeliveryLocations();
        this.randomReduction = randomReduction;
    }

    @Override
    public int[] apply(int[] solution) {
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);
        List<IntegerDouble> orderClusterValues = getOrderClusterValues(vehicleSchedules);
        getDummyClusterValues(solution, orderClusterValues);//updating list with dummy vehicles
        orderClusterValues.sort(Comparator.comparing(IntegerDouble::getValue));
        HashSet<Integer> ordersToRemove = getNonClusteredItems(amount,orderClusterValues, vehicleSchedules);

        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertClustered(ordersToRemove,solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;
    }

    private void getDummyClusterValues(int[] solution, List<IntegerDouble> orderClusterValues) {
        int vehicle = 0;
        boolean[] done = new boolean[orderAmount+1];
        for (int i = 0;i<solution.length;i++){
            int solutionElement = solution[i];
            if (vehicle==vehicleAmount){
                while(i < solution.length){
                    solutionElement=solution[i];
                    if(!done[solutionElement]){
                        done[solutionElement]=true;
                        orderClusterValues.add(new IntegerDouble(solutionElement,0.0));
                    }
                    i++;
                }
            }else if (solutionElement==0){
                    vehicle++;
            }
        }
    }

    private int[] insertClustered(HashSet<Integer> ordersToRemove, int[] solutionWithoutOrders) {
        int[] result = solutionWithoutOrders.clone();
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solutionWithoutOrders);
        List<VehicleOrderCostSchedule> vehicleOrderClusterValueSchedules = getVehicleOrderCostSchedules(ordersToRemove, vehicleSchedules);
        vehicleOrderClusterValueSchedules.sort(Comparator.comparing(VehicleOrderCostSchedule::getNegativeCost));

        while (vehicleOrderClusterValueSchedules.size()>0) {
            VehicleOrderCostSchedule bestOrderCluster = vehicleOrderClusterValueSchedules.remove(0);
            if(bestOrderCluster.vehicle!=vehicleAmount) {
                vehicleSchedules.set(bestOrderCluster.vehicle, bestOrderCluster.schedule);
                result = createNewSolution(bestOrderCluster, result);
            }
            if (vehicleOrderClusterValueSchedules.size()>0) {
                vehicleOrderClusterValueSchedules = updateVehicleOrderClusterSchedule( bestOrderCluster, vehicleOrderClusterValueSchedules, vehicleSchedules);
            }
        }
        return result;
    }

    private List<VehicleOrderCostSchedule> getVehicleOrderCostSchedules(HashSet<Integer> ordersToRemove, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> vehicleOrderClusterValueSchedules = new ArrayList<>();
        for (Integer order : ordersToRemove) {
            vehicleOrderClusterValueSchedules.add(findBestPossibleClusterValueForOrder(order, vehicleSchedules));
        }
        return vehicleOrderClusterValueSchedules;
    }

    private VehicleOrderCostSchedule findBestPossibleClusterValueForOrder(Integer order, List<List<Integer>> vehicleSchedules) {
        double bestClusterValue = -1.0;
        VehicleOrderCostSchedule bestVOCS= null;
        for (int i = 0; i < vehicleSchedules.size(); i++) {
            List<Integer> vehicleSchedule = vehicleSchedules.get(i);
            VehicleOrderCostSchedule bestScheduleWithOrder = findBestScheduleCostForOrderInVehicle(order,i,vehicleSchedule);
            if(bestScheduleWithOrder.schedule.size()>0) {
                double clusterValue = getOrderClusterValueForSchedule(order, 2*(vehicleSchedule.size()), vehicleSchedule);
                if (clusterValue > bestClusterValue) {
                    bestClusterValue = clusterValue;
                    bestVOCS = new VehicleOrderCostSchedule(i , order, bestClusterValue, bestScheduleWithOrder.schedule);
                }
            }
        }
        return bestVOCS!=null ? bestVOCS:new VehicleOrderCostSchedule(vehicleAmount,order,0.0,new ArrayList<>());
    }

    private List<VehicleOrderCostSchedule> updateVehicleOrderClusterSchedule(VehicleOrderCostSchedule updatedSchedule,List<VehicleOrderCostSchedule> vehicleOrderClusterValueSchedules, List<List<Integer>> vehicleSchedules) {
        List<VehicleOrderCostSchedule> result = new ArrayList<>();
        VehicleOrderCostSchedule newBestVOSC;
        for (int i = 0; i < vehicleOrderClusterValueSchedules.size(); i++) {
            VehicleOrderCostSchedule schedule = vehicleOrderClusterValueSchedules.get(i);
            int orderToCheck = schedule.order;
            newBestVOSC = schedule;

            double clusterValueInVehicle = getOrderClusterValueForSchedule(orderToCheck,2*updatedSchedule.schedule.size(), updatedSchedule.schedule);
            VehicleOrderCostSchedule bestScheduleInVehicle = findBestScheduleCostForOrderInVehicle(orderToCheck,updatedSchedule.vehicle,updatedSchedule.schedule);
            if (clusterValueInVehicle>schedule.cost&&bestScheduleInVehicle.schedule.size()>0){
                newBestVOSC = new VehicleOrderCostSchedule(updatedSchedule.vehicle, orderToCheck, clusterValueInVehicle, bestScheduleInVehicle.schedule);
            } else if (schedule.vehicle==updatedSchedule.vehicle){
                newBestVOSC = findBestPossibleClusterValueForOrder(orderToCheck,vehicleSchedules);
            }
            if (result.size()==0){
                result.add(newBestVOSC);
            } else if (newBestVOSC.cost>result.get(0).cost){
                result.add(0,newBestVOSC);
            } else {
                result.add(newBestVOSC);
            }
        }
        return result;

    }

    private HashSet<Integer> getNonClusteredItems(int amount, List<IntegerDouble> orderClusterValues, List<List<Integer>> vehicleSchedules) {
        HashSet<Integer> result = new HashSet<>();
        while (result.size()<amount&&orderClusterValues.size()>0){
            double position = random.nextDouble();
            position=Math.pow(position,randomReduction);
            int idx = (int) Math.round(position*(orderClusterValues.size()-1));
            IntegerDouble element = orderClusterValues.remove(idx);
            int order = element.key;
            int vehicle = getVehicle(order,vehicleSchedules);
            List<Integer> vehicleSchedule;
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

    protected List<IntegerDouble> getOrderClusterValues(List<List<Integer>> vehicleSchedules) {
        List<IntegerDouble> result = new ArrayList<>();
        for (int i = 0; i < vehicleSchedules.size(); i++) {
            List<Integer> vehicleSchedule = vehicleSchedules.get(i);
            //maximum possible clustered nodes are 2 (pickup/delivery) times the size of the schedule minus own pickup/delivery.
            boolean[] done = new boolean[orderAmount];
            for (int j = 0; j < vehicleSchedule.size(); j++) {
                int order = vehicleSchedule.get(j);
                if(!done[order-1]) {
                    done[order-1] = true;
                    result.add(new IntegerDouble(order,getOrderClusterValueForSchedule(order,2*(vehicleSchedule.size()-2),vehicleSchedule)));
                }
            }
        }
        return result;
    }

    private double getOrderClusterValueForSchedule(int order, double maxClusteredNodes, List<Integer> vehicleSchedule) {
        int orderPickupCluster = dataSet.getCluster(orderPickupLocations[order-1]-1);
        int orderDeliveryCluster = dataSet.getCluster(orderDeliveryLocations[order-1]-1);
        double clusteredLocations = 0;
        boolean[] pickedup = new boolean[orderAmount+1];
        for (int k = 0; k < vehicleSchedule.size(); k++) {
            Integer scheduleElement = vehicleSchedule.get(k);
            if (scheduleElement !=order){
                if (!pickedup[scheduleElement]){
                    int scheduleElementCluster = dataSet.getCluster(orderPickupLocations[scheduleElement-1]-1);
                    clusteredLocations+= scheduleElementCluster==orderPickupCluster ? 1.0:0.0;
                    clusteredLocations+= scheduleElementCluster==orderDeliveryCluster ? 1.0:0.0;
                    pickedup[scheduleElement] = true;
                }else{
                    int scheduleElementCluster = dataSet.getCluster(orderDeliveryLocations[scheduleElement-1]-1);
                    clusteredLocations+= scheduleElementCluster==orderPickupCluster ? 1.0:0.0;
                    clusteredLocations+= scheduleElementCluster==orderDeliveryCluster ? 1.0:0.0;
                }
            }
        }
        if(maxClusteredNodes==0){
            return 0.0;
        }
        return clusteredLocations / maxClusteredNodes;
    }
}
