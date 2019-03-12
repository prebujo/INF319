package reader;

import dataObjects.IData;

public interface IReader {
    IData readDataFromFile(String fileName) throws Exception;
}
