package reader;

import dataObjects.DataSet;
import dataObjects.IDataSet;

import java.io.File;
import java.util.*;

public class FlowReaderPrint implements IReader {

    /**
     * function reads data from files on format *input*-demand.csv/tariffs_LTL.csv etc..
     * LTL_tariffs must be sorted first on kg, then on KM
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    public IDataSet readDataFromFile(String fileName) throws Exception {

        IDataSet result;

        File demandFile = new File("res/"+fileName+"-demand.csv");
        Scanner scanFile = new Scanner(demandFile);

        //ignore first line
        scanFile.nextLine();
        int orderCounter = 0;
        int locationCounter = 0;
        int factoryCounter = 0;

        ArrayList<Double> orderWeight = new ArrayList<>();
        ArrayList<Double> orderVolume = new ArrayList<>();
        ArrayList<Integer> orderPickupLocation = new ArrayList<>();
        ArrayList<Integer> orderDeliveryLocation = new ArrayList<>();
        ArrayList<Integer> orderDeliveryDock = new ArrayList<>();
        ArrayList<Integer> factoryArray = new ArrayList<>();
        HashSet<Integer> factories = new HashSet<>();
        HashMap<String,Integer> locationIndexMap = new HashMap<>();

        while(scanFile.hasNext()){
            String line = scanFile.nextLine();
            String[] splitLine = line.split(",");

            if(!locationIndexMap.containsKey(splitLine[0])) {
                locationCounter++;
                locationIndexMap.put(splitLine[0], locationCounter);
            }

            if(!locationIndexMap.containsKey(splitLine[1])){
                locationCounter++;
                factories.add(locationCounter);
                factoryCounter++;
                locationIndexMap.put(splitLine[1],locationCounter);
            }

            for(int i = 0; i< Integer.parseInt(splitLine[10]);i++){
                orderDeliveryDock.add(Integer.parseInt(splitLine[11]));
                orderPickupLocation.add(locationIndexMap.get(splitLine[0]));
                orderDeliveryLocation.add(locationIndexMap.get(splitLine[1]));
                orderWeight.add(Double.parseDouble(splitLine[8]));
                orderVolume.add(Double.parseDouble(splitLine[9]));
                orderCounter++;
            }

        }
        int[] factoryList = createSetList(factories, locationCounter);
        int[] locations = createInterationsArray(locationCounter);
        int[] orderPickupLocations = toIntArray(orderPickupLocation);
        int[] orderDeliveryLocations = toIntArray(orderDeliveryLocation);
        double[] orderVolumes = toDoubleArray(orderVolume);
        double[] orderWeights = toDoubleArray(orderWeight);
        int[] orderDeliveryDocks = toIntArray(orderDeliveryDock);
        System.out.println("OrderAmount: "+orderCounter);
        System.out.println("LocationAmount: "+locationCounter);
        System.out.println("Factory Amount: "+factoryCounter);
        System.out.println("Factories:");
        printList(factoryList);
        System.out.println("PickupLocations: ");
        printList(orderPickupLocations);
        System.out.println("DeliveryLocations: ");
        printList(orderDeliveryLocations);
        System.out.println("Weights:");
        printList(orderWeights);
        System.out.println("Volumes:");
        printList(orderVolumes);
        System.out.println("Delivery Docks:");
        printList(orderDeliveryDocks);

        int vehicleCounter = orderCounter/2;
        int stopCounter = locationCounter;

        File tariffLTLfile = new File("res/"+fileName+"-tariffs_LTL.csv");
        Scanner tariffScan = new Scanner(tariffLTLfile);
        tariffScan.nextLine(); //ignore headlines

        int kmDimension=0;
        int kgDimension=0;
        HashMap<String, Integer> kmInterval = new HashMap<>();
        HashMap<Double, Integer> kgInterval = new HashMap<>();
        ArrayList<Double> kmIntervalList = new ArrayList<>();
        ArrayList<Double> kgIntervalList = new ArrayList<>();
        ArrayList<Double> volIntervalList = new ArrayList<>();
        ArrayList<ArrayList<Double>> tariffMatrix = new ArrayList<>();


        while(tariffScan.hasNext()){
            String line = tariffScan.nextLine();
            String[] splitLine = line.split(",");

            String km = splitLine[10];
            if(!kmInterval.containsKey(km)){
                tariffMatrix.add(new ArrayList<>());
                kmInterval.put(km, kmDimension);
                String inf = "inf";
                if(!km.equals(inf)) {
                    kmIntervalList.add(Double.parseDouble(km));
                } else {
                    kmIntervalList.add(Double.MAX_VALUE);
                }
                kmDimension++;
            }
            Double kg = Double.parseDouble(splitLine[3]);
            if(!kgInterval.containsKey(kg)){
                kgInterval.put(kg, kgDimension);
                kgIntervalList.add(kg);
                volIntervalList.add(Double.parseDouble(splitLine[4]));
                kgDimension++;
            }
            tariffMatrix.get(kmDimension-1).add(Double.parseDouble(splitLine[2]));
        }

        //adding same fixed cost matrix for all vehicles:
        double[][][] fixCostMatrices = twoDimensionTo3DDoubleArray(tariffMatrix,vehicleCounter);

        double[][] distanceIntervals = oneDimensionTo2DDoubleArray(kmIntervalList, vehicleCounter);
        double[][] weightIntervals = oneDimensionTo2DDoubleArray(kgIntervalList,vehicleCounter);
        double[][] volIntervals = oneDimensionTo2DDoubleArray(volIntervalList,vehicleCounter);
        double[] vehicleKGCapacities = createArrayOfSingleElement(weightIntervals[0][weightIntervals[0].length-1], vehicleCounter);
        double[] vehicleVOLCapacities = createArrayOfSingleElement(volIntervals[0][volIntervals[0].length-1],vehicleCounter);
        int[] distanceDimensions = createArrayOfSingleElement(distanceIntervals[0].length, vehicleCounter);
        int[] weightDimensions = createArrayOfSingleElement(weightIntervals[0].length,vehicleCounter);

        double[][][] kmCostMatrices = new double[vehicleCounter][weightIntervals[0].length][distanceIntervals[0].length];
        double[][][] kgCostMatrices = new double[vehicleCounter][weightIntervals[0].length][distanceIntervals[0].length];

        double[] orderPenalties = createArrayOfSingleElement(fixCostMatrices[0][weightIntervals[0].length-1][distanceIntervals[0].length-1]*2, orderCounter);


        System.out.println("Tariff List: ");
        printList(fixCostMatrices);

        System.out.println("Weight Intervals:");
        printList(weightIntervals);

        System.out.println("distance Intervals:");
        printList(distanceIntervals);

        System.out.println("distance Dimensions:");
        printList(distanceDimensions);

        System.out.println("weight dimensions: ");
        printList(weightDimensions);

        System.out.println("order penalties");
        printList(orderPenalties);

        boolean[][] vehicleCanPickupOrder = createAllTrueArray(vehicleCounter, orderCounter);
        boolean[][] vehicleCanVisitNodes = createAllTrueArray(vehicleCounter,locationCounter);
        int[] vehicleStartingLocations = createArrayOfSingleElement(0,vehicleCounter);
        int[] vehicleDestinationLocations = createArrayOfSingleElement(locationCounter+1,vehicleCounter);

        File distancesFile = new File("res/"+fileName+"-distances.csv");
        Scanner distanceScan = new Scanner(distancesFile);

        String firstLine = distanceScan.nextLine(); //ignore headlines
        String[] splitFirstLine = firstLine.split(",");

        double[][] travelDistance = new double[locationCounter][locationCounter];

        while(distanceScan.hasNext()){
            String line = distanceScan.nextLine();
            String[] splitLine = line.split(",");
            int firstLocation = locationIndexMap.get(splitLine[0]);
            for (int i = 0; i<locationCounter;i++){
                travelDistance[firstLocation-1][locationIndexMap.get(splitFirstLine[1+i])-1] = Double.parseDouble(splitLine[1+i]);
            }
        }

        System.out.println("travel Distance:");
        printList(travelDistance);

        double[][][] travelTimes = twoDimensionTo3DDoubleArray(travelDistance,vehicleCounter);

        System.out.println("travel times:");
        printList(travelTimes);

        double[][] stopCosts = new double[vehicleCounter][locationCounter];
        double[][] lowerTimeWindows = new double[locationCounter][5];
        double[][] upperTimeWindows = new double[locationCounter][5];
        int[] timeWindowAmounts = new int[locationCounter];
        int[] factoryStopCapacity = new int[factoryCounter];

        File locationsFile = new File("res/"+fileName+"-locations.csv");
        Scanner locationsScan = new Scanner(locationsFile);
        locationsScan.nextLine(); //ignore headlines

        while(locationsScan.hasNext()) {
            String line = locationsScan.nextLine();
            String[] splitLine = line.split(",");
            Integer location = locationIndexMap.get(splitLine[0])-1;
            stopCosts[0][location] = Double.parseDouble(splitLine[1]);
            if(factories.contains(location+1)){
                factoryStopCapacity[factoryList[location]-1] = Integer.parseInt(splitLine[3]);
            }
            timeWindowAmounts[location] = Integer.parseInt(splitLine[4]);
            for (int i = 0;i<timeWindowAmounts[location];i++){
                lowerTimeWindows[location][i] = Double.parseDouble(splitLine[5+2*i]);
                upperTimeWindows[location][i] = Double.parseDouble(splitLine[6+2*i]);
            }
        }

        System.out.println("stop costs:");
        printList(stopCosts);

        System.out.println("factory Stop Capacities: ");
        printList(factoryStopCapacity);

        System.out.println("Timewindow amounts: ");
        printList(timeWindowAmounts);

        System.out.println("lower timewindows:");
        printList(lowerTimeWindows);

        System.out.println("upper timewindows: ");
        printList(upperTimeWindows);


        result = new DataSet(vehicleCounter, orderCounter, locationCounter, factoryCounter);

        result.setOrderVolumes(orderVolumes);
        result.setOrderWeights(orderWeights);
        result.setOrderPickupLocations(orderPickupLocations);
        result.setOrderDeliveryLocations(orderDeliveryLocations);
        result.setOrderDeliveryDocks(orderDeliveryDocks);
        result.setLocations(locations);
        result.setDistanceIntervals(distanceIntervals);
        result.setWeightIntervals(weightIntervals);
        result.setDistanceDimensions(distanceDimensions);
        result.setWeightDimensions(weightDimensions);
        result.setFixCostMatrices(fixCostMatrices);
        result.setKgCostMatrices(kgCostMatrices);
        result.setKmCostMatrices(kmCostMatrices);
        result.setOrderPenalties(orderPenalties);
        result.setVehicleWeightCapacities(vehicleKGCapacities);
        result.setVehicleVolumeCapacities(vehicleVOLCapacities);
        result.setVehicleCanVisitNode(vehicleCanVisitNodes);
        result.setVehicleCanPickupOrder(vehicleCanPickupOrder);
        result.setVehicleStartingLocations(vehicleStartingLocations);
        result.setVehicleDestinationLocations(vehicleDestinationLocations);
        result.setTravelDistances(travelDistance);
        result.setTravelTimes(travelTimes);
        result.setStopCosts(stopCosts);
        result.setLowerTimeWindows(lowerTimeWindows);
        result.setUpperTimeWindows(upperTimeWindows);
        result.setTimeWindowAmounts(timeWindowAmounts);
        result.setFactories(factoryList);
        result.setFactoryStopCapacities(factoryStopCapacity);

        return result;
    }

    @Override
    public List<IDataSet> readDataFromFiles(String instance, List<String> instanceSizes) throws Exception {
        return null;
    }

    private int[] createSetList(HashSet<Integer> factories, int size) {
        int[] result = new int[size];
        int counter = 1;
        for (Integer i:factories) {
            result[i-1] = counter;
            counter++;
        }
        return result;
    }

    private void printList(double[][] twoDimensionList) {
        for (int i = 0; i<twoDimensionList.length;i++){
            for(int j = 0; j<twoDimensionList[0].length;j++){
                System.out.println((i+1)+" "+(j+1)+" "+ twoDimensionList[i][j]);
            }
        }

    }

    private boolean[][] createAllTrueArray(int firstDimension, int secondDimension) {
        boolean[][] result = new boolean[firstDimension][secondDimension];
        for (int i = 0;i<firstDimension;i++){
            for (int j = 0; j<secondDimension;j++){
                result[i][j] = true;
            }
        }
        return result;
    }

    private int[] createInterationsArray(int limit) {
        int[] result = new int[limit];
        for (int i = 1;i<=limit;i++){
            result[i-1] = i;
        }
        return result;
    }

    private double[][] oneDimensionTo2DDoubleArray(ArrayList<Double> list, int firstDimensionSize) {
        double[][] result = new double[firstDimensionSize][list.size()];
        for (int i = 0; i<list.size();i++){
            result[0][i]=list.get(i);
        }
        for (int i = 0;i<firstDimensionSize;i++){
            result[i] = result[0];
        }
        return result;
    }

    private int[] createArrayOfSingleElement(int element, int length) {
        int[] result = new int[length];
        for (int i = 0;i<length;i++){
            result[i] = element;
        }
        return result;
    }

    private double[] createArrayOfSingleElement(double element, int length) {
        double[] result = new double[length];
        for (int i = 0;i<length;i++){
            result[i] = element;
        }
        return result;
    }

    private void printList(int[][] list) {
        for (int i = 0; i<list.length;i++) {
            for (int j = 0; j < list[0].length; j++) {
                System.out.println((i+1) + " " + (j+1) + " " + list[i][j]);
            }
        }
    }

    private int[][] oneDimensionTo2DIntArray(ArrayList<Integer> list, int firstDimensionSize) {
        int[][] result = new int[firstDimensionSize][list.size()];
        for (int i = 0; i<list.size();i++){
            result[0][i]=list.get(i);
        }
        for (int i = 0;i<firstDimensionSize;i++){
            result[i] = result[0];
        }
        return result;
    }

    private void printList(double[][][] threeDimensionList) {
        for (int i = 0; i<threeDimensionList.length;i++){
            for(int j = 0; j<threeDimensionList[0].length;j++){
                for (int k = 0; k<threeDimensionList[0][0].length; k++){
                    System.out.println((i+1)+" "+(j+1)+" "+(k+1)+" "+ threeDimensionList[i][j][k]);
                }
            }
        }
    }

    private double[][][] twoDimensionTo3DDoubleArray(ArrayList<ArrayList<Double>> array, int firstDimensionSize) {
        int secondDimensionSize = array.get(0).size();   //X different weight intervals
        int thirdDimensionSize = array.size();           //Y different km intervals
        double[][][] result = new double[firstDimensionSize][secondDimensionSize][thirdDimensionSize];
        for (int i = 0;i<secondDimensionSize;i++){
            for (int j = 0;j<thirdDimensionSize;j++){
                result[0][i][j] = array.get(j).get(i);
            }
        }

        for(int i = 1; i<firstDimensionSize;i++){
            result[i] = result[0];
        }
        return result;
    }


    private double[][][] twoDimensionTo3DDoubleArray(double[][] array, int firstDimensionSize) {
        int secondDimensionSize = array[0].length;   //X different weight intervals
        int thirdDimensionSize = array.length;           //Y different km intervals
        double[][][] result = new double[firstDimensionSize][secondDimensionSize][thirdDimensionSize];
        for (int i = 0;i<secondDimensionSize;i++){
            for (int j = 0;j<thirdDimensionSize;j++){
                result[0][i][j] = array[i][j];
            }
        }

        for(int i = 1; i<firstDimensionSize;i++){
            result[i] = result[0];
        }
        return result;
    }

    private void printList(double[] list) {
        for (double i:list){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    private void printList(int[] list) {
        for (int i:list){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    private double[] toDoubleArray(ArrayList<Double> orderVolume) {
        double[] result = new double[orderVolume.size()];
        for (int i = 0;i<orderVolume.size();i++){
            result[i] = orderVolume.get(i);
        }
        return result;
    }

    private int[] toIntArray(ArrayList<Integer> orderPickupLocation) {
        int[] result = new int[orderPickupLocation.size()];
        for (int i = 0;i<orderPickupLocation.size();i++){
            result[i] = orderPickupLocation.get(i);
        }
        return result;
    }
}
