package electric.circuits.data;

import electric.circuits.Utils;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a {@code Variable} that is the result of the linear combination of
 * a set of other {@code Variable}s. Each underlying {@code Variable} has a
 * coefficient used to determine the value of this {@code CombinedVariable}. The
 * {@link #resolve} method will attempt to compute the result by resolving the
 * dependencies. Note that it is not possible to set manually the value of a
 * {@code CombinedVariable}.
 *
 * @author Tomer Moran
 */
@Deprecated
public class CombinedVariable extends Variable {

	/**
	 * A map containing the dependencies and their coefficients.
	 */
	private final Map<Variable, Double> bindings;

	public CombinedVariable() {
		this.bindings = new IdentityHashMap<>();
	}

	/**
	 * Adds a given variable with a given coefficient to this linear
	 * combination. If the variable is already present, the coefficients are
	 * added.
	 *
	 * @param var the {@code Varible} to add. May not be {@code null} or equal
	 * to {@code this}.
	 * @param coefficient the coefficient to use. May not be zero.
	 */
	public void add(Variable var, Double coefficient) {
		if (var == this || var == null || Utils.equals(coefficient, 0)) {
			throw new IllegalArgumentException();
		}

		// Do not allow nested CombinedVariables
		if (var instanceof CombinedVariable) {
			((CombinedVariable) var).bindings.forEach(this::add);
			return;
		}

		// Add the coefficients in the map
		bindings.merge(var, coefficient, Double::sum);
	}

	@Override
	public boolean resolve() {
		if (isSolved()) {
			return true;
		}

		double result = 0;

		// Iterate over all the dependencies
		for (Entry<Variable, Double> entry : bindings.entrySet()) {
			Variable var = entry.getKey();

			// If the dependency is not yet solved, try to solve it.
			if (!var.resolve()) {
				return false;
			}

			// Add the result 
			result += (entry.getValue() * var.get());
		}

		// Mark as solved
		solve(result);
		return true;
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
