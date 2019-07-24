package main;

import generators.IInstanceGenerator;
import generators.InstanceGenerator;

public class TestInstanceGeneratorMain {
    public static void main(String[] args) throws Throwable {

        IInstanceGenerator generator = new InstanceGenerator();
        for (int i = 0; i < 5; i++) {
            if(i<2) {
                generator.makeInstance(9, 5, 3, 3, 2, i + 1, 1);
            } else if (i<4){
                generator.makeInstance(9, 5, 3, 3, 2, i + 1, 2);
            } else {
                generator.makeInstance(9, 5, 3, 3, 2, i + 1, 3);
            }
        }

    }

}
