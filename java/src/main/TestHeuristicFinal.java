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

import java.lang.reflect.Array;
import java.util.*;

public class TestHeuristicFinal {
    public static void main(String[] args) throws Throwable {

        //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

        //reads data from .dat file
        IReader reader = new Reader();

        List<IDataResult> results = new ArrayList<>();
        List<IOperator> operators = null;
        IFeasibility feasibility = null;
        String instance = "Inst1_";
        List<String> instanceSizes = Arrays.asList("Ord_4_Veh_3_Loc_7", "Ord_9_Veh_5_Loc_6","Ord_10_Veh_6_Loc_7","Ord_12_Veh_7_Loc_9","Ord_15_Veh_8_Loc_11");
        List<IDataSet> dataSets = reader.readDataFromFiles(instance,instanceSizes);
        int i = 0;
        for (IDataSet dataSet:dataSets) {

            Random random = new Random(101+i);
            feasibility = new Feasibility(dataSet);

            operators = getOperators(feasibility, dataSet, random);

            AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
            IDataResult result = alns.optimize(operators);
            results.add(result);
            i++;
        }

        IPrinter printer = new Printer();

        printer.printDataToFile(instance,instanceSizes,results,operators);
    }

    private static List<IOperator> getOperators(IFeasibility feasibility, IDataSet dataSet, Random random) {
        List<IOperator> operators;
        operators=new ArrayList<>();
        operators.add(new SwapTwo(dataSet, random, feasibility, "swap2"));
        operators.add(new ExchangeThree(dataSet, random, feasibility, "exch3"));
        operators.add(new RemoveAndReinsertRandom(dataSet, random, feasibility, 1, Math.min(dataSet.getOrderAmount()/4,5), "r&R1_4"));
        operators.add(new RemoveAndReinsert(dataSet, random, feasibility, 1, Math.min(dataSet.getOrderAmount()/4,5), "r&r1_4"));
        operators.add(new ReturnSameSolution("retSame"));
        return operators;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

}
