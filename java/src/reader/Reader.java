package reader;

import dataObjects.DataSet;
import dataObjects.IDataSet;

import java.io.File;
import java.util.*;

public class Reader implements IReader {
    @Override
    public IDataSet readDataFromFile(String fileName) throws Exception {
        IDataSet result;

        File file = new File("/home/preben/repo/master/java/res/instances/"+fileName+".dat");
        Scanner fileScanner = new Scanner(file);
        nextSection(fileScanner);
        int vehicleAmount = getIntFromLineAndMoveToNextSection(fileScanner,1); //#1
        int orderAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#2
        int locationsAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#3
        int factoryAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#4


        int[] factories = getSetList(fileScanner, factoryAmount, locationsAmount); //#5

        int[] stopCapacities = getIntegerList(fileScanner, factoryAmount);  //#6

        int[] orderPickupLocations = getIntegerList(fileScanner, orderAmount); //#7
        int[] orderDeliveryLocations = getIntegerList(fileScanner, orderAmount); //#8

        getSetList(fileScanner, locationsAmount,orderAmount); //#9

        double[] orderWeights = getDoubleList(fileScanner,orderAmount); //#10

        double[] orderVolumes = getDoubleList(fileScanner,orderAmount); //#11

        boolean[][] vehicleCanVisitLocation = getBooleanList(fileScanner,vehicleAmount, locationsAmount); //#12

        boolean[][] vehicleCanPickupOrder = getBooleanList(fileScanner, vehicleAmount, orderAmount);   //#13

        double[] vehicleWeightCapacities = getDoubleList(fileScanner,vehicleAmount);    //#14

        double[] vehicleVolumeCapacities = getDoubleList(fileScanner,vehicleAmount);    //#15

        List<HashSet<Integer>> orderCanBePickedUpBy = getAllOrderVehicleCompatabilities(orderAmount,vehicleAmount,vehicleCanPickupOrder,
                vehicleCanVisitLocation,orderPickupLocations,orderDeliveryLocations,orderWeights,orderVolumes,vehicleWeightCapacities,vehicleVolumeCapacities);

        int[] distanceDimensions = getIntegerList(fileScanner,vehicleAmount); //#16
        int[] weightDimensions = getIntegerList(fileScanner,vehicleAmount); //#17
        int maxDistanceDimension = getMaxValue(distanceDimensions);
        int maxWeightDimension = getMaxValue(weightDimensions);

        double[][] distanceIntervals = getDouble2DList(fileScanner,vehicleAmount,maxDistanceDimension); //#18

        double[][] weightIntervals = getDouble2DList(fileScanner, vehicleAmount, maxWeightDimension); //#19

        double[][][] kmCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension,maxWeightDimension);     //#20

