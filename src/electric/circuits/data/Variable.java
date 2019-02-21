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

	/**
	 * Stores whether or not this {@code Variable} is solved. If that is the
	 * case, it is possible to fetch its value using {@link #get}.
	 */
	private boolean solved;

	/**
	 * Stores the numerical value of this {@code Variable}.
	 */
	private final SimpleDoubleProperty value;

	public Variable() {
		this.value = new SimpleDoubleProperty();
	}

	/**
	 * Returns the value of this {@code Variable} <b>if, and only if, it is
	 * declared as solved.</b> If that is not the case, an exception is thrown.
	 *
	 * @return the value of this {@code Variable} if solved.
	 * @throws IllegalStateException if this {@code Variable} is marked as
	 * unsolved.
	 */
	public double get() {
		if (isSolved()) {
			return value.get();
		}

		throw new IllegalStateException();
	}

	/**
	 * @return {@code true} if this {@code Variable} is marked as solved,
	 * {@code false} otherwise.
	 */
	public boolean isSolved() {
		return solved;
	}

	/**
	 * Marks this {@code Variable} as solved, and assigns it the given numerical
	 * value. If this {@code Variable} is already solved, this method throws an exception.
	 *
	 * @param v the value to set.
	 * @throws IllegalStateException if already solved.
	 */
	public void solve(double v) {
		if (isSolved()) {
			throw new IllegalStateException();
		}

		solved = true;
		value.set(v);
	}

	/**
	 * Attempts to resolve the value of this {@code Variable}. By default, does
	 * nothing. Subclasses are free to override this method to specify a
	 * resolving method. The method then returns whether or not this was
	 * successful (default return value is {@code false}).
	 *
	 * @return {@code true} if the solving was successful (i.e. this
	 * {@code Variable} is now solved); {@code false} otherwise.
	 */
	public boolean resolve() {
		return false;
	}
}
