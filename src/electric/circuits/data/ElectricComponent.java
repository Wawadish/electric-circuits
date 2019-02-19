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
		this.current = new Variable();
	}

	/**
	 *
	 * @return the image to display for this component.
	 */
	public Image image() {
		return image;
	}

	/**
	 *
	 * @return the electric current variable of this component. The returned
	 * value is never {@code null}.
	 */
	public Variable current() {
		return current;
	}

	/**
	 * Sets the electric current variable of this component.
	 *
	 * @param current the new {@code Variable} to set. Must not be {@code null}.
	 * @throws NullPointerException if {@code current} is {@code null}.
	 */
	public void setCurrent(Variable current) {
		if (current == null) {
			throw new NullPointerException();
		}

		this.current = current;
	}

	/**
	 *
	 * @return the wire connected to the left of this component, of {@code null}
	 * if none.
	 */
	public ElectricWire leftWire() {
		return leftWire;
	}

	/**
	 *
	 * @return the wire connected to the right of this component, of
	 * {@code null} if none.
	 */
	public ElectricWire rightWire() {
		return rightWire;
	}

	/**
	 * Sets the wire connected to the left of this component.
	 * @param leftWire the new wire to set. Might be {@code null}.
	 */
	public void setLeftWire(ElectricWire leftWire) {
		this.leftWire = leftWire;
	}

	/**
	 * Sets the wire connected to the right of this component.
	 * @param rightWire the new wire to set. Might be {@code null}.
	 */
	public void setRightWire(ElectricWire rightWire) {
		this.rightWire = rightWire;
	}

}
