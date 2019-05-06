package main;

import Heuristic.AdaptiveLargeNeighbourhoodSearch;
import Heuristic.IHeuristic;
import dataObjects.IDataResult;
import dataObjects.IDataSet;
import reader.IReader;
import reader.Reader;

import java.util.Random;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();

        IReader reader = new Reader();

        //creates a dataset from file
        IDataSet dataset = reader.readDataFromFile(fileName);

        Random random = new Random(1);

        System.out.println("Optimizing with ALNS");
        IHeuristic alns = new AdaptiveLargeNeighbourhoodSearch(dataset, random);
        IDataResult dataResults = alns.optimize();

        dataResults.printData();
        dataResults.printDataToFile("alns_results.txt");

    }
}
