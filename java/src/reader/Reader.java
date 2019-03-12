package reader;

import dataObjects.DataSet;
import dataObjects.IData;

import java.io.FileReader;

class Reader implements IReader {
    @Override
    public IData readDataFromFile(String fileName) throws Exception {
        IData dataSet = new DataSet();

        FileReader fileReader = new FileReader("/home/preben/repo/master/java/res/"+fileName);

        fileReader.read();


        return dataSet;
    }
}
