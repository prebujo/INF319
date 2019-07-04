package main;

import generators.IInstanceGenerator;
import generators.InstanceGenerator;

public class TestInstanceGeneratorMain {
    public static void main(String[] args) throws Throwable {

        IInstanceGenerator generator = new InstanceGenerator();
        generator.makeInstance(4,3,3,4, 3, 1, 1);

    }

}
