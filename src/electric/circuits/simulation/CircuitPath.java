package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.WireCrawler;
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

	public CircuitPath(CircuitPath path) {
		this.connections = new LinkedList<>(path.connections);
	}

	public CircuitPath(ElectricConnection start) {
		this();
		connections.add(start);
	}

	/**
	 *
	 * @return the size of this circuit, equal to the number of connections in
	 * it.
	 */
	public int size() {
		return connections.size();
	}

	/**
	 * Returns whether or not a certain {@code ElectricComponent} is present in
	 * this circuit.
	 *
	 * @param comp the component to search for. If {@code null}, {@code false}
	 * is automatically returned.
	 * @return {@code true} if the given component is contained in the circuit,
	 * {@code false} otherwise.
	 */
	public boolean contains(ElectricComponent comp) {
		if (comp == null) {
			return false;
		}

		return connections.stream().anyMatch(c -> c.component() == comp);
	}

	/**
	 * Returns whether or not a given {@code WireGroup} is traversed by this
	 * circuit. Searching this requires the help of an {@code WireCrawler}. Note
	 * that the latter must be the same instance used to construct the given
	 * {@code WireGroup}.
	 *
	 * @param group the wire group to check for.
	 * @param wireCrawler the wire group generator.
	 * @return
	 */
	public boolean contains(WireGroup group, WireCrawler wireCrawler) {
		// Preliminary checks
		if (group == null || connections.size() < 2) {
			return false;
		}

		// Start iterating at the second element since we fetch the previous wire connections
		Iterator<ElectricConnection> it = connections.listIterator(1);
		while (it.hasNext()) {

			// Fetch explore connection and check if the incoming wire group matches
			ElectricConnection conn = it.next();
			if (group == wireCrawler.expand(conn.wirePrevious())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Appends a given {@code ElectricConnection} to this circuit.
	 *
	 * @param conn the connection to append.
	 * @return {@code this} instance, for chaining.
	 */
	public CircuitPath append(ElectricConnection conn) {
		connections.add(conn);
		return this;
	}

	/**
	 * Clones {@code this} instance, and appends to it a given
	 * {@code ElectricConnection}.
	 *
	 * @param conn the connection to append.
	 * @return the cloned instance, with the appended connection.
	 */
	public CircuitPath copyAndAppend(ElectricConnection conn) {
		return new CircuitPath(this).append(conn);
	}

	/**
	 * Returns whether or not this circuit would be closed with a given
	 * connection. This will be the case if and only if the first component of
	 * this circuit is the same as the given one, and if the said component will
	 * be connected from both sides.
	 *
	 * @param conn the connection to evaluate.
	 * @return {@code true} if the given connection would close this circuit,
	 * {@code false} otherwise.
	 */
	public boolean isClosedWith(ElectricConnection conn) {
		if (connections.isEmpty()) {
			return false;
		}

		return connections.getFirst().equals(conn);
	}

	/**
	 * @return a {@link Stream} of the components that constitute this circuit.
	 */
	public Stream<ElectricComponent> components() {
		return connections.stream().map(con -> con.component());
	}

	public Stream<WireGroup> wireGroups(WireCrawler map) {
		return connections.stream().limit(size() - 1).map(c -> map.expand(c.wireNext()));
	}

	/**
	 * Returns the next {@code WireGroup} at the end of this circuit. The wire
	 * group can then be used to find the next components to add to this
	 * circuit.
	 *
	 * @param map the wire group generator instance.
	 * @return the {@code WireGroup} that emerges from this circuit.
	 */
	public WireGroup explore(WireCrawler map) {
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
