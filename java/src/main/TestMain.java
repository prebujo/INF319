package main;

import dataObjects.IDataSet;
import reader.FlowReader;
import reader.IReader;

import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) throws Throwable {
        System.out.println("Welcome to Preben's PDP solver! Please enter the name of the file you would like to read: ");
        //Scanner scan = new Scanner(System.in);
        //String fileName = scan.nextLine();

        IReader reader = new FlowReader();

        //creates a dataset from file
        IDataSet dataset = reader.readDataFromFile("AT-DE_W");



    }
}
