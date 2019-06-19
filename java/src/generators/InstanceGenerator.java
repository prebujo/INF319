package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InstanceGenerator implements IInstanceGenerator {
    private final Random random;
    private final int COST_STRUCTURE_DIMENSION_LOWER_BOUND = 2;
    private final int COST_STRUCTURE_DIMENSION_UPPER_BOUND = 7;

    public InstanceGenerator(){
        this.random = new Random(13);
    }
    @Override
    public void makeInstance(int orderAmount, int vehicleAmount, int pickupLocationAmount, int deliveryLocationAmount, int factoryAmount, int instanceID, int instanceType) {

        try {
            FileWriter fileWriter = new FileWriter("res/Inst"+instanceID+"_Ord_"+orderAmount+"_Veh_"+vehicleAmount+"_Loc_"+(pickupLocationAmount+deliveryLocationAmount)+".dat");



            int itemNumber = 1;

            String parameterName = "nv";
            String parameterDescription = "amount of Vehicles";
            printNumber(itemNumber++, vehicleAmount, parameterName, parameterDescription, fileWriter);

            parameterName = "no";
            parameterDescription = "amount of Orders";
            printNumber(itemNumber++,orderAmount,parameterName,parameterDescription,fileWriter);

            int totalLocationsAmount = pickupLocationAmount+deliveryLocationAmount;
            parameterName = "nl";
            parameterDescription = "amount of locations";
            printNumber(itemNumber++,totalLocationsAmount,parameterName,parameterDescription,fileWriter);

            parameterName = "nf";
            parameterDescription = "amount of factories";
            printNumber(itemNumber++,factoryAmount,parameterName,parameterDescription,fileWriter);

            parameterName = "na";
            parameterDescription = "amount of distance intervals";

            int[] distanceIntervals = oneDimensionTable(vehicleAmount, itemNumber++,COST_STRUCTURE_DIMENSION_LOWER_BOUND,COST_STRUCTURE_DIMENSION_UPPER_BOUND, parameterName, parameterDescription, fileWriter);

            parameterName = "nb";
            parameterDescription = "amount of weight intervals";

            int[] weightIntervals = oneDimensionTable(vehicleAmount,itemNumber++,COST_STRUCTURE_DIMENSION_LOWER_BOUND,COST_STRUCTURE_DIMENSION_UPPER_BOUND,parameterName,parameterDescription,fileWriter);

            int[] factoryLocationAmounts = new int[factoryAmount];

            int counter = deliveryLocationAmount;
            for (int i = 0;i<factoryAmount;i++){
                factoryLocationAmounts[i]=1;
                counter--;
            }

            while (counter>0){
                factoryLocationAmounts[random.nextInt(factoryAmount)]++;
                counter--;
            }

            parameterName = "N_f_2";
            parameterDescription = "factory sets";

            fileWriter.write("# "+itemNumber+" "+ parameterDescription+"\n");
            int location = pickupLocationAmount+1;
            for (int i = 0;i<factoryAmount;i++) {
                fileWriter.write("set " + parameterName + "["+(i+1)+"] := ");
                counter=factoryLocationAmounts[i];
                while(counter>0){
                    fileWriter.write(" "+location++);
                    counter--;
                }
                fileWriter.write(" ;\n");
            }
            fileWriter.write("\n");






            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int[] oneDimensionTable(int tableDimension, int itemNumber, int lowerBound, int upperBound, String parameterName, String parameterDescription, FileWriter fileWriter) throws IOException {
        int[] table = new int[tableDimension];
        fileWriter.write("# "+itemNumber+" "+ parameterDescription+"\n");
        fileWriter.write("param "+parameterName+" :=\n");
        for (int i = 0;i<tableDimension-1;i++){
            table[i] = lowerBound+random.nextInt(upperBound-lowerBound);
            fileWriter.write((i+1)+" "+table[i]+"\n");
        }
        table[tableDimension-1] = lowerBound+random.nextInt(upperBound-lowerBound+1);
        fileWriter.write(tableDimension+" "+table[tableDimension-1]+" ;\n\n");
        return table;
    }

    private void printNumber(int itemNumber, int outputNumber, String parameterName, String parameterDescription, FileWriter fileWriter) throws IOException {
        fileWriter.write("# "+itemNumber+" "+ parameterDescription+"\n");
        fileWriter.write("param "+parameterName+" :=\n");
        fileWriter.write(outputNumber+" ;\n\n");

    }
}
