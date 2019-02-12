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

    public Image getImage() {
	return image;
    }

    public Variable getCurrent() {
	return current;
    }

    public void setCurrent(Variable current) {
	this.current = current;
    }

}
