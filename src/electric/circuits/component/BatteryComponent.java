package electric.circuits.component;

import electric.circuits.data.ElectricComponent;
import electric.circuits.simulation.SimulationContext;
import javafx.scene.image.Image;

/**
 *
 * @author Tomer Moran
 */
public class BatteryComponent extends ElectricComponent {

	private double voltage;

	public BatteryComponent(SimulationContext context) {
		super(context);
	}

	public double voltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

}
