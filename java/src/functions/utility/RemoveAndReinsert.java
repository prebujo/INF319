package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.*;

/**
 * Operator that removes randomly and tries to reinsert given amount of orders in a given solution.The removal operator selects random orders to getElementsToRemove
 * and the insertion operator reinserts the removed orders into a randomly selected vehicle if possible.
 */
public class RemoveAndReinsert extends Operator {

    protected int lowerLimit;
    protected int upperLimit;

    public RemoveAndReinsert(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet){
        super(name,random,feasibilityCheck,dataSet);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }
}
