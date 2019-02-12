package electric.circuits.data;

/**
 *
 * @author Tomer Moran
 */
public class ScaledVariable extends Variable {

    private final Variable var;
    private final double coefficient;

    public ScaledVariable(Variable var, double coefficient) {
	this.var = var;
	this.coefficient = coefficient;
    }

    @Override
    public double get() {
	return coefficient * var.get();
    }

    @Override
    public boolean resolve() {
	return isSolved() || var.resolve();
    }

}
