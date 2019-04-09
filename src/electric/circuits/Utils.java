package electric.circuits;

import electric.circuits.data.ComponentType;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Utility class with a collection of methods.
 *
 * @author Tomer Moran
 */
public class Utils {

	/**
	 * Whether or not debug mode is activated. When {@code true}, debug messages
	 * are printed to the standard output. When {@code false}, no debug messages
	 * are printed.
	 */
	public static final boolean DEBUG = false;

	/**
	 * Accuracy threshold used by {@link #equals}.
	 */
	public static final double EPSILON = 0.001;

	/**
	 * Compares whether two {@code double}s are very close to each other, up to
	 * a certain threshold defined by {@link #EPSILON}.
	 *
	 * @param a the first {@code double} to check.
	 * @param b the second {@code double} to check.
	 * @return {@code true} if the difference of the two {@code double}s is
	 * within the threshold, {@code false} otherwise.
	 */
	public static boolean equals(double a, double b) {
		return Math.abs(a - b) <= EPSILON;
	}

	/**
	 * Connects two {@code ElectricWire}s together.
	 *
	 * @param wire1 the first wire.
	 * @param wire2 the second wire.
	 */
	public static void connect(ElectricWire wire1, ElectricWire wire2) {
		if (wire1 == wire2) {
			throw new AssertionError();
		}

		wire1.wires().add(wire2);
		wire2.wires().add(wire1);
	}

	/**
	 * Disconnects two {@code ElectricWire}s from each other.
	 *
	 * @param wire1 the first wire.
	 * @param wire2 the second wire.
	 */
	public static void disconnect(ElectricWire wire1, ElectricWire wire2) {
		wire1.wires().remove(wire2);
		wire2.wires().remove(wire1);
	}

	/**
	 * Disconnects all {@code ElectricWire}s from a given wire.
	 *
	 * @param wire the wire to disconnect.
	 */
	public static void disconnectAll(ElectricWire wire) {
		// Disconnect both ways
		wire.wires().forEach(w -> {
			w.wires().remove(wire);
		});

		// Clear connections
		wire.wires().clear();
	}

	/**
	 * Connects an {@code ElectricComponent} with two {@code ElectricWire}s.
	 *
	 * @param comp the component to connect.
	 * @param wire1 the first wire to connect, to the left.
	 * @param wire2 the first wire to connect, to the right.
	 */
	public static void connect(ElectricComponent comp, ElectricWire wire1, ElectricWire wire2) {
		comp.setLeftWire(wire1);
		comp.setRightWire(wire2);

		wire1.endpoints()[1] = new ElectricConnection(comp, true);
		wire2.endpoints()[0] = new ElectricConnection(comp, false);
	}

	/**
	 * Starts a component drag.
	 *
	 * @param source the source {@code Node} from which the drag was started.
	 * @param type the type of the component being dragged.
	 */
	public static void startDrag(Node source, ComponentType type) {
		Image image = type.getImage();
		Dragboard db = source.startDragAndDrop(TransferMode.COPY);
		db.setDragView(image, image.getWidth() / 2, image.getHeight() / 2);

		ClipboardContent cc = new ClipboardContent();
		cc.put(DataFormat.IMAGE, image);
		cc.put(DataFormat.PLAIN_TEXT, type);
		db.setContent(cc);
	}

	/**
	 * Converts a given screen coordinate to grid coordinate.
	 *
	 * @param x the screen coordinate.
	 * @return the associated grid coordinate.
	 */
	public static int toGrid(double x) {
		return (int) (x / SandboxPane.GRID_SIZE);
	}

	/**
	 * Converts a given grid coordinate to screen coordinate.
	 *
	 * @param grid the grid coordinate to convert.
	 * @return the associated screen coordinate.
	 */
	public static double toPixel(int grid) {
		return grid * SandboxPane.GRID_SIZE;
	}

	/**
	 * Snaps a given screen coordinate to the grid.
	 *
	 * @param x the screen coordinate to snap.
	 * @return
	 */
	public static double snapToGrid(double x) {
		return SandboxPane.GRID_SIZE * Math.floor(x / SandboxPane.GRID_SIZE);
	}

	/**
	 * Prints a debug message to {@code System.out} if, and only if,
	 * {@link #DEBUG} is set to {@code true}.
	 *
	 * @param obj the object to print. Can be {@code null}.
	 */
	public static void debug(Object obj) {
		if (DEBUG) {
			System.out.println(obj);
		}
	}
}
