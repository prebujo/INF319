package dataObjects;

public class IntegerDouble {
    public int key;
    public double value;

    public IntegerDouble(Integer key, Double value){
        this.key = key;
        this.value = value;
    }

    public Double getNegativeValue(){
        return -value;
    }
    public Double getValue() {return value;}

}
