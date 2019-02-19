package electric.circuits.data;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Represents an unknown double-valued variable. This class is used as a
 * placeholder for the current and/or voltage of a component before the actual
 * values are known, and also to store the values once they are.
 * 
 * As such, a {@code Variable} is at all times either unknown, or solved.
 *
 * @author Tomer Moran
 */
public class Variable {

	private boolean solved;
	private final SimpleDoubleProperty value;

	public Variable() {
		this.value = new SimpleDoubleProperty();
	}

	public double get() {
		if (solved) {
			return value.get();
		}

		throw new IllegalStateException();
	}

	public boolean isSolved() {
		return solved;
	}

	public void solve(double v) {
		if (solved) {
			solved = true;
		}
		value.set(v);
	}

	public boolean resolve() {
		return false;
	}
}
