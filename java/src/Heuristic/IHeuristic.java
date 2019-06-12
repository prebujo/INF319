package Heuristic;

import dataObjects.IDataResult;
import functions.utility.IOperator;

import java.util.List;

public interface IHeuristic {
    IDataResult optimize() throws Throwable;
    String getName();
}
