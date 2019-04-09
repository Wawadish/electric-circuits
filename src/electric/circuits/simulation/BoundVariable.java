package electric.circuits.simulation;

import electric.circuits.simulation.Variable;
import electric.circuits.component.ElectricComponent;

/**
 *
 * @author Tomer Moran
 */
public class BoundVariable extends Variable {

	public static boolean bind(ElectricComponent comp1, ElectricComponent comp2) {
		if (comp1 == comp2 || comp1 == null || comp2 == null) {
			return false;
		}

		boolean bound1 = (comp1.current() instanceof BoundVariable);
		boolean bound2 = (comp2.current() instanceof BoundVariable);

		// Neither variables are bound
		if (!bound1 && !bound2) {
			comp1.setCurrent(new BoundVariable(comp2));
			return true;
		}

		// Both variables are bound: bind their parents instead
		if (bound1 && bound2) {
			bind(((BoundVariable) comp1.current()).comp, ((BoundVariable) comp2.current()).comp);
		}

		// One variable is bound, the other is not
		// If the bound variable relies on unbound one, do nothing
		// Else, bind the unbound to the bound
		if (bound1) {
			BoundVariable b1 = (BoundVariable) comp1.current();
			if (b1.isBoundTo(comp2.current()))
				return false;
			
			comp2.setCurrent(new BoundVariable(comp1));
		} else {
			BoundVariable b2 = (BoundVariable) comp2.current();
			if (b2.isBoundTo(comp1.current()))
				return false;
			
			comp1.setCurrent(new BoundVariable(comp2));
		}
		
		return true;
	}

	private final ElectricComponent comp;

	public BoundVariable(ElectricComponent comp) {
		if (comp == null) {
			throw new NullPointerException();
		}

		this.comp = comp;
	}

	@Override
	public Variable parent() {
		Variable var = comp.current();
		return (var instanceof BoundVariable) ? ((BoundVariable) var).parent() : var;
	}

	/**
	 * Returns whether or not this {@code BoundVariable} is directly or
	 * indirectly bound to a given variable.
	 *
	 * @param var the variable to check for.
	 * @return {@code true} if {@code var} is among the parents of this
	 * {@code BoundVariable}; {@code false} otherwise.
	 */
	public boolean isBoundTo(Variable var) {
		Variable parent = comp.current();
		if (this == var || parent == var)
			return true;

		return (parent instanceof BoundVariable) ? ((BoundVariable) parent).isBoundTo(var) : false;
	}

	@Override
	public double get() {
		return comp.current().get();
	}

	@Override
	public boolean resolve() {
		return comp.current().resolve();
	}

	@Override
	public boolean isSolved() {
		return comp.current().isSolved();
	}

	@Override
	public void solve(double v) {
		comp.current().solve(v);
	}

}
