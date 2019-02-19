package electric.circuits.simulation;

import electric.circuits.Utils;
import electric.circuits.data.DummyComponent;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import electric.circuits.data.WireGroup;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class DummySimulation {

	public static void main(String[] args) {
		DummyComponent battery = new DummyComponent("Battery");
		DummyComponent led1 = new DummyComponent("LED1");
		DummyComponent led2 = new DummyComponent("LED2");
		DummyComponent led3 = new DummyComponent("LED3");

		ElectricWire wire1 = new ElectricWire();
		ElectricWire wire2 = new ElectricWire();
		ElectricWire wire3 = new ElectricWire();
		ElectricWire wire4 = new ElectricWire();
		ElectricWire wire5 = new ElectricWire();
		ElectricWire wire6 = new ElectricWire();
		Utils.connect(wire1, wire3);
		Utils.connect(wire1, wire5);
		Utils.connect(wire2, wire4);
		Utils.connect(wire2, wire6);

		Utils.connect(battery, wire2, wire1);
		Utils.connect(led1, wire1, wire2);
		Utils.connect(led2, wire3, wire4);
		Utils.connect(led3, wire5, wire6);

		Set<CircuitPath> paths = explorePaths(battery, true);
		paths.stream().forEach(System.out::println);
	}

	private static Set<CircuitPath> explorePaths(ElectricComponent start, boolean left) {

		// Prepare the search
		ExpandedWireMap wireMap = new ExpandedWireMap();
		Set<CircuitPath> solutions = new HashSet<>();
		Queue<CircuitPath> queue = new LinkedList<>();
		
		// Insert starting point
		queue.add(new CircuitPath(new ElectricConnection(start, left)));
		
		// Loop for as long as there are paths to expand
		while (!queue.isEmpty()) {
			CircuitPath path = queue.poll();

			// Check if this wire group was already traversed
			WireGroup group = path.next(wireMap);
			if (path.contains(group, wireMap)) {
				continue;
			}

			// Loop through potential extensions to current path
			for (ElectricConnection conn : group.connections()) {
				// Check for solution
				if (path.isClosedWith(conn)) {
					solutions.add(path.copyAndAppend(conn));
					continue;
				}

				// Check for inner loop
				if (path.contains(conn.component())) {
					continue;
				}

				// Append to path and keep searching
				queue.add(path.copyAndAppend(conn));
			}
		}

		return solutions;
	}
}
