package electric.circuits;

import electric.circuits.data.ElectricComponent;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author stavr
 */
public class SandboxComponent {

    private static final double JUNCTION_RADIUS = 10;

    private int gridX;
    private int gridY;
    private final Circle junction1;
    private final Circle junction2;
    private final ImageView imageView;

    private final SandboxPane pane;
    private final ElectricComponent component;

    public SandboxComponent(SandboxPane pane, ElectricComponent component) {
        this.pane = pane;
        this.component = component;
        this.imageView = new ImageView(component.getType().getImage());
        this.junction1 = new Circle(JUNCTION_RADIUS);
        this.junction2 = new Circle(JUNCTION_RADIUS);
    }

    public void initialize() {
        // Initialize circles
        junction1.setFill(Color.WHITE);
        junction2.setFill(Color.WHITE);
        junction1.setStroke(Color.BLACK);
        junction2.setStroke(Color.BLACK);

        // Setup position bindings
        junction1.centerXProperty().bind(imageView.xProperty());
        junction2.centerXProperty().bind(imageView.xProperty().add(imageView.getImage().widthProperty()));

        DoubleBinding centerY = imageView.yProperty().add(imageView.getImage().heightProperty().divide(2));
        junction1.centerYProperty().bind(centerY);
        junction2.centerYProperty().bind(centerY);

        // Update the position of the ImageView
        updatePosition();

        // Add the elements to the pane
        pane.getChildren().addAll(imageView, junction1, junction2);

        imageView.setOnDragDetected(e -> {
            pane.getChildren().removeAll(imageView, junction1, junction2);
            pane.components().remove(this);

            Utils.startDrag(pane, component.getType());
        });

        imageView.setOnMouseClicked(e -> {
            pane.setSelectedComponent(this);
            System.out.println("X: " + this.gridX + " Y: " + this.gridY);
            e.consume();
        });
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

    public ImageView getImageView() {
        return imageView;
    }

    public void removeFromPane() {
        imageView.setVisible(false);
        junction1.setVisible(false);
        junction2.setVisible(false);
    }

}
