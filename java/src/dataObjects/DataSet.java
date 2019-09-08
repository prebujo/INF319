package dataObjects;

import java.util.*;

public class DataSet implements IDataSet {
    private int vehicleAmount;
    private int orderAmount;
    private int locationsAmount;
    private int factoryAmount;
    private int stopAmount;
    private int[] orderPickupLocations;
    private int[] orderDeliveryLocations;
    private int[] weightDimensions;
    private int[] distanceDimensions;
    private int[] factories;
    private int[] factoryStopCapacities;
    private int[] locations;
    private boolean[][] vehicleCanVisitLocation;
    private boolean[][] vehicleCanPickupOrder;
    private int[] vehicleStartingLocations;
    private int[] vehicleDestinationLocations;
    private double[] vehicleWeightCapacities;
    private double[] vehicleVolumeCapacities;
    private double[] orderWeights;
    private double[] orderVolumes;
    private double[] orderPenalties;
    private double[][] distanceIntervals;
    private double[][] weightIntervals;
    private double[][][] kmCostMatrices;
    private double[][][] kgCostMatrices;
    private double[][][] fixCostMatrices;
    private double[][] stopCosts;
    private int[] timeWindowAmounts;
    private double[][] lowerTimeWindows;
    private double[][] upperTimeWindows;
    private double[][][] travelTimes;
    private double[][] travelDistances;
    private int[] orderDeliveryDock;
    private double maxDistance;
    private double latestPickupTimeWindow;
    private double latestDeliveryTimeWindow;
    private double[][] orderSimilarity;
    private double psi = 0.75;
    private double omega = 0.8;
    private double phi = 1;
    private double tao = 0.25;
    private double chi = 0.1;
    private double maxWeight;
    private double[][] locationClusters;
    private List<List<Integer>> locationClusterList;
    private int[] cluster;


    public DataSet(int vehicleAmount, int orderAmount, int locationsAmount, int factoryAmount){
        this.vehicleAmount = vehicleAmount;
        this.orderAmount = orderAmount;
        this.locationsAmount = locationsAmount;
        this.factoryAmount = factoryAmount;
    }



    @Override
    public int getOrderAmount() {
        return orderAmount;
    }

    @Override
    public int getVehicleAmount() {
        return vehicleAmount;
    }

    @Override
    public int getLocationsAmount() {
        return locationsAmount;
    }

    @Override
    public int getFactoryAmount() {
        return factoryAmount;
    }

    @Override
    public void setWeightDimensions(int[] weightDimensions) {
        this.weightDimensions = weightDimensions;
    }

    @Override
    public int[] getWeightDimensions() {
        return weightDimensions;
    }

    @Override
    public void setDistanceDimensions(int[] distanceDimensions) {
        this.distanceDimensions = distanceDimensions;
    }

    @Override
    public int[] getDistanceDimensions() { return distanceDimensions; }

    @Override
    public void setFactories(int[] factories){
        this.factories = factories;
    }

    @Override
    public int[] getFactories() {
        return factories;
    }

    @Override
    public void setFactoryStopCapacities(int[] factoryStopCapacities) {
        this.factoryStopCapacities = factoryStopCapacities;
    }

    @Override
    public int[] getFactoryStopCapacities() {
        return factoryStopCapacities;
    }

    @Override
    public void setLocations(int[] locations) {
        this.locations = locations;
    }

    @Override
    public int[] getLocations() {
        return locations;
    }

    @Override
    public void setVehicleCanVisitLocation(boolean[][] vehicleCanVisitNode) {
        this.vehicleCanVisitLocation = vehicleCanVisitNode;
    }

    @Override
    public boolean[][] getVehicleCanVisitLocation() {
        return vehicleCanVisitLocation;
    }

    @Override
    public void setVehicleCanPickupOrder(boolean[][] vehicleCanPickupOrder) {
        this.vehicleCanPickupOrder = vehicleCanPickupOrder;
    }

    @Override
    public boolean[][] getVehicleCanPickupOrder() {
        return vehicleCanPickupOrder;
    }

    @Override
    public void setVehicleStartingLocations(int[] vehicleStartingLocations) {
        this.vehicleStartingLocations = vehicleStartingLocations;
    }

