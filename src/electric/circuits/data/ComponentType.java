package electric.circuits.data;

import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.component.DummyComponent;
import electric.circuits.simulation.SimulationContext;
import javafx.scene.image.Image;

/**
 * Represents a type of electric component that can be used to build circuits.
 *
 * @author Tomer Moran
 */
public enum ComponentType {

	/**
	 * Provides a fixed voltage source.
	 */
	BATTERY,
	/**
	 * Produces light of a certain color.
	 */
	LED,
	/**
	 * Provides a resistance to the circuit; this reduces the voltage and the
	 * current.
	 */
	RESISTOR;

	private static final Image PLACEHOLDER = new Image("file:assets/images/placeholder.png");

	public ElectricComponent create(SimulationContext context) {
		// TODO: implement method
		switch (this) {
			case BATTERY:
				return new DummyBatteryComponent(context, "BATTERY", 15);
			case LED:
				return new DummyComponent(context, ComponentType.LED, "LED");
			case RESISTOR:
				return new DummyComponent(context, ComponentType.RESISTOR, "RESISTOR");
			default:
				throw new AssertionError("Unimplemented case " + this);
		}
	}

	public Image getImage() {
		return PLACEHOLDER;
	}
}
