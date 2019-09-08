package main;

import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import generators.ISolutionGenerator;
import generators.SolutionGenerator;
import printer.IPrinter;
import printer.Printer;
import reader.IReader;
import reader.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomSolutionGeneratorMain {

    public static void main(String args[]) throws Exception {
        String file = "Inst1_";
        String instance = "Inst1_";
        List<String> instanceSizes = Arrays.asList("Ord_4_Veh_3_Loc_7", "Ord_12_Veh_7_Loc_9","Ord_35_Veh_20_Loc_22", "Ord_80_Veh_45_Loc_45", "Ord_150_Veh_80_Loc_85");
        IReader reader = new Reader();
        List<IDataSet> dataSets = reader.readDataFromFiles(instance,instanceSizes);
        Random random = new Random(101);
        List<List<int[]>> feasibleSolutionsList = new ArrayList<>();
        IFeasibility feasibility;
        for (IDataSet dataSet:dataSets) {
            feasibility = new Feasibility(dataSet);
            ISolutionGenerator solutionGenerator = new SolutionGenerator(random, feasibility);
            feasibleSolutionsList.add(solutionGenerator.getRandomSolutions(20, dataSet.getVehicleAmount(), dataSet.getOrderAmount(), feasibility));
        }

        IPrinter printer = new Printer();

        printer.printSolutionsToFile(instance,feasibleSolutionsList);

    }
}
