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

    private static final int RUNS_AMOUNT = 10;
    private final int INITIAL_SEGMENT_LENGTH = 100;
    private final double INITIAL_ACCEPTANCE_PROBABILITY = 0.8;
    private final IDataSet dataSet;
    private final int vehicleAmount;
    private final int orderAmount;
    private final Random random;
    private final int lowScore = 1;
    private final int mediumScore = 4;
    private final int highScore = 8;
    private final int ITERATIONS = 10000;
    private String heuristicName;
    private int SEGMENT_LENGTH = 100;

    public AdaptiveLargeNeighbourhoodSearch(IDataSet dataSet, Random random, String heuristicName){
        this.dataSet = dataSet;
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.orderAmount = dataSet.getOrderAmount();
        this.random = random;
        this.heuristicName = heuristicName;
    }

    public IDataResult optimize() throws Throwable {
        return optimize(new ArrayList<>());
    }
    public IDataResult optimize(List<IOperator> operators) throws Throwable {
        //Variables that are equal for each run
        double decreasePercentage = 0.995;
        double historyWeight = 0.80;

        //Generating start solution
        ISolutionGenerator solutionGenerator = new SolutionGenerator(random);


        //Hashset to store acceptedSolutions

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataSet);
        int[][] bestSolutions = new int[RUNS_AMOUNT][dataSet.getVehicleAmount()+dataSet.getOrderAmount()*2];
        double[] bestObjectives = new double[RUNS_AMOUNT];
        int[] bestOverallSolution = new int[dataSet.getVehicleAmount()+dataSet.getOrderAmount()*2];
        double averageObjective = 0;
        double averageRunTime = 0;
        double averageImprovement = 0;
        int[] bestIterations = new int[RUNS_AMOUNT];
        double[] heuristicRunningTime = new double[RUNS_AMOUNT];
        int amountOfOperators = operators.size();
        double[] operatorTime = new double[amountOfOperators];
        int[] operatorRunningTimes = new int[amountOfOperators];
        Double[][][] weightData = new Double[RUNS_AMOUNT][ITERATIONS / SEGMENT_LENGTH][amountOfOperators];
        Double[][][] scoreData = new Double[RUNS_AMOUNT][ITERATIONS][amountOfOperators + 1];

        int[] dummySolution = solutionGenerator.createDummySolution(dataSet.getVehicleAmount(), dataSet.getOrderAmount());
        double noTransportObjective =  objectiveFunction.calculateSolution(dummySolution);
        double currentObjective = noTransportObjective;
        double bestOverallObjective = noTransportObjective;
        double bestObjective = currentObjective;
        double acceptedObjective = currentObjective;
        double initialSolutionRunningTime=0.0;
        double initialSolutionAverageObjective = 0.0;
        double initalSolutionAverageImprovement = 0.0;
        IDataResult data = new DataResult(operators, heuristicName, ITERATIONS / SEGMENT_LENGTH, ITERATIONS, operators.size(), orderAmount, vehicleAmount, dataSet.getLocationsAmount());
        data.setNoTransportObjective(noTransportObjective);


        for (int i = 0; i < RUNS_AMOUNT; i++) {
            long initialSolutionStartTimer = System.nanoTime();
            int[] startSolution = solutionGenerator.createDummyStartSolution(dataSet.getVehicleAmount(), dataSet.getOrderAmount());
            initialSolutionRunningTime += (double) (System.nanoTime() - initialSolutionStartTimer) / 1_000_000_000;
            double startSolutionObjective = objectiveFunction.calculateSolution(startSolution);
            initialSolutionAverageObjective += startSolutionObjective;
            initalSolutionAverageImprovement += (noTransportObjective - startSolutionObjective) / noTransportObjective;

            int[] bestSolution = startSolution;
            int[] acceptedSolution = startSolution;
            int[] currentSolution;
            currentObjective=objectiveFunction.calculateSolution(startSolution);

            HashSet<String> acceptedSolutions = new HashSet<>();
            acceptedSolutions.add(toString(startSolution));
            //int iteration = 0;
            int iteration = 0;
            double temperature = 1000;
            int currentSegment = 0;

            //Initializing weights and scores
            Double[] weight = assignEqualStartWeights(amountOfOperators);
            Double[] score = new Double[amountOfOperators];
            Double[] accumulatedTotalScore;
            int[] runTimes = new int[amountOfOperators];

            data.setNoTransportObjective(currentObjective);

            //STEP 1: SELECTING THE INITIAL TEMPERATURE SECTION:
            long heuristicStartTimer = System.nanoTime();

            for (int m = 0; m < score.length; m++) {
                score[m] = 0.0;
                runTimes[m] = 0;
            }

            weightData[i][currentSegment++] = weight.clone(); //saving weights used in current run.
            int segmentIteration = 0;
            Double objectiveDifference = 0.0;
            int objectiveCounter = 0;
            while (segmentIteration < INITIAL_SEGMENT_LENGTH) {

                currentSolution = acceptedSolution.clone();

                //choosing operator based on probabilities of this segment
                int operator = getOperator(amountOfOperators, weight, random.nextDouble());

                runTimes[operator]++;
                operatorRunningTimes[operator] += 1;

                //applying operator and saving time spent
                long operatorStartTimer = System.nanoTime();
                currentSolution = operators.get(operator).apply(currentSolution);
                operatorTime[operator] += (double) (System.nanoTime() - operatorStartTimer) / 1_000_000_000;

                if (!acceptedSolutions.contains(toString(currentSolution))) {
                    currentObjective = objectiveFunction.calculateSolution(currentSolution);
                    if (currentObjective < bestObjective) {
                        score[operator] += (highScore - mediumScore);
                        bestSolution = currentSolution.clone();
                        bestObjective = currentObjective;
                        bestIterations[i] = iteration + 1;
                    }

                    int result = 0;
                    if (currentObjective < acceptedObjective) {
                        result = mediumScore;
                    } else if (random.nextDouble() < INITIAL_ACCEPTANCE_PROBABILITY) {
                        result = lowScore;
                    }
                    if (result < mediumScore) {
                        objectiveDifference += Math.abs(acceptedObjective - currentObjective);
                        objectiveCounter++;
                    }
                    if (result > 0) {
                        acceptedSolutions.add(toString(currentSolution));
                        score[operator] += result;
                        acceptedObjective = currentObjective;
                        acceptedSolution = currentSolution.clone();
                    }
                }
                scoreData[i][iteration++] = score.clone();
                segmentIteration++;
            }


            accumulatedTotalScore = updateAccumulatedTotalScore(amountOfOperators, score, runTimes);
            weight = updateWeights(weight, accumulatedTotalScore, historyWeight);

            temperature = -(objectiveDifference / objectiveCounter) / Math.log(INITIAL_ACCEPTANCE_PROBABILITY);

            //TODO: Experiment with the temperature selection segment length

            //INITIAL TEMPERATURE SELECTED


            //STEP 2: STARTING THE ALNS ALGORITHM WITH THE CHOSEN TEMPERATURE
            while (iteration < ITERATIONS) {
                segmentIteration = 0;
                for (int m = 0; m < score.length; m++) {
                    score[m] = 0.0;
                    runTimes[m] = 0;
                }

                weightData[i][currentSegment++] = weight.clone(); //saving weights used in current run.

                while (segmentIteration < SEGMENT_LENGTH) {

                    currentSolution = acceptedSolution.clone();

                    //choosing operator based on probabilities of this segment
                    int operator = getOperator(amountOfOperators, weight, random.nextDouble());


                    runTimes[operator]++;
                    operatorRunningTimes[operator] += 1;

                    long operatorStartTimer = System.nanoTime();
                    currentSolution = operators.get(operator).apply(currentSolution);
                    operatorTime[operator] += (double) (System.nanoTime() - operatorStartTimer) / 1_000_000_000;

                    if (!acceptedSolutions.contains(toString(currentSolution))) {
                        currentObjective = objectiveFunction.calculateSolution(currentSolution);
                        if (currentObjective < bestObjective) {
                            score[operator] += (highScore - mediumScore);
                            bestSolution = currentSolution.clone();
                            bestObjective = currentObjective;
                            bestIterations[i] = iteration + 1;
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
                    scoreData[i][iteration++] = score.clone();
                    segmentIteration++;
                }


                accumulatedTotalScore = updateAccumulatedTotalScore(amountOfOperators, score, runTimes);
                weight = updateWeights(weight, accumulatedTotalScore, historyWeight);
            }
            //ALNS DONE

            //UPDATING DATA
            heuristicRunningTime[i] = (double) (System.nanoTime() - heuristicStartTimer) / 1_000_000_000;
            averageRunTime+=heuristicRunningTime[i];
            averageObjective+=bestObjective;
            averageImprovement += (noTransportObjective-bestObjective)/noTransportObjective;

            bestObjectives[i] = bestObjective;
            if (bestObjective<bestOverallObjective){
                bestOverallObjective=bestObjective;
                bestOverallSolution=bestSolution.clone();
            }
            //DONE UPDATING DATA for this run, continuing to next run
        }

        //SAVING DATA
        data.setScoreData(scoreData);
        data.setOperatorWeightData(weightData);
        data.setOperatorRunningTimes(operatorRunningTimes);
        data.setOperatorTime(operatorTime);
        data.setBestSolution(bestOverallSolution);
        data.setBestIterations(bestIterations);
        data.setBestObjective(bestOverallObjective);
        data.setBestObjectives(bestObjectives);
        data.setBestImprovement((noTransportObjective-bestOverallObjective)/noTransportObjective);
        data.setInitialSolutionRunningTime(initialSolutionRunningTime/RUNS_AMOUNT);
        data.setInitialSolutionAverageObjective(initialSolutionAverageObjective/RUNS_AMOUNT);
        data.setInitialSolutionAverageImprovement(initalSolutionAverageImprovement/RUNS_AMOUNT);
        data.setAverageImprovement(averageImprovement/RUNS_AMOUNT);
        data.setAverageObjective(averageObjective/RUNS_AMOUNT);
        data.setRunningTime(averageRunTime/RUNS_AMOUNT);
        //DONE SAVING DATA

        return data;
    }

    private int getOperator(int amountOfOperators, Double[] weight, double choice) {
        int operator = 0;
        double sum = 0.0;
        for (int k = 0; k < amountOfOperators; k++) {
            sum += weight[k];
            if (choice < sum) {
                operator = k;
                break;
            }
        }
        return operator;
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
        double minScore = 0;
        if(zeroAmounts>0){
            minScore = (0.05*tempResult[tempResult.length-1])/(1-0.05*zeroAmounts);
        }
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
        if(accumulatedTotalScore[0]!=0) {
            result[0] = weight[0] * historyWeight + accumulatedTotalScore[0] / accumulatedTotalScore[accumulatedTotalScore.length - 1] * (1 - historyWeight);
            for (int k = 1; k < weight.length; k++) {
                result[k] = weight[k] * historyWeight + (accumulatedTotalScore[k] - accumulatedTotalScore[k - 1]) / accumulatedTotalScore[accumulatedTotalScore.length - 1] * (1.0 - historyWeight);
            }
            return result;
        }
        else{
            return weight;
        }
    }
}
