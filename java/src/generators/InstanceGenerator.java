package generators;

import dataObjects.Elipse;
import dataObjects.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.PI;

public class InstanceGenerator implements IInstanceGenerator {
    private static final double ORDER_WEIGHT_LOWER_BOUND = 50;
    private static final double ORDER_WEIGHT_MEDIUM_BOUND = 2000;
    private static final double ORDER_WEIGHT_UPPER_BOUND = 24000;
    private static final double VEHICLE_WEIGHT_UPPER_BOUND_SMALL = 12000;
    private static final double VEHICLE_WEIGHT_UPPER_BOUND_MEDIUM = 18000;
    private static final double ORDER_VOLUME_PORTION_LOWER_BOUND = 35;
    private static final double ORDER_VOLUME_PORTION_UPPER_BOUND = 450;
    private static final double ORDER_VOLUME_PORTION_MEDIAN = 120;
    private static final double ORDER_VOLUME_UPPER_BOUND = 102;
    private static final double VEHICLE_VOLUME_UPPER_BOUND_SMALL = 55;
    private static final double VEHICLE_VOLUME_UPPER_BOUND_MEDIUM = 71;
    private static final double FIXED_INCREASE_RATE_LARGE = 1.64;
    private static final double FIXED_INCREASE_RATE_MEDIUM = 1.56;
    private static final double FIXED_INCREASE_RATE_SMALL = 1.50;
    private static final int LARGEST_INSTANCE_ORDER_AMOUNT = 150;
    private static final double LARGE_NUMBER = 9999999;
    private static final double SMALL_VEHICLE_STOP_MULTIPLIER = 1;
    private static final double MEDIUM_VEHICLE_STOP_MULTIPLIER = 1.5;
    private static final double LARGE_VEHICLE_STOP_MULTIPLIER = 2;
    private static final int TIME_WINDOW_LOWER_BOUND_HOURS = 2;
    private static final int MINIMUM_OPENING_TIME = 3;
    private static final int MINIMUM_CLOSED_TIME = 1;
    private static final double TRAVEL_TIME_PERCENT_OF_DISTANCE = 0.6;
    private static final double TRAVEL_TIME_PERCENT_VARIATION = 0.1;
    private static final int MAX_INSTANCE_ORDERS = 200;
    private final double UPPER_BOUND_PROBABILITY = 0.9;
    private Random random;
    private final int COST_STRUCTURE_DISTANCE_DIMENSION = 10;
    private final int COST_STRUCTURE_WEIGHT_DIMENSION = 28;
    private final double MINIMUM_COST_OF_NO_TRANSPORT = 20000;
    private int TIMEWINDOW_AMOUNT_LOWER_BOUND = 3;
    private final List<Elipse> possibilitiesGermany;
    private final List<Elipse> possibilitiesEurope;
    private int UNIFORM_UPPER_BOUND_X=1500;
    private int UNIFORM_UPPER_BOUND_Y=1500;
    private double distMult = 30;
    private double weightMult= 10;
    private double sizeMult= 1000;
    private double SPECIAL_ABILITY_PROBABILITY= 0.95;

    public InstanceGenerator(){
        possibilitiesEurope = generateEuropeList();
        possibilitiesGermany = generateGermanyList();
    }

    private List<Elipse> generateEuropeList() {
        return Arrays.asList(   new Elipse(new Point(1300,1300),300,340),   //Germany
                                new Elipse(new Point(1300,1300),300,340),
                                new Elipse(new Point(1300,1300),300,340),
                                new Elipse(new Point(680,870),300,320),     //France
                                new Elipse(new Point(680,870),300,320),
                                new Elipse(new Point(680,870),300,320),
                                new Elipse(new Point(350,300), 330,300),    //Spain
                                new Elipse(new Point(350,300), 330,300),
                                new Elipse(new Point(350,300), 330,300),
                                new Elipse(new Point(470,1620), 200,300),   //Great Britain
                                new Elipse(new Point(470,1620), 200,300),
                                new Elipse(new Point(1250,480), 150,300),   //Italy
                                new Elipse(new Point(1250,480), 150,300),
                                new Elipse(new Point(1900,1350), 350,300),  //Poland
                                new Elipse(new Point(1900,1350), 350,300),
                                new Elipse(new Point( 1650, 1040), 130,100),
                                new Elipse(new Point(1850,820), 150,100),
                                new Elipse(new Point(1500,860),180,90),
                                new Elipse(new Point(1100,850),110,100),
                                new Elipse(new Point(860,1250),110,90),
                                new Elipse(new Point(920,1460),90,110));
    }

    private List<Elipse> generateGermanyList(){
        return Arrays.asList(
                new Elipse(new Point(525,570),55,60),   //Berlin
                new Elipse(new Point(525,570),55,60),   //Berlin
                new Elipse(new Point(525,570),55,60),   //Berlin
                new Elipse(new Point(300,775),35,35),
                new Elipse(new Point(295,690),45,45),
                new Elipse(new Point(295,690),45,45),   //Hamburg
                new Elipse(new Point(295,690),45,45),
                new Elipse(new Point(200,640),35,30),
                new Elipse(new Point(80,415),50,70),    //Düsseldorf/Cologne
                new Elipse(new Point(80,415),50,70),
                new Elipse(new Point(80,415),50,70),
                new Elipse(new Point(405,95),80,50),    //Munich
                new Elipse(new Point(405,95),80,50),
                new Elipse(new Point(405,95),80,50),
                new Elipse(new Point(205,305), 30,40),    //Frankfurt
                new Elipse(new Point(205,305), 30,40),
                new Elipse(new Point(273,550), 30,45),
                new Elipse(new Point(240,165), 35,35),
                new Elipse(new Point(368,245), 35,35),
                new Elipse(new Point(540,415), 35,35),
                new Elipse(new Point(455,445), 35,35),
                new Elipse(new Point(90,210), 35,40),
                new Elipse(new Point(445,760), 30,30)
                );
    }

