package writer;

import dataObjects.IDataResult;

public class Printer implements IPrinter{
    @Override
    public void printDataToFile(IDataResult dataResult, String inputFileName) {
        System.out.println("printing data from running " + dataResult.getHeuristicName()+"on data from file " + inputFileName+" .....");

 


    }
}
