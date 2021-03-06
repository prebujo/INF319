package heuristic;

import dataObjects.IDataResult;

public interface IHeuristic {
    IDataResult optimize() throws Throwable;
    String getName();
}
