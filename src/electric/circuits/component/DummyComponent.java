package electric.circuits.component;

import electric.circuits.data.ElectricComponent;

/**
 * Represents a dummy electric component used for testing.
 *
 * @author Tomer Moran
 */
public class DummyComponent extends ElectricComponent {

	private final String name;

	public DummyComponent(String name) {
		super(null);
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
