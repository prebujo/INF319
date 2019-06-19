package generators;

public interface IInstanceGenerator {
    /**
     *
     * @param orderAmount
     * @param vehicleAmount
     * @param locationAmount
     * @param instanceType is the type of instance, 1 = international (europe), 2= local (germany), 3 = uniform distribution
     */
    void makeInstance(int orderAmount, int vehicleAmount, int locationAmount, int instanceType);
}
