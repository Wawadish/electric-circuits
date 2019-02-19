package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.WireGroup;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Represents a path between components. As this class is used during the search
 * phase, it is not guaranteed to represent a closed loop, although
 * {@code CircuitPath}s present in the final solution set should all be. When
 * this is the case, the first and last nodes are the same.
 *
 * A {@code CircuitPath} stores not only the components of the circuit, but also
 * the side from which they are connected. This is important notably during the
 * search phase, in which one must distinguish between the two sides.
 *
 * @author Tomer Moran
 */
public class CircuitPath implements Iterable<ElectricConnection> {

	private final LinkedList<ElectricConnection> connections;

	public CircuitPath() {
		this.connections = new LinkedList<>();
	}

	public CircuitPath(ElectricConnection start) {
		this();
		connections.add(start);
	}

	public CircuitPath(CircuitPath path) {
		this.connections = new LinkedList<>(path.connections);
	}

	public int size() {
		return connections.size();
	}

	public boolean contains(ElectricComponent comp) {
		return connections.stream().anyMatch(c -> c.component() == comp);
	}

	public boolean contains(WireGroup group, ExpandedWireMap wireMap) {
		if (connections.size() < 2) {
			return false;
		}

		Iterator<ElectricConnection> it = connections.listIterator(1);
		while (it.hasNext()) {
			ElectricConnection conn = it.next();
			if (group == wireMap.expand(conn.wirePrevious())) {
				return true;
			}
		}

		return false;
	}

	public CircuitPath append(ElectricConnection conn) {
		connections.add(conn);
		return this;
	}

	public CircuitPath copyAndAppend(ElectricConnection conn) {
		return new CircuitPath(this).append(conn);
	}

	public boolean isClosedWith(ElectricConnection conn) {
		ElectricConnection start = connections.getFirst();
		return start.component() == conn.component() && start.isLeft() == conn.isLeft();
	}

	public Stream<ElectricComponent> components() {
		return connections.stream().map(con -> con.component());
	}

	public WireGroup next(ExpandedWireMap map) {
		ElectricConnection connection = connections.getLast();
		return map.expand(connection.wireNext());
	}

	@Override
	public Iterator<ElectricConnection> iterator() {
		return connections.iterator();
	}

	@Override
	public String toString() {
		return connections.toString();
	}

}
