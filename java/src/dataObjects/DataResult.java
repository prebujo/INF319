package dataObjects;

import functions.utility.IOperator;

import java.util.HashSet;
import java.util.List;

public class DataResult implements IDataResult {
    private int[] objectiveResults;
    private HashSet<String> solutions;
    private int[] bestSolution;
    private int[] segments;
    private int segmentAmount;
    private int iterations;
    private int operatorAmount;
    private double[][] operatorWeight;
    private double[][] operatorTime;
    private double[] operatorAverageTime;
    private List<IOperator> operators;
    private List<IDataObject> listOfData;


    public DataResult(List<IOperator> operators, int segmentAmount, int iterations, int operatorAmount){
        this.segmentAmount = segmentAmount;
        this.iterations = iterations;
        this.operatorAmount = operatorAmount;
        this.operatorTime = new double[operatorAmount][segmentAmount];
        this.operatorAverageTime = new double[operatorAmount];
        this.operators = operators;

    }

    @Override
    public void printData() {

        System.out.printf("|    Segments |");
        for (int i = 0; i<segmentAmount;i++){
            System.out.printf(" %5d |"+(i+1));
        }
        System.out.println();

        for (int i = 0; i<operatorWeight.length;i++){
            System.out.printf(""+operators.get(i).getName()+" weights: ");
            for (int j = 0; j<operatorWeight[i].length;j++){
                System.out.printf(" %3.2d |"+operatorWeight[i][j]);
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
    public void printDataToFile(String filename){

    }

    @Override
    public void addObjectData(IDataObject object){
        this.listOfData.add(object);
    }

    @Override
    public void setWeightData(double[][] weightData){
        this.operatorWeight = weightData;
    }

    @Override
    public void setSolutions(HashSet<String> solutions){
        this.solutions = solutions;
    }

    @Override
    public int[] getBestSolution() {
        return bestSolution;
    }

    @Override
    public void setBestSolution(int[] solution){
        this.bestSolution = solution;
    }

}
