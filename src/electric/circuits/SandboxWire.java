package electric.circuits;

import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import java.util.function.Consumer;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Tomer Moran
 */
public class SandboxWire implements Connectable {

	private static final double JUNCTION_RADIUS = 6;

	private final SandboxPane pane;
	private final Circle junctions[];
	private final Connectable connections[];
	private final Line line;

	private final ElectricWire wire;
	private SandboxComponent component;

	public SandboxWire(SandboxPane pane) {
		this.pane = pane;
		this.junctions = new Circle[]{
			new Circle(JUNCTION_RADIUS),
			new Circle(JUNCTION_RADIUS)
		};

		this.connections = new Connectable[2];
		this.line = new Line();
		this.wire = new ElectricWire();
	}

	public void initialize(double x, double y) {
		initialize(null, true, x, y);
	}

	public void initialize(SandboxComponent comp, boolean left) {
		initialize(comp, left, 0, 0);
	}

	public void initialize(SandboxComponent comp, boolean left, double x, double y) {
		this.component = comp;
		if (comp != null) {
			wire.endpoints()[0] = new ElectricConnection(comp.getComponent(), left);
		}

		// Initialize the line
		line.startXProperty().bind(junctions[0].centerXProperty());
		line.startYProperty().bind(junctions[0].centerYProperty());
		line.endXProperty().bind(junctions[1].centerXProperty());
		line.endYProperty().bind(junctions[1].centerYProperty());
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(5);
		pane.getChildren().add(line);

		// Initialize circles
		for (int i = 0; i < junctions.length; ++i) {
			Circle j = junctions[i];
			j.setFill(Color.WHITE);
			j.setStroke(Color.BLACK);
			pane.getChildren().add(j);
		}

		// Setup position
		if (comp != null) {
			ImageView imageView = comp.getImageView();

			double centerX = left ? imageView.getX() : imageView.getX() + imageView.getImage().getWidth();
			double centerY = imageView.getY() + imageView.getImage().getHeight() / 2;
			forEachJunction(j -> {
				j.setCenterX(centerX);
				j.setCenterY(centerY);
			});
		} else {
			forEachJunction(j -> {
				j.setCenterX(x);
				j.setCenterY(y);
			});
		}

		setupDrag(junctions[1]);

		// If this is a hanging wire, both ends should be made draggable
		if (comp == null) {
			setupDrag(junctions[0]);
		}

		forEachJunction(j -> {
			j.setOnDragOver(e -> {
				WireDragData wdd = pane.getWireDrag();
				SandboxWire other = wdd.getWire();
				if (this.component == other.component && this.wire == other.wire) {
					e.acceptTransferModes();
					return;
				}

				e.acceptTransferModes(TransferMode.MOVE);
				Circle circle = wdd.getCircle();
				circle.setCenterX(j.getCenterX());
				circle.setCenterY(j.getCenterY());
				e.consume();
			});

			j.setOnDragDropped(e -> {
				WireDragData wdd = pane.endWireDrag();
				Circle circle = wdd.getCircle();
				SandboxWire other = wdd.getWire();

				if (this.component == other.component && this.wire == other.wire) {
					return;
				}

				circle.setCenterX(j.getCenterX());
				circle.setCenterY(j.getCenterY());

				// Replace the circle and the line at the top of the stack
				// so that they can be easily dragged away
				pane.getChildren().removeAll(circle);
				pane.getChildren().add(circle);

				System.out.println("Connecting two elements");
				Utils.connect(wire, other.wire);
			});

			j.setOnMousePressed(e -> {
				pane.setSelectedObject((this.component != null) ? component : this);
			});
		});
	}

	private void setupDrag(Circle circle) {
		circle.setOnDragDetected(e -> {
			// Replace the circle and the line at the bottom of the stack
			// so that the drag and drop mechanics work properly.
			pane.getChildren().removeAll(circle, line);
			pane.getChildren().add(0, circle);
			pane.getChildren().add(0, line);
			pane.startWireDrag(new WireDragData(circle));
		});
	}

	private void forEachJunction(Consumer<Circle> cons) {
		for (int i = 0; i < junctions.length; ++i) {
			cons.accept(junctions[i]);
		}
	}

	public void removeFromPane() {
		pane.getChildren().remove(line);
		forEachJunction(j -> {
			pane.getChildren().remove(j);
		});

		// Disconnect all
		Utils.disconnectAll(wire);
	}

	public Circle[] junctions() {
		return junctions;
	}

	public Connectable[] connections() {
		return connections;

	}

	public SandboxComponent component() {
		return component;
	}

	public class WireDragData {

		private final Circle circle;

		public WireDragData(Circle circle) {
			this.circle = circle;
		}

		public Circle getCircle() {
			return circle;
		}

		public SandboxWire getWire() {
			return SandboxWire.this;
		}
	}
}
