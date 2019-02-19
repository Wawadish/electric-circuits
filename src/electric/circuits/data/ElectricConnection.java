package electric.circuits.data;

/**
 *
 * @author Tomer Moran
 */
public class ElectricConnection {

	private final boolean left;
	private final ElectricComponent component;

	public ElectricConnection(ElectricComponent component, boolean left) {
		this.component = component;
		this.left = left;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return !left;
	}

	public ElectricComponent component() {
		return component;
	}
	
	public ElectricWire wirePrevious() {
		return left ? component.leftWire() : component.rightWire();
	}
	
	public ElectricWire wireNext() {
		return !left ? component.leftWire() : component.rightWire();
	}

	@Override
	public String toString() {
		return (left ? ">" + component : component + "<");
	}

}
