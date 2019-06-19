package printer;

import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.utility.IOperator;

import java.util.List;

public interface IPrinter {

    void printDataToFile(String filename, IDataSet dataSet, IDataResult result, List<IOperator> operators);

    void printDataListToFile(String filename, List<IDataResult> result);

    void printData(IDataResult result);

}
