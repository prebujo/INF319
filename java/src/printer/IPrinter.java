package printer;

import dataObjects.BitStringAndDataResult;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import functions.utility.IOperator;

import java.util.List;

public interface IPrinter {

    void printDataToFile(String filename, IDataSet dataSet, IDataResult result, List<IOperator> operators);

    void printDataToFile(String outputFilename, List<String> fileNames, List<IDataResult> result, List<IOperator> operators);

    void printOperatorDataToFile(String outputFileName, List<BitStringAndDataResult> results, List<IOperator> operators);

    void printData(IDataResult result);

    void printSolutionsToFile(String ouputFileName,List<List<int[]>> feasibleSolutionsList);
}
