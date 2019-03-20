package main;

import dataObjects.IData;
import functions.FactoryDockFeasible;
import functions.IFeasibility;
import functions.ObjectiveFunction;
import functions.WeightAndVolumeFeasible;
import reader.IReader;
import reader.Reader;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();

        IReader reader = new Reader();

        //creates a dataset from file
        IData dataset = reader.readDataFromFile(fileName);

        System.out.println("Amount of Vehicles: "+dataset.getVehicleAmount());


        System.out.println("Amount of Orders: "+dataset.getOrderAmount());

        System.out.println("Amount of Factories: "+dataset.getFactoryAmount());

        System.out.println("Amount of stops: " +dataset.getStopAmount());

        System.out.println("Amount of weight Dimension: "+dataset.getWeightDimension()[0]);

        System.out.println("Amount of distance Dimension: "+dataset.getDistanceDimension()[0]);

        System.out.println("Factory Node 8: "+dataset.getFactory()[7]);
        System.out.println("Factory Node 2: "+dataset.getFactory()[1]);
        System.out.println("Factory Node 9: "+dataset.getFactory()[8]);
        System.out.println("Factory Node 10: "+dataset.getFactory()[9]);
        System.out.println("Factory Node 11: "+dataset.getFactory()[10]);

//        System.out.println("Factory Set 2: " + dataset.getFactorySet(1));
//        System.out.println("Factory Set 3: " + dataset.getFactorySet(2));

        System.out.println("Factory Stop Capacity 1: " + dataset.getFactoryStopCapacity()[0]);
//        System.out.println("Factory Stop Capacity 2: " + dataset.getFactoryStopCapacity()[1]);
//        System.out.println("Factory Stop Capacity 3: " + dataset.getFactoryStopCapacity()[2]);

        System.out.println("Location Node 2: "+dataset.getLocation()[1]);
//        System.out.println("Location Node 8: "+dataset.getLocation()[7]);
//        System.out.println("Location Node 9: "+dataset.getLocation()[8]);
//        System.out.println("Location Node 9: "+dataset.getLocation()[5]);

        System.out.println("Vehicle Node Collection: "+dataset.getVehicleNode());

        System.out.println("Vehicle Pickup Node Collection: "+dataset.getVehiclePickupNode());

        System.out.println("Vehicle 1 Starting Node: " + dataset.getVehicleStartingLocation()[0] + " and ending Node: " + dataset.getVehicleDestinationLocation()[0]);

//        System.out.println("Vehicle 2 Starting Node: " + dataset.getVehicleStartingLocation()[1] + " and ending Node: " + dataset.getVehicleDestinationLocation()[1]);

        System.out.println("Vehicle 1 Capacity: " + dataset.getVehicleWeightCapacity()[0]);
//        System.out.println("Vehicle 2 Capacity: " + dataset.getVehicleWeightCapacity()[1]);

        System.out.println("Order 1 Weight: " + dataset.getOrderWeight()[0]);
//        System.out.println("Order 3 Weight: " + dataset.getOrderWeight()[2]);

        System.out.println("Order 1 Penalty: " + dataset.getOrderPenalty()[0]);
//        System.out.println("Order 3 Penalty: " + dataset.getOrderPenalty()[2]);

        System.out.println("Distance interval 0: " + dataset.getDistanceInterval()[0][0]);
//        System.out.println("Distance interval 2: " + dataset.getDistanceInterval()[2]);

        System.out.println("Weight interval 0: " + dataset.getWeightInterval()[0][0]);
//        System.out.println("Weight interval 2: " + dataset.getWeightInterval()[2]);

        System.out.println("Cost per km 1 1 1: " + dataset.getKmCostMatrix()[0][0][0]);
//        System.out.println("Cost per km 2 1 2: " + dataset.getKmCostMatrix()[1][0][1]);

        System.out.println("Cost per kg 1 1 1: " + dataset.getKgCostMatrix()[0][0][0]);
//        System.out.println("Cost per kg 2 1 1: " + dataset.getKgCostMatrix()[1][0][0]);

        System.out.println("Cost fixed 1 1 1: " + dataset.getFixCostMatrix()[0][0][0]);
//        System.out.println("Cost fixed 2 1 2: " + dataset.getFixCostMatrix()[1][0][1]);

        System.out.println("Cost per stop 1 3 : " + dataset.getStopCostMatrix()[0][2]);
        System.out.println("Cost per stop 1 1: " + dataset.getStopCostMatrix()[0][0]);

        System.out.println("Amount of timewindows node 3: " + dataset.getTimeWindowAmount()[2]);
//        System.out.println("Amount of timewindows node 8: " + dataset.getTimeWindowAmount()[7]);

        System.out.println("lower TimeWindow 1 4 : " + dataset.getLowerTimeWindow()[0][3]);
        System.out.println("lower Timewindow 1 1: " + dataset.getLowerTimeWindow()[0][0]);

        System.out.println("upper TimeWindow 1 4 : " + dataset.getUpperTimeWindow()[0][3]);
        System.out.println("upper Timewindow 2 2: " + dataset.getUpperTimeWindow()[0][0]);

//        System.out.println("Travel Time 1 8 9 : " + dataset.getTravelTime()[0][7][8]);
        System.out.println("Travel Time 1 2 3 : " + dataset.getTravelTime()[0][0][2]);

//        System.out.println("Travel Distance 8 9 : " + dataset.getTravelDistance()[7][8]);
        System.out.println("Travel Distance 2 3 : " + dataset.getTravelDistance()[0][2]);

        //superSimple possible solution
//        int[] solutionRepresentation = {0,1,3};

        //simple possible solution
        int[] solutionRepresentation = {0,0,0,2,9,3,4,11,10,1,8};

        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataset);

        int solution = objectiveFunction.calculateSolution(solutionRepresentation);

        System.out.println("Solution:" + solution);

        IFeasibility feasible1 = new WeightAndVolumeFeasible(dataset);
        IFeasibility feasible2 = new FactoryDockFeasible(dataset);

        System.out.println("Solution is Weight and Volume feasible-> "+ feasible1.check(solutionRepresentation) );
        System.out.println("Solution is Factory Dock feasible-> "+ feasible2.check(solutionRepresentation) );


    }
}
