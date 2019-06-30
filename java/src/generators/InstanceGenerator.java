package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InstanceGenerator implements IInstanceGenerator {
    private static final double ORDER_WEIGHT_LOWER_BOUND = 50;
    private static final double ORDER_WEIGHT_MEDIUM_BOUND = 2000;
    private static final double ORDER_WEIGHT_UPPER_BOUND = 24000;
    private static final double VEHICLE_WEIGHT_UPPER_BOUND_SMALL = 12000;
    private static final double VEHICLE_WEIGHT_UPPER_BOUND_MEDIUM = 18000;
    private static final double ORDER_VOLUME_PORTION_LOWER_BOUND = 4;
    private static final double ORDER_VOLUME_PORTION_MEDIUM_BOUND = 265;
    private static final double ORDER_VOLUME_PORTION_UPPER_BOUND = 1000;
    private static final double ORDER_VOLUME_MEDIUM_BOUND = 50;
    private static final double ORDER_VOLUME_UPPER_BOUND = 102;
    private static final double VEHICLE_VOLUME_UPPER_BOUND_SMALL = 55;
    private static final double VEHICLE_VOLUME_UPPER_BOUND_MEDIUM = 71;
    private static final double FIXED_INCREASE_RATE_LARGE = 1.64;
    private static final double FIXED_INCREASE_RATE_MEDIUM = 1.56;
    private static final double FIXED_INCREASE_RATE_SMALL = 1.50;
    private static final int LARGEST_INSTANCE_ORDER_AMOUNT = 100;
    private static final double LARGE_NUMBER = 9999999;
    private final Random random;
    private final int COST_STRUCTURE_DISTANCE_DIMENSION = 10;
    private final int COST_STRUCTURE_WEIGHT_DIMENSION = 28;
    private final double MINIMUM_COST_OF_NO_TRANSPORT = 20000;
    private final double MAXIMUM_COST_OF_NO_TRANSPORT = 40000;

    public InstanceGenerator(){
        this.random = new Random(13);
    }
    @Override
    public void makeInstance(int orderAmount, int vehicleAmount, int pickupLocationAmount, int deliveryLocationAmount, int factoryAmount, int instanceID, int instanceType) {

        try {
            FileWriter fileWriter = new FileWriter("res/Inst"+instanceID+"_Ord_"+orderAmount+"_Veh_"+vehicleAmount+"_Loc_"+(pickupLocationAmount+deliveryLocationAmount)+".dat");



            int itemNumber = 1;

            printNumber(itemNumber++, vehicleAmount, "nv", "amount of Vehicles", fileWriter);

            printNumber(itemNumber++,orderAmount,"no","amount of Orders",fileWriter);

            int totalLocationsAmount = pickupLocationAmount+deliveryLocationAmount;

            printNumber(itemNumber++,totalLocationsAmount,"nl","amount of locations",fileWriter);

            printNumber(itemNumber++,factoryAmount,"nf","amount of factories",fileWriter);

            int[] factoryLocationAmounts = new int[factoryAmount];

            int counter = deliveryLocationAmount;
            for (int i = 0;i<factoryAmount;i++){
                factoryLocationAmounts[i]=1;
                counter--;
            }


            while (counter>0){
                int selected = random.nextInt(factoryAmount);
                factoryLocationAmounts[selected]++;
                counter--;
            }


            writeHeaderSection(itemNumber++, "factory sets", fileWriter);

            String setName = "N_f_2";
            int[] locationElements = createPossibleElementsTable(deliveryLocationAmount,pickupLocationAmount+1);
            int locationElementsCounter = deliveryLocationAmount;
            for (int i = 0;i<factoryAmount;i++) {
                writeSetNumber(setName, i, fileWriter);
                counter=factoryLocationAmounts[i];
                while(counter>0){
                    int choiceIdx = random.nextInt(locationElementsCounter--);
                    fileWriter.write(" "+locationElements[choiceIdx]);
                    if(choiceIdx!=locationElementsCounter){
                        locationElements[choiceIdx] = locationElements[locationElementsCounter];
                    }
                    counter--;
                }
                fileWriter.write(" ;\n");
            }
            fileWriter.write("\n");

            writeNewParameterSection(itemNumber++,"H","factory stop capacities",fileWriter);
            for (int i = 0;i<factoryAmount-1;i++){
                fileWriter.write((i+1)+" "+(1+random.nextInt(factoryLocationAmounts[i]))+"\n");
            }
            fileWriter.write(factoryAmount+" "+(1+random.nextInt(factoryLocationAmounts[factoryAmount-1]))+" ;\n\n");

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
            double[] orderVolumes = oneDimensionTable(orderAmount,ORDER_VOLUME_PORTION_LOWER_BOUND,ORDER_VOLUME_PORTION_MEDIUM_BOUND,ORDER_VOLUME_PORTION_UPPER_BOUND, vehicleVolumeMax, fileWriter, orderWeights);

            writeNewParameterSection(itemNumber++,"C_2", "cost of no transport",fileWriter);
            double[] costOfNoTransport = oneDimensionTable(orderAmount,MINIMUM_COST_OF_NO_TRANSPORT,MAXIMUM_COST_OF_NO_TRANSPORT,MAXIMUM_COST_OF_NO_TRANSPORT,fileWriter);

            double probability = 0.8;

            boolean[] pickuplocationRequiresSpecialProperty = makeProbabilityTable(pickupLocationAmount+1, probability);
            boolean[] orderRequiresSpecialProperty = makeProbabilityTable(orderAmount,probability);

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

            for (int i = 1; i<=vehicleAmount;i++){
                if (i<=vehicleAmount/3){
                    distanceDimensions[i-1]=type1DistanceDimensions;
                    weightDimensions[i-1] = type1WeightDimensions;
                    distanceIntervals[i-1]=type1DistanceInterval;
                    weightIntervals[i-1]=type1WeightInterval;
                    vehicleVolumeCapacity[i-1]= VEHICLE_VOLUME_UPPER_BOUND_SMALL;
                    vehicleWeightCapacity[i-1]= VEHICLE_WEIGHT_UPPER_BOUND_SMALL;
                } else if (i<=vehicleAmount/3*2){
                    distanceDimensions[i-1] = type2DistanceDimensions;
                    weightDimensions[i-1]=type2WeightDimensions;
                    distanceIntervals[i-1]=type2DistanceInterval;
                    weightIntervals[i-1]=type2WeightInterval;
                    vehicleVolumeCapacity[i-1]= VEHICLE_VOLUME_UPPER_BOUND_MEDIUM;
                    vehicleWeightCapacity[i-1]= VEHICLE_WEIGHT_UPPER_BOUND_MEDIUM;
                } else {
                    distanceDimensions[i-1] = type3DistanceDimensions;
                    weightDimensions[i-1]=type3WeightDimensions;
                    distanceIntervals[i-1]=type3DistanceInterval;
                    weightIntervals[i-1]=type3WeightInterval;
                    vehicleVolumeCapacity[i-1]=ORDER_VOLUME_UPPER_BOUND;
                    vehicleWeightCapacity[i-1]=ORDER_WEIGHT_UPPER_BOUND;
                }
                for(int j = 0;j<orderAmount;j++) {
                    //first type of vehicle, can pickup all types of orders at all locations..
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
                    } else //third type of vehicle, smaller capacity, no special ability at location or for order
                        {
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
            writeBooleanSet("N_vp",0,vehicleCanPickupOrder,fileWriter);

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
                        if ((i + 1) > distanceDimensions[v]) {
                            costPerKm[v][i][j] = -1.0;
                            costPerKg[v][i][j] = -1.0;
                            costFixed[v][i][j] = -1.0;
                            continue;
                        }
                        int x = distance_length/distanceDimensions[v]*(i+1)-1;
                        int y = weight_length/weightDimensions[v]*(i+1)-1;
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





            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
        while(i<maxLength-1){
            result[i]=0.0;
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
                if(table[j][i]==0.0) {
                    fileWriter.write((j + 1) + " " + (i + 1) + " . \n");
                } else {
                    fileWriter.write((j + 1) + " " + (i + 1) + " " + table[j][i] + "\n");
                }
            }
        }
        if(table[table.length-1][table[0].length-1]==0.0) {
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
            if(random.nextDouble()>0.9){
                table[i]+=random.nextDouble()*(upperBound-mediumBound);
            }
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = lowerBound+random.nextDouble()*(upperBound-lowerBound);
        if(random.nextDouble()>0.9){
            table[tableDimension-1]+=random.nextDouble()*(upperBound-mediumBound);
        }
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private double[] oneDimensionTable(int tableDimension, double lowerBound, double mediumBound, double upperBound, double vehicleVolumeMax, FileWriter fileWriter, double[] regulator) throws IOException {
        double[] table = new double[tableDimension];
        for (int i = 0;i<tableDimension-1;i++){
            table[i] = lowerBound+random.nextDouble()*(mediumBound-lowerBound);
            if(random.nextDouble()>0.9){
                table[i]+=random.nextDouble()*(upperBound-mediumBound);
            }
            table[i]=regulator[i]/table[i];
            if(table[i]>vehicleVolumeMax){
                table[i] = ORDER_VOLUME_MEDIUM_BOUND+random.nextDouble()*(vehicleVolumeMax-ORDER_VOLUME_MEDIUM_BOUND);
            }
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = lowerBound+random.nextDouble()*(upperBound-lowerBound);
        if(random.nextDouble()>0.9){
            table[tableDimension-1]+=random.nextDouble()*(upperBound-mediumBound);
        }
        table[tableDimension-1]=regulator[tableDimension-1]/table[tableDimension-1];
        if(table[tableDimension-1]>vehicleVolumeMax){
            table[tableDimension-1] = ORDER_VOLUME_MEDIUM_BOUND+random.nextDouble()*(vehicleVolumeMax-ORDER_VOLUME_MEDIUM_BOUND);
        }
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private void printNumber(int itemNumber, int outputNumber, String parameterName, String parameterDescription, FileWriter fileWriter) throws IOException {
        writeNewParameterSection(itemNumber++,parameterName,parameterDescription,fileWriter);
        fileWriter.write(outputNumber+" ;\n\n");

    }
}