    @Override
    public int[] getVehicleStartingLocations() {
        return vehicleStartingLocations;
    }

    @Override
    public void setVehicleDestinationLocations(int[] vehicleDestinationLocations) {
        this.vehicleDestinationLocations = vehicleDestinationLocations;
    }

    @Override
    public int[] getVehicleDestinationLocations() {
        return vehicleDestinationLocations;
    }

    @Override
    public void setVehicleWeightCapacities(double[] vehicleWeightCapacities) {
        this.vehicleWeightCapacities = vehicleWeightCapacities;
    }

    @Override
    public double[] getVehicleWeightCapacities() {
        return vehicleWeightCapacities;
    }

    @Override
    public void setOrderWeights(double[] orderWeights) {
        this.orderWeights = orderWeights;
    }

    @Override
    public double[] getOrderWeights() {
        return orderWeights;
    }

    @Override
    public void setVehicleVolumeCapacities(double[] vehicleVolumeCapacities) {
        this.vehicleVolumeCapacities = vehicleVolumeCapacities;
    }

    @Override
    public double[] getVehicleVolumeCapacities() {
        return vehicleVolumeCapacities;
    }

    @Override
    public void setOrderVolumes(double[] orderVolumes) {
        this.orderVolumes = orderVolumes;
    }

    @Override
    public double[] getOrderVolumes() {
        return orderVolumes;
    }

    @Override
    public void setOrderPenalties(double[] orderPenalties) {
        this.orderPenalties = orderPenalties;
    }

    @Override
    public double[] getOrderPenalties() {
        return orderPenalties;
    }

    @Override
    public void setDistanceIntervals(double[][] distanceIntervals) {
        this.distanceIntervals = distanceIntervals;
    }

    @Override
    public double[][] getDistanceIntervals() {
        return distanceIntervals;
    }

    @Override
    public void setWeightIntervals(double[][] weightIntervals) {
        this.weightIntervals = weightIntervals;
    }

    @Override
    public double[][] getWeightIntervals() {
        return weightIntervals;
    }

    @Override
    public void setKmCostMatrices(double[][][] kmCostMatrices) {
        this.kmCostMatrices = kmCostMatrices;
    }

    @Override
    public double[][][] getKmCostMatrices() {
        return kmCostMatrices;
    }

    @Override
    public void setKgCostMatrices(double[][][] kgCostMatrices) {
        this.kgCostMatrices = kgCostMatrices;
    }

    @Override
    public double[][][] getKgCostMatrices() {
        return kgCostMatrices;
    }

    @Override
    public void setFixCostMatrices(double[][][] fixCostMatrices) {
        this.fixCostMatrices = fixCostMatrices;
    }

    @Override
    public double[][][] getFixCostMatrices() {
        return fixCostMatrices;
    }

    @Override
    public void setStopCosts(double[][] stopCosts) {
        this.stopCosts = stopCosts;
    }

    @Override
    public double[][] getStopCosts() {
        return stopCosts;
    }

    @Override
    public void setTimeWindowAmounts(int[] timeWindowAmounts) {
        this.timeWindowAmounts = timeWindowAmounts;
    }

    @Override
    public int[] getTimeWindowAmounts() {
        return timeWindowAmounts;
    }

    @Override
    public void setLowerTimeWindows(double[][] lowerTimeWindows) {
        this.lowerTimeWindows = lowerTimeWindows;
    }

    @Override
    public double[][] getLowerTimeWindows() {
        return lowerTimeWindows;
    }

    @Override
    public void setUpperTimeWindows(double[][] upperTimeWindows) {
        this.upperTimeWindows = upperTimeWindows;
    }

    @Override
    public double[][] getUpperTimeWindows() {
        return upperTimeWindows;
    }

    @Override
    public void setTravelTimes(double[][][] travelTimes) {
        this.travelTimes = travelTimes;
    }

    @Override
    public double[][][] getTravelTimes() {
        return travelTimes;
    }

    @Override
    public void setTravelDistances(double[][] travelDistances) {
        this.travelDistances = travelDistances;
    }

