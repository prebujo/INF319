package dataObjects;

import functions.utility.IOperator;

import java.util.HashSet;
import java.util.List;

public class DataResult implements IDataResult {
    private final int orderAmount;
    private final int vehicleAmount;
    private final int locationsAmount;
    private final String name;
    private int[] objectiveResults;
    private HashSet<String> solutions;
    private int[] bestSolution;
    private int[] segments;
    private int segmentAmount;
    private int iterations;
    private int operatorAmount;
    private Double[][] operatorWeightData;
    private List<IOperator> operators;
    private List<IDataObject> listOfData;
    private String heuristicName;
    private double bestObjective;
    private double initialObjective;
    private double runningTime;
    private int bestIteration;
    private Double[][] scoreData;
    private double[] operatorTime;
    private int[] operatorRunningTimes;
    private double[] bestSolutions;
    private double initialSolutionAverageObjective;
    private double initialSolutionRunningTime;
    private double initialSolutionAverageImprovement;
    private double averageObjective;
    private double averageImprovement;
    private double bestImprovement;


    public DataResult(List<IOperator> operators, String heuristicName, int segmentAmount, int iterations, int operatorAmount, int orderAmount, int vehicleAmount, int locationsAmount){
        this.segmentAmount = segmentAmount;
        this.iterations = iterations;
        this.operatorAmount = operatorAmount;
        this.operators = operators;
        this.heuristicName = heuristicName;
        this.orderAmount = orderAmount;
        this.vehicleAmount = vehicleAmount;
        this.locationsAmount = locationsAmount;
        this.name = "Inst"+"_";

    }

    @Override
    public void printData() {

        System.out.printf("|     Segments |");
        for (int i = 0; i< operatorWeightData.length; i++){
            System.out.printf(" %5d |",(i+1));
        }
        System.out.println();

        for (int i = 0; i< operatorWeightData[0].length; i++){
            System.out.printf("%6s weights: ",operators.get(i).getName());
            for (int j = 0; j< operatorWeightData.length; j++){
                System.out.printf(" %3.3f |", operatorWeightData[j][i]);
            }
            System.out.println();
        }

//        System.out.printf("|             |");
//        for (IOperator operator:operators){
//            System.out.printf(" %8d |", operator.getName());
//        }
//        System.out.println();
//        System.out.printf("| Average Time: |");
//        for (int i = 0; i<operatorAverageTime.length;i++) {
//            System.out.printf(" %8d |", operatorAverageTime[i]);
//        }
//        System.out.println();

    }

    @Override
    public void addObjectData(IDataObject object){
        this.listOfData.add(object);
    }

    @Override
    public void setOperatorWeightData(Double[][] OperatorWeightData){
        this.operatorWeightData = OperatorWeightData;
    }

    @Override
    public void setSolutions(HashSet<String> solutions){
        this.solutions = solutions;
    }

    @Override
    public void setBestObjective(double bestObjective){
        this.bestObjective = bestObjective;
    }

    @Override
    public void setInitialObjective(double initialObjective){
        this.initialObjective = initialObjective;
    }

    @Override
    public void setRunningTime (double runningTime){
        this.runningTime = runningTime;
    }

    @Override
    public int[] getBestSolution() {
        return bestSolution;
    }

    @Override
    public String getHeuristicName() {
        return heuristicName;
    }

    @Override
    public void setBestSolution(int[] solution){
        this.bestSolution = solution;
    }

    @Override
    public void setBestIteration(int bestIteration){
        this.bestIteration = bestIteration;
    }

    @Override
    public void setScoreData(Double[][] scoreData) {
        this.scoreData = scoreData;
    }

    @Override
    public void setOperatorTime(double[] operatorTime) {
        this.operatorTime = operatorTime;
    }
    @Override
    public void setOperatorRunningTimes(int[] operatorRunningTimes) {
        this.operatorRunningTimes = operatorRunningTimes;
    }

    @Override
    public double getNoTransportObjective() {
        return initialObjective;
    }

    @Override
    public double getBestObjective() {
        return bestObjective;
    }

    @Override
    public double getRunningTime() {
        return runningTime;
    }

    @Override
    public int getBestIteration() {
        return bestIteration;
    }

    @Override
    public double[] getOperatorTime() {
        return operatorTime;
    }

    @Override
    public int[] getOperatorRunningTimes() {
        return operatorRunningTimes;
    }

    @Override
    public Double[][] getOperatorWeightData() {
        return operatorWeightData;
    }

    @Override
    public Double[][] getScoreData() {
        return scoreData;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public double[] getBestSolutions() {
        return bestSolutions;
    }
    @Override
    public void setBestSolutions(double[] bestSolutions) {
        this.bestSolutions = bestSolutions;
    }

    @Override
    public double getInitialSolutionAverageObjective() {
        return initialSolutionAverageObjective;
    }

    @Override
    public double getInitialSolutionRunningTime() {
        return initialSolutionRunningTime;
    }

    @Override
    public double getInitialSolutionAverageImprovement() {
        return initialSolutionAverageImprovement;
    }

    @Override
    public double getAverageObjective() {
        return averageObjective;
    }

    @Override
    public double getAverageImprovement() {
        return averageImprovement;
    }

    @Override
    public double getBestImprovement() {
        return bestImprovement;
    }

    @Override
    public void setInitialSolutionAverageObjective(double initialSolutionAverageObjective) {
        this.initialSolutionAverageObjective = initialSolutionAverageObjective;
    }

    @Override
    public void setInitialSolutionRunningTime(double initialSolutionRunningTime) {
        this.initialSolutionRunningTime = initialSolutionRunningTime;
    }

    @Override
    public void setInitialSolutionAverageImprovement(double initialSolutionAverageImprovement) {
        this.initialSolutionAverageImprovement = initialSolutionAverageImprovement;
    }

    public void setAverageObjective(double averageObjective) {
        this.averageObjective = averageObjective;
    }

    public void setAverageImprovement(double averageImprovement) {
        this.averageImprovement = averageImprovement;
    }

    public void setBestImprovement(double bestImprovement) {
        this.bestImprovement = bestImprovement;
    }
}
