package functions.utility;

import dataObjects.IDataSet;
import dataObjects.IntegerDouble;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveNonClusteredInsertClustered extends RemoveAndReinsert{

    private final int randomReduction;
    private final IDataSet dataSet;

    public RemoveNonClusteredInsertClustered(String name, int lowerLimit, int upperLimit, int randomReduction, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.dataSet = dataSet;
        this.randomReduction = randomReduction;
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        List<List<Integer>> vehicleSchedules = getVehicleSchedules(solution);
        List<IntegerDouble> orderClusterValues = getOrderClusterValues(vehicleSchedules);
        orderClusterValues.sort(Comparator.comparing(IntegerDouble::getValue));
        HashSet<Integer> ordersToRemove = getNonClusteredItems(amount,orderClusterValues, vehicleSchedules);

        int[] solutionWithoutOrders = removeOrdersFromSolution(ordersToRemove,solution);
        int[] newSolution = insertClustered(ordersToRemove,solutionWithoutOrders);

        return newSolution!=null ? newSolution:solution;
    }

    private int[] insertClustered(HashSet<Integer> ordersToRemove, int[] solutionWithoutOrders) {
        return new int[0];
    }

    private HashSet<Integer> getNonClusteredItems(int amount, List<IntegerDouble> orderClusterValues, List<List<Integer>> vehicleSchedules) {
        return null;
    }

    protected List<IntegerDouble> getOrderClusterValues(List<List<Integer>> vehicleSchedules) {
        List<IntegerDouble> result = new ArrayList<>();
        int[] orderPickupLocations = dataSet.getOrderPickupLocations();
        int[] orderDeliveryLocations = dataSet.getOrderDeliveryLocations();

        for (int i = 0; i < vehicleSchedules.size(); i++) {
            List<Integer> vehicleSchedule = vehicleSchedules.get(i);
            int maxClusteredNodes = 2*(vehicleSchedule.size()-2); //maximum possible clustered nodes are 2 (pickup/delivery) times the size of the schedule minus own pickup/delivery.
            for (int j = 0; j < vehicleSchedule.size(); j++) {
                int order = vehicleSchedule.get(j);
                int orderPickupCluster = dataSet.getCluster(orderPickupLocations[order-1]-1);
                int orderDeliveryCluster = dataSet.getCluster(orderDeliveryLocations[order-1]-1);
                int clusteredLocations = 0;
                boolean[] pickedup = new boolean[orderAmount+1];
                for (int k = 0; k < vehicleSchedule.size(); k++) {
                    Integer scheduleElement = vehicleSchedule.get(k);
                    if (scheduleElement !=order){
                        if (!pickedup[scheduleElement]){
                            int scheduleElementCluster = dataSet.getCluster(orderPickupLocations[scheduleElement-1]-1);
                            clusteredLocations+= scheduleElementCluster==orderPickupCluster ? 1:0;
                            clusteredLocations+= scheduleElementCluster==orderDeliveryCluster ? 1:0;
                            pickedup[scheduleElement] = true;
                        }else{
                            int scheduleElementCluster = dataSet.getCluster(orderDeliveryLocations[scheduleElement-1]-1);
                            clusteredLocations+= scheduleElementCluster==orderPickupCluster ? 1:0;
                            clusteredLocations+= scheduleElementCluster==orderDeliveryCluster ? 1:0;
                        }
                    }
                }
                result.add(new IntegerDouble(order,(double)(clusteredLocations/maxClusteredNodes)));
            }
        }
        return result;
    }
}