    @Override
    public double[][] getTravelDistances() {
        return travelDistances;
    }

    @Override
    public int[] getOrderPickupLocations() {
        return orderPickupLocations;
    }

    @Override
    public void setOrderPickupLocations(int[] orderPickupLocations) {
        this.orderPickupLocations = orderPickupLocations;
    }

    @Override
    public int[] getOrderDeliveryLocations() {
        return orderDeliveryLocations;
    }

    @Override
    public void setOrderDeliveryLocations(int[] orderDeliveryLocations) {
        this.orderDeliveryLocations = orderDeliveryLocations;
    }

    @Override
    public void setOrderDeliveryDocks(int[] orderDeliveryDock) {
        this.orderDeliveryDock = orderDeliveryDock;
    }

    @Override
    public double getMaxDistance() {
        return maxDistance;
    }

    @Override
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public double getLatestPickupTimeWindow() {
        return latestPickupTimeWindow;
    }

    @Override
    public void setLatestPickupTimeWindow(double latestPickupTimeWindow) {
        this.latestPickupTimeWindow=latestPickupTimeWindow;
    }


    @Override
    public double getLatestDeliveryTimeWindow() {
        return latestDeliveryTimeWindow;
    }

    @Override
    public void setLatestDeliveryTimeWindow(double latestDeliveryTimeWindow) {
        this.latestDeliveryTimeWindow = latestDeliveryTimeWindow;
    }

    @Override
    public double getOrderSimilarity(int order1, int order2){
        assert(orderSimilarity.length>order1);
        assert (orderSimilarity[order1].length>order2);
        return orderSimilarity[order1][order2];
    }

    @Override
    public void setOrderSimilarities(){
        this.orderSimilarity=getOrderSimilarities(orderAmount);
    }

    @Override
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public double getMaxWeight(){
        return maxWeight;
    }

    @Override
    public void setLocationClusters() {

        //make clusters for each possible k (maksimum k equal to vehicle amount) and keep cluster with best possible measurement

        double bestClusterValue = -1.0;
        List<List<Integer>> bestClusterList = new ArrayList<>();
        double[][] bestCluster = new double[0][0];
        int maxK = Math.min(vehicleAmount,locationsAmount/2);
        for (int k = 2; k <= maxK; k++) {
            int currentK = locationsAmount;
            double[][] currentDistanceMatrix = travelDistances.clone();
            List<List<Integer>> clusterList = new ArrayList<>();
            for (int i = 0; i<travelDistances.length;i++){
                clusterList.add(Arrays.asList(i));
            }

            while(currentK>k) {
                int bestI = 0;
                int bestJ = 0;
                double shortestDistance = Double.MAX_VALUE;
                for (int i = 0; i < currentDistanceMatrix.length - 1; i++) {
                    for (int j = i + 1; j < currentDistanceMatrix.length; j++) {
                        if (currentDistanceMatrix[i][j] < shortestDistance){
                            shortestDistance=currentDistanceMatrix[i][j];
                            bestI=i;
                            bestJ=j;
                        }
                    }
                }
                currentDistanceMatrix= mergeRowAndColumn(bestI,bestJ,currentDistanceMatrix, clusterList);
                currentK=currentDistanceMatrix.length;
            }
            double currentClusterValue = calculateSiluetteCoefficient(currentDistanceMatrix, clusterList);
            if (currentClusterValue>bestClusterValue){
                bestClusterList = clusterList;
                bestCluster=currentDistanceMatrix;
                bestClusterValue=currentClusterValue;
            }
        }
        locationClusters = bestCluster;
        locationClusterList = bestClusterList;
        cluster = createIntList(bestClusterList);
        System.out.println("Best cluster k: " + bestCluster.length+"/"+ maxK);
    }

    private int[] createIntList(List<List<Integer>> bestClusterList) {
        int[] result = new int[locationsAmount];
        for (int i = 0; i < bestClusterList.size(); i++) {
            for (Integer j:bestClusterList.get(i)) {
                result[j]=(i+1);
            }
        }
        return result;
    }

