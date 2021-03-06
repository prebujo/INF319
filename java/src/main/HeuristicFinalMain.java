package main;

import generators.ISolutionGenerator;
import generators.SolutionGenerator;
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

public class HeuristicFinalMain {
    public static void main(String[] args) throws Throwable {
        String[] instances = new String[]{"Inst4_"};
        for(String instance:instances) {
            long startTimer = System.nanoTime();
            //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

            //reads data from .dat file
            IReader reader = new Reader();

            List<IDataResult> results = new ArrayList<>();
            List<IOperator> operators = null;
            IFeasibility feasibility;
            List<String> instanceSizes = Arrays.asList("Ord_12_Veh_7_Loc_9");
            List<IDataSet> dataSets = reader.readDataFromFiles(instance, instanceSizes);
            int i = 0;
            for (IDataSet dataSet : dataSets) {
                long clusterStartTime = System.nanoTime();
                dataSet.setLocationClusters();
                System.out.println(((double) (System.nanoTime() - clusterStartTime) / 1_000_000_000) / 60 + " min to find cluster");

                Random random = new Random(13 + i);
                feasibility = new Feasibility(dataSet);

                boolean withRandomSolutions = false;
                List<int[]> feasibleSolutions = new ArrayList<>();
                if (withRandomSolutions) {
                    ISolutionGenerator solutionGenerator = new SolutionGenerator(random, feasibility);
                    feasibleSolutions = solutionGenerator.getRandomSolutions(20, dataSet.getVehicleAmount(), dataSet.getOrderAmount(), feasibility);
                }

                operators = getOperators(feasibility, dataSet, random);
                List<IOperator> wildOperators = getWildOperators(feasibility, dataSet, random);

                AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
                IDataResult result = alns.optimize(operators, true, withRandomSolutions, feasibleSolutions, wildOperators);
                results.add(result);
                i++;
            }

            IPrinter printer = new Printer();

            printer.printDataToFile(instance, instanceSizes, results, operators);

            System.out.println(((double) (System.nanoTime() - startTimer) / 1_000_000_000) / 60 + " min");
        }
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
        operators.add(new SwapTwoFirstFit("swapf", random, feasibility, dataSet));
        operators.add(new ExchangeThree("exch3",random,feasibility,dataSet));
//        operators.add(new TwoOpt("2-opt",random,feasibility, dataSet));
        operators.add(new RemoveRandomInsertFirst("rrif", 1,Math.max(a, b),random,feasibility,dataSet));
        return operators;
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
        operators.add(new SwapTwoFirstFit("swapf", random, feasibility, dataSet));
//        operators.add(new ExchangeThree("exch3",rand6om,feasibility,dataSet));
//        operators.add(new TwoOpt("2-opt",random,feasibility, dataSet));
        operators.add(new RemoveRandomInsertFirst("rrif", 1,Math.max(a, b),random,feasibility,dataSet));
        operators.add(new RemoveNonClusteredInsertClustered("rncic", 4,1,Math.max(a,b),random,feasibility,dataSet));
        operators.add(new RemoveExpensiveInsertGreedy("reig", 4,1, Math.max(a,b), random, feasibility, dataSet));
        operators.add(new RemoveSimilarInsertRegret("rsirg", 4, 1, Math.max(a, b),3, random, feasibility, dataSet));
        return operators;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

}
