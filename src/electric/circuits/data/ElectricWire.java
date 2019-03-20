package electric.circuits.data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an electric wire that connects components together. A wire can
 * connect directly at most 2 {@link ElectricComponent}s. However, it can also
 * be branched with other {@code ElectricWire}s, which together can connect any
 * number of components together.
 *
 * In order to find all the components and wires connected together by a single
 * graph of wires, one should refer to
 * {@link ExpandedWireMap}.
 *
 * @author Tomer Moran
 */
public class ElectricWire {

	/**
	 * The two direct connections of this wire. This array always has a size of
	 * 2.
	 */
	private final ElectricConnection[] endpoints;

	/**
	 * A set of all the wires this {@code ElectricWire} is directly connected
	 * to. Note that these wires may also be connected to other wires, forming a
	 * graph of wires.
	 */
	private final Set<ElectricWire> wires;

	public ElectricWire() {
		this.endpoints = new ElectricConnection[2];
		this.wires = new HashSet<>();
	}

	/**
	 * Returns the direction component connections of this {@code ElectricWire}.
	 * The array is guaranteed to have size 2, but its elements may be
	 * {@code null}. The first element of the array represents the 'left'
	 * connection <b>of this {@code ElectricWire}</b>, and likewise the second
	 * one represents the 'right' connection.
	 *
	 * @return the direct connections of this {@code ElectricWire}.
	 */
	public ElectricConnection[] endpoints() {
		return endpoints;
	}

	/**
	 *
	 * @return the set of all directly connected wires. May be empty.
	 */
	public Set<ElectricWire> wires() {
		return wires;
	}
	
}
