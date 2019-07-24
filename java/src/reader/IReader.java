package reader;

import dataObjects.IDataSet;

import java.util.List;

public interface IReader {
    IDataSet readDataFromFile(String fileName) throws Exception;

    List<IDataSet> readDataFromFiles(String instance,List<String> instanceSizes)throws Exception;
}
