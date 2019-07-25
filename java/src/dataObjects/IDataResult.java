package dataObjects;

import java.util.HashSet;

public interface IDataResult {
    void printData();

    void addObjectData(IDataObject object);

    void setOperatorWeightData(Double[][][] weightData);

    void setBestSolution(int[] solution);

    void setSolutions(HashSet<String> solutions);

    void setRunningTime(double runningTime);

    int[] getBestSolution();

    String getHeuristicName();

    void setNoTransportObjective(double currentObjective);

    void setBestObjective(double bestObjective);

    void setBestIterations(int[] bestIterations);

    void setScoreData(Double[][][] scoreData);

    void setOperatorTime(double[] operatorTime);

    void setOperatorRunningTimes(int[] operatorRunningTimes);

    double getNoTransportObjective();

    double getBestObjective();

    double getRunningTime();

    int[] getBestIterations();

    double[] getOperatorTime();

    int[] getOperatorRunningTimes();

    Double[][][] getOperatorWeightData();

    Double[][][] getScoreData();

    String getName();

    double[] getBestSolutions();

    void setBestSolutions(double[] bestSolutions);

    double getInitialSolutionAverageObjective();

    double getInitialSolutionRunningTime();

    double getInitialSolutionAverageImprovement();

    double getAverageObjective();

    double getAverageImprovement();

    double getBestImprovement();

    void setInitialSolutionAverageObjective(double initialSolutionAverageObjective);

    void setInitialSolutionRunningTime(double initialSolutionRunningTime);

    void setInitialSolutionAverageImprovement(double initialSolutionAverageImprovement);

    void setAverageObjective(double averageObjective);

    void setAverageImprovement(double averageImprovement);

    void setBestImprovement(double bestImprovement);

    double[] getBestObjectives();

    void setBestObjectives(double[] bestObjectives);
}
