package dataObjects;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerDouble that = (IntegerDouble) o;
        return key == that.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
