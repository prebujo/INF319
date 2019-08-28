package main;

import heuristic.AdaptiveLargeNeighbourhoodSearch;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import printer.IPrinter;
import printer.Printer;
import reader.IReader;
import reader.Reader;

import java.util.*;

public class HeuristicFinalMain2 {
    public static void main(String[] args) throws Throwable {

        //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

        //reads data from .dat file
        IReader reader = new Reader();

        List<IDataResult> results = new ArrayList<>();
        List<IOperator> operators = null;
        IFeasibility feasibility;
        String instance = "Inst1_";
        List<String> instanceSizes = Arrays.asList("Ord_4_Veh_3_Loc_7", "Ord_7_Veh_4_Loc_10","Ord_9_Veh_5_Loc_6", "Ord_10_Veh_6_Loc_7", "Ord_12_Veh_7_Loc_9");
        List<IDataSet> dataSets = reader.readDataFromFiles(instance,instanceSizes);
        int i = 0;
        for (IDataSet dataSet:dataSets) {
            Random random = new Random(101+i);
            feasibility = new Feasibility(dataSet);

            operators = getOperators(feasibility, dataSet, random);

            AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
            IDataResult result = alns.optimize(operators, getWildOperators(feasibility,dataSet,random));
            results.add(result);
            i++;
        }

        IPrinter printer = new Printer();

        printer.printDataToFile(instance,instanceSizes,results,operators);
    }

    private static List<IOperator> getOperators(IFeasibility feasibility, IDataSet dataSet, Random random) {
        int orderAmount = dataSet.getOrderAmount();
        int a = (int) (orderAmount *0.1);
        int b = 5;
        if (orderAmount <b){
            b = 3;
        }
        List<IOperator> operators;
        operators=new ArrayList<>();
        operators.add(new SwapTwoFirstFit2("swapf", random, feasibility, dataSet));
        operators.add(new RemoveSimilarInsertRegret("rsirg", 4, 1, Math.max(a, b),3, random, feasibility, dataSet));
//        operators.add(new ExchangeThree("exch3",random,feasibility,dataSet));
//        operators.add(new SwapTwo("swap2", random, feasibility, dataSet));
        operators.add(new RemoveExpensiveInsertGreedy("reig", 4,1, Math.max(a,b), random, feasibility, dataSet));
        operators.add(new TwoOpt("2-opt",random,feasibility, dataSet));
        operators.add(new RemoveRandomInsertFirst("rrif", 1,Math.max(a, b),random,feasibility,dataSet));
        return operators;
    }

    private static List<IOperator> getWildOperators(IFeasibility feasibility, IDataSet dataSet, Random random) {
        int orderAmount = dataSet.getOrderAmount();
        int a = (int) (orderAmount *0.3);
        int b = 7;
        if (orderAmount <b){
            b = 4;
        }
        List<IOperator> operators;
        operators=new ArrayList<>();
        operators.add(new SwapTwoFirstFit2("swapf", random, feasibility, dataSet));
//        operators.add(new SwapTwo("swap2", random, feasibility, dataSet));
        operators.add(new TwoOpt("2-opt",random,feasibility, dataSet));
        operators.add(new RemoveRandomInsertFirst("rrif", 1,Math.max(a, b),random,feasibility,dataSet));
        return operators;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

}

