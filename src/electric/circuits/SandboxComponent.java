package electric.circuits;

import electric.circuits.data.ElectricComponent;
import javafx.scene.image.ImageView;

/**
 *
 * @author stavr
 */
public class SandboxComponent {


	private int gridX;
	private int gridY;
	
	private final ImageView imageView;

	private final SandboxPane pane;
	private final ElectricComponent component;
    
    private SandboxWire wireLeft, wireRight;
    
	public SandboxComponent(SandboxPane pane, ElectricComponent component) {
		this.pane = pane;
		this.component = component;
		this.imageView = new ImageView(component.getType().getImage());
		
        this.wireLeft = new SandboxWire(pane);
        this.wireRight = new SandboxWire(pane);
	}

	public void initialize() {
		// Update the position of the ImageView
		updatePosition();

		// Add the elements to the pane
		pane.getChildren().addAll(imageView);
        wireLeft.initialize(this, true);
        wireRight.initialize(this, false);

		imageView.setOnDragDetected(e -> {
            wireLeft.removeFromPane();
            wireRight.removeFromPane();
			pane.getChildren().removeAll(imageView);
			pane.components().remove(this);

			Utils.startDrag(pane, component.getType());
		});
	}

	public void move(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		updatePosition();
	}

    public ImageView getImageView() {
        return imageView;
    }

	private void updatePosition() {
		imageView.setX(gridX * SandboxPane.GRID_SIZE);
		imageView.setY(gridY * SandboxPane.GRID_SIZE);
	}

	public ElectricComponent getComponent() {
		return component;
	}
}
