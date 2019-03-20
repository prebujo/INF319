package reader;

import dataObjects.DataSet;
import dataObjects.IData;

import java.io.File;
import java.util.*;

public class Reader implements IReader {
    @Override
    public IData readDataFromFile(String fileName) throws Exception {
        IData result;

        File file = new File("/home/preben/repo/master/java/res/"+fileName);
        Scanner fileScanner = new Scanner(file);
        nextSection(fileScanner);
        int vehicleAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int orderAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int factoryAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int stopAmount = getIntFromLineAndMoveToNextSection(fileScanner,1);

        int[] distanceDimension = getIntegerList(fileScanner,vehicleAmount);
        int[] weightDimension = getIntegerList(fileScanner,vehicleAmount);

        int[] factorySet = getSetList(fileScanner, factoryAmount, 2*orderAmount);

        int[] stopCapacity = getIntegerList(fileScanner, factoryAmount);

        int[] locationSet = getSetList(fileScanner, stopAmount, 2*orderAmount);

        List<Collection<Integer>> vehicleNodeSet = getListCollection(fileScanner, vehicleAmount);

        List<Collection<Integer>> vehiclePickupNodeSet = getListCollection(fileScanner, vehicleAmount);

        int[] vehicleStartingLocation = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleDestinationLocation = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleWeightCapacity = getIntegerList(fileScanner,vehicleAmount);

        int[] orderWeight = getIntegerList(fileScanner,orderAmount);

        int[] vehicleVolumeCapacity = getIntegerList(fileScanner,vehicleAmount);

        int[] orderVolume = getIntegerList(fileScanner,orderAmount);

        int[] orderPenalty = getIntegerList(fileScanner,orderAmount);

        int maxDistanceDimension = getMaxValue(distanceDimension);
        int maxWeightDimension = getMaxValue(weightDimension);

        int[][] distanceInterval = getInteger2DList(fileScanner,vehicleAmount,maxDistanceDimension+1);

        int[][] weightInterval = getInteger2DList(fileScanner, vehicleAmount, maxWeightDimension+1);

        int[][][] kmCostMatrix = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension,maxWeightDimension);

        int[][][] kgCostMatrix = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);

        int[][][] fixCostMatrix = getInteger3DList(fileScanner, vehicleAmount, maxDistanceDimension, maxWeightDimension);

        int[][] stopCosts = getInteger2DList(fileScanner, vehicleAmount, 2*orderAmount);

        int[] timeWindowAmounts = getIntegerList(fileScanner,2*orderAmount);

        int timeWindowMax = getMaxValue(timeWindowAmounts);

        int[][] lowerTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][] upperTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][][] travelTime = getInteger3DList(fileScanner, vehicleAmount, 2*orderAmount, 2*orderAmount);

        int[][] travelDistance = getInteger2DList(fileScanner, 2*orderAmount, 2*orderAmount);

        result = new DataSet(vehicleAmount,orderAmount,factoryAmount,stopAmount);
        result.setWeightDimension(weightDimension);
        result.setDistanceDimension(distanceDimension);
        result.setFactory(factorySet);
        result.setFactoryStopCapacity(stopCapacity);
        result.setLocation(locationSet);
        result.setVehicleNode(vehicleNodeSet);
        result.setVehiclePickupNode(vehiclePickupNodeSet);
        result.setVehicleStartingLocation(vehicleStartingLocation);
        result.setVehicleDestinationLocation(vehicleDestinationLocation);
        result.setVehicleWeightCapacity(vehicleWeightCapacity);
        result.setOrderWeight(orderWeight);
        result.setVehicleVolumeCapacity(vehicleVolumeCapacity);
        result.setOrderVolume(orderVolume);
        result.setOrderPenalty(orderPenalty);
        result.setDistanceInterval(distanceInterval);
        result.setWeightInterval(weightInterval);
        result.setKmCostMatrix(kmCostMatrix);
        result.setKgCostMatrix(kgCostMatrix);
        result.setFixCostMatrix(fixCostMatrix);
        result.setStopCostMatrix(stopCosts);
        result.setTimeWindowAmount(timeWindowAmounts);
        result.setLowerTimeWindow(lowerTimeWindows);
        result.setUpperTimeWindow(upperTimeWindows);
        result.setTravelTime(travelTime);
        result.setTravelDistance(travelDistance);
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
