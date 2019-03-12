package main;

import dataObjects.IData;
import functions.ObjectiveFunction;
import reader.IReader;
import reader.Reader;

import java.sql.SQLOutput;
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

        System.out.println("Amount of weight Dimension: "+dataset.getWeightDimension());

        System.out.println("Amount of distance Dimension: "+dataset.getDistanceDimension());

        System.out.println("Factory Node 8: "+dataset.getFactories()[7]);
        System.out.println("Factory Node 3: "+dataset.getFactories()[2]);
        System.out.println("Factory Node 9: "+dataset.getFactories()[8]);
        System.out.println("Factory Node 10: "+dataset.getFactories()[9]);

//        System.out.println("Factory Set 2: " + dataset.getFactorySet(1));
//        System.out.println("Factory Set 3: " + dataset.getFactorySet(2));

        System.out.println("Factory Stop Capacity 1: " + dataset.getFactoryStopCapacities()[0]);
        System.out.println("Factory Stop Capacity 2: " + dataset.getFactoryStopCapacities()[1]);
        System.out.println("Factory Stop Capacity 3: " + dataset.getFactoryStopCapacities()[2]);

        System.out.println("Location Collection: "+dataset.getLocations());

        System.out.println("Vehicle Node Collection: "+dataset.getVehicleNodes());

        System.out.println("Vehicle Pickup Node Collection: "+dataset.getVehiclePickupNodes());

        System.out.println("Vehicle 1 Starting Node: " + dataset.getVehicleStartingLocations()[0] + " and ending Node: " + dataset.getVehicleDestinationLocations()[0]);

        System.out.println("Vehicle 2 Starting Node: " + dataset.getVehicleStartingLocations()[1] + " and ending Node: " + dataset.getVehicleDestinationLocations()[1]);

        System.out.println("Vehicle 1 Capacity: " + dataset.getVehicleCapacities()[0]);
        System.out.println("Vehicle 2 Capacity: " + dataset.getVehicleCapacities()[1]);

        System.out.println("Order 1 Weight: " + dataset.getOrderWeights()[0]);
        System.out.println("Order 3 Weight: " + dataset.getOrderWeights()[2]);

        System.out.println("Order 1 Penalty: " + dataset.getOrderPenalties()[0]);
        System.out.println("Order 3 Penalty: " + dataset.getOrderPenalties()[2]);

        System.out.println("Distance interval 0: " + dataset.getDistanceIntervals()[0]);
        System.out.println("Distance interval 2: " + dataset.getDistanceIntervals()[2]);

        System.out.println("Weight interval 0: " + dataset.getWeightIntervals()[0]);
        System.out.println("Weight interval 2: " + dataset.getWeightIntervals()[2]);

        System.out.println("Cost per km 1 2 2: " + dataset.getKmCostMatrix()[0][1][1]);
        System.out.println("Cost per km 2 1 2: " + dataset.getKmCostMatrix()[1][0][1]);

        System.out.println("Cost per kg 1 2 1: " + dataset.getKgCostMatrix()[0][1][0]);
        System.out.println("Cost per kg 2 1 1: " + dataset.getKgCostMatrix()[1][0][0]);

        System.out.println("Cost fixed 1 2 2: " + dataset.getFixCostMatrix()[0][1][1]);
        System.out.println("Cost fixed 2 1 2: " + dataset.getFixCostMatrix()[1][0][1]);

        System.out.println("Cost per stop 1 4 : " + dataset.getStopCostMatrix()[0][3]);
        System.out.println("Cost per stop 2 2: " + dataset.getStopCostMatrix()[1][1]);

        System.out.println("Amount of timewindows node 3: " + dataset.getTimeWindowAmounts()[2]);
        System.out.println("Amount of timewindows node 8: " + dataset.getTimeWindowAmounts()[7]);

        System.out.println("lower TimeWindow 1 4 : " + dataset.getLowerTimeWindows()[0][3]);
        System.out.println("lower Timewindow 2 2: " + dataset.getLowerTimeWindows()[1][1]);

        System.out.println("upper TimeWindow 1 4 : " + dataset.getUpperTimeWindows()[0][3]);
        System.out.println("upper Timewindow 2 2: " + dataset.getUpperTimeWindows()[1][1]);

        System.out.println("Travel Time 1 8 9 : " + dataset.getTravelTime()[0][7][8]);
        System.out.println("Travel Time 3 2 3 : " + dataset.getTravelTime()[2][1][2]);

        System.out.println("Travel Distance 8 9 : " + dataset.getTravelDistance()[7][8]);
        System.out.println("Travel Distance 2 3 : " + dataset.getTravelDistance()[1][2]);

        int[] solutionRepresentation = {1,2,9,8,0,4,11,0,3,10,0};

//        ObjectiveFunction objectiveFunction = new ObjectiveFunction(dataset,solutionRepresentation);
//
//        int solution = objectiveFunction.calculateSolution();







    }
}
