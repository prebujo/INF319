package functions.utility;

public class ReturnSameSolution implements IOperator {
    private final String name;
    private String description;
    public ReturnSameSolution(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        return solution;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
