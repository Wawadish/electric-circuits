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
	BATTERY("file:assets/battery.png"),
	/**
	 * Produces light of a certain color.
	 */
	LED("file:assets/lamp_off.png"),
	/**
	 * Provides a resistance to the circuit; this reduces the voltage and the
	 * current.
	 */
	RESISTOR("file:assets/resistor.png");

	public static final Image LED_ON = new Image("file:assets/lamp_on.gif", 100, 100, true, true);
	public static final Image LED_OFF = LED.getImage();
	
	private final Image image;

	private ComponentType(String url) {
		this.image = new Image(url, 100, 100, true, true);
	}

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
		return image;
	}
}
