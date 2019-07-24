package main;

import Heuristic.AdaptiveLargeNeighbourhoodSearch;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import printer.IPrinter;
import printer.Printer;
import reader.IReader;
import reader.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestHeuristicFinal {
    public static void main(String[] args) throws Throwable {

        //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

        //reads data from .dat file
        IReader reader = new Reader();

        List<IDataResult> results = new ArrayList<>();
        IDataSet dataSet = null;
        List<IOperator> operators = null;
        IFeasibility feasibility = null;

        for (int i = 0; i < 5; i++) {
            operators=new ArrayList<>();
            String fileName = "Inst"+(i+1)+"_Ord_20_Veh_10_Loc_11";
            dataSet = reader.readDataFromFile(fileName);

            Random random = new Random(101+i);

            feasibility = new Feasibility(dataSet);

            AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, feasibility, random, "alns");

            String swapDescription = "Swaps the position of 2 orders from one vehicle to another";
            operators.add(new SwapTwo(dataSet, random, feasibility, "swap2", swapDescription));
            String exchangeDescription = "exchanges the order of 3 orders a delivery schedule of a";
            operators.add(new ExchangeThree(dataSet, random, feasibility, "exch3", exchangeDescription));
            String removeAndReinsertTwoToFourDescription = "remove and reinsert operator that removes between 2-4 random elements from solution and reinserts them in randomly selected vehicles";
            operators.add(new RemoveAndReinsertRandom(dataSet, random, feasibility, 1, 3, "r&R1_4", removeAndReinsertTwoToFourDescription));
            String removeAndReinsertOneDescription = "remove and reinsert operator that removes between 1-1 random elements from solution and reinserts them in randomly selected vehicles";
            operators.add(new RemoveAndReinsert(dataSet, random, feasibility, 1, 3, "r&r1_4", removeAndReinsertOneDescription));
            String returnSame = "return the same solution";
            operators.add(new ReturnSameSolution("retSame", returnSame));

            IDataResult result = alns.optimize(operators);
            results.add(result);
        }

        IPrinter printer = new Printer();

        printer.printDataToFile("Ord_4_Veh_3_Loc_7",dataSet,results,operators);
    }

    private static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

}
