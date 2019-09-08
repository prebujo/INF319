package reader;

import dataObjects.IDataSet;

import java.io.FileNotFoundException;
import java.util.List;

public interface IReader {
    IDataSet readDataFromFile(String fileName) throws Exception;

    List<IDataSet> readDataFromFiles(String instance,List<String> instanceSizes)throws Exception;

    List<List<int[]>> readSolutionsFromFile(String instance, int i) throws FileNotFoundException;
}
