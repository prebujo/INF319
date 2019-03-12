package functions;

import dataObjects.DataSet;
import dataObjects.IData;

import java.util.Collection;
import java.util.List;

public class ObjectiveFunction {
    private IData dataSet;
    private int[] solution;
    public ObjectiveFunction(IData dataSet, int[] solution){
        this.dataSet = dataSet;
        this.solution = solution;
    }

    public int calculateSolution(int[] solution) {
        this.solution = solution;
        return calculateSolution();
    }

    public int calculateSolution() {
        int result=0;

        int vehicles = dataSet.getVehicleAmount();
        int orders = dataSet.getOrderAmount();
        int stops = dataSet.getStopAmount();
        int[] vehicleStartingLocations = dataSet.getVehicleStartingLocations();
        int[] vehicleDestinationLocations = dataSet.getVehicleDestinationLocations();
        int[][] stopCostMatrix = dataSet.getStopCostMatrix();
        int[][] travelDistance = dataSet.getTravelDistance();
        int[] orderWeights = dataSet.getOrderWeights();
        List<Collection<Integer>> locations = dataSet.getLocations();


        int i = 0;
        for (int v = 0; v < vehicles+1; v++) {
            int vehicleCost = 0;
            int solutionElement = 0;
            int vehicleTotalDistance = 0;
            int vehicleWeight = 0;
            int vehicleMaxWeight = 0;
            int vehiclePosition = vehicleStartingLocations[v];
            int currentStop = 0;
            boolean[] visited = new boolean[orders];
            boolean[] visitedStop = new boolean[stops];
            while (i < solution.length) {
                solutionElement = solution[i];
                if (solutionElement == 0) {
                    i++;
                    break;
                }


                currentStop = 1;
                if(!visitedStop[currentStop])
                vehicleCost += stopCostMatrix[v][solutionElement];
                vehicleTotalDistance += travelDistance[vehiclePosition][solutionElement];

                if(!visited[solutionElement]) {
                    visited[solutionElement] = true;
                    vehicleCost += stopCostMatrix[v][solutionElement];
                    vehicleTotalDistance += travelDistance[vehiclePosition][solutionElement];

                    //TODO: continue this method also with weight
                    vehicleWeight += orderWeights[solutionElement];
                    if(vehicleWeight > vehicleMaxWeight) {
                        vehicleMaxWeight = vehicleWeight;
                    }
                }
                else{

                }



            }
            if(solutionElement == 0){
                result += vehicleCost;
                continue;
            }

        }
        return result;
    }
}
