package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InstanceGenerator implements IInstanceGenerator {
    private static final double ORDER_WEIGHT_LOWER_BOUND = 50;
    private static final double ORDER_WEIGHT_MEDIUM_BOUND = 2000;
    private static final double ORDER_WEIGHT_UPPER_BOUND = 24000;
    private static final double ORDER_VOLUME_PORTION_LOWER_BOUND = 4;
    private static final double ORDER_VOLUME_PORTION_MEDIUM_BOUND = 265;
    private static final double ORDER_VOLUME_PORTION_UPPER_BOUND = 1000;
    private static final double ORDER_VOLUME_MEDIUM_BOUND = 50;
    private static final double ORDER_VOLUME_UPPER_BOUND = 100;
    private final Random random;
    private final int COST_STRUCTURE_DIMENSION_LOWER_BOUND = 2;
    private final int COST_STRUCTURE_DIMENSION_UPPER_BOUND = 7;
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

            writeNewParameterSection(itemNumber++,"na","amount of distance intervals",fileWriter);

            int[] distanceIntervals = oneDimensionTable(vehicleAmount, COST_STRUCTURE_DIMENSION_LOWER_BOUND,COST_STRUCTURE_DIMENSION_UPPER_BOUND, fileWriter);

            writeNewParameterSection(itemNumber++,"nb","amount of weight intervals",fileWriter);

            int[] weightIntervals = oneDimensionTable(vehicleAmount,COST_STRUCTURE_DIMENSION_LOWER_BOUND,COST_STRUCTURE_DIMENSION_UPPER_BOUND,fileWriter);

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


            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
