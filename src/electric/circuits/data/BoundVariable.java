package electric.circuits.data;

/**
 *
 * @author Tomer Moran
 */
public class BoundVariable extends Variable {

	public static void bind(ElectricComponent comp1, ElectricComponent comp2) {
		if (comp1 == comp2) {
			throw new IllegalArgumentException();
		}

		if (!(comp1.current() instanceof BoundVariable)) {
			comp1.setCurrent(new BoundVariable(comp2));
			return;
		}

		if (!(comp2.current() instanceof BoundVariable)) {
			comp2.setCurrent(new BoundVariable(comp1));
			return;
		}

		bind(((BoundVariable) comp1.current()).comp, ((BoundVariable) comp2.current()).comp);
	}

	private final ElectricComponent comp;

	public BoundVariable(ElectricComponent comp) {
		if (comp == null) {
			throw new NullPointerException();
		}

		this.comp = comp;
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
