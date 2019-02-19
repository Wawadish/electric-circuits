package electric.circuits.simulation;

import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class ExpandedWireMap {

	private final Map<ElectricWire, Set<ElectricConnection>> map = new IdentityHashMap<>();

	public Set<ElectricConnection> expand(ElectricWire start) {
		if (start == null) {
			return Collections.EMPTY_SET;
		}

		if (map.get(start) != null) {
			return map.get(start);
		}

		Set<ElectricConnection> connections = new HashSet<>();

		Queue<ElectricWire> queue = new LinkedList<>();
		Set<ElectricWire> visited = new HashSet<>();
		queue.add(start);

		// Collect all components
		while (!queue.isEmpty()) {
			ElectricWire w = queue.poll();
			if (!visited.add(w)) {
				continue;
			}

			// Add components
			connections.add(w.endpoints()[0]);
			connections.add(w.endpoints()[1]);

			// Explore neighbor wires
			for (ElectricWire w2 : w.wires()) {
				// This should never be the case
				if (map.get(w2) != null) {
					throw new AssertionError();
				}

				// Enqueue neighbor wire
				queue.add(w2);
			}
		}

		Set<ElectricConnection> set = Collections.unmodifiableSet(connections);

		// Record the results
		for (ElectricWire wire : visited) {
			map.put(wire, set);
		}

		return set;
	}

}
