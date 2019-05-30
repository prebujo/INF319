package main;

import Heuristic.AdaptiveLargeNeighbourhoodSearch;
import Heuristic.IHeuristic;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.utility.IOperator;
import functions.utility.ISolutionGenerator;
import functions.utility.SolutionGenerator;
import reader.FlowReader;
import reader.IReader;
import writer.IPrinter;
import writer.Printer;

import java.util.Random;

public class TestHeuristicMain {

    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        //Scanner scan = new Scanner(System.in);
        String fileName = "AT-DE_W";

        IReader reader = new FlowReader();

        //creates a dataset from file
        IDataSet dataset = reader.readDataFromFile(fileName);

        Random random = new Random(101);

        IHeuristic alns = new AdaptiveLargeNeighbourhoodSearch(dataset,random, "alns");

        IDataResult results = alns.optimize();

        results.printData();

        results.printDataToFile(fileName);
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
