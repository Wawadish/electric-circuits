package electric.circuits.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class ElectricWire {

	private final ElectricConnection[] endpoints;
	private final Set<ElectricWire> wires;

	public ElectricWire() {
		this.endpoints = new ElectricConnection[2];
		this.wires = new HashSet<>();
	}

	public ElectricConnection[] endpoints() {
		return endpoints;
	}
	
	public Set<ElectricWire> wires() {
		return wires;
	}
	
	

}
