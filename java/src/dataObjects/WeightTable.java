package dataObjects;

import java.util.ArrayList;
import java.util.List;

public class WeightTable implements IDataObject {
    private List<ArrayList<Double>> object;
    private String name = "Weights";

    @Override
    public Object getData() {
        return object;
    }

    @Override
    public void setData(Object object) {
        this.object = (List<ArrayList<Double>>)object;
    }

    public String getName(){
        return name;
    }
}
