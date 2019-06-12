package dataObjects;

import java.util.HashSet;

public interface IDataResult {
    void printData();

    void printDataToFile(String filename);

    void addObjectData(IDataObject object);

    void setWeightData(Double[][] weightData);

    void setBestSolution(int[] solution);

    void setSolutions(HashSet<String> solutions);

    void setRunningTime(double runningTime);

    int[] getBestSolution();

    String getHeuristicName();

    void setInitialObjective(double currentObjective);

    void setBestObjective(double bestObjective);

    void setBestIteration(int bestIteration);

    void setScoreData(Double[][] scoreData);

    void setOperatorTime(double[] operatorTime);

    void setOperatorRunningTimes(int[] operatorRunningTimes);
}
