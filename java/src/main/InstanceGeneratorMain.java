package main;

import generators.IInstanceGenerator;
import generators.InstanceGenerator;

public class InstanceGeneratorMain {
    public static void main(String[] args) throws Throwable {

        IInstanceGenerator generator = new InstanceGenerator();
        int orderAmount = 12;
        int vehicleAmount = 7;
        int pickupLocationAmount = 4;
        int deliveryLocationAmount = 5;
        int factoryAmount = 4;
        generator.makeInstance(orderAmount, vehicleAmount, pickupLocationAmount, deliveryLocationAmount, factoryAmount, 4, 2);

    }

}
