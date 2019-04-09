package electric.circuits;

import electric.circuits.data.ElectricWire;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Tomer Moran
 */
public class SandboxWire {

	// The radius of the junction circle
	private static final double JUNCTION_RADIUS = 6;

	// The graphics of this wire
	private final Line line;
	private final SandboxPane pane;
	private final Circle junctions[];

	// The backend related to this wire
	private final ElectricWire wire;
	private SandboxComponent component;
	private final Set<ElectricWire>[] cachedConnectionsPerJunction;

	public SandboxWire(SandboxPane pane) {
		this.pane = pane;
		this.junctions = new Circle[]{
			new Circle(JUNCTION_RADIUS),
			new Circle(JUNCTION_RADIUS)
		};

		this.line = new Line();
		this.wire = new ElectricWire();
		this.cachedConnectionsPerJunction = new Set[]{
			new HashSet<>(),
			new HashSet<>()
		};

		// Assigns to each junction their index
		forEachJunction((j, i) -> j.setUserData(i));
	}

	/**
	 * Initializes this {@code SandboxWire} as a hanging wire at the given
	 * screen coordinates.
	 *
	 * @param x the x screen coordinate.
	 * @param y the y screen coordinate.
	 */
	public void initialize(double x, double y) {
		initialize(null, true, x, y);
	}

	/**
	 * Initializes this {@code SandboxWire} for a given
	 * {@code SandboxComponent}.
	 *
	 * @param comp the {@code SandboxComponent} to associate this wire with.
	 * @param left whether or not this wire is attached to the left
	 * ({@code true}) or the right ({@code false}) of the component.
	 */
	public void initialize(SandboxComponent comp, boolean left) {
		initialize(comp, left, 0, 0);
	}

	// Internal method to handle the initiation of both hanging and attached wires
	private void initialize(SandboxComponent comp, boolean left, double x, double y) {
		this.component = comp;

		// Initialize the line
		line.startXProperty().bind(junctions[0].centerXProperty());
		line.startYProperty().bind(junctions[0].centerYProperty());
		line.endXProperty().bind(junctions[1].centerXProperty());
		line.endYProperty().bind(junctions[1].centerYProperty());
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(5);
		pane.getChildren().add(line);

		// Initialize the circles
		for (int i = 0; i < junctions.length; ++i) {
			Circle j = junctions[i];
			j.setFill(Color.WHITE);
			j.setStroke(Color.BLACK);
			pane.getChildren().add(j);
		}

		// Setup the position
		if (comp != null) {
			ImageView imageView = comp.getImageView();

			double centerX = left ? imageView.getX() : imageView.getX() + imageView.getImage().getWidth();
			double centerY = imageView.getY() + imageView.getImage().getHeight() / 2;
			forEachJunction((j, i) -> {
				j.setCenterX(centerX);
				j.setCenterY(centerY);
			});
		} else {
			forEachJunction((j, i) -> {
				j.setCenterX(x);
				j.setCenterY(y);
			});
		}

		// Make one end draggable
		setupDrag(junctions[1], 1);

		// If this is a hanging wire, both ends should be made draggable
		if (comp == null) {
			setupDrag(junctions[0], 0);
		}

		forEachJunction((j, i) -> {
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

				Utils.debug("Connecting two elements");
				Utils.connect(wire, other.wire);
				cachedConnectionsPerJunction[i].add(other.wire);
				other.cachedConnectionsPerJunction[(int) circle.getUserData()].add(wire);
				pane.runSimulation();
			});

			j.setOnMousePressed(e -> {
				pane.setSelectedObject((this.component != null) ? component : this);
				e.consume();
			});
		});
	}

	/**
	 * Sets up the dragging mechanics of a circle junction.
	 *
	 * @param circle the circle to set up.
	 * @param i the index of the junction.
	 */
	private void setupDrag(Circle circle, int i) {
		circle.setOnDragDetected(e -> {
			cachedConnectionsPerJunction[i].forEach(w -> {
				Utils.disconnect(wire, w);
				Utils.debug("disconnecting " + wire + " from " + w);
			});

			cachedConnectionsPerJunction[i].clear();

			// Replace the circle and the line at the bottom of the stack
			// so that the drag and drop mechanics work properly.
			pane.getChildren().removeAll(circle, line);
			pane.getChildren().add(0, circle);
			pane.getChildren().add(0, line);
			pane.startWireDrag(new WireDragData(circle));
			pane.runSimulation();
		});
	}

	/**
	 * Utility method to apply some code for each junction.
	 *
	 * @param cons the {@code BiConsumer} to apply.
	 */
	private void forEachJunction(BiConsumer<Circle, Integer> cons) {
		for (int i = 0; i < junctions.length; ++i) {
			cons.accept(junctions[i], i);
		}
	}

	/**
	 * Removes this wire from the sandbox pane.
	 */
	public void removeFromPane() {
		pane.getChildren().remove(line);
		forEachJunction((j, i) -> {
			pane.getChildren().remove(j);
		});

		// Disconnect all
		Utils.disconnectAll(wire);
	}

	public Circle[] junctions() {
		return junctions;
	}

	public SandboxComponent component() {
		return component;
	}

	public ElectricWire wire() {
		return wire;
	}

	/**
	 * Utility class which holds data pertaining to a wire drag.
	 */
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
