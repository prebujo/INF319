package dataObjects;

import functions.utility.IOperator;

import java.io.FileWriter;
import java.io.IOException;
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
    private String heuristicName;
    private double bestObjective;
    private double initialObjective;
    private double runningTime;


    public DataResult(List<IOperator> operators, int segmentAmount, int iterations, int operatorAmount, String heuristicName){
        this.segmentAmount = segmentAmount;
        this.iterations = iterations;
        this.operatorAmount = operatorAmount;
        this.operatorTime = new double[operatorAmount][segmentAmount];
        this.operatorAverageTime = new double[operatorAmount];
        this.operators = operators;
        this.heuristicName = heuristicName;

    }

    @Override
    public void printData() {

        System.out.printf("|     Segments |");
        for (int i = 0; i<operatorWeight.length;i++){
            System.out.printf(" %5d |",(i+1));
        }
        System.out.println();

        for (int i = 0; i<operatorWeight[0].length;i++){
            System.out.printf("%6s weights: ",operators.get(i).getName());
            for (int j = 0; j<operatorWeight.length;j++){
                System.out.printf(" %3.3f |",operatorWeight[j][i]);
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
        try {
            FileWriter fileWriter = new FileWriter("RESULTS_"+filename+".csv");
            String line = "Results from runnning " + heuristicName + " on input file instance "+ filename+"\n";
            fileWriter.write(line);
            fileWriter.write("\n");

            line = "Instance,Initial_Obj,Best_Obj,Run_Time\n";

            fileWriter.write(line);

            line = filename+","+initialObjective+","+bestObjective+","+runningTime+"\n";
            fileWriter.write(line);

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
