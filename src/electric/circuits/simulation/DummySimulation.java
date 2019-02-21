package electric.circuits.simulation;

import electric.circuits.data.WireCrawler;
import electric.circuits.Utils;
import electric.circuits.data.BoundVariable;
import electric.circuits.data.DummyComponent;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import electric.circuits.data.WireGroup;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class DummySimulation {

	public static void main(String[] args) {
		WireCrawler wireMap = new WireCrawler();
		DummyComponent battery = new DummyComponent("Battery");
		DummyComponent led1 = new DummyComponent("LED1");
		DummyComponent led2 = new DummyComponent("LED2");
		DummyComponent led3 = new DummyComponent("LED3");
		DummyComponent res = new DummyComponent("RES1");

		ElectricWire wire1 = new ElectricWire();
		ElectricWire wire2 = new ElectricWire();
		ElectricWire wire3 = new ElectricWire();
		ElectricWire wire4 = new ElectricWire();
		ElectricWire wire5 = new ElectricWire();
		ElectricWire wire6 = new ElectricWire();
		ElectricWire wire7 = new ElectricWire();
		Utils.connect(wire1, wire3);
		Utils.connect(wire1, wire5);
		Utils.connect(wire2, wire4);
		Utils.connect(wire2, wire7);

		Utils.connect(battery, wire2, wire1);
		Utils.connect(led1, wire1, wire2);
		Utils.connect(led2, wire3, wire4);
		Utils.connect(led3, wire5, wire6);
		Utils.connect(res, wire6, wire7);

		// 1. Find all paths
		Set<CircuitPath> paths = explorePaths(battery, true, wireMap);
		paths.stream().forEach(System.out::println);

		// 2. Reduce the unknowns
		paths.stream().flatMap(c -> c.wireGroups(wireMap))
				.filter(WireGroup::hasTwoConnections)
				.forEach(wg -> {
					// Collect the two components
					Iterator<ElectricConnection> it = wg.connections().iterator();
					ElectricConnection conn1 = it.next();
					ElectricConnection conn2 = it.next();

					// Bind the two components together
					System.out.println("binding " + conn1.component() + " and " + conn2.component());
					BoundVariable.bind(conn1.component(), conn2.component());
				});
	}

	private static Set<CircuitPath> explorePaths(ElectricComponent start, boolean left, WireCrawler wireMap) {

		// Prepare the search
		Set<CircuitPath> solutions = new HashSet<>();
		Queue<CircuitPath> queue = new LinkedList<>();

		// Insert starting point
		queue.add(new CircuitPath(new ElectricConnection(start, left)));

		// Loop for as long as there are paths to expand
		while (!queue.isEmpty()) {
			CircuitPath path = queue.poll();

			// Check if this wire group was already traversed
			WireGroup group = path.explore(wireMap);
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
