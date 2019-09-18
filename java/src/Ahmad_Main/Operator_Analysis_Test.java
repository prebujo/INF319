package Ahmad_Main;

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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Operator_Analysis_Test {
    public static void main(String[] args) throws Throwable {
        long startTimer = System.nanoTime();
        //reads data from .dat file
        IReader reader = new Reader();

        List<String> instances = Arrays.asList("Inst1_", "Inst2_", "Inst3_","Inst4_","Inst5_");
        int s=0;

        for (String instance:instances){
            String fileSize = "Ord_4_Veh_3_Loc_7";
            IDataSet dataSet = reader.readDataFromFile(instance + fileSize);
            dataSet.setLocationClusters();
            Random random = new Random(101 + s);
            IFeasibility feasibility = new Feasibility(dataSet);
            AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet, random, "alns");
            List<IOperator> operators = getOperators(feasibility, dataSet, random);
            ArrayList<BitStringAndOperators> operatorCombinations = new ArrayList<>();
            getOperatorsCombinations(0, operators.size(), "", new ArrayList<IOperator>(), operators, operatorCombinations);
            ArrayList<BitStringAndDataResult> dataResults = new ArrayList<>();
            runOperatorAnalysis(operatorCombinations, getWildOperators(dataSet), new ArrayList<>(), alns, dataResults);
            IPrinter printer = new Printer();

            printer.printOperatorDataToFile(instance + fileSize, dataResults, operators);

            System.out.println(((double) (System.nanoTime() - startTimer) / 1_000_000_000) / 60 + " min");
            s++;
        }
    }

    private static void getOperatorsCombinations(int i, int n, String s, List<IOperator> operators, List<IOperator> operatorsPossibilities, List<BitStringAndOperators> result) {
        if (i == n) {
            result.add(new BitStringAndOperators(s, new ArrayList<>(operators)));
            return;

        }
        List<IOperator> newOperators = new ArrayList<>(operators);
        getOperatorsCombinations(i + 1, n, s.concat("0"), newOperators, operatorsPossibilities, result);

        newOperators.add(operatorsPossibilities.get(i));
        getOperatorsCombinations(i + 1, n, s.concat("1"), newOperators, operatorsPossibilities, result);
    }

    private static void runOperatorAnalysis(List<BitStringAndOperators> combinations, List<IOperator> wildOperators,List<int[]> feasibleSolutions, AdaptiveLargeNeighbourhoodSearch alns, List<BitStringAndDataResult> dataResults) throws Throwable {
        int i = 1;
        for (BitStringAndOperators combination : combinations) {
            System.out.println(i + "/" + combinations.size());
            dataResults.add(new BitStringAndDataResult(combination.getBitString(), alns.optimize(combination.getOperators(), false, false, feasibleSolutions, wildOperators))); //actual:
            i++;
        }
    }

    private static List<IOperator> getWildOperators(IDataSet dataSet) {

        return Arrays.asList(new RemoveRandomInsertFirst("",0,0,null,null,dataSet));
    }

    private static List<IOperator> getOperators(IFeasibility feasibility, IDataSet dataSet, Random random) {
        int orderAmount = dataSet.getOrderAmount();
        int a = (int) (orderAmount * 0.1);
        int b = 5;
        if (orderAmount < b) {
            b = 3;
        }
        List<IOperator> operators;
        operators = new ArrayList<>();
        operators.add(new SwapTwoFirstFit("swapf", random, feasibility, dataSet));
        operators.add(new ExchangeThree("exch3", random, feasibility, dataSet));
        operators.add(new TwoOpt("2-opt", random, feasibility, dataSet));
        operators.add(new RemoveRandomInsertFirst("rrif", 1, Math.max(a, b), random, feasibility, dataSet));
        operators.add(new RemoveNonClusteredInsertClustered("rncic", 4, 1, Math.max(a, b), random, feasibility, dataSet));
        operators.add(new RemoveExpensiveInsertGreedy("reig", 4, 1, Math.max(a, b), random, feasibility, dataSet));
        operators.add(new RemoveSimilarInsertRegret("rsirg", 4, 1, Math.max(a, b), 3, random, feasibility, dataSet));
        return operators;
    }
}

