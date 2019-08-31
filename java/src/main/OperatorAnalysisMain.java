package main;

import dataObjects.BitStringAndDataResult;
import dataObjects.IDataResult;
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

public class OperatorAnalysisMain {
    public static void main(String[] args) throws Throwable {

        //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

        //reads data from .dat file
        IReader reader = new Reader();

        List<IDataResult> results = new ArrayList<>();
        IFeasibility feasibility;
        String instance = "Inst1_";
        String instanceSize ="Ord_80_Veh_45_Loc_45";
        IDataSet dataSet = reader.readDataFromFile(instance+instanceSize);
        Random random = new Random(101);
        feasibility = new Feasibility(dataSet);
        List<IOperator> operators = getOperators(feasibility, dataSet, random);
        List<IOperator> wildOperators = getWildOperators(feasibility,dataSet,random);

        List<BitStringAndDataResult> dataResults = new ArrayList<>();
        AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
        runOperatorAnalysis(0,operators.size(),"",new ArrayList<IOperator>(), operators, wildOperators, alns, dataResults);
        IPrinter printer = new Printer();
        printer.printOperatorDataToFile(instance+instanceSize,dataResults, operators);

        }

    private static void runOperatorAnalysis(int i, int n, String operatorsBit, List<IOperator> operators, List<IOperator> operatorsPossibilities, List<IOperator> wildOperators,AdaptiveLargeNeighbourhoodSearch alns, List<BitStringAndDataResult> dataResults) throws Throwable {
        if(i==n) {
            if (operators.size()==0) {
                dataResults.add(new BitStringAndDataResult(operatorsBit,null));
            } else{
                IDataResult result=alns.optimize(operators,wildOperators);
                dataResults.add(new BitStringAndDataResult(operatorsBit,result));
            }
            return;
        }

        runOperatorAnalysis(i+1,n,operatorsBit.concat("0"),operators,operatorsPossibilities,wildOperators,alns,dataResults);

        List<IOperator> newOperators = new ArrayList<>(operators);
        newOperators.add(operatorsPossibilities.get(i));
        runOperatorAnalysis(i+1,n,operatorsBit.concat("1"),newOperators,operatorsPossibilities,wildOperators,alns,dataResults);
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
//        operators.add(new SwapTwo("swap2", random, feasibility, dataSet));
//        operators.add(new ExchangeThree("exch3",random,feasibility,dataSet));
        operators.add(new RemoveSimilarInsertRegret("rsirg", 4, 1, Math.max(a, b),3, random, feasibility, dataSet));
        operators.add(new RemoveExpensiveInsertGreedy("reig", 4,1, Math.max(a,b), random, feasibility, dataSet));
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
