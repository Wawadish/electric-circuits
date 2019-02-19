package electric.circuits.data;

import javafx.scene.image.Image;

/**
 *
 * Parent class for all electric components in a circuit.
 *
 * @author Tomer Moran
 */
public class ElectricComponent {

	private final Image image;
	private Variable current;
	private ElectricWire leftWire, rightWire;

	public ElectricComponent(Image image) {
		this.image = image;
	}

	public Image image() {
		return image;
	}

	public Variable current() {
		return current;
	}

	public void setCurrent(Variable current) {
		this.current = current;
	}

	public ElectricWire leftWire() {
		return leftWire;
	}

	public ElectricWire rightWire() {
		return rightWire;
	}

	public void setLeftWire(ElectricWire leftWire) {
		this.leftWire = leftWire;
	}

	public void setRightWire(ElectricWire rightWire) {
		this.rightWire = rightWire;
	}
	
	
}
