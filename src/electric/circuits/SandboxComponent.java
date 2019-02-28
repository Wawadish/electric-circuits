/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package electric.circuits;

import electric.circuits.data.ElectricComponent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author stavr
 */
public class SandboxComponent extends ImageView {

	private int gridX;
	private int gridY;
	private Circle junction1;
	private Circle junction2;

	private boolean moved = true;

	private final ElectricComponent component;

	public SandboxComponent(ElectricComponent component) {
		this.component = component;

		initialize();
	}

	private void initialize() {
		double width = getImage().getWidth();
		double height = img.getImage().getHeight();

		//gets the right and left x coordinate of the image
		double left_x = img.getX();
		double right_x = img.getX() + width;

		//gets the y coordinate of the midpoint of the image
		double bottom = img.getY() - height;
		double midpoint = (img.getY() - bottom) / 2;

		//creates a circle that acts as a junction for the left side of the image
		Circle leftC = new Circle(left_x, midpoint, 10);
		leftC.setFill(Color.WHITE);
		leftC.setStroke(Color.BLACK);

		//creates a circle that acts as a junction for the right side of the image
		Circle rightC = new Circle(right_x, midpoint, 10);
		rightC.setFill(Color.WHITE);
		rightC.setStroke(Color.BLACK);

		//adds the junctions to the pane
		getChildren().addAll(leftC, rightC);
	}

	public void move(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		moved = true;
	}

	private void updatePosition() {
		if (moved) {

			moved = false;
		}
	}

	public void addTo(SandboxPane pane) {

	}

}
