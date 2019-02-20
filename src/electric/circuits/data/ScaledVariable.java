package electric.circuits.data;

/**
 * Represents a {@code Variable} that depends on another {@code Variable} and on
 * a scaling coefficient. The {@link #resolve} method checks if the underlying
 * variable is solved. The {@link #get} method returns the value of the
 * underlying variable multiplied by the scaling factor. Note that it is not
 * possible to directly solve {@code ScaledVariable}s; calling
 * {@link #solve(double)} will result in an exception.
 *
 * @author Tomer Moran
 */
public class ScaledVariable extends Variable {

	/**
	 * The underlying variable this {@code ScaledVariable} depends on.
	 */
	private final Variable var;

	/**
	 * The scaling coefficient used.
	 */
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

	@Override
	public boolean isSolved() {
		return var.isSolved();
	}

	@Override
	/**
	 * Throws {@link UnsupportedOperationException}. See class documentation for
	 * more details.
	 */
	public void solve(double v) {
		throw new UnsupportedOperationException();
	}

}
