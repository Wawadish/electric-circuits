package electric.circuits.component;

import electric.circuits.data.ElectricComponent;
import javafx.scene.image.Image;

/**
 *
 * @author Tomer Moran
 */
public class BatteryComponent extends ElectricComponent {

	private double voltage;

	public BatteryComponent(Image image) {
		super(image);
	}

	public double voltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

}
