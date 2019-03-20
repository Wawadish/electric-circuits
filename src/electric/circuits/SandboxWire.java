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

	private static final double JUNCTION_RADIUS = 10;

	private final SandboxPane pane;
	private final Circle junctions[];
	private final Connectable connections[];
	private final Line line;

	private final ElectricWire wire;

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

	public void initialize(SandboxComponent comp, boolean left) {
		wire.endpoints()[0] = new ElectricConnection(comp.getComponent(), left);

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
		ImageView imageView = comp.getImageView();

		double centerX = left ? imageView.getX() : imageView.getX() + imageView.getImage().getWidth();
		double centerY = imageView.getY() + imageView.getImage().getHeight() / 2;
		forEachJunction(j -> {
			j.setCenterX(centerX);
			j.setCenterY(centerY);
		});

		junctions[1].setOnDragDetected(e -> pane.startWireDrag(new WireDragData(junctions[1])));

		forEachJunction(j -> {
			j.setOnDragOver(e -> {
				// Only accept wire drags
				e.acceptTransferModes(TransferMode.MOVE);
				WireDragData wdd = pane.getDraggedWire();
				Circle circle = wdd.getCircle();
				circle.setCenterX(j.getCenterX());
				circle.setCenterY(j.getCenterY());
				e.consume();

			});

			j.setOnDragDropped(e -> {
				WireDragData wdd = pane.getDraggedWire();
				Circle circle = wdd.getCircle();
				circle.setCenterX(j.getCenterX());
				circle.setCenterY(j.getCenterY());
			});
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
	}

	public Circle[] junctions() {
		return junctions;
	}

	public Connectable[] connections() {
		return connections;

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
