package printer;

import dataObjects.BitStringAndDataResult;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.utility.IOperator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Printer implements IPrinter {

    public void printDataToFile(String filename, IDataSet dataSet, IDataResult dataResult, List<IOperator> operators) {
        try {
            FileWriter fileWriter = new FileWriter("res/"+filename+"_RESULTS.csv");
            String line = "Results from runnning " + dataResult.getHeuristicName() + " on input file instance "+ filename+"\n";
            fileWriter.write(line);
            fileWriter.write("\n");

            line = "Instance,Initial_Obj,Best_Obj,Run_Time\n";

            fileWriter.write(line);

            line = filename+"_"+dataSet.getOrderAmount()+"_Ord_"+dataSet.getVehicleAmount()+"_Veh_"+dataSet.getLocationsAmount()+"_Loc_"+","+dataResult.getNoTransportObjective()+","+dataResult.getBestObjective()+","+dataResult.getRunningTime()+"\n";
            fileWriter.write(line);

            fileWriter.write("\n");

            line = "Best Solution found";

            int[] bestSolution = dataResult.getBestSolution();
            for (int i = 0; i< bestSolution.length; i++){
                line+=","+bestSolution[i];
            }
            line+="\n"+"Iteration found:,"+dataResult.getBestIterations();

            fileWriter.write(line);
            fileWriter.write("\n\n");

            line = "Operator,Operator Time, Operator Running Time, Average operator Running Time\n";
            fileWriter.write(line);



            double[] operatorTime = dataResult.getOperatorTime();
            int[] operatorRunningTimes = dataResult.getOperatorRunningTimes();
            for (int i = 0; i<operators.size(); i++){
                line = operators.get(i).getName()+ "," +operatorTime[i]+","+operatorRunningTimes[i]+","+operatorTime[i]/operatorRunningTimes[i];
                fileWriter.write(line+"\n");
            }
            fileWriter.write("\n");

            Double[][][] operatorWeightData = dataResult.getOperatorWeightData();
            line = "Segments";
            for (int i = 0; i<operatorWeightData[0].length;i++){
                line+=","+(i+1);
            }
            line+="\n";
            fileWriter.write(line);

            writeOperatorTableHorizontal(operatorWeightData[0], fileWriter, "weight", operators);

            Double[][][] scoreData = dataResult.getScoreData();

            line = "\n";
            line += "Operator";
            for (int i = 0; i<scoreData[0][0].length; i++){
                line += ","+ operators.get(i).getName()+" score" ;
            }
            line+="\n";
            fileWriter.write(line);

            writeOperatorTableVertical(scoreData[0],fileWriter,"score");

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printDataToFile(String outputFileName, List<String> instanceSizes, List<IDataResult> results, List<IOperator> operators) {
        try {

            FileWriter fileWriter = new FileWriter("res/results/"+outputFileName+"RESULTS.csv");

            String line = ",Best objective found \n";
            fileWriter.write(line);

            line = "Instance,Run 1,Run 2, Run 3, Run 4, Run 5, Run 6, Run 7, Run 8, Run 9, Run 10 \n";
            fileWriter.write(line);

            //printing each run's results for each instance
            for (int i = 0; i < results.size(); i++) {
                fileWriter.write(instanceSizes.get(i));
                double[] bestObjectives = results.get(i).getBestObjectives();
                for (int j = 0; j < bestObjectives.length; j++) {
                    fileWriter.write(","+bestObjectives[j]);
                }
                fileWriter.write("\n");
            }
            fileWriter.write("\n");

            fileWriter.write("Best solution iteration,Run 1,Run 2, Run 3, Run 4, Run 5, Run 6, Run 7, Run 8, Run 9, Run 10\n");
            for (int i = 0; i < results.size(); i++) {
                fileWriter.write(instanceSizes.get(i));
                int[] bestIterations = results.get(i).getBestIterations();
                for (int k = 0; k < bestIterations.length; k++) {
                    fileWriter.write( ","+ bestIterations[k]);
                }
                fileWriter.write("\n");
            }
            fileWriter.write("\n");

            fileWriter.write("Instance, No transport Objective, Initial Solution Average Objective, Initial Solution Average Imporvement, Average Objective, Average Improvement, Best Objective, Best Improvement, Initial Solution Average Run Time, Average Total Run Time \n");
            for (int i = 0; i < results.size(); i++) {
                fileWriter.write(instanceSizes.get(i)+",");
                IDataResult result = results.get(i);
                fileWriter.write(result.getNoTransportObjective()+","+result.getInitialSolutionAverageObjective()+","+result.getInitialSolutionAverageImprovement()+","+result.getAverageObjective()+","+result.getAverageImprovement()+","+result.getBestObjective()+","+result.getBestImprovement()+","+result.getInitialSolutionRunningTime()+","+result.getRunningTime()+"\n");
            }
            fileWriter.write("\n");

            fileWriter.write("Operator");
            for (int i = 0; i < instanceSizes.size(); i++) {
                fileWriter.write(","+instanceSizes.get(i)+" Average Running Time,"+instanceSizes.get(i)+" Average Times Rum");
            }
            fileWriter.write("\n");

            for (int i = 0; i < operators.size(); i++) {
                fileWriter.write(operators.get(i).getName());
                for (int j = 0; j < instanceSizes.size(); j++) {
                    int operatorRunningTime = results.get(j).getOperatorRunningTimes()[i];
                    fileWriter.write(","+results.get(j).getOperatorTime()[i]/ operatorRunningTime +","+operatorRunningTime);
                }
                fileWriter.write("\n");
            }

            fileWriter.write("\n");

            int run = 1;
            for (int k = 0; k < instanceSizes.size(); k++) {
                fileWriter.write("Weights per segments Run " + run +" "+instanceSizes.get(k));
                for (int i = 0; i < 100; i++) {
                    fileWriter.write("," + (i+1));
                }
                fileWriter.write("\n");
                for (int i = 0; i < operators.size(); i++) {
                    fileWriter.write(operators.get(i).getName());
                    for (int j = 0; j < 100; j++) {
                        Double[][][] operatorWeights = results.get(k).getOperatorWeightData();
                        fileWriter.write("," + operatorWeights[run-1][j][i]);
                    }
                    fileWriter.write("\n");
                }
                fileWriter.write("\n");
                run+=2;
            }


            fileWriter.write("Operator Score per Iteration\n");
            run = 1;
            for (int i = 0; i < instanceSizes.size(); i++) {
                fileWriter.write(","+instanceSizes.get(i)+" Run "+run+",,,,");
                run+=2;
            }
            fileWriter.write("\n");

            fileWriter.write("Iteration");
            for (int i = 0; i < instanceSizes.size(); i++) {
                for (int j = 0; j < operators.size(); j++) {
                    fileWriter.write(","+operators.get(j).getName());
                }
            }
            fileWriter.write("\n");

            for (int k = 0; k < 10000; k++) {
                fileWriter.write(""+(k+1));
                run = 1;
                for (int j = 0; j < instanceSizes.size(); j++) {
                    Double[][][] scoreData = results.get(j).getScoreData();
                    for (int i = 0; i < operators.size(); i++) {
                        fileWriter.write("," + scoreData[(run-1)][k][i]);
                    }
                    run+=2;
                }
                fileWriter.write("\n");
            }





            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printOperatorDataToFile(String outputFileName, List<BitStringAndDataResult> results, List<IOperator> operators) {
        try {
            FileWriter fileWriter = new FileWriter("res/results/"+outputFileName+"_OPERATOR_ANALYSIS.csv");

            String line = ",Operator Analysis for "+outputFileName+" \n\n";
            fileWriter.write(line);
            line = "Bit Strings represent use of the following operators in order: \n";
            fileWriter.write(line);
            line="";
            for (IOperator operator:operators) {
                line+=","+operator.getName();
            }
            line+="\n\n";
            fileWriter.write(line);

            fileWriter.write("Operators (1=used), Initial Objective, Average Objective,Best Objective,Best Realtive to Average,Average Run Time\n");

            line = "";
            for (BitStringAndDataResult bitAndResult : results) {
                IDataResult dataResult = bitAndResult.getDataResult();
                double averageObjective=0.0;
                double bestObjective=0.0;
                double noTransportObjective = 0.0;
                double runningTime = 0.0;
                if (dataResult!=null) {
                    averageObjective  = dataResult.getAverageObjective();
                    bestObjective = dataResult.getBestObjective();
                    noTransportObjective = dataResult.getNoTransportObjective();
                    runningTime = dataResult.getRunningTime();
                }
                line=bitAndResult.getBitString()+","+ noTransportObjective +","+ averageObjective +","+ bestObjective +","+((averageObjective - bestObjective)/ averageObjective) +","+ runningTime +"\n";
                fileWriter.write(line);
            }

            fileWriter.write("\n");

            for (IOperator operator :
                    operators) {
                fileWriter.write(operator.getName() + "," + operator.getDescription()+"\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void printData(IDataResult result) {
        //TODO: printer to console.. possibly not necessary.

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

    private <E> void writeOperatorTableHorizontal(E[][] table, FileWriter fileWriter, String dataType, List<IOperator> operators) throws IOException {
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
}
