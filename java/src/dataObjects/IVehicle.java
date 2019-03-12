package dataObjects;

public interface IVehicle {

    Node getStart();

    void setStart(Node node);

    int getWeightLimit();

    int getVolumeLimit();

    void setWeightLimit(int weightLimit);

    void setVolumeLimit(int volumeLimit);

}
