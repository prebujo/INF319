package generators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;

public class InstanceGenerator implements IInstanceGenerator {
    @Override
    public void makeInstance(int orderAmount, int vehicleAmount, int locationAmount, int instanceType) {

        try {
            FileWriter fileWriter = new FileWriter("res/"+orderAmount+"_Ord_"+vehicleAmount+"_Veh_"+locationAmount+"_Loc.dat");

            int itemNumber = 1;

            String parameterName = "nv";
            String parameterDescription = "amount of Vehicles";
            printNumber(fileWriter, parameterName, itemNumber++, vehicleAmount, parameterDescription);

            parameterName = "no";
            parameterDescription = "amount of Orders";
            printNumber(fileWriter,parameterName,itemNumber++,orderAmount,parameterDescription);

            parameterName = "nl";
            parameterDescription = "amount of locations";
            printNumber(fileWriter,parameterName,itemNumber++,locationAmount,parameterDescription);




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printNumber(FileWriter fileWriter, String paramName, int itemNumber, int outputNumber, String paramDescription) throws IOException {
        fileWriter.write("# "+itemNumber+" "+ paramDescription+"\n");
        fileWriter.write("param "+paramName+" :=\n");
        fileWriter.write(outputNumber+" ;\n\n");

    }
}
