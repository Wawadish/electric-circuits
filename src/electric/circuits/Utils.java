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
 *
 * @author Tomer Moran
 */
public class Utils {

    public static final double EPSILON = 0.001;

    public static boolean equals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    public static void connect(ElectricWire wire1, ElectricWire wire2) {
        wire1.wires().add(wire2);
        wire2.wires().add(wire1);
    }
	
	public static void disconnect(ElectricWire wire1, ElectricWire wire2) {
		wire1.wires().remove(wire2);
        wire2.wires().remove(wire1);
	}

    public static void connect(ElectricComponent comp, ElectricWire wire1, ElectricWire wire2) {
        comp.setLeftWire(wire1);
        comp.setRightWire(wire2);

        wire1.endpoints()[1] = new ElectricConnection(comp, true);
        wire2.endpoints()[0] = new ElectricConnection(comp, false);
    }

    public static void connect(ElectricComponent comp, boolean compLeft, ElectricWire wire, boolean wireLeft) {
        if (compLeft) {
            comp.setLeftWire(wire);
        } else {
            comp.setRightWire(wire);
        }

        wire.endpoints()[wireLeft ? 0 : 1] = new ElectricConnection(comp, compLeft);
    }

    public static void startDrag(Node source, ComponentType type) {
        Image image = type.getImage();
        Dragboard db = source.startDragAndDrop(TransferMode.COPY);
        db.setDragView(image, image.getWidth() / 2, image.getHeight() / 2);

        ClipboardContent cc = new ClipboardContent();
        cc.put(DataFormat.IMAGE, image);
        cc.put(DataFormat.PLAIN_TEXT, type);
        db.setContent(cc);
    }

    public static int toGrid(double x) {
        return (int) (x / SandboxPane.GRID_SIZE);
    }

    public static double toPixel(int grid) {
        return grid * SandboxPane.GRID_SIZE;
    }

    public static double snapToGrid(double x) {
        return SandboxPane.GRID_SIZE * Math.floor(x / SandboxPane.GRID_SIZE);
    }

}
