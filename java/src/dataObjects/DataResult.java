package dataObjects;

import functions.utility.IOperator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class DataResult implements IDataResult {
    private final int orderAmount;
    private final int vehicleAmount;
    private final int locationsAmount;
    private int[] objectiveResults;
    private HashSet<String> solutions;
    private int[] bestSolution;
    private int[] segments;
    private int segmentAmount;
    private int iterations;
    private int operatorAmount;
    private Double[][] operatorWeight;
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


    public DataResult(List<IOperator> operators, String heuristicName, int segmentAmount, int iterations, int operatorAmount, int orderAmount, int vehicleAmount, int locationsAmount){
        this.segmentAmount = segmentAmount;
        this.iterations = iterations;
        this.operatorAmount = operatorAmount;
        this.operators = operators;
        this.heuristicName = heuristicName;
        this.orderAmount = orderAmount;
        this.vehicleAmount = vehicleAmount;
        this.locationsAmount = locationsAmount;

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
            FileWriter fileWriter = new FileWriter("res/RESULTS_"+filename+".csv");
            String line = "Results from runnning " + heuristicName + " on input file instance "+ filename+"\n";
            fileWriter.write(line);
            fileWriter.write("\n");

            line = "Instance,Initial_Obj,Best_Obj,Run_Time,Segments,Segment_length\n";

            fileWriter.write(line);

            line = filename+"_"+orderAmount+"_Ord_"+vehicleAmount+"_Veh_"+locationsAmount+"_Loc_"+","+initialObjective+","+bestObjective+","+runningTime+","+segmentAmount+","+(iterations/segmentAmount)+"\n";
            fileWriter.write(line);

            fileWriter.write("\n");

            line = "Best Solution found";

            for (int i = 0; i<bestSolution.length;i++){
                line+=","+bestSolution[i];
            }
            line+="\n"+"Iteration found:,"+bestIteration;

            fileWriter.write(line);
            fileWriter.write("\n\n");

            line = "Operator Times\nOperator,Operator Time, Operator Running Time, Average operator Running Time\n";
            fileWriter.write(line);

            for (int i = 0; i<operatorAmount; i++){
                line = operators.get(i).getName()+ "," +operatorTime[i]+","+operatorRunningTimes[i]+","+operatorTime[i]/operatorRunningTimes[i];
                fileWriter.write(line+"\n");
            }
            fileWriter.write("\n");

            line = "Segments";
            for (int i = 0; i<operatorWeight.length;i++){
                line+=","+(i+1);
            }
            line+="\n";
            fileWriter.write(line);

            writeOperatorTableHorizontal(operatorWeight, fileWriter, "weight");

            line = "\n";
            line += "Operator";
            for (int i = 0; i<scoreData[0].length; i++){
                line += ","+ operators.get(i).getName()+" score" ;
            }
            line+="\n";
            fileWriter.write(line);

            writeOperatorTableVertical(scoreData,fileWriter,"score");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <E> void writeOperatorTableVertical(E[][] table, FileWriter fileWriter, String dataType) throws IOException {
        String line;
        for(int i = 0; i<table.length;i++) {
            line = (i+1)+"";
            for (int j = 0; j < table[0].length; j++) {
                line += "," + table[i][j];
            }
            fileWriter.write(line + "\n");
        }
        fileWriter.write("\n");

    }

    private <E> void writeOperatorTableHorizontal(E[][] table, FileWriter fileWriter, String dataType) throws IOException {
        String line;
        for (int i = 0; i<table[0].length; i++){
            line = operators.get(i).getName()+" "+ dataType ;
            for (int j = 0; j<table.length;j++){
                line+="," +table[j][i];
            }
            fileWriter.write(line+"\n");
        }
        fileWriter.write("\n");
    }

    @Override
    public void addObjectData(IDataObject object){
        this.listOfData.add(object);
    }

    @Override
    public void setWeightData(Double[][] weightData){
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
}