    protected double calculateSiluetteCoefficient(double[][] clusteredDistanceMatrix, List<List<Integer>> clusterList) {
        double totalSiluette = 0.0;

        for (int i = 0;i<clusterList.size();i++){
            List<Integer> cluster = clusterList.get(i);
            for (int clusterMember : cluster) {
                double cohesion = getCohesion(clusterMember, cluster);
                double separation = getSepraration(clusterMember, cluster, clusterList);
                double silhouette = (separation - cohesion) / Math.max(separation, cohesion);
                totalSiluette += silhouette;
            }
        }
        return totalSiluette/locationsAmount;
    }

    private double getSepraration(int currentNode, List<Integer> ownCluster, List<List<Integer>> clusterList) {
        double minDistance = Double.MAX_VALUE;
        double currentClusterDistance = 0.0;
        for (List<Integer> cluster:clusterList){
            if (cluster==ownCluster){
                continue;
            }
            for (Integer i: cluster) {
                currentClusterDistance+=travelDistances[currentNode][i];
            }
            currentClusterDistance=currentClusterDistance/cluster.size();
            if (currentClusterDistance<minDistance){
                minDistance=currentClusterDistance;
            }
        }
        return minDistance;
    }

    protected double getCohesion(int currentNode, List<Integer> cluster) {
        if (cluster.size()==1){
            return 0;
        }
        double result = 0.0;
        for (Integer i:cluster){
                result += travelDistances[currentNode][i];
        }
        return result/(cluster.size()-1);
    }

    protected double[][] mergeRowAndColumn(int bestI, int bestJ, double[][] currentDistanceMatrix, List<List<Integer>> clusterList) {
        assert(bestI<bestJ);
        double[][] result = new double[currentDistanceMatrix.length-1][currentDistanceMatrix.length-1];
        int resultI = 0;
        for (int i = 0; i < currentDistanceMatrix.length; i++) {
            int resultJ = 0;
            for (int j = 0; j < currentDistanceMatrix.length; j++) {
                if (i == bestJ || j == bestJ) {
                    continue;
                }else if (i == bestI) {
                    result[resultI][resultJ++]= Math.min(currentDistanceMatrix[i][j], currentDistanceMatrix[bestJ][j]);
                }else if(j == bestI){
                    result[resultI][resultJ++] = Math.min(currentDistanceMatrix[i][j], currentDistanceMatrix[i][bestJ]);
                }else {
                    result[resultI][resultJ++] = currentDistanceMatrix[i][j];
                }
            }
            if (i != bestJ) {
                resultI++;
            }
        }
        List<Integer> collection = new ArrayList<>(clusterList.get(bestI));
        List<Integer> collectionToBeAdded = clusterList.get(bestJ);
        Optional.ofNullable(collectionToBeAdded).ifPresent(collection::addAll);
        clusterList.set(bestI,collection);
        clusterList.remove(bestJ);
        return result;
    }

    private double[][] getOrderSimilarities(int orderAmount) {
        double[][] result = new double[orderAmount][orderAmount];
        for (int order1 = 0;order1<orderAmount;order1++) {
            for (int order2 = 0; order2 < orderAmount; order2++) {
                if (order2 == order1) {
                    continue;
                }
                result[order1][order2] = getOrderSimilarity2(order1,order2);
            }
        }
        return result;
    }

    private Double getOrderSimilarity2(int order1, int order2 ) {
        int order1PickupLocation = orderPickupLocations[order1]-1;
        int order2PickupLocation = orderPickupLocations[order2]-1;
        double pickupDistanceDifference = travelDistances[order1PickupLocation][order2PickupLocation];
        int order1DeliveryLocation = orderDeliveryLocations[order1]-1;
        int order2DeliveryLocation = orderDeliveryLocations[order2]-1;
        double deliveryDistanceDifference = travelDistances[order1DeliveryLocation][order2DeliveryLocation];
        double weightDifference = Math.abs(orderWeights[order1]-orderWeights[order2])/maxWeight;
        List<HashSet<Integer>> orderCanBePickedUpBy = getAllOrderVehicleCompatabilities();
        HashSet<Integer> order1VehicleSet = orderCanBePickedUpBy.get(order2);
        HashSet<Integer> order2VehicleSet = orderCanBePickedUpBy.get(order1);
        long intersectionSize = order1VehicleSet.stream().filter(order2VehicleSet::contains).count();
        double timeWindowsOverlapping =0.0;
        timeWindowsOverlapping+= getTimeWindowsOverlappingFactor(order1PickupLocation, order2PickupLocation);
        timeWindowsOverlapping+= getTimeWindowsOverlappingFactor(order1DeliveryLocation, order2DeliveryLocation);

        Double result = psi*((pickupDistanceDifference+deliveryDistanceDifference)/(maxDistance*2))+ omega*weightDifference +
                phi*(1-intersectionSize/Math.min(order1VehicleSet.size(),order2VehicleSet.size()))+
                tao*(factories[order1DeliveryLocation]==factories[order2DeliveryLocation]? 0:1) +
                chi*(timeWindowsOverlapping);
        return result;
    }

