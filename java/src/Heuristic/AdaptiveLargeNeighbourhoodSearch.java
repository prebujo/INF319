package Heuristic;

import dataObjects.DataResult;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.Feasibility;
import functions.utility.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AdaptiveLargeNeighbourhoodSearch implements IHeuristic{

    private final IDataSet dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final Feasibility feasibilityCheck;
    private final Random random;
    private final int lowScore = 1;
    private final int mediumScore = 2;
    private final int highScore = 4;
    private String heuristicName;

    public AdaptiveLargeNeighbourhoodSearch(IDataSet dataSet, Random random, String heuristicName){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = new Feasibility(dataSet);
        this.random = random;
        this.heuristicName = heuristicName;
    }

    @Override
    public IDataResult optimize() throws Throwable {

        //Generating start solution
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);

        int[] startSolution = solutionGenerator.createDummySolution(dataSet.getVehicleAmount(), dataSet.getOrderAmount());

        //Hashset to store solutions
        HashSet<String> solutions = new HashSet<>();
        solutions.add(toString(startSolution));
        System.out.println(toString(startSolution));

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        int[] bestSolution = startSolution;
        int[] acceptedSolution = startSolution;
        int[] currentSolution = startSolution;
        double currentObjective = objectiveFunction.calculateSolution(currentSolution);
        double bestObjective = currentObjective;
        double acceptedObjective = currentObjective;

        List<IOperator> operators = new ArrayList<>();

        operators.add(new RemoveAndReinsertRandom(dataSet, random, feasibilityCheck, 2, 4, "r&r2_4"));
        operators.add(new RemoveAndReinsertRandom(dataSet, random, feasibilityCheck, 1, 1, "r&r1_1"));
        operators.add(new SwapTwo(dataSet, random, feasibilityCheck, "swap2"));
        operators.add(new ExchangeThree(dataSet,random,feasibilityCheck,"exch3"));
        int amountOfOperators = operators.size();
        //int iteration = 0;
        int i = 10000;
        double temperature = 1000;
        double decreasePercentage = 0.995;
        double historyWeight = 0.80;
        int[][] operatorWeight;
        double[][] operatorTime;

        //Need to make a section here to decide initial temperature, basically same algorithm with fixed temperature and
        //80% chance of accepting a solution.

        //Initializing weights and scores
        double[] weight = new double[amountOfOperators];
        int segmentLength = 100;
        int currentSegment = 0;
        double[][] weightData = new double[i/segmentLength][amountOfOperators+1];
        double[] score = new double[amountOfOperators];
        int[] runTimes = new int[amountOfOperators];

        weight[0] = 1;
        for (int m = 1; m < weight.length; m++) {
            weight[m] = weight[m-1]+1.0;
        }

        IDataResult data = new DataResult(operators,i/segmentLength,i,operators.size(), heuristicName);
        data.setInitialObjective(currentObjective);
        while (i > 0) {
            int j = segmentLength;
            for (int m = 0;m<score.length;m++) {
                score[m] = 0;
                runTimes[m] = 0;
            }

            //segmentLength
            while (j > 0) {
                currentSolution = acceptedSolution;

                //choosing operator based on probabilities of this segment
                double choice = random.nextDouble()*weight[weight.length-1];
                int operator = 0;
                for (int k = 0; k < amountOfOperators; k++) {
                    if (choice < weight[k]) {
                        operator = k;
                        break;
                    }
                }
                runTimes[operator]++;

                currentSolution = operators.get(operator).apply(currentSolution);

                if(!solutions.contains(toString(currentSolution))) {
                    currentObjective = objectiveFunction.calculateSolution(currentSolution);
                    if (currentObjective < bestObjective) {
                        score[operator] += (highScore - mediumScore);
                        bestSolution = currentSolution;
                        bestObjective = currentObjective;
                        //TODO: Save data to print
                        //iteration = i;
                    }

                    int result = accept(currentObjective, acceptedObjective, temperature);
                    if (result > 0) {
                        solutions.add(toString(currentSolution));
                        score[operator] += result;
                        acceptedObjective = currentObjective;
                        acceptedSolution = currentSolution;
                    }
                }
                temperature = temperature * decreasePercentage;
                i--;
                j--;
            }

            weightData[currentSegment++] = weight;
            weight = getWeights(weight,score,runTimes,historyWeight);
        }
        data.setWeightData(weightData);
        data.setBestSolution(bestSolution);
        data.setSolutions(solutions);
        data.setBestObjective(bestObjective);
        return data;
    }

    private void printArray(double[] weight) {
        for (int i = 0;i<weight.length;i++){
            System.out.println(" "+weight[i]);
        }
    }

    private String toString(int[] solution){
        String result = "";
        for (int i = 0;i<solution.length;i++){
            result += Integer.toString(solution[i]);
        }
        return result;
    }

    @Override
    public String getName() {
        return heuristicName;
    }

    /**
     * Solutions are accepted if Objective is better than current objective or with acceptWorseSolution method
     * @param currentObjective
     * @param acceptedObjective
     * @param temperature
     * @return
     */
    private int accept(double currentObjective, double acceptedObjective, double temperature) {
        if(currentObjective<acceptedObjective){
            return mediumScore;
        } else if(acceptWorseSolution(currentObjective,acceptedObjective,temperature)){
            return lowScore;
        }
        return 0;
    }

    /**
     * Worse solutions are accepted with probability e^(-|f-f_curr|/T)
     * @param currentObjective
     * @param acceptedObjective
     * @param temperature
     * @return
     */
    private boolean acceptWorseSolution(double currentObjective, double acceptedObjective, double temperature) {
        double acceptProbability = Math.exp(-Math.abs(currentObjective-acceptedObjective)/temperature);
        if(random.nextDouble()<acceptProbability){
            return true;
        }
        return false;
    }

    private double[] getWeights(double[] weight, double[] score, int[] runTimes, double historyWeight) {
        double[] result = new double[weight.length];
        double scoredWeight;
        if (runTimes[0]==0){
            scoredWeight=0;
        } else {
            scoredWeight = score[0]/runTimes[0];
        }
        result[0] = weight[0]* historyWeight+scoredWeight*(1-historyWeight);
        for (int k = 1;k<weight.length;k++){
            if(runTimes[k]==0){
                scoredWeight=0;
            } else{
                scoredWeight = score[k]/runTimes[k]*(1-historyWeight);
            }
            result[k] = weight[k-1] + ((weight[k]-weight[k-1])* historyWeight+scoredWeight);
        }
        return result;
    }
}
