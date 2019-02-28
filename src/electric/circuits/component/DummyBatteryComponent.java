package electric.circuits.component;

import electric.circuits.simulation.SimulationContext;

/**
 * Represents a dummy electric component used for testing.
 *
 * @author Tomer Moran
 */
public class DummyBatteryComponent extends BatteryComponent {

	private final String name;

	public DummyBatteryComponent(SimulationContext context, String name, double voltage) {
		super(context);
		this.name = name;
		setVoltage(voltage);
	}

	@Override
	public String toString() {
		return name;
	}
}
