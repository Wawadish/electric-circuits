package electric.circuits.data;

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
public class WireCrawler {

	public static final WireGroup EMPTY_GROUP = new WireGroup(Collections.EMPTY_SET, Collections.EMPTY_SET);

	private final Map<ElectricWire, WireGroup> map = new IdentityHashMap<>();

	public WireGroup expand(ElectricWire start) {
		if (start == null) {
			return EMPTY_GROUP;
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

			// Add connections
			ElectricConnection e0 = w.endpoints()[0];
			if (e0 != null) {
				connections.add(e0);
			}

			ElectricConnection e1 = w.endpoints()[1];
			if (e1 != null) {
				connections.add(e1);
			}

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

		WireGroup group = new WireGroup(
				Collections.unmodifiableSet(visited),
				Collections.unmodifiableSet(connections));

		// Record the results
		for (ElectricWire wire : visited) {
			map.put(wire, group);
		}

		return group;
	}
}
