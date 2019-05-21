package main;

import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import reader.FlowReader;
import reader.IReader;

import java.util.Random;
import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        //Scanner scan = new Scanner(System.in);
        //String fileName = scan.nextLine();

        IReader reader = new FlowReader();

        //creates a dataset from file
        IDataSet dataset = reader.readDataFromFile("AT-DE_W");

        Random random = new Random(4);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);

        int[] solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(),dataset.getOrderAmount());

        System.out.println("Solution: ");
        printArray(solution);

        solution = solutionGenerator.createDummySolution(dataset.getVehicleAmount(),dataset.getOrderAmount());

        System.out.println("Solution: ");
        printArray(solution);

        ObjectiveFunction objective = new ObjectiveFunction(dataset);
        double solutionObjective = objective.calculateSolution(solution);

        System.out.println("Solution Objective: "+solutionObjective);

        solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(),dataset.getOrderAmount());

        IFeasibility feasibility = new Feasibility(dataset);
        int iterations = 0;
        while(!feasibility.check(solution)){
            solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(),dataset.getOrderAmount());
            iterations++;
        }
        System.out.println("Solution found after "+iterations+" iterations:");
        printArray(solution);

        solutionObjective = objective.calculateSolution(solution);
        System.out.println("Solution objective: "+solutionObjective);

        IOperator swap = new SwapTwo(dataset,random,feasibility,"swap2");
        solution = checkOperator(swap,solution,solutionObjective,objective);

        IOperator exchangeThree = new ExchangeThree(dataset,random,feasibility,"exchange3");
        solution = checkOperator(exchangeThree,solution,solutionObjective,objective);






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

        solutionObjective = objective.calculateSolution(solution);
        System.out.println("Solution objective: "+solutionObjective);
        return solution;
    }
}
