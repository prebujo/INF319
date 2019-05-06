package dataObjects;

import java.util.HashSet;

public interface IDataResult {
    void printData();

    void printDataToFile(String filename);

    void addObjectData(IDataObject object);

    void setWeightData(double[][] weightData);

    void setBestSolution(int[] solution);

    void setSolutions(HashSet<String> solutions);

    int[] getBestSolution();
}
