package electric.circuits.component;

import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import electric.circuits.simulation.SimulationContext;

/**
 * Represents a dummy electric component used for testing.
 *
 * @author Tomer Moran
 */
public class DummyComponent extends ElectricComponent {

	private final String name;

	public DummyComponent(SimulationContext context, ComponentType type, String name) {
		super(context, type);
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