        double[][][] kgCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);    //#21

        double[][][] fixCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);   //#22

        double[][] stopCosts = getDouble2DList(fileScanner, vehicleAmount, locationsAmount);    //#23

        double[] orderPenalties = getDoubleList(fileScanner,orderAmount);   //#24

        int[] timeWindowAmounts = getIntegerList(fileScanner,locationsAmount);  //#25

        int timeWindowMax = getMaxValue(timeWindowAmounts);

        double[][] lowerTimeWindows = getDouble2DList(fileScanner, locationsAmount, timeWindowMax); //#26


        double[][] upperTimeWindows = getDouble2DList(fileScanner, locationsAmount, timeWindowMax); //#27
        int lastPickupLocation = getMaxValue(orderPickupLocations);
        double latestPickupTime = getMaxValueFromTo(0,lastPickupLocation,timeWindowAmounts,upperTimeWindows);
        double latestDeliveryTime = getMaxValueFromTo(lastPickupLocation,locationsAmount,timeWindowAmounts,upperTimeWindows);

        double[][][] travelTimes = getDouble3DList(fileScanner, vehicleAmount, locationsAmount, locationsAmount);   //#28

        double[][] travelDistances = getDouble2DList(fileScanner, locationsAmount, locationsAmount);    //#29

        double maxDistance = getMaxValue(travelDistances);

        result = new DataSet(vehicleAmount,orderAmount,locationsAmount, factoryAmount);
        result.setWeightDimensions(weightDimensions);
        result.setDistanceDimensions(distanceDimensions);
        result.setFactories(factories);
        result.setFactoryStopCapacities(stopCapacities);
        result.setOrderPickupLocations(orderPickupLocations);
        result.setOrderDeliveryLocations(orderDeliveryLocations);
        result.setVehicleCanVisitLocation(vehicleCanVisitLocation);
        result.setVehicleCanPickupOrder(vehicleCanPickupOrder);
        result.setVehicleWeightCapacities(vehicleWeightCapacities);
        result.setOrderWeights(orderWeights);
        result.setVehicleVolumeCapacities(vehicleVolumeCapacities);
        result.setOrderVolumes(orderVolumes);
        result.setOrderPenalties(orderPenalties);
        result.setDistanceIntervals(distanceIntervals);
        result.setWeightIntervals(weightIntervals);
        result.setKmCostMatrices(kmCostMatrices);
        result.setKgCostMatrices(kgCostMatrices);
        result.setFixCostMatrices(fixCostMatrices);
        result.setStopCosts(stopCosts);
        result.setTimeWindowAmounts(timeWindowAmounts);
        result.setLowerTimeWindows(lowerTimeWindows);
        result.setUpperTimeWindows(upperTimeWindows);
        result.setTravelTimes(travelTimes);
        result.setTravelDistances(travelDistances);
        result.setMaxDistance(maxDistance);
        result.setLatestPickupTimeWindow(latestPickupTime);
        result.setLatestDeliveryTimeWindow(latestDeliveryTime);
        return result;
    }


    private double getMaxValueFromTo(int fromIdx, int toIdx, int[] timeWindowAmounts, double[][] upperTimeWindows) {
        double max = 0.0;
        for (int i = fromIdx; i < toIdx; i++) {
            double latestTime = upperTimeWindows[i][ timeWindowAmounts[i]- 1];
            if(latestTime>max){
                max=latestTime;
            }
        }
        return max;
    }

    @Override
    public List<IDataSet> readDataFromFiles(String instance, List<String> instanceSizes) throws Exception {
        List<IDataSet> result = new ArrayList<>();
        for (String fileName :
                instanceSizes) {
            result.add(readDataFromFile(instance+fileName));
        }
        return result;
    }

    private double[][] getDouble2DReverseList(Scanner fileScanner, int x, int y) {
        double[][] result = new double[y][x];
        fileScanner.nextLine();
        for (int i = 0;i<x;i++){
            for (int j = 0;j<y;j++){
                result[j][i] = getDoubleFromLine(fileScanner, 3);
            }
        }
        nextSection(fileScanner);
        return result;
    }

    private double[][] getDouble2DList(Scanner fileScanner, int x, int y) {
        double[][] result = new double[x][y];
        fileScanner.nextLine();
        for (int i = 0;i<x;i++){
            for (int j = 0;j<y;j++){
                result[i][j] = getDoubleFromLine(fileScanner, 3);
            }
        }
        nextSection(fileScanner);
        return result;
    }

    private double[] getDoubleList(Scanner fileScanner, int lineAmount) {
        double[] result = new double[lineAmount];
        fileScanner.nextLine();
        for (int i = 0; i<lineAmount ;i++){
            result[i] = getDoubleFromLine(fileScanner, 2);
        }
        nextSection(fileScanner);
        return result;
    }

    private double getDoubleFromLine(Scanner fileScanner, int i) {
        moveToNextInt(fileScanner);
        for (int j = 0;j<(i-1);j++) {
            fileScanner.nextInt();
        }
        if(fileScanner.hasNextDouble()) {
            return fileScanner.nextDouble();
        }
        //if input is no int ie. "." return 0
        else{
            fileScanner.next();
            return 0;
        }
    }

    private boolean[][] getBooleanList(Scanner fileScanner, int i, int j) {
        boolean[][] result = new boolean[i][j];
        for (int ii = 0;ii<i;ii++){
            moveToNextInt(fileScanner);
            while(fileScanner.hasNextInt()){
                result[ii][fileScanner.nextInt()-1] = true;
            }
            fileScanner.nextLine();
        }
        nextSection(fileScanner);
        return result;
    }

    private int getIntFromLineAndMoveToNextSection(Scanner fileScanner, int i) {
        int result = getIntFromLine(fileScanner,i);
        nextSection(fileScanner);
        return result;
    }

    private int[] getSetList(Scanner fileScanner, int lineAmount, int tableSize) {
        int[] result = new int[tableSize];
        for (int i = 1; i<=lineAmount;i++){
            moveToNextInt(fileScanner);
            while(fileScanner.hasNextInt()){
                result[fileScanner.nextInt()-1] = i;
            }
            fileScanner.nextLine();
        }
        nextSection(fileScanner);
        return result;
    }

    private int[][] getInteger2DList(Scanner fileScanner, int x, int y) {
        int[][] result = new int[x][y];
        fileScanner.nextLine();
        for (int i = 0;i<x;i++){
            for (int j = 0;j<y;j++){
                result[i][j] = getIntFromLine(fileScanner, 3);
            }
        }
        nextSection(fileScanner);
        return result;
    }

    private int getMaxValue(int[] list) {
        int max = 0;
        for(int i:list){
            if(i>max)
                max = i;
        }
        return max;
    }

    private double getMaxValue(double[][] list) {
        double max = 0.0;
        for(double[] i:list){
            for (double j:i) {
                if (j > max)
                    max = j;
            }
        }
        return max;
    }

    /**
     *
     * @param fileScanner
     * @param x 1st dimension
     * @param y 2nd
     * @param z 3rd
     * @return a 3 dimensional list with values from fileScanner
     */
    private int[][][] getInteger3DList(Scanner fileScanner, int x, int y, int z) {
        int[][][] result = new int[x][y][z];
        fileScanner.nextLine();
        for (int i = 0;i<x;i++){
            for (int j = 0;j<y;j++){
                for (int k = 0; k<z;k++){
                    result[i][j][k] = getIntFromLine(fileScanner, 4);
                }
            }
        }
        nextSection(fileScanner);
        return result;
    }

    private double[][][] getDouble3DList(Scanner fileScanner, int x, int y, int z) {
        double[][][] result = new double[x][y][z];
        fileScanner.nextLine();
        for (int i = 0;i<x;i++){
            for (int j = 0;j<y;j++){
                for (int k = 0; k<z;k++){
                    result[i][j][k] = getDoubleFromLine(fileScanner, 4);
                }
            }
        }
        nextSection(fileScanner);
        return result;
    }

    /**
     *
     * @param fileScanner
     * @param i position of int in line
     * @return int number i in line will be returned
     */
    private int getIntFromLine(Scanner fileScanner, int i) {
        moveToNextInt(fileScanner);
        for (int j = 0;j<(i-1);j++) {
            fileScanner.nextInt();
        }
        if(fileScanner.hasNextInt()) {
            return fileScanner.nextInt();
        }
        //if input is no int ie. "." return 0
            else{
                fileScanner.next();
                return 0;
        }

    }

    private void moveToNextInt(Scanner fileScanner) {
        while (!fileScanner.hasNextInt()) {
            fileScanner.next();
        }
    }

    private int[] getIntegerList(Scanner fileScanner, int lineAmount) {
        int[] result = new int[lineAmount];
        fileScanner.nextLine();
        for (int i = 0; i<lineAmount ;i++){
            result[i] = getIntFromLine(fileScanner, 2);
        }
        nextSection(fileScanner);
        return result;
    }

    private List<Collection<Integer>> getListCollection(Scanner fileScanner, int lineAmount) {
        List<Collection<Integer>> result = new ArrayList<>(lineAmount);
        for (int i = 0; i<lineAmount; i++){
            Collection<Integer> factorySet = getIntegersFromLine(fileScanner);
            result.add(factorySet);
        }
        nextSection(fileScanner);
        return result;
    }

    private Collection<Integer> getIntegersFromLine(Scanner fileScanner) {
        Collection<Integer> result = new HashSet<>();
        moveToNextInt(fileScanner);
        while(fileScanner.hasNextInt()){
            result.add(fileScanner.nextInt());
        }
        fileScanner.nextLine();
        return result;
    }

    private boolean nextSection(Scanner fileScanner) {
        boolean result = false;
        String firstString = fileScanner.next();
        while(!firstString.equals("#")&&fileScanner.hasNext()) {
            fileScanner.nextLine();
            result = false;
            firstString = fileScanner.next();
        }
        if (firstString.equals("#")) {
            result = true;
        }
        if(fileScanner.hasNext()) {
            fileScanner.nextLine();
        }
        return result;
    }

    private int findNextInt(Scanner fileScanner) {

        moveToNextInt(fileScanner);
        return fileScanner.nextInt();
    }
}
