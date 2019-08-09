package dataObjects;

public class OrderAndSimilarity {
    public int order;
    public Double similarity;
    public OrderAndSimilarity(int order, Double similarity){
        this.order=order;
        this.similarity=similarity;
    }
    public Double getSimilarity(){
        return similarity;
    }
}