    private double getTimeWindowsOverlappingFactor(int order1Location, int order2Location) {
        double quotient = 0.0;
        double dividendPart2 = 0.0;
        double maxUpperTimeWindow = 0.0;
        double minLowerTimewindow = 0.0;
        int timeWindowAmount1 = timeWindowAmounts[order1Location];
        int timeWindowAmount2 = timeWindowAmounts[order2Location];
        for(int timewindow1 = 0; timewindow1< timeWindowAmount1; timewindow1++){
            double ltw1 = lowerTimeWindows[order1Location][timewindow1];
            double utw1 = upperTimeWindows[order1Location][timewindow1];
            double utw_1 = -1.0; //set negative for first iteration
            if (timewindow1>0) {
                utw_1 = upperTimeWindows[order1Location][timewindow1 - 1];
            }
            for (int timewindow2 = 0; timewindow2< timeWindowAmount2; timewindow2++){
                double ltw2 = lowerTimeWindows[order2Location][timewindow2];
                double utw2 = upperTimeWindows[order2Location][timewindow2];
                double utw_2 = -1.0; //set negative for first iteration
                if (timewindow1==0&&timewindow2==0){
                    minLowerTimewindow = Math.min(ltw1,ltw2);
                }
                if (timewindow1==(timeWindowAmount1 -1)&&timewindow2==(timeWindowAmount2 -1)){
                    maxUpperTimeWindow=Math.max(utw1,utw2);
                }
                if(timewindow2>0){
                    utw_2 = upperTimeWindows[order2Location][timewindow2-1];
                }
                if (ltw1 < utw2 && ltw2 < utw1){
                    quotient += Math.min(utw1,utw2)-Math.max(ltw1,ltw2);
                }
                if (ltw1>utw_2 && ltw2>utw_1&&utw_1!=-1&&utw_2!=-1){
                    dividendPart2 += Math.min(ltw1,ltw2) - Math.max(utw_1,utw_2);
                }
                if (timewindow2==(timeWindowAmount2-1)&&utw2<ltw1){
                    dividendPart2+= ltw1-Math.max(utw2,utw_1);
                }
                if (timewindow1==(timeWindowAmount1-1)&&utw1<ltw2){
                    dividendPart2+= ltw2-Math.max(utw1,utw_2);
                }
            }
        }

        return 1-quotient/(maxUpperTimeWindow-minLowerTimewindow-dividendPart2);
    }

    private List<HashSet<Integer>> getAllOrderVehicleCompatabilities() {
        List<HashSet<Integer>> result = new ArrayList<>();

        for (int o = 0; o < orderAmount; o++) {
            HashSet<Integer> vehiclesCompatableWithOrder = new HashSet<>();
            for (int v = 0; v < vehicleAmount; v++) {
                if(vehicleCanPickupOrder[v][o]&&vehicleCanVisitLocation[v][orderPickupLocations[o]-1]&&vehicleCanVisitLocation[v][orderDeliveryLocations[o]-1]){
                    vehiclesCompatableWithOrder.add(v);
                }
            }
            result.add(vehiclesCompatableWithOrder);
        }
        return result;
    }

    @Override
    public List<List<Integer>> getLocationClusters() {
        return locationClusterList;
    }

    @Override
    public int getCluster(int location){
        return cluster[location];
    }
}
