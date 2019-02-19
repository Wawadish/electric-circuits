package electric.circuits;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;

/**
 *
 * @author Tomer Moran
 */
public class Utils {

	public static final double EPSILON = 0.001;

	public static boolean equals(double a, double b) {
		return Math.abs(a - b) <= EPSILON;
	}

	public static void connect(ElectricComponent comp, ElectricWire wire, boolean compLeft, boolean wireLeft) {
		if (compLeft) {
			comp.setLeftWire(wire);
		} else {
			comp.setRightWire(wire);
		}

		wire.endpoints()[wireLeft ? 0 : 1] = new ElectricConnection(comp, compLeft);
	}
}
