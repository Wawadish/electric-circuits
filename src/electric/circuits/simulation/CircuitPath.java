package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import electric.circuits.data.WireGroup;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Stream;

/**
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
		if (connections.size() < 2)
			return false;
		
		Iterator<ElectricConnection> it = connections.listIterator(1);
		while (it.hasNext()) {
			ElectricConnection conn = it.next();
			if (group == wireMap.expand(conn.wirePrevious()))
				return true;
		}
		
		return false;
	}

	public boolean isAllowed(WireGroup group, ElectricConnection connection, ExpandedWireMap map) {
		for (ElectricConnection c : connections) {
			if (c.component() == connection.component()) {
				return false;
			}

			if (group == map.expand(connection.wirePrevious())) {
				System.out.println(this + "; @ wire " + group + ": " + connection);
				return false;
			}
		}

		return true;
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
