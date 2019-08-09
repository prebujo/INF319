package functions.utility;

import dataObjects.DoublePair;
import dataObjects.IDataSet;
import dataObjects.OrderAndSimilarity;
import dataObjects.VehicleAndSchedule;
import functions.feasibility.IFeasibility;

import java.util.*;

public class RemoveSimilarInsertRegret extends RemoveAndReinsert {
    private final double maxDistance;
    private double psi = 0.8;
    private double chi = 0.5;
    private double phi = 1;
    private double omega = 1;
    private double tao = 0.3;
    private final double[][][] travelTimes;
    private final double latestPickupTime;
    private final double latestDeliveryTime;
    private final List<HashSet<Integer>> orderCanBePickedUpBy;
    private final int[] factories;

    public RemoveSimilarInsertRegret(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.maxDistance = dataSet.getMaxDistance();
        this.travelTimes = dataSet.getTravelTimes();
        this.latestDeliveryTime=dataSet.getLatestDeliveryTimeWindow();
        this.latestPickupTime = dataSet.getLatestPickupTimeWindow();
        this.orderCanBePickedUpBy = getAllOrderVehicleCompatabilities(dataSet.getOrderAmount(),dataSet.getVehicleAmount(),dataSet.getVehicleCanPickupOrder(),
                dataSet.getVehicleCanVisitLocation(),dataSet.getOrderPickupLocations(),dataSet.getOrderDeliveryLocations());
        this.factories = dataSet.getFactories();
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        //remove orders that are similar. first run Shaw, then remove those similar
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        int randomOrder = 1 + random.nextInt(orderAmount);
        List<OrderAndSimilarity> orderSimilarities = getOrderSimilarities(randomOrder,solution);
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

        return new int[0];
    }

    private List<OrderAndSimilarity> getOrderSimilarities(int order, int[] solution) {
        List<OrderAndSimilarity> result = new ArrayList<>();
        VehicleAndSchedule vehicleScheduleOfOrder = getVehicleScheduleOfOrder(order, solution);
        DoublePair orderPickupAndDeliveryTime = getPickupAndDeliveryTime(order,vehicleScheduleOfOrder);
        for (int order2 = 1; order2 < orderAmount+1; order2++) {
            if (order2==order){
                continue;
            }
            Double similarity = getOrderSimilarity(order,vehicleScheduleOfOrder.vehicle, orderPickupAndDeliveryTime,order2,solution);
            result.add(new OrderAndSimilarity(order2, similarity));
        }
        return result;
    }

    private Double getOrderSimilarity(int order1, int order1Vehicle, DoublePair order1PickupAndDeliveryTime, int order2, int[] solution) {
        double pickupDistanceDifference = travelDistance[orderPickupLocations[order1]][orderPickupLocations[order2]];
        double deliveryDistanceDifference = travelDistance[orderDeliveryLocations[order1]][orderDeliveryLocations[order2]];
        VehicleAndSchedule order2VehicleSchedule = getVehicleScheduleOfOrder(order2,solution);
        DoublePair order2pickupAndDeliveryTime = getPickupAndDeliveryTime(order2,order2VehicleSchedule);
        HashSet<Integer> order1VehicleSet = orderCanBePickedUpBy.get(order2-1);
        HashSet<Integer> order2VehicleSet = orderCanBePickedUpBy.get(order1-1);
        long intersectionSize = order1VehicleSet.stream().filter(order2VehicleSet::contains).count();

        Double result = psi*((pickupDistanceDifference+deliveryDistanceDifference)/(maxDistance*2))+
                chi*(Math.abs(order1PickupAndDeliveryTime.firstDouble-order2pickupAndDeliveryTime.firstDouble)/latestPickupTime+Math.abs(order1PickupAndDeliveryTime.secondDouble-order2pickupAndDeliveryTime.secondDouble)/latestDeliveryTime)+
                phi*(1-intersectionSize/Math.min(order1VehicleSet.size(),order2VehicleSet.size()))+
                omega*(order1Vehicle==order2VehicleSchedule.vehicle ? 1:0)+
                tao*(factories[order1]==factories[order2]? 0:1);
        return result;
    }

    private DoublePair getPickupAndDeliveryTime(int order2, VehicleAndSchedule vehicleSchedule) {
        if (vehicleSchedule.vehicle==vehicleAmount){
            return new DoublePair(0.0,0.0);
        }
        double pickupTime = 0.0;
        double deliveryTime = 0.0;
        boolean[] pickedUp = new boolean[orderAmount+1];
        List<Integer> schedule = vehicleSchedule.schedule;
        int vehicleLocation = 0;
        int solutionLocation = 0;
        double currentTime = 0.0;
        for (int i = 0; i < schedule.size(); i++) {
            int solutionElement = schedule.get(i);
            if (!pickedUp[solutionElement]){
                solutionLocation=orderPickupLocations[solutionElement];
            }else{
                solutionLocation=orderDeliveryLocations[solutionElement];
            }
            if (vehicleLocation!=0){
                currentTime+=travelTimes[vehicleSchedule.vehicle][vehicleLocation][solutionLocation];
            }
            if (solutionElement==order2&&!pickedUp[solutionElement]){
                pickupTime=currentTime;
            } else if(solutionElement==order2&&pickedUp[solutionElement]){
                deliveryTime=currentTime;
            }
            vehicleLocation=solutionLocation;
        }

        return new DoublePair(pickupTime,deliveryTime);
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

    private List<HashSet<Integer>> getAllOrderVehicleCompatabilities(int orderAmount, int vehicleAmount,boolean[][] vehicleCanPickupOrder, boolean[][] vehicleCanVisitLocation, int[] orderPickupLocations, int[] orderDeliveryLocations) {
        List<HashSet<Integer>> result = new ArrayList<>();

        for (int o = 0; o < orderAmount; o++) {
            HashSet<Integer> vehiclesCompatableWithOrder = new HashSet<>();
            for (int v = 0; v < vehicleAmount; v++) {
                if(vehicleCanPickupOrder[v][o]&&vehicleCanVisitLocation[v][orderPickupLocations[o]]&&vehicleCanVisitLocation[v][orderDeliveryLocations[o]]){
                    vehiclesCompatableWithOrder.add(v);
                }
            }
            result.add(vehiclesCompatableWithOrder);
        }
        return result;
    }
}
