package functions;

import dataObjects.IData;

public class FactoryDockFeasible implements IFeasibility {

    private final int[] factory;
    private final int[] factoryStopCapacity;
    private final int vehicleAmount;
    private final int factoryAmount;

    public FactoryDockFeasible(IData dataSet) {
        this.vehicleAmount = dataSet.getVehicleAmount();
        this.factory = dataSet.getFactory();
        this.factoryAmount = dataSet.getFactoryAmount();
        this.factoryStopCapacity = dataSet.getFactoryStopCapacity();
    }

    @Override
    public boolean check(int[] solution) {
        int i = 0;
        for(int v = 0;v<vehicleAmount;v++) {
            int solutionElement = solution[i] - 1;
            int counter = 1;
            while(solutionElement!=-1) {
                if(i>0) {
                    int previousSolutionElement = solution[i - 1] - 1;
                    if (previousSolutionElement == -1) {
                        i++;
                        solutionElement = solution[i]-1;
                        continue;
                    }
                    if (factory[solutionElement] != factory[previousSolutionElement] || factory[solutionElement] == 0) {
                        counter = 1;
                    } else {
                        counter++;
                        if (counter > factoryStopCapacity[factory[solutionElement] - 1]) {
                            return false;
                        }
                    }
                }
                i++;
                solutionElement = solution[i]-1;
            }
            i++;
        }
        return true;
    }
}
