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
        int distanceDimension = getIntFromLineAndMoveToNextSection(fileScanner,1);
        int weightDimension = getIntFromLineAndMoveToNextSection(fileScanner,1);

//        List<Collection<Integer>> factorySet = getListCollection(fileScanner, factoryAmount);

        int[] factorySet = getSetList(fileScanner, factoryAmount, 2*orderAmount);

        int[] stopCapacity = getIntegerList(fileScanner, factoryAmount);

        int[] locationSet = getSetList(fileScanner, stopAmount, 2*orderAmount);

        List<Collection<Integer>> vehicleNodeSet = getListCollection(fileScanner, vehicleAmount);

        List<Collection<Integer>> vehiclePickupNodeSet = getListCollection(fileScanner, vehicleAmount);

        int[] vehicleStartingLocation = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleDestinationLocation = getIntegerList(fileScanner, vehicleAmount);

        int[] vehicleCapacity = getIntegerList(fileScanner,vehicleAmount);

        int[] orderWeight = getIntegerList(fileScanner,orderAmount);

        int[] orderPenalty = getIntegerList(fileScanner,orderAmount);

        int[] distanceInterval = getIntegerList(fileScanner,distanceDimension+1);

        int[] weightInterval = getIntegerList(fileScanner, weightDimension+1);

        int[][][] kmCostMatrix = getInteger3DList(fileScanner, vehicleAmount, distanceDimension, weightDimension);

        int[][][] kgCostMatrix = getInteger3DList(fileScanner, vehicleAmount, distanceDimension, weightDimension);

        int[][][] fixCostMatrix = getInteger3DList(fileScanner, vehicleAmount, distanceDimension, weightDimension);

        int[][] stopCosts = getInteger2DList(fileScanner, vehicleAmount, 2*orderAmount);

        int[] timeWindowAmounts = getIntegerList(fileScanner,2*orderAmount);

        int timeWindowMax = getMaxValue(timeWindowAmounts);

        int[][] lowerTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][] upperTimeWindows = getInteger2DList(fileScanner, timeWindowMax, 2*orderAmount);

        int[][][] travelTime = getInteger3DList(fileScanner, vehicleAmount, 2*orderAmount, 2*orderAmount);

        int[][] travelDistance = getInteger2DList(fileScanner, 2*orderAmount, 2*orderAmount);

        result = new DataSet(vehicleAmount,orderAmount,factoryAmount,stopAmount,distanceDimension,weightDimension);
        result.setFactories(factorySet);
        result.setFactoryStopCapacities(stopCapacity);
        result.setLocations(locationSet);
        result.setVehicleNodes(vehicleNodeSet);
        result.setVehiclePickupNodes(vehiclePickupNodeSet);
        result.setVehicleStartingLocations(vehicleStartingLocation);
        result.setVehicleDestinationLocations(vehicleDestinationLocation);
        result.setVehicleCapacities(vehicleCapacity);
        result.setOrderWeights(orderWeight);
        result.setOrderPenalties(orderPenalty);
        result.setDistanceIntervals(distanceInterval);
        result.setWeightIntervals(weightInterval);
        result.setKmCostMatrix(kmCostMatrix);
        result.setKgCostMatrix(kgCostMatrix);
        result.setFixCostMatrix(fixCostMatrix);
        result.setStopCostMatrix(stopCosts);
        result.setTimeWindowAmounts(timeWindowAmounts);
        result.setLowerTimeWindows(lowerTimeWindows);
        result.setUpperTimeWindows(upperTimeWindows);
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