    @Override
    public void makeInstance(int orderAmount, int vehicleAmount, int pickupLocationAmount, int deliveryLocationAmount, int factoryAmount, int instanceID, int instanceType) {

        try {
            FileWriter fileWriter = new FileWriter("res/instances/Inst"+instanceID+"_Ord_"+orderAmount+"_Veh_"+vehicleAmount+"_Loc_"+(pickupLocationAmount+deliveryLocationAmount)+".dat");

            this.random = new Random(10+instanceID);

            int itemNumber = 1;

            printNumber(itemNumber++, vehicleAmount, "nv", "amount of Vehicles", fileWriter);

            printNumber(itemNumber++,orderAmount,"no","amount of Orders",fileWriter);

            int totalLocationsAmount = pickupLocationAmount+deliveryLocationAmount;

            printNumber(itemNumber++,totalLocationsAmount,"nl","amount of locations",fileWriter);

            printNumber(itemNumber++,factoryAmount,"nf","amount of factories",fileWriter);

            writeHeaderSection(itemNumber++, "factory sets", fileWriter);

            String setName = "N_f_2";

            int[][] factories = new int[factoryAmount][deliveryLocationAmount];

            int[] factory = new int[totalLocationsAmount+1];
            for (int i = 0; i < pickupLocationAmount + 1; i++) {
                factory[i] = i;
            }
            int counter = pickupLocationAmount+1;
            int[] indexes = new int[factoryAmount];
            for (int i = 0; i < factoryAmount; i++) {
                factory[counter] = counter;
                factories[i][0]=counter++;
                indexes[i]++;
            }

            while (counter<totalLocationsAmount+1){
                int factoryChoice = random.nextInt(factoryAmount);
                factory[counter] = factories[factoryChoice][0];
                factories[factoryChoice][indexes[factoryChoice]++]=counter++;
            }

            for (int i = 0;i<factoryAmount;i++) {
                writeSetNumber(setName, i, fileWriter);
                counter=0;
                while(factories[i][counter]!=0){
                    fileWriter.write(" "+factories[i][counter++]);
                }
                fileWriter.write(" ;\n");
            }

            fileWriter.write("\n");

            writeNewParameterSection(itemNumber++,"H","factory stop capacities",fileWriter);
            for (int i = 0;i<factoryAmount-1;i++){
                fileWriter.write((i+1)+" "+(1+random.nextInt(indexes[i]))+"\n");
            }
            fileWriter.write(factoryAmount+" "+(1+random.nextInt(indexes[factoryAmount-1]))+" ;\n\n");

            writeNewParameterSection(itemNumber++,"L_pd","pickup locations for each order",fileWriter);

            int[] pickupLocation = oneDimensionTableWithRandomElements(orderAmount, pickupLocationAmount, 1, fileWriter);

            writeNewParameterSection(itemNumber++, "L_dd", "delivery locations for each order", fileWriter);

            int[] deliveryLocation = oneDimensionTableWithRandomElements(orderAmount, deliveryLocationAmount,pickupLocationAmount+1, fileWriter);

            int[][] locationOrderSet = new int[totalLocationsAmount+1][orderAmount];
            int[] locationOrderSetCounter = new int[totalLocationsAmount+1];
            for(int i = 0;i<orderAmount;i++) {
                locationOrderSet[pickupLocation[i]][locationOrderSetCounter[pickupLocation[i]]++] = i+1;
            }
            for(int i = 0;i<orderAmount;i++) {
                locationOrderSet[deliveryLocation[i]][locationOrderSetCounter[deliveryLocation[i]]++] = i+1;
            }

            writeHeaderSection(itemNumber++,"location sets",fileWriter);
            setName = "L_2";
            for(int i = 0;i<totalLocationsAmount;i++){
                writeSetNumber(setName, i, fileWriter);
                int j = 0;
                while(locationOrderSet[i+1][j]!=0){
                    fileWriter.write(" "+locationOrderSet[i+1][j++]);
                }
                fileWriter.write(" ;\n");
            }
            fileWriter.write("\n");

            double vehicleVolumeMax = 102;

            writeNewParameterSection(itemNumber++,"Q_kg_2","order weights",fileWriter);
            double[] orderWeights = oneDimensionTable(orderAmount,ORDER_WEIGHT_LOWER_BOUND,ORDER_WEIGHT_MEDIUM_BOUND,ORDER_WEIGHT_UPPER_BOUND,fileWriter);

            writeNewParameterSection(itemNumber++,"Q_vol_2","order volumes",fileWriter);
            double[] orderVolumes = oneDimensionVolumeTable(orderAmount,ORDER_VOLUME_PORTION_MEDIAN, ORDER_VOLUME_PORTION_LOWER_BOUND,ORDER_VOLUME_PORTION_UPPER_BOUND, fileWriter, orderWeights);

            boolean[] pickuplocationRequiresSpecialProperty = makeProbabilityTable(pickupLocationAmount+1, SPECIAL_ABILITY_PROBABILITY);
            boolean[] orderRequiresSpecialProperty = makeProbabilityTable(orderAmount, SPECIAL_ABILITY_PROBABILITY);

            int[] distanceDimensions = new int[vehicleAmount];

            int[] weightDimensions = new int[vehicleAmount];


            int type1DistanceDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_DISTANCE_DIMENSION);
            int type2DistanceDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_DISTANCE_DIMENSION);
            int type3DistanceDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_DISTANCE_DIMENSION);
            int maxDistanceDimension = Math.max(type1DistanceDimensions,Math.max(type2DistanceDimensions,type3DistanceDimensions));
            int type1WeightDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_WEIGHT_DIMENSION);
            int type2WeightDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_WEIGHT_DIMENSION);
            int type3WeightDimensions = getRandomNumber(orderAmount, COST_STRUCTURE_WEIGHT_DIMENSION);
            int maxWeightDimension = Math.max(type1WeightDimensions,Math.max(type2WeightDimensions,type3WeightDimensions));
            double[] distanceInterval = {100,250,500,1000,1500,2000,2500,3000,6000,LARGE_NUMBER};
            double[] weightInterval = {50,75,100,200,300,400,500,600,700,800,900,1000,1250,1500,1750,2000,2500,3000,3500,4000,5000,6000,7000,8000,9000,10000,15000,24000};

            double[] type1DistanceInterval = getIntervalTable(distanceInterval,type1DistanceDimensions,maxDistanceDimension);
            double[] type2DistanceInterval = getIntervalTable(distanceInterval,type2DistanceDimensions,maxDistanceDimension);
            double[] type3DistanceInterval = getIntervalTable(distanceInterval,type3DistanceDimensions,maxDistanceDimension);
            double[] type1WeightInterval = getIntervalTable(weightInterval,type1WeightDimensions,maxWeightDimension);
            double[] type2WeightInterval = getIntervalTable(weightInterval,type2WeightDimensions,maxWeightDimension);
            double[] type3WeightInterval = getIntervalTable(weightInterval,type3WeightDimensions,maxWeightDimension);
            double[][] distanceIntervals = new double[vehicleAmount][maxDistanceDimension];
            double[][] weightIntervals = new double[vehicleAmount][maxWeightDimension];

            boolean[][] vehicleCanPickupOrder = new boolean[vehicleAmount][orderAmount];
            boolean[][] vehicleCanVisitLocation = new boolean[vehicleAmount][totalLocationsAmount+1];
            double[] vehicleVolumeCapacity = new double[vehicleAmount];
            double[] vehicleWeightCapacity = new double[vehicleAmount];
            double[][] stopCosts = new double[vehicleAmount][totalLocationsAmount];
            double[] stopMultiplier=new double[vehicleAmount];

            for (int i = 1; i<=vehicleAmount;i++){
                if (i<=vehicleAmount/3){
                    stopMultiplier[i-1] = SMALL_VEHICLE_STOP_MULTIPLIER;
                    distanceDimensions[i-1]=type1DistanceDimensions;
                    weightDimensions[i-1] = type1WeightDimensions;
                    distanceIntervals[i-1]=type1DistanceInterval;
                    weightIntervals[i-1]=type1WeightInterval;
                    vehicleVolumeCapacity[i-1]= VEHICLE_VOLUME_UPPER_BOUND_SMALL;
                    vehicleWeightCapacity[i-1]= VEHICLE_WEIGHT_UPPER_BOUND_SMALL;
                } else if (i<=vehicleAmount/3*2){
                    stopMultiplier[i-1] = MEDIUM_VEHICLE_STOP_MULTIPLIER;
                    distanceDimensions[i-1] = type2DistanceDimensions;
                    weightDimensions[i-1]=type2WeightDimensions;
                    distanceIntervals[i-1]=type2DistanceInterval;
                    weightIntervals[i-1]=type2WeightInterval;
                    vehicleVolumeCapacity[i-1]= VEHICLE_VOLUME_UPPER_BOUND_MEDIUM;
                    vehicleWeightCapacity[i-1]= VEHICLE_WEIGHT_UPPER_BOUND_MEDIUM;
                } else {
                    stopMultiplier[i-1] = LARGE_VEHICLE_STOP_MULTIPLIER;
                    distanceDimensions[i-1] = type3DistanceDimensions;
                    weightDimensions[i-1]=type3WeightDimensions;
                    distanceIntervals[i-1]=type3DistanceInterval;
                    weightIntervals[i-1]=type3WeightInterval;
                    vehicleVolumeCapacity[i-1]=ORDER_VOLUME_UPPER_BOUND;
                    vehicleWeightCapacity[i-1]=ORDER_WEIGHT_UPPER_BOUND;
                }
                for(int j = 0;j<orderAmount;j++) {

                    //first type of vehicle, smaller capacity, no special ability at location or for order
                    if (i <= vehicleAmount / 3) {
                        if(orderWeights[j]> vehicleWeightCapacity[i-1]||orderVolumes[j]>vehicleVolumeCapacity[i-1]||
                                pickuplocationRequiresSpecialProperty[pickupLocation[j]]||
                                orderRequiresSpecialProperty[j]){
                            vehicleCanPickupOrder[i-1][j]=false;
                        } else {
                            vehicleCanPickupOrder[i-1][j] = true;
                        }
                    } else if (i <= vehicleAmount / 3 * 2) //second type of vehicle, medium capacity, can pickup order special ability but not location special ability
                    {
                        if(orderWeights[j]> vehicleWeightCapacity[i-1]||orderVolumes[j]>vehicleVolumeCapacity[i-1]||
                                pickuplocationRequiresSpecialProperty[pickupLocation[j]]){
                            vehicleCanPickupOrder[i-1][j]=false;
                        } else {
                            vehicleCanPickupOrder[i-1][j] = true;
                        }
                    } else
                        {//third type of vehicle, can pickup all types of orders at all locations..
                            vehicleCanPickupOrder[i-1][j]=true;
                        }
                    if(pickuplocationRequiresSpecialProperty[pickupLocation[j]]&&i<=vehicleAmount/3*2) {
                        vehicleCanVisitLocation[i - 1][pickupLocation[j]] = false;
                        vehicleCanVisitLocation[i - 1][deliveryLocation[j]] = false;
                    } else {
                        vehicleCanVisitLocation[i - 1][pickupLocation[j]] = true;
                        vehicleCanVisitLocation[i - 1][deliveryLocation[j]] = true;
                    }
                }
            }

            writeHeaderSection(itemNumber++,"Locations vehicle can visit",fileWriter );
            writeBooleanSet("N_v_2", 1,vehicleCanVisitLocation, fileWriter);

            writeHeaderSection(itemNumber++,"Orders vehicles can pick up",fileWriter);
            writeBooleanSet("N_vp_2",0,vehicleCanPickupOrder,fileWriter);

            writeNewParameterSection(itemNumber++,"K_kg","vehicle weight capacity", fileWriter);

            writeTable(vehicleWeightCapacity, fileWriter);

            writeNewParameterSection(itemNumber++,"K_vol","vehicle volume capacity", fileWriter);
            writeTable(vehicleVolumeCapacity,fileWriter);

            writeNewParameterSection(itemNumber++,"na","amount of distance intervals",fileWriter);
            writeTable(distanceDimensions,fileWriter);

            writeNewParameterSection(itemNumber++,"nb","amount of weight intervals",fileWriter);
            writeTable(weightDimensions,fileWriter);

            writeNewParameterSection(itemNumber++, "B_2","Distance intervals",fileWriter);
            writeTable(distanceIntervals,fileWriter);

            writeNewParameterSection(itemNumber++, "Z_2","Weight intervals",fileWriter);
            writeTable(weightIntervals,fileWriter);

            //COST STRUCTURES
            double[][][] costPerKmStd = new double[3][COST_STRUCTURE_DISTANCE_DIMENSION][COST_STRUCTURE_WEIGHT_DIMENSION];
            double[][][] costPerKgStd = new double[3][COST_STRUCTURE_DISTANCE_DIMENSION][COST_STRUCTURE_WEIGHT_DIMENSION];
            double[][][] costFixedStd = new double[3][COST_STRUCTURE_DISTANCE_DIMENSION][COST_STRUCTURE_WEIGHT_DIMENSION];

            double[] kmZeroCostsLargeVehicle =  {0,0.02,0.035,0.035,0.04,0.035,0.03,0.03,0.03,0.025};
            double[] kmZeroCostsMediumVehicle = {0,0.02,0.035,0.03,0.035,0.03,0.027,0.027,0.025,0.022};
            double[] kmZeroCostsSmallVehicle =  {0,0.02,0.03,0.03,0.03,0.03,0.025,0.025,0.025,0.020};
            double[] kgZeroCostsLargeVehicle = {0.4,0.4,0.4,0.1,0.05,0.05,0.02,0.02,0.02,0.02};
            double[] kgZeroCostsMediumVehicle = {0.3,0.3,0.3,0.1,0.05,0.05,0.02,0.02,0.02,0.02};
            double[] kgZeroCostsSmallVehicle = {0.3,0.3,0.3,0.1,0.05,0.05,0.02,0.02,0.02,0.02};
            double[] fixedZeroCosts = {0,0.59,1.27,5,7,10,12,15,17,18,20,21,24,27,30,32,37,40,48,52,58,65,70,74,78,83,109.45,151.1};

            //largest vehicle cost structure
            for (int i = 0;i<COST_STRUCTURE_DISTANCE_DIMENSION;i++){
                costPerKmStd[0][i][0] = kmZeroCostsSmallVehicle[i];
                costPerKmStd[1][i][0] = kmZeroCostsMediumVehicle[i];
                costPerKmStd[2][i][0] = kmZeroCostsLargeVehicle[i];
                costPerKgStd[0][i][0] = kgZeroCostsSmallVehicle[i];
                costPerKgStd[1][i][0] = kgZeroCostsMediumVehicle[i];
                costPerKgStd[2][i][0] = kgZeroCostsLargeVehicle[i];
                costFixedStd[0][i][0] = fixedZeroCosts[0];
                costFixedStd[1][i][0] = fixedZeroCosts[0];
                costFixedStd[2][i][0] = fixedZeroCosts[0];
                for (int j = 1;j<COST_STRUCTURE_WEIGHT_DIMENSION;j++){
                    double relativeWeight = (weightInterval[j]-weightInterval[j-1])/weightInterval[j];
                    double relativeToPrevWeight = (weightInterval[j]-weightInterval[j-1])/weightInterval[j-1];
                    costPerKmStd[0][i][j]=costPerKmStd[0][i][j-1]+relativeWeight*costPerKmStd[0][i][j-1]/2;
                    costPerKmStd[1][i][j]=costPerKmStd[1][i][j-1]+relativeWeight*costPerKmStd[1][i][j-1]/2;
                    costPerKmStd[2][i][j]=costPerKmStd[2][i][j-1]+relativeWeight*costPerKmStd[2][i][j-1]/2;
                    costPerKgStd[0][i][j]=costPerKgStd[0][i][j-1]-relativeToPrevWeight*costPerKgStd[0][i][j-1]/2;
                    costPerKgStd[1][i][j]=costPerKgStd[1][i][j-1]-relativeToPrevWeight*costPerKgStd[1][i][j-1]/2;
                    costPerKgStd[2][i][j]=costPerKgStd[2][i][j-1]-relativeToPrevWeight*costPerKgStd[2][i][j-1]/2;
                    costFixedStd[0][i][j]=fixedZeroCosts[j]*Math.pow(FIXED_INCREASE_RATE_SMALL,(double)i);
                    costFixedStd[1][i][j]=fixedZeroCosts[j]*Math.pow(FIXED_INCREASE_RATE_MEDIUM,(double)i);
                    costFixedStd[2][i][j]=fixedZeroCosts[j]*Math.pow(FIXED_INCREASE_RATE_LARGE,(double)i);
                }
            }

            double[][][] costPerKm = new double[vehicleAmount][maxDistanceDimension][maxWeightDimension];
            double[][][] costPerKg = new double[vehicleAmount][maxDistanceDimension][maxWeightDimension];
            double[][][] costFixed = new double[vehicleAmount][maxDistanceDimension][maxWeightDimension];

            for (int v = 0;v<vehicleAmount;v++){
                int distance_length = costPerKmStd[0].length;
                int weight_length = costPerKmStd[0][0].length;
                for (int i = 0;i<maxDistanceDimension;i++) {
                    for (int j = 0;j<maxWeightDimension;j++) {
                        if ((i + 1) > distanceDimensions[v]||(j+1)>weightDimensions[v]) {
                            costPerKm[v][i][j] = -1.0;
                            costPerKg[v][i][j] = -1.0;
                            costFixed[v][i][j] = -1.0;
                            continue;
                        }
                        int x = (distance_length*(i+1))/distanceDimensions[v]-1;
                        int y = (weight_length*(j+1))/weightDimensions[v]-1;
                        if ((v + 1) <= vehicleAmount / 3) {
                            costPerKm[v][i][j] = costPerKmStd[0][x][y];
                            costPerKg[v][i][j] = costPerKgStd[0][x][y];
                            costFixed[v][i][j] = costFixedStd[0][x][y];
                        } else if ((v + 1) <= vehicleAmount / 3 * 2) {
                            costPerKm[v][i][j] = costPerKmStd[1][x][y];
                            costPerKg[v][i][j] = costPerKgStd[1][x][y];
                            costFixed[v][i][j] = costFixedStd[1][x][y];
                        } else {
                            costPerKm[v][i][j] = costPerKmStd[2][x][y];
                            costPerKg[v][i][j] = costPerKgStd[2][x][y];
                            costFixed[v][i][j] = costFixedStd[2][x][y];
                        }
                    }
                }
            }

            writeNewParameterSection(itemNumber++,"C_km", "cost per kilometer", fileWriter);
            writeTable(costPerKm,fileWriter);

            writeNewParameterSection(itemNumber++,"C_kg", "cost per kilo", fileWriter);
            writeTable(costPerKg,fileWriter);

            writeNewParameterSection(itemNumber++,"C_fix", "fixed costs", fileWriter);
            writeTable(costFixed,fileWriter);

            for (int i = 0; i < totalLocationsAmount; i++) {
                stopCosts[0][i] = 20 + random.nextInt(11);
            }

            for (int i = 1;i<vehicleAmount;i++){
                for (int j = 0; j < totalLocationsAmount; j++) {
                    stopCosts[i][j] = stopMultiplier[i]*stopCosts[0][j];
                }
            }

            double[][] travelDistance = new double[totalLocationsAmount][totalLocationsAmount];
            double[][][] travelTimeTypes = new double[3][totalLocationsAmount][totalLocationsAmount];
            double[][][] travelTimes = new double[vehicleAmount][totalLocationsAmount][totalLocationsAmount];
            List<Point> locations = new ArrayList<>(totalLocationsAmount);
            HashSet<Point> locationsHash = new HashSet<>(totalLocationsAmount);
            List<Elipse> possibilities = null;

            //deciding possible elipses based on instance type
            if(instanceType==1){
                possibilities=possibilitiesEurope;
            } else if (instanceType==2){
                possibilities=possibilitiesGermany;
            }

            //generating locations from elipses or uniform distribution
            for (int i = 0; i < totalLocationsAmount; i++) {
                if (factory[(i+1)]!=(i+1)){
                    Point location = getRandomFactoryPoint(locations.get(factory[(i+1)]-1));
                    //avoiding same locations twice...
                    while (locationsHash.contains(location)){
                        location = getRandomFactoryPoint(locations.get(factory[(i+1)]-1));
                    }
                    locations.add(location);
                    locationsHash.add(location);
                } else if(instanceType==3){
                    Point location = new Point(random.nextInt(UNIFORM_UPPER_BOUND_X + 1), random.nextInt(UNIFORM_UPPER_BOUND_Y + 1));
                    //avoiding same locations twice...
                    while (locationsHash.contains(location)){
                        location = new Point(random.nextInt(UNIFORM_UPPER_BOUND_X + 1), random.nextInt(UNIFORM_UPPER_BOUND_Y + 1));
                    }
                    locations.add(location);
                    locationsHash.add(location);
                } else {
                    int choice = random.nextInt(possibilities.size());
                    Point randomPointFromElipse = getRandomPointFromElipse(possibilities.get(choice));
                    //avoiding same location twice
                    while(locationsHash.contains(randomPointFromElipse)){   //avoid two of the same location
                        randomPointFromElipse = getRandomPointFromElipse(possibilities.get(choice));
                    }
                    locations.add(randomPointFromElipse);
                    locationsHash.add(randomPointFromElipse);
                }
            }

            //calculating travel times and travel times for each type of vehicles
            for (int i = 0; i <totalLocationsAmount ; i++) {
                for (int j = 0; j <totalLocationsAmount; j++) {
                    if(i==j) {
                        travelDistance[i][j] = -1.0;
                        travelTimeTypes[0][i][j] = -1.0;
                        travelTimeTypes[1][i][j] = -1.0;
                        travelTimeTypes[2][i][j] = -1.0;
                    } else if(i>j){
                        travelDistance[i][j] = travelDistance[j][i];
                        travelTimeTypes[0][i][j] = travelTimeTypes[0][j][i];
                        travelTimeTypes[1][i][j] = travelTimeTypes[1][j][i];
                        travelTimeTypes[2][i][j] = travelTimeTypes[2][j][i];
                    } else {
                        travelDistance[i][j] = locations.get(i).distanceTo(locations.get(j));
                        travelTimeTypes[0][i][j] = ((travelDistance[i][j] * TRAVEL_TIME_PERCENT_OF_DISTANCE) + (Math.floor(random.nextDouble() * travelDistance[i][j] * 2 * TRAVEL_TIME_PERCENT_VARIATION) - (travelDistance[i][j]*TRAVEL_TIME_PERCENT_VARIATION)));
                        travelTimeTypes[1][i][j] = travelTimeTypes[0][i][j]*1.025;
                        travelTimeTypes[2][i][j] = travelTimeTypes[0][i][j]*1.05;
                    }
                }
            }

            //assigning travel times to vehicles
            for (int i = 0; i < vehicleAmount; i++) {
                for (int j = 0; j < totalLocationsAmount; j++) {
                    for (int k = 0; k < totalLocationsAmount; k++) {
                        if(!vehicleCanVisitLocation[i][j+1]||!vehicleCanVisitLocation[i][k+1]) {
                            travelTimes[i][j][k] = -1.0;
                        }else if((i+1)<=vehicleAmount/3){
                            travelTimes[i][j][k] = travelTimeTypes[0][j][k];
                        }else if ((i+1)<=vehicleAmount*2/3) {
                            travelTimes[i][j][k] = travelTimeTypes[1][j][k];
                        } else {
                            travelTimes[i][j][k] = travelTimeTypes[2][j][k];
                        }
                    }
                }
            }

            double[] costOfNoTransport = new double[orderAmount];

            for (int i = 0; i < orderAmount; i++) {
                double dist = travelDistance[pickupLocation[i]-1][deliveryLocation[i]-1];
                double weight = orderWeights[i];
                double size = orderVolumes[i];
                costOfNoTransport[i] = MINIMUM_COST_OF_NO_TRANSPORT+dist* distMult +weight* weightMult +size* sizeMult;
            }

            writeNewParameterSection(itemNumber++,"C_stop_2", "stopping costs", fileWriter);
            writeTable(stopCosts,fileWriter);

            writeNewParameterSection(itemNumber++,"C_2", "cost of no transport",fileWriter);
            writeTable(costOfNoTransport,fileWriter);

            //TIME WINDOWS

            int timewindowAmountUpperBound = 4;
            if(instanceType==1){
                if(orderAmount>MAX_INSTANCE_ORDERS/2) {
                    timewindowAmountUpperBound+=4;
                }else if(orderAmount>MAX_INSTANCE_ORDERS/8*3){
                    timewindowAmountUpperBound+=3;
                }else if(orderAmount>MAX_INSTANCE_ORDERS/4){
                    timewindowAmountUpperBound+=2;
                } else if(orderAmount>MAX_INSTANCE_ORDERS/8){
                    timewindowAmountUpperBound++;
                }
            } else if(instanceType==3){
                if(orderAmount>MAX_INSTANCE_ORDERS/2) {
                    timewindowAmountUpperBound+=2;
                }else if(orderAmount>MAX_INSTANCE_ORDERS/4){
                    timewindowAmountUpperBound++;
                }
            }

            int[] timewindowAmounts = new int[totalLocationsAmount];
            int maxTimeWindowAmount=0;
            int[] timewindowType = new int[totalLocationsAmount]; //type 1 means one time window per day, type 0 means two timewindows per day

            for (int i = 0; i < totalLocationsAmount; i++) {
                timewindowAmounts[i] = TIMEWINDOW_AMOUNT_LOWER_BOUND + random.nextInt(timewindowAmountUpperBound- TIMEWINDOW_AMOUNT_LOWER_BOUND +1);
                if(timewindowAmounts[i]>maxTimeWindowAmount){
                    maxTimeWindowAmount=timewindowAmounts[i];
                }
                timewindowType[i] = random.nextInt(2);
            }

            writeNewParameterSection(itemNumber++,"pi_2", "timewindow amounts at location", fileWriter);
            writeTable(timewindowAmounts,fileWriter);

            double[][] timewindowLowerBounds = new double[totalLocationsAmount][maxTimeWindowAmount];
            double[][] timewindowUpperBounds = new double[totalLocationsAmount][maxTimeWindowAmount];

            for (int i = 0; i < totalLocationsAmount; i++) {
                timewindowLowerBounds[i][0]=random.nextInt(TIME_WINDOW_LOWER_BOUND_HOURS+1)*60;
                timewindowUpperBounds[i][0]= timewindowLowerBounds[i][0]+(MINIMUM_OPENING_TIME+random.nextInt(TIME_WINDOW_LOWER_BOUND_HOURS+1))*60;
                timewindowLowerBounds[i][1]= (24*60 + timewindowLowerBounds[i][0])*timewindowType[i] + (1-timewindowType[i])*(timewindowUpperBounds[i][0]+(MINIMUM_CLOSED_TIME + random.nextInt(2))*60);
                timewindowUpperBounds[i][1]= timewindowLowerBounds[i][1]+(MINIMUM_OPENING_TIME+random.nextInt(TIME_WINDOW_LOWER_BOUND_HOURS+1))*60;
            }

            for (int i = 0; i < totalLocationsAmount; i++) {
                for (int j = 2; j < maxTimeWindowAmount; j++) {
                    if((j+1)>timewindowAmounts[i]){
                        timewindowLowerBounds[i][j] = -1.0;
                        timewindowUpperBounds[i][j] = -1.0;
                    }else if(timewindowType[i]==1){
                        timewindowLowerBounds[i][j]= 24*60 + timewindowLowerBounds[i][j-1];
                        timewindowUpperBounds[i][j]= 24*60 + timewindowUpperBounds[i][j-1];
                    } else if(j%2==1){
                        timewindowLowerBounds[i][j]= timewindowUpperBounds[i][j-1]+(MINIMUM_CLOSED_TIME + random.nextInt(2))*60;
                        timewindowUpperBounds[i][j]= timewindowLowerBounds[i][j]+(timewindowUpperBounds[i][j-2]-timewindowLowerBounds[i][j-2]);
                    } else {
                        timewindowLowerBounds[i][j]= timewindowLowerBounds[i][j-2] + 24*60;
                        timewindowUpperBounds[i][j]= timewindowUpperBounds[i][j-2] + 24*60;
                    }
                }
            }

            writeNewParameterSection(itemNumber++,"T_l_2", "timewindow lower bounds", fileWriter);
            writeTable(timewindowLowerBounds,fileWriter);

            writeNewParameterSection(itemNumber++,"T_u_2", "timewindow upper bounds", fileWriter);
            writeTable(timewindowUpperBounds,fileWriter);





            writeNewParameterSection(itemNumber++,"T_2", "Travel time", fileWriter);
            writeTable(travelTimes,fileWriter);



            writeNewParameterSection(itemNumber++,"D_2", "Travel distance", fileWriter);
            writeTable(travelDistance,fileWriter);

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Point getRandomPointFromElipse(Elipse elipse){
        return elipse.getPointInside(random.nextDouble(),random.nextDouble()*2*PI);
    }
    private Point getRandomFactoryPoint(Point point){
        return point.getFactoryPoint(random.nextDouble(),random.nextDouble()*2*PI);
    }

    private double[] getIntervalTable(double[] selection, int amount,int maxLength) {
        double[] result = new double[maxLength];
        int selectionLength = selection.length;
        int i = 0;
        while(i<amount-1){
            result[i]=selection[((1+i)*selectionLength/amount)-1];
            i++;
        }
        result[i++] = selection[selectionLength-1];
        while(i<maxLength){
            result[i++]=-1.0;
        }
        return result;
    }

    private int getRandomNumber(int instanceSize, int maxValue) {
        return 2 + random.nextInt((maxValue - 2) * instanceSize / LARGEST_INSTANCE_ORDER_AMOUNT + 1);
    }

    private void writeTable(double[] table, FileWriter fileWriter) throws IOException {
        for (int i = 0; i < table.length-1;i++){
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        fileWriter.write((table.length)+" "+table[table.length-1]+" ;\n\n");
    }

    private void writeTable(int[] table, FileWriter fileWriter) throws IOException {
        for (int i = 0; i < table.length-1;i++){
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        fileWriter.write((table.length)+" "+table[table.length-1]+" ;\n\n");
    }

    private void writeTable(double[][] table, FileWriter fileWriter) throws IOException {
        for(int j = 0;j<table.length;j++) {
            for (int i = 0; i < table[j].length; i++) {
                if(i+1==table[table.length-1].length&&j+1==table.length){
                    break;
                }
                if(table[j][i]==-1.0) {
                    fileWriter.write((j + 1) + " " + (i + 1) + " . \n");
                } else {
                    fileWriter.write((j + 1) + " " + (i + 1) + " " + table[j][i] + "\n");
                }
            }
        }
        if(table[table.length-1][table[0].length-1]==-1.0) {
            fileWriter.write((table.length) + " " + (table[0].length) + " . ;\n\n");
        } else {
            fileWriter.write((table.length) + " " + (table[0].length) + " " + table[table.length - 1][table[0].length - 1] + " ;\n\n");
        }
    }

    private void writeTable(double[][][] table, FileWriter fileWriter) throws IOException {
        for(int k = 0;k<table.length;k++)
        for(int j = 0;j<table[k].length;j++) {
            for (int i = 0; i < table[k][j].length; i++) {
                if(i+1==table[table.length-1][table[k].length-1].length&&j+1==table[table.length-1].length&&k+1==table.length){
                    break;
                }
                if(table[k][j][i]==(-1.0)) {
                    fileWriter.write((k+1)+" "+(j + 1) + " " + (i + 1) + " . \n");
                } else {
                    fileWriter.write((k+1)+" "+(j + 1) + " " + (i + 1) + " " + table[k][j][i] + "\n");
                }
            }
        }
        if(table[table.length-1][table[0].length-1][table[0][0].length-1]==(-1.0)) {
            fileWriter.write((table.length) + " " + (table[0].length) + " "+ table[0][0].length+" . ;\n\n");
        } else {
            fileWriter.write((table.length) + " " + (table[0].length) + " " +table[0][0].length+" "+ table[table.length - 1][table[0].length - 1][table[0][0].length-1] + " ;\n\n");
        }
    }

    private void writeBooleanSet(String setName, int startIndex, boolean[][] table, FileWriter fileWriter) throws IOException {
        for(int i = 0;i<table.length;i++){
            writeSetNumber(setName,i,fileWriter);
            for (int j = startIndex;j<table[0].length;j++){
                if(table[i][j]) {
                    fileWriter.write( (j+1-startIndex)+ " ");
                }
            }
            fileWriter.write(";\n");
        }
        fileWriter.write("\n");
    }

    private boolean[] makeProbabilityTable(int tableSize, double probability) {
        boolean[] pickupLocationRequiresSpecialProperty = new boolean[tableSize];

        for (int i = 0; i<tableSize;i++){
            if(random.nextDouble()>probability){
                pickupLocationRequiresSpecialProperty[i]=true;
            } else {
                pickupLocationRequiresSpecialProperty[i] = false;
            }
        }
        return pickupLocationRequiresSpecialProperty;
    }

    private void writeSetNumber(String outputName, int i, FileWriter fileWriter) throws IOException {
        fileWriter.write("set " + outputName + "["+(i+1)+"] := ");
    }

    private void writeHeaderSection(int itemNumber, String outputDescription, FileWriter fileWriter) throws IOException {
        fileWriter.write("# " + (itemNumber) + " " + outputDescription + "\n");
    }

    private void writeNewParameterSection(int itemNumber, String parameterName, String parameterDescription, FileWriter fileWriter) throws IOException {
        writeHeaderSection(itemNumber++, parameterDescription, fileWriter);

        fileWriter.write("param "+parameterName+" :=\n");
    }

    private int[] oneDimensionTableWithRandomElements(int tabelSize, int notRandomLimit, int startElement, FileWriter fileWriter) throws IOException {
        int[] result = new int[tabelSize];

        int[] elements = createPossibleElementsTable(notRandomLimit, startElement);
        int counter=notRandomLimit;
        int order = 0;
        while(counter>0&&order<tabelSize-1){
            int choiceIdx = random.nextInt(counter--);
            result[order++] = elements[choiceIdx];
            if(choiceIdx!=counter){
                elements[choiceIdx] = elements[counter];
            }
            fileWriter.write((order)+" "+result[order-1]+"\n");
        }
        while (order<tabelSize-1){
            result[order++]=startElement+random.nextInt(notRandomLimit);
            fileWriter.write((order)+" "+result[order-1]+"\n");
        }
        if (counter>0){
            result[order++] = elements[0];
            fileWriter.write((order)+" "+result[order-1]+" ;\n\n");
        } else{
            result[order++]=startElement+random.nextInt(notRandomLimit);
            fileWriter.write((order)+" "+result[order-1]+" ;\n\n");
        }
        return result;
    }

    private int[] createPossibleElementsTable(int tableSize, int startElement) {
        int[] elements = new int[tableSize];
        for(int i  = 0;i<tableSize;i++){
            elements[i] = startElement+i;
        }
        return elements;
    }

    private int[] oneDimensionTable(int tableDimension, int number, FileWriter fileWriter) throws IOException {
        int[] table = new int[tableDimension];
        for (int i = 0;i<tableDimension-1;i++){
            table[i] = number;
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = number;
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private int[] oneDimensionTable(int tableDimension, int lowerBound, int upperBound, FileWriter fileWriter) throws IOException {
        int[] table = new int[tableDimension];
        for (int i = 0;i<tableDimension-1;i++){
            table[i] = lowerBound+random.nextInt(upperBound-lowerBound+1);
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = lowerBound+random.nextInt(upperBound-lowerBound+1);
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private double[] oneDimensionTable(int tableDimension, double lowerBound, double mediumBound, double upperBound, FileWriter fileWriter) throws IOException {
        double[] table = new double[tableDimension];
        for (int i = 0;i<tableDimension-1;i++){
            table[i] = lowerBound+random.nextDouble()*(mediumBound-lowerBound);
            if(random.nextDouble()> UPPER_BOUND_PROBABILITY){
                table[i]+=random.nextDouble()*(upperBound-mediumBound);
            }
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = lowerBound+random.nextDouble()*(upperBound-lowerBound);
        if(random.nextDouble()> UPPER_BOUND_PROBABILITY){
            table[tableDimension-1]+=random.nextDouble()*(upperBound-mediumBound);
        }
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    //TODO: fix this!!
    private double[] oneDimensionVolumeTable(int tableDimension, double median, double lowerBound, double upperBound, FileWriter fileWriter, double[] regulator) throws IOException {
        double[] table = new double[tableDimension];
        for (int i = 0;i<tableDimension-1;i++){

            table[i] = median + random.nextDouble()*getNormalSide(median, lowerBound, upperBound);
            table[i]=regulator[i]/table[i];
            while(table[i]>ORDER_VOLUME_UPPER_BOUND){
                table[i] = median + random.nextDouble()*getNormalSide(median, lowerBound, upperBound);
                table[i]=regulator[i]/table[i];
            }
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[table.length-1] = median + random.nextDouble()*getNormalSide(median, lowerBound, upperBound);
        table[table.length-1]=regulator[table.length-1]/table[table.length-1];
        while(table[table.length-1]>ORDER_VOLUME_UPPER_BOUND){
            table[table.length-1] = median + random.nextDouble()*getNormalSide(median, lowerBound, upperBound);
            table[table.length-1]=regulator[table.length-1]/table[table.length-1];
        }
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private double getNormalSide(double median, double lowerBound, double upperBound) {
        return random.nextBoolean() ? lowerBound-median:upperBound-median;
    }

    private void printNumber(int itemNumber, int outputNumber, String parameterName, String parameterDescription, FileWriter fileWriter) throws IOException {
        writeNewParameterSection(itemNumber++,parameterName,parameterDescription,fileWriter);
        fileWriter.write(outputNumber+" ;\n\n");

    }
}
