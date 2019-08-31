package dataObjects;

public class BitStringAndDataResult {
    private String bitString;
    private IDataResult dataResult;
    public BitStringAndDataResult(String bitString, IDataResult dataResult){
        this.bitString=bitString;
        this.dataResult=dataResult;
    }

    public String getBitString() {
        return bitString;
    }

    public IDataResult getDataResult() {
        return dataResult;
    }
}
