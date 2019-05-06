package Heuristic;

import dataObjects.IDataResult;

public interface IHeuristic {
    IDataResult optimize() throws Throwable;
}
