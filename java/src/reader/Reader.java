package reader;

import dataObjects.DataSet;
import dataObjects.IDataSet;

import java.io.File;
import java.util.*;

public class Reader implements IReader {
    @Override
    public IDataSet readDataFromFile(String fileName) throws Exception {
        IDataSet result;

        File file = new File("/home/preben/repo/master/java/res/"+fileName);
        Scanner fileScanner = new Scanner(file);
        nextSection(fileScanner);
        int vehicleAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int orderAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int factoryAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int stopAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);

        int[] distanceDimensions = getIntegerList(fileScanner,vehicleAmount);
        int[] weightDimensions = getIntegerList(fileScanner,vehicleAmount);

        int[] factories = getSetList(fileScanner, factoryAmount, 2*orderAmount);

        int[] stopCapacities = getIntegerList(fileScanner, factoryAmount);

        int[] locations = getSetList(fileScanner, stopAmount, 2*orderAmount);

        boolean[][] vehicleCanVisitNode = getBooleanList(fileScanner,vehicleAmount, 2*orderAmount);

        boolean[][] vehicleCanPickupNode = getBooleanList(fileScanner, vehicleAmount, 2*orderAmount);

        int[] vehicleStartingLocations = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleDestinationLocations = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleWeightCapacities = getIntegerList(fileScanner,vehicleAmount);

        int[] orderWeights = getIntegerList(fileScanner,orderAmount);

        int[] vehicleVolumeCapacities = getIntegerList(fileScanner,vehicleAmount);

        int[] orderVolumes = getIntegerList(fileScanner,orderAmount);

        int[] orderPenalties = getIntegerList(fileScanner,orderAmount);

        int maxDistanceDimension = getMaxValue(distanceDimensions);
        int maxWeightDimension = getMaxValue(weightDimensions);

        int[][] distanceIntervals = getInteger2DList(fileScanner,vehicleAmount,maxDistanceDimension+1);

        int[][] weightIntervals = getInteger2DList(fileScanner, vehicleAmount, maxWeightDimension+1);

        int[][][] kmCostMatrices = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension,maxWeightDimension);

        int[][][] kgCostMatrices = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);

        int[][][] fixCostMatrices = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);

        int[][] stopCosts = getInteger2DList(fileScanner, vehicleAmount, 2*orderAmount);

        int[] timeWindowAmounts = getIntegerList(fileScanner,2*orderAmount);

        int timeWindowMax = getMaxValue(timeWindowAmounts);

        int[][] lowerTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][] upperTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][][] travelTimes = getInteger3DList(fileScanner, vehicleAmount, 2*orderAmount, 2*orderAmount);

        int[][] travelDistances = getInteger2DList(fileScanner, 2*orderAmount, 2*orderAmount);

        result = new DataSet(vehicleAmount,orderAmount,factoryAmount,stopAmount);
        result.setWeightDimensions(weightDimensions);
        result.setDistanceDimensions(distanceDimensions);
        result.setFactories(factories);
        result.setFactoryStopCapacities(stopCapacities);
        result.setLocations(locations);
        result.setVehicleCanVisitNode(vehicleCanVisitNode);
        result.setVehicleCanPickupOrder(vehicleCanPickupNode);
        result.setVehicleStartingLocations(vehicleStartingLocations);
        result.setVehicleDestinationLocations(vehicleDestinationLocations);
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

    private boolean[][] getBooleanList(Scanner fileScanner, int i, int j) {
        boolean[][] result = new boolean[i][j+1];
        for (int ii = 0;ii<i;ii++){
            moveToNextInt(fileScanner);
            while(fileScanner.hasNextInt()){
                result[ii][fileScanner.nextInt()] = true;
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
