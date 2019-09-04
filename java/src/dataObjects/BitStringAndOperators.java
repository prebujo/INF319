package dataObjects;

import functions.utility.IOperator;

import java.util.List;

public class BitStringAndOperators {
    private List<IOperator> operators;
    private String bitString;
    public BitStringAndOperators(String bitString, List<IOperator> operators){
        this.bitString = bitString;
        this.operators = operators;
    }

    public String getBitString() {
        return bitString;
    }

    public List<IOperator> getOperators() {
        return operators;
    }
}
