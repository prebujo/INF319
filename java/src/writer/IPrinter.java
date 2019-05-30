package writer;

import dataObjects.IDataResult;

public interface IPrinter {
    void printDataToFile(IDataResult dataResult, String inputFileName);
}
