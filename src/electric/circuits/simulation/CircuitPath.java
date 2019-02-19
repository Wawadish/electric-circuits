package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
		this.connections = new LinkedList<>();
		connections.add(start);
	}

	public CircuitPath(CircuitPath path) {
		this.connections = new LinkedList<>(path.connections);
	}

	public int size() {
		return connections.size();
	}

	public boolean contains(ElectricComponent component) {
		return connections.stream().anyMatch(con -> con.component() == component);
	}
	
	public CircuitPath append(ElectricConnection conn) {
		connections.add(conn);
		return this;
	}
	
	public CircuitPath copyAndAppend(ElectricConnection conn) {
		return new CircuitPath(this).append(conn);
	}

	public Stream<ElectricComponent> components() {
		return connections.stream().map(con -> con.component());
	}
	
	public Set<ElectricConnection> next(ExpandedWireMap map) {
		ElectricConnection connection = connections.getLast();
		ElectricComponent comp = connection.component();
		
		ElectricWire wire = connection.isLeft() ? comp.rightWire() : comp.leftWire();
		return map.expand(wire);
	}

	@Override
	public Iterator<ElectricConnection> iterator() {
		return connections.iterator();
	}

}
