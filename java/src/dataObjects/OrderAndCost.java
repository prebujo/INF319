package dataObjects;

public class OrderAndCost {
    public int order;
    public double cost;

    public OrderAndCost(Integer order, Double cost){
        this.order = order;
        this.cost = cost;
    }

    public Double getCost(){
        return -cost;
    }

}
