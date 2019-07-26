package functions.utility;

public class ReturnSameSolution implements IOperator {
    private final String name;
    private String description;
    public ReturnSameSolution(String name){
        this.name = name;
        this.description = "return the same solution";
    }

    @Override
    public int[] apply(int[] solution) throws Throwable {
        return solution.clone();
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
