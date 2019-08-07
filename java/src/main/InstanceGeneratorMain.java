package main;

import generators.IInstanceGenerator;
import generators.InstanceGenerator;

public class InstanceGeneratorMain {
    public static void main(String[] args) throws Throwable {

        IInstanceGenerator generator = new InstanceGenerator();
        int orderAmount = 10;
        int vehicleAmount = 6;
        int pickupLocationAmount = 3;
        int deliveryLocationAmount = 4;
        int factoryAmount = 3;
        for (int i = 0; i < 5; i++) {
            if(i< 2) {
                generator.makeInstance(orderAmount, vehicleAmount, pickupLocationAmount, deliveryLocationAmount, factoryAmount, i + 1, 1);
            } else if (i< 4){
                generator.makeInstance(orderAmount, vehicleAmount, pickupLocationAmount, deliveryLocationAmount, factoryAmount, i + 1, 2);
            } else {
                generator.makeInstance(orderAmount, vehicleAmount, pickupLocationAmount, deliveryLocationAmount, factoryAmount, i + 1, 3);
            }
        }

    }

}
