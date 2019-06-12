package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.HashSet;
import java.util.Random;

public class RemoveAndReinsertRandom extends RemoveAndReinsert {

    public RemoveAndReinsertRandom(IDataSet dataSet, Random random, IFeasibility feasibilityCheck, int lowerLimit, int upperLimit, String name, String description) {
        super(dataSet, random, feasibilityCheck, lowerLimit, upperLimit, name, description);
    }

    @Override
    protected HashSet<Integer> getElementsToRemove(int amountOfElements, int[] solution){
        HashSet<Integer> result = new HashSet<>(amountOfElements);

        while (amountOfElements>0){
            int choice = random.nextInt(orderAmount)+1;
            while(result.contains(choice)){
                choice = random.nextInt(orderAmount)+1;
            }
            result.add(choice);
            amountOfElements--;
        }

        return result;
    }
}
