package reader;

import dataObjects.DataSet;
import dataObjects.IDataSet;

import java.io.File;
import java.util.*;

public class Reader implements IReader {
    @Override
    public IDataSet readDataFromFile(String fileName) throws Exception {
        IDataSet result;

        File file = new File("/home/preben/repo/master/java/res/"+fileName+".dat");
        Scanner fileScanner = new Scanner(file);
        nextSection(fileScanner);
        int vehicleAmount = getIntFromLineAndMoveToNextSection(fileScanner,1); //#1
        int orderAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#2
        int locationsAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#3
        int factoryAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);  //#4

        int[] distanceDimensions = getIntegerList(fileScanner,vehicleAmount); //#5
        int[] weightDimensions = getIntegerList(fileScanner,vehicleAmount); //#6

        int[] factories = getSetList(fileScanner, factoryAmount, 2*orderAmount); //#7

        int[] stopCapacities = getIntegerList(fileScanner, factoryAmount);  //#8

        int[] orderPickupLocations = getIntegerList(fileScanner, orderAmount); //#9.1
        int[] orderDeliveryLocations = getIntegerList(fileScanner, orderAmount); //#9.2

        getSetList(fileScanner, locationsAmount,orderAmount);

        double[] orderWeights = getDoubleList(fileScanner,orderAmount); //#10

        double[] orderVolumes = getDoubleList(fileScanner,orderAmount); //#11

        double[] orderPenalties = getDoubleList(fileScanner,orderAmount);   //#12

        boolean[][] vehicleCanVisitNode = getBooleanList(fileScanner,vehicleAmount, locationsAmount); //#13

        boolean[][] vehicleCanPickupNode = getBooleanList(fileScanner, vehicleAmount, orderAmount);   //#14

        double[] vehicleWeightCapacities = getDoubleList(fileScanner,vehicleAmount);    //#15

        double[] vehicleVolumeCapacities = getDoubleList(fileScanner,vehicleAmount);    //#16

        int maxDistanceDimension = getMaxValue(distanceDimensions); //#17
        int maxWeightDimension = getMaxValue(weightDimensions);

        double[][] distanceIntervals = getDouble2DList(fileScanner,vehicleAmount,maxDistanceDimension); //#17

        double[][] weightIntervals = getDouble2DList(fileScanner, vehicleAmount, maxWeightDimension); //#18

        double[][][] kmCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension,maxWeightDimension);     //#19

        double[][][] kgCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);    //#20

        double[][][] fixCostMatrices = getDouble3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);   //#21

        double[][] stopCosts = getDouble2DList(fileScanner, vehicleAmount, locationsAmount);    //#22

        int[] timeWindowAmounts = getIntegerList(fileScanner,locationsAmount);  //#23

        int timeWindowMax = getMaxValue(timeWindowAmounts);

        double[][] lowerTimeWindows = getDouble2DReverseList(fileScanner, timeWindowMax, locationsAmount); //#24

        double[][] upperTimeWindows = getDouble2DReverseList(fileScanner, timeWindowMax, locationsAmount); //#25

        double[][][] travelTimes = getDouble3DList(fileScanner, vehicleAmount, locationsAmount, locationsAmount);   //#26

        double[][] travelDistances = getDouble2DList(fileScanner, locationsAmount, locationsAmount);    //#27

        result = new DataSet(vehicleAmount,orderAmount,locationsAmount, factoryAmount);
        result.setWeightDimensions(weightDimensions);
        result.setDistanceDimensions(distanceDimensions);
        result.setFactories(factories);
        result.setFactoryStopCapacities(stopCapacities);
        result.setOrderPickupLocations(orderPickupLocations);
        result.setOrderDeliveryLocations(orderDeliveryLocations);
        result.setVehicleCanVisitNode(vehicleCanVisitNode);
        result.setVehicleCanPickupOrder(vehicleCanPickupNode);
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
        if(fileScanner.hasNextInt()) {
            return (double) fileScanner.nextDouble();
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
                    result[i][j][k] = getIntFromLine(fileScanner, 4);
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
