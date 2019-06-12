package Heuristic;

import dataObjects.DataResult;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.ObjectiveFunction;
import functions.feasibility.IFeasibility;
import functions.utility.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AdaptiveLargeNeighbourhoodSearch implements IHeuristic{

    private final IDataSet dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final IFeasibility feasibilityCheck;
    private final Random random;
    private final int lowScore = 1;
    private final int mediumScore = 4;
    private final int highScore = 8;
    private final int ITERATIONS = 10000;
    private String heuristicName;
    private int SEGMENT_LENGTH = 100;

    public AdaptiveLargeNeighbourhoodSearch(IDataSet dataSet, IFeasibility feasibility, Random random, String heuristicName){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.feasibilityCheck = feasibility;
        this.random = random;
        this.heuristicName = heuristicName;
    }

    public IDataResult optimize() throws Throwable {
        return optimize(new ArrayList<>());
    }
    public IDataResult optimize(List<IOperator> operators) throws Throwable {

        //Generating start solution
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);

        int[] startSolution = solutionGenerator.createDummySolution(dataSet.getVehicleAmount(), dataSet.getOrderAmount());

        //Hashset to store acceptedSolutions
        HashSet<String> acceptedSolutions = new HashSet<>();
        acceptedSolutions.add(toString(startSolution));
        System.out.println(toString(startSolution));

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        int[] bestSolution = startSolution;
        int[] acceptedSolution = startSolution;
        int[] currentSolution = startSolution;
        double currentObjective = objectiveFunction.calculateSolution(currentSolution);
        double bestObjective = currentObjective;
        double acceptedObjective = currentObjective;


        int amountOfOperators = operators.size();
        //int iteration = 0;
        int i = 0;
        int bestIteration = 0;
        double temperature = 1000;
        double decreasePercentage = 0.995;
        double historyWeight = 0.80;
        double[] operatorTime = new double[amountOfOperators];
        int[] operatorRunningTimes = new int[amountOfOperators];
        int currentSegment = 0;

        //TODO: Need to make a section here to decide initial temperature, basically same algorithm with fixed temperature and
        // 80% chance of accepting a solution.

        //Initializing weights and scores
        Double[] weight = assignEqualStartWeights(amountOfOperators);
        Double[][] weightData = new Double[ITERATIONS/SEGMENT_LENGTH][amountOfOperators];
        Double[] score = new Double[amountOfOperators];
        Double[] accumulatedTotalScore;
        Double[][] scoreData = new Double[ITERATIONS][amountOfOperators+1];
        int[] runTimes = new int[amountOfOperators];

        IDataResult data = new DataResult(operators,heuristicName,ITERATIONS/ SEGMENT_LENGTH,ITERATIONS, operators.size(), orderAmount,vehicleAmount,dataSet.getLocationsAmount());
        data.setInitialObjective(currentObjective);

        double heuristicRunningTime = 0;
        long heuristicStartTimer = System.nanoTime();


        

        while (i < ITERATIONS) {
            int segmentIteration = 0;
            for (int m = 0;m<score.length;m++) {
                score[m] = 0.0;
                runTimes[m] = 0;
            }

            weightData[currentSegment++] = weight.clone(); //saving weights used in current run.

            while (segmentIteration < SEGMENT_LENGTH) {

                currentSolution = acceptedSolution.clone();

                //choosing operator based on probabilities of this segment
                double choice = random.nextDouble();
                int operator = 0;
                double sum = 0.0;
                for (int k = 0; k < amountOfOperators; k++) {
                    sum += weight[k];
                    if (choice < sum) {
                        operator = k;
                        break;
                    }
                }


                runTimes[operator]++;
                operatorRunningTimes[operator]+=1;

                long operatorStartTimer = System.nanoTime();
                currentSolution = operators.get(operator).apply(currentSolution);
                operatorTime[operator] += (double)(System.nanoTime() - operatorStartTimer)/1_000_000_000;

                if(!acceptedSolutions.contains(toString(currentSolution))) {
                    currentObjective = objectiveFunction.calculateSolution(currentSolution);
                    if (currentObjective < bestObjective) {
                        score[operator] += (highScore - mediumScore);
                        bestSolution = currentSolution.clone();
                        bestObjective = currentObjective;
                        bestIteration = i + 1;
                        //TODO: Save data to print
                        //iteration = i;
                    }

                    int result = accept(currentObjective, acceptedObjective, temperature);
                    if (result > 0) {
                        acceptedSolutions.add(toString(currentSolution));
                        score[operator] += result;
                        acceptedObjective = currentObjective;
                        acceptedSolution = currentSolution.clone();
                    }
                }
                temperature = temperature * decreasePercentage;
                scoreData[i++] = score.clone();
                segmentIteration++;
            }


            accumulatedTotalScore= updateAccumulatedTotalScore(amountOfOperators, score, runTimes);
            weight = updateWeights(weight,accumulatedTotalScore,historyWeight);
        }
        heuristicRunningTime = (double)(System.nanoTime() - heuristicStartTimer)/1_000_000_000;

        data.setOperatorTime(operatorTime);
        data.setOperatorRunningTimes(operatorRunningTimes);
        data.setWeightData(weightData);
        data.setScoreData(scoreData);
        data.setBestSolution(bestSolution);
        data.setSolutions(acceptedSolutions);
        data.setBestObjective(bestObjective);
        data.setBestIteration(bestIteration);
        data.setRunningTime(heuristicRunningTime);

        return data;
    }

    private Double[] updateAccumulatedTotalScore(int amountOfOperators, Double[] score, int[] runTimes) {
        Double[] tempResult = new Double[amountOfOperators];
        Double totalScore;
        int zeroAmounts=0;
        if(runTimes[0]>0) {
            totalScore = score[0] / runTimes[0];
        } else {
            totalScore = 0.0;
        }
        if (totalScore==0.0){
            zeroAmounts++;
        }
        tempResult[0] = totalScore;


        for (int o = 1;o<amountOfOperators;o++){
            if(runTimes[o]>0) {
                totalScore = score[o] / runTimes[o];
            } else {
                totalScore = 0.0;
            }
            if(totalScore==0.0){
                zeroAmounts++;
            }
            tempResult[o] = tempResult[o-1] + totalScore;
        }

        //ensuring minimum weight of 5 % for operators without scores
        Double[] result = new Double[amountOfOperators];
        if(zeroAmounts>0){
            double minScore = (0.05*tempResult[tempResult.length-1])/(1-0.05*zeroAmounts);
            if(tempResult[0] == 0.0){
                result[0] = minScore;
            } else {
                result[0] = tempResult[0];
            }
            for (int o = 1;o<amountOfOperators;o++){
                if(tempResult[o]-tempResult[o-1] == 0.0){
                    result[o] = result[o-1] + minScore;
                } else {
                    result[o] = result[o-1] + tempResult[o]-tempResult[o-1];
                }
            }
        }

        return result;
    }

    private Double[] assignEqualStartWeights(int operatorAmount) {
        Double[] result = new Double[operatorAmount];
        for (int m = 0; m < result.length; m++) {
            result[m] = 1.0/operatorAmount;
        }
        return result;
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

    private Double[] updateWeights(Double[] weight, Double[] accumulatedTotalScore, double historyWeight) {
        Double[] result = new Double[weight.length];
        result[0] = weight[0]*historyWeight + accumulatedTotalScore[0]/accumulatedTotalScore[accumulatedTotalScore.length-1]*(1-historyWeight);
        for (int k = 1;k<weight.length;k++){
            result[k] = weight[k]*historyWeight + (accumulatedTotalScore[k]-accumulatedTotalScore[k-1])/accumulatedTotalScore[accumulatedTotalScore.length-1]*(1.0-historyWeight);
        }
        return result;
    }
}
