package electric.circuits.wire;

import electric.circuits.component.ElectricConnection;
import java.util.Set;

/**
 * Represents a group of {@code ElectricWire}s and the combination of all of
 * their {@code ElectricConnection}s. Instances of this class are built by
 * {@link ExpandedWireMap}.
 *
 * @author Tomer Moran
 */
public class WireGroup {

	/**
	 * The set of all wires in this group.
	 */
	private final Set<ElectricWire> wires;

	/**
	 * The set of all connections in this wire group.
	 */
	private final Set<ElectricConnection> connections;

	public WireGroup(Set<ElectricWire> wires, Set<ElectricConnection> connections) {
		this.wires = wires;
		this.connections = connections;
	}

	/**
	 *
	 * @return the set of all wires in this group.
	 */
	public Set<ElectricWire> wires() {
		return wires;
	}

	/**
	 *
	 * @return the set of all connections in this group.
	 */
	public Set<ElectricConnection> connections() {
		return connections;
	}

	/**
	 * Returns whether or not the number of connections is equation to 2. Such
	 * {@code WireGroup}s have the special property that they do not split
	 * current: the two components connected must therefore have the same
	 * current (but not necessarily the same voltage). This realization is
	 * crucial in solving Kirchhoff's equations.
	 *
	 * @return {@code true} if there are 2 connections in this group;
	 * {@code false} otherwise.
	 */
	public boolean hasTwoConnections() {
		return connections.size() == 2;
	}

	@Override
	public String toString() {
		return connections.toString();
	}
}
