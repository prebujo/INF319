package functions.utility;

import dataObjects.IDataSet;
import functions.feasibility.IFeasibility;

import java.util.HashSet;
import java.util.Random;

public class RemoveAndReinsertRandom extends RemoveAndReinsert {

    public RemoveAndReinsertRandom(String name, int lowerLimit, int upperLimit, Random random, IFeasibility feasibilityCheck, IDataSet dataSet) {
        super(name, lowerLimit, upperLimit, random, feasibilityCheck, dataSet);
        this.description = "remove and reinsert operator that removes between 2-4 random elements from solution and reinserts them in randomly selected vehicles";
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
