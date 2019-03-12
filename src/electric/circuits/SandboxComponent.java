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
		
		imageView.setOnMouseClicked(e -> {
            pane.setSelectedComponent(this);
            System.out.println("X: " + this.gridX + " Y: " + this.gridY);
            e.consume();
			
			InfoPane.topTitle.setVisible(true);
			InfoPane.k_title.setVisible(true);
			InfoPane.kir_eq.setVisible(true);
			InfoPane.o_title.setVisible(true);
			InfoPane.ohm_eq.setVisible(true);
			InfoPane.resistance.setVisible(true);
			InfoPane.resistance_box.setVisible(true);
			InfoPane.voltage.setVisible(true);
			InfoPane.voltage_box.setVisible(true);
        });
	}
	
	 public void removeFromPane() {
        imageView.setVisible(false);
        junction1.setVisible(false);
        junction2.setVisible(false);
    }

	public void move(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		updatePosition();
	}
	
	private void updatePosition() {
		imageView.setX(gridX * SandboxPane.GRID_SIZE);
		imageView.setY(gridY * SandboxPane.GRID_SIZE);
	}
	
	public ElectricComponent getComponent() {
		return component;
	}
	
	public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }
}
