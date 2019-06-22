package generators;

public interface IInstanceGenerator {
    /**
     *
     * @param orderAmount
     * @param vehicleAmount
     * @param pickupLocationAmount
     * @param deliveryLocationAmount
     * @param factoryAmount
     * @param instanceID
     * @param instanceType is the type of instance, 1 = international (europe), 2= local (germany), 3 = uniform distribution
     */
    void makeInstance(int orderAmount, int vehicleAmount, int pickupLocationAmount, int deliveryLocationAmount, int factoryAmount, int instanceID, int instanceType);
}
