package reader;

import dataObjects.IDataSet;

public interface IReader {
    IDataSet readDataFromFile(String fileName) throws Exception;
}
