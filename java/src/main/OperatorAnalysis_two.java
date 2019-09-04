package main;

import dataObjects.BitStringAndDataResult;
import dataObjects.BitStringAndOperators;
import dataObjects.IDataSet;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import heuristic.AdaptiveLargeNeighbourhoodSearch;
import printer.IPrinter;
import printer.Printer;
import reader.IReader;
import reader.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OperatorAnalysis_two {
    public static void main(String[] args) throws Throwable {
        //reads data from .dat file
        IReader reader = new Reader();

        IFeasibility feasibility;
        String instance = "Inst1_";
        String fileSize = "Ord_35_Veh_20_Loc_22";
        IDataSet dataSet = reader.readDataFromFile(instance+ fileSize);
        Random random = new Random(101);
        feasibility = new Feasibility(dataSet);
        AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
        List<IOperator> operators= getOperators(feasibility, dataSet, random);
        ArrayList<BitStringAndOperators> operatorCombinations = new ArrayList<>();
        getOperatorsCombinations(0,operators.size(),"",new ArrayList<IOperator>(),operators, operatorCombinations);
        ArrayList<BitStringAndDataResult> dataResults = new ArrayList<>();
        runOperatorAnalysis(operatorCombinations,getWildOperators(feasibility,dataSet,random),alns, dataResults);
        IPrinter printer = new Printer();

        printer.printOperatorDataToFile(instance+fileSize,dataResults,operators);
    }

    private static void getOperatorsCombinations(int i, int n, String s, List<IOperator> operators, List<IOperator> operatorsPossibilities, List<BitStringAndOperators> result) {
        if(i==n) {
            result.add(new BitStringAndOperators(s,new ArrayList<>(operators)));
            return;

        }
        List<IOperator> newOperators = new ArrayList<>(operators);
        getOperatorsCombinations(i+1,n,s.concat("0"),newOperators,operatorsPossibilities,result);

        newOperators.add(operatorsPossibilities.get(i));
        getOperatorsCombinations(i+1,n,s.concat("1"),newOperators,operatorsPossibilities,result);
    }

    private static void runOperatorAnalysis(List<BitStringAndOperators> combinations, List<IOperator> wildOperators, AdaptiveLargeNeighbourhoodSearch alns, List<BitStringAndDataResult> dataResults) throws Throwable {
        for (BitStringAndOperators combination:combinations ) {
            dataResults.add(new BitStringAndDataResult(combination.getBitString(),alns.optimize(combination.getOperators(),wildOperators))); //actual:
        }
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
//        operators.add(new SwapTwoFirstFit2("swapf", random, feasibility, dataSet));
        operators.add(new RemoveNonClusteredInsertClustered("rncic", 4,1,Math.max(a,b),random,feasibility,dataSet));
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


}
