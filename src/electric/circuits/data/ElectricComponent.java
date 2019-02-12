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
    
    public ElectricComponent(Image image) {
	this.image = image;
    }
}
