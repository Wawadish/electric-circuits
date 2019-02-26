package electric.circuits.component;

import electric.circuits.data.ElectricComponent;

/**
 * Represents a dummy electric component used for testing.
 *
 * @author Tomer Moran
 */
public class DummyBatteryComponent extends BatteryComponent {

	private final String name;

	public DummyBatteryComponent(String name, double voltage) {
		super(null);
		this.name = name;
		setVoltage(voltage);
	}

	@Override
	public String toString() {
		return name;
	}
}
