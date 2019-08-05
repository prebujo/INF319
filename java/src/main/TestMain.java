package main;

import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.Feasibility;
import functions.feasibility.IFeasibility;
import functions.utility.*;
import reader.IReader;
import reader.Reader;

import java.util.Random;

public class TestMain {

    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        //Scanner scan = new Scanner(System.in);
        //String fileName = scan.nextLine();

        IReader reader = new Reader();

        //creates a dataset from file
        IDataSet dataset = reader.readDataFromFile("Inst1_Ord_4_Veh_3_Loc_7");

        Random random = new Random(101);
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);

        int[] solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(), dataset.getOrderAmount());

        System.out.println("Solution: ");
        printArray(solution);

        solution = solutionGenerator.createDummySolution(dataset.getVehicleAmount(), dataset.getOrderAmount());

        System.out.println("Solution: ");
        printArray(solution);

        ObjectiveFunction objective = new ObjectiveFunction(dataset);
        double solutionObjective = objective.calculateSolution(solution);

        System.out.println("Solution Objective: " + solutionObjective);

        solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(), dataset.getOrderAmount());

        IFeasibility feasibility = new Feasibility(dataset);
        int iterations = 0;
        while (!feasibility.check(solution)) {
            solution = solutionGenerator.randomlyAssignOrders(dataset.getVehicleAmount(), dataset.getOrderAmount());
            iterations++;
        }
        System.out.println("Solution found after " + iterations + " iterations:");
        printArray(solution);

        solutionObjective = objective.calculateSolution(solution);
        System.out.println("Solution objective: " + solutionObjective);

        IOperator swap = new SwapTwo("swap2", random, feasibility, dataset);
        solution = checkOperator(swap, solution, solutionObjective, objective);

        solutionObjective = objective.calculateSolution(solution);

        IOperator exchangeThree = new ExchangeThree("exchange3", random, feasibility, dataset);
        solution = checkOperator(exchangeThree, solution, solutionObjective, objective);

        solutionObjective = objective.calculateSolution(solution);

        IOperator reinsertTwoOrThree = new RemoveAndReinsert("reinsert", 2, 3, random, feasibility, dataset);
        solution = checkOperator(reinsertTwoOrThree, solution, solutionObjective, objective);
        solutionObjective = objective.calculateSolution(solution);

        IOperator reinsertRandomTwoOrThree = new RemoveAndReinsertRandom("reinsertRandom", 2, 3, random, feasibility, dataset);
        solution = checkOperator(reinsertRandomTwoOrThree, solution, solutionObjective, objective);
        solutionObjective = objective.calculateSolution(solution);

        int i = 10000;
        while (i > 0) {
            int[] newSolution = reinsertRandomTwoOrThree.apply(solution);
            double objec = objective.calculateSolution(newSolution);
            if (objec < solutionObjective) {
                solution = newSolution;
                solutionObjective = objec;
            }
            i--;
        }

        System.out.println("best objective after 1000 iter: " + solutionObjective);
        System.out.println("solution: ");
        printArray(solution);

//    solution = new int[]{3,3,0, 0, 8, 13, 11, 13, 8, 11, 0, 10, 14, 6, 10, 6, 14, 0, 5, 19, 18, 19, 18, 5, 17, 15, 17, 15, 0, 0, 0, 0, 16, 9, 9, 16, 12, 7, 7, 12, 0, 2, 2, 1, 1,4,4 };
        solution = new int[]{2,2,0,3,4,3,4,0,1,1,0};
        System.out.println("is solution feasible: "+ feasibility.check(solution));








    }

    public static void printArray(int[] array) {
        for (int i = 0; i<array.length;i++){
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

    public static int[] checkOperator(IOperator operator, int[] solution, double solutionObjective, ObjectiveFunction objective) throws Throwable {
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
