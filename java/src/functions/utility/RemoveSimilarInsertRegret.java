package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.Random;

public class RemoveSimilarInsertRegret extends RemoveAndReinsert {
    public RemoveSimilarInsertRegret(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        //remove orders that are similar. first run Shaw, then remove those similar
        //build shaw with P for picked up by same car (0 or 1 if same). T time of pickup/delivery. d distance between each pickup/delivery node.
        //and if orders can be picked up from the same vehicles or not, add also if orders are deliverd in the same factories
        //test if similar weight will work or test if better with small weight (wi + wj), the smaller the more likely they will fit together
        //experiment with the size of the greek constants to be multiplied.

        //reinsert similar orders based on regret-k.
        return new int[0];
    }
}
