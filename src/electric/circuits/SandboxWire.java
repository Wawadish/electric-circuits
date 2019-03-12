package electric.circuits;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tomer Moran
 */
public class SandboxWire {

    private static final double JUNCTION_RADIUS = 10;

    private final Circle junction1;
    private final Circle junction2;
    private final SandboxPane pane;

    public SandboxWire(SandboxPane pane) {
        this.pane = pane;
        this.junction1 = new Circle(JUNCTION_RADIUS);
        this.junction2 = new Circle(JUNCTION_RADIUS);
    }

    public void initialize(SandboxComponent comp, boolean left) {
        // Initialize circles
        junction1.setFill(Color.WHITE);
        junction2.setFill(Color.WHITE);
        junction1.setStroke(Color.BLACK);
        junction2.setStroke(Color.BLACK);

        // Setup position bindings
        ImageView imageView = comp.getImageView();

        DoubleProperty centerX = imageView.xProperty();
        DoubleBinding centerY = imageView.yProperty().add(imageView.getImage().heightProperty().divide(2));

        if (left) {
            junction1.centerXProperty().bind(centerX);
            junction2.centerXProperty().bind(centerX);
        } else {
            DoubleBinding binding = centerX.add(imageView.getImage().widthProperty());
            junction1.centerXProperty().bind(binding);
            junction2.centerXProperty().bind(binding);
        }

        junction1.centerYProperty().bind(centerY);
        junction2.centerYProperty().bind(centerY);

        pane.getChildren().addAll(junction1, junction2);
    }

    public void removeFromPane() {
        pane.getChildren().removeAll(junction1, junction2);
    }
}
