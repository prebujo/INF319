package functions.utility;

import dataObjects.DoublePair;
import dataObjects.IDataSet;
import dataObjects.OrderAndSimilarity;
import dataObjects.VehicleAndSchedule;
import functions.feasibility.IFeasibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RemoveSimilarInsertRegret extends RemoveAndReinsert {
    private final double maxDistance;
    private double psi = 1;
    private double chi = 0.5;
    private double phi = 1;
    private double omega = 1;
    private final double[][][] travelTimes;
    private final double latestPickupTime;
    private final double latestDeliveryTime;
    private final List<HashSet<Integer>> orderCanBePickedUpBy;

    public RemoveSimilarInsertRegret(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.maxDistance = dataSet.getMaxDistance();
        this.travelTimes = dataSet.getTravelTimes();
        this.latestDeliveryTime=dataSet.getLatestDeliveryTimeWindow();
        this.latestPickupTime = dataSet.getLatestPickupTimeWindow();
        this.orderCanBePickedUpBy = getAllOrderVehicleCompatabilities(dataSet.getOrderAmount(),dataSet.getVehicleAmount(),dataSet.getVehicleCanPickupOrder(),
                dataSet.getVehicleCanVisitLocation(),dataSet.getOrderPickupLocations(),dataSet.getOrderDeliveryLocations());
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        //remove orders that are similar. first run Shaw, then remove those similar
        int amount = lowerLimit+ random.nextInt(upperLimit-lowerLimit+1);
        int randomOrder = 1 + random.nextInt(orderAmount);
        List<OrderAndSimilarity> orderSimilarities = getOrderSimilarities(randomOrder,solution);
        HashSet<Integer> ordersToRemove = getMostSimilarOrders(amount,orderSimilarities);


        //build shaw with P for picked up by same car (0 or 1 if same). T time of pickup/delivery. d distance between each pickup/delivery node.
        //and if orders can be picked up from the same vehicles or not, add also if orders are deliverd in the same factories
        //test if similar weight will work or test if better with small weight (wi + wj), the smaller the more likely they will fit together
        //experiment with the size of the greek constants to be multiplied.

        //reinsert similar orders based on regret-k.
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
        VehicleAndSchedule vehicleSchedule = getVehicleScheduleOfOrder(order2,solution);
        DoublePair order2pickupAndDeliveryTime = getPickupAndDeliveryTime(order2,vehicleSchedule);
        HashSet<Integer> order1VehicleSet = orderCanBePickedUpBy.get(order2-1);
        HashSet<Integer> order2VehicleSet = orderCanBePickedUpBy.get(order1-1);
        long intersectionSize = order1VehicleSet.stream().filter(order2VehicleSet::contains).count();

        Double result = psi*((pickupDistanceDifference+deliveryDistanceDifference)/(maxDistance*2))+
                chi*(Math.abs(order1PickupAndDeliveryTime.firstDouble-order2pickupAndDeliveryTime.firstDouble)/latestPickupTime+Math.abs(order1PickupAndDeliveryTime.secondDouble-order2pickupAndDeliveryTime.secondDouble)/latestDeliveryTime)+
                phi*(1-intersectionSize/Math.min(order1VehicleSet.size(),order2VehicleSet.size()))+
                omega*(order1Vehicle==vehicleSchedule.vehicle ? 1:0);

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

    private HashSet<Integer> getMostSimilarOrders(int amount, List<OrderAndSimilarity> orderSimilarities) {
        return null;
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
