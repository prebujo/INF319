package main;

import Heuristic.AdaptiveLargeNeighbourhoodSearch;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import printer.IPrinter;
import printer.Printer;
import reader.FlowReader;
import reader.IReader;
import reader.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestHeuristicMain {

    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        //Scanner scan = new Scanner(System.in);
        String fileName = "simple";

        //reads data from 4flow file
//        IReader reader = new FlowReader();
//        IDataSet dataSet = reader.readDataFromFile(fileName);

        //reads data from .dat file
        IReader reader = new Reader();
        IDataSet dataSet = reader.readDataFromFile(fileName);

        Random random = new Random(101);

        IFeasibility feasibility = new Feasibility(dataSet);

        AdaptiveLargeNeighbourhoodSearch alns = new AdaptiveLargeNeighbourhoodSearch(dataSet,feasibility, random, "alns");

        List<IOperator> operators = new ArrayList<>();

        String swapDescription="Swaps the position of 2 orders from one vehicle to another";
        operators.add(new SwapTwo(dataSet, random, feasibility, "swap2", swapDescription));
        String exchangeDescription="exchanges the order of 3 orders a delivery schedule of a";
        operators.add(new ExchangeThree(dataSet,random,feasibility,"exch3", exchangeDescription));
        String removeAndReinsertTwoToFourDescription="remove and reinsert operator that removes between 2-4 random elements from solution and reinserts them in randomly selected vehicles";
        operators.add(new RemoveAndReinsertRandom(dataSet, random, feasibility, 1, 3, "r&R1_4", removeAndReinsertTwoToFourDescription));
        String removeAndReinsertOneDescription="remove and reinsert operator that removes between 1-1 random elements from solution and reinserts them in randomly selected vehicles";
        operators.add(new RemoveAndReinsert(dataSet, random, feasibility, 1, 3, "r&r1_4", removeAndReinsertOneDescription));
        String returnSame = "return the same solution";
        operators.add(new ReturnSameSolution("retSame", returnSame));

        IDataResult results = alns.optimize(operators);

        IPrinter printer = new Printer();

        printer.printDataToFile(fileName,dataSet,results,operators);
    }
    private static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

    private static int[] checkOperator(IOperator operator, int[] solution, double solutionObjective, ObjectiveFunction objective) throws Throwable {
        int iterations = 0;
        System.out.println("performing operator "+ operator.getName());
        double currentObjective = solutionObjective;
        while(currentObjective == solutionObjective){
            solution = operator.apply(solution);
            currentObjective = objective.calculateSolution(solution);
            iterations++;
        }

        System.out.println("Solution found after "+iterations+" iterations:");
        printArray(solution);

        System.out.println("Solution objective: "+currentObjective);
        return solution;
    }
}
