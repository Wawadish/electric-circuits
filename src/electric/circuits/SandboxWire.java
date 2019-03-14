package electric.circuits;

import java.util.function.Consumer;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tomer Moran
 */
public class SandboxWire implements Connectable {

    private static final double JUNCTION_RADIUS = 10;

    private final SandboxPane pane;
    private final Circle junctions[];
    private final Connectable connections[];

    public SandboxWire(SandboxPane pane) {
        this.pane = pane;
        this.junctions = new Circle[]{
            new Circle(JUNCTION_RADIUS),
            new Circle(JUNCTION_RADIUS)
        };

        this.connections = new Connectable[2];
    }

    public void initialize(SandboxComponent comp, boolean left) {
        // Initialize circles
        for (int i = 0; i < junctions.length; ++i) {
            Circle j = junctions[i];
            j.setFill(Color.WHITE);
            j.setStroke(Color.BLACK);
            pane.getChildren().add(j);
        }
        // Setup position
        ImageView imageView = comp.getImageView();

        double centerX = left ? imageView.getX() : imageView.getX() + imageView.getImage().getWidth();
        double centerY = imageView.getY() + imageView.getImage().getHeight() / 2;
        forEachJunction(j -> {
            j.setCenterX(centerX);
            j.setCenterY(centerY);
        });

        junctions[0].setOnDragDetected(e -> pane.startWireDrag(new WireDragData(0, this)));
    }

    private void forEachJunction(Consumer<Circle> cons) {
        for (int i = 0; i < junctions.length; ++i) {
            cons.accept(junctions[i]);
        }
    }

    public void removeFromPane() {
        forEachJunction(j -> {
            pane.getChildren().remove(j);
        });
    }

    public Circle[] junctions() {
        return junctions;
    }

    public Connectable[] connections() {
        return connections;

    }

    public static class WireDragData {

        public final int index;
        public final Circle circle;
        public final SandboxWire wire;

        public WireDragData(int index, SandboxWire wire) {
            this.index = index;
            this.wire = wire;
            this.circle = wire.junctions[index];
        }

    }
}
