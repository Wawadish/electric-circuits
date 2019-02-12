package electric.circuits.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class ElectricWire {
    private final ElectricComponent[] endpoints;
    private final Set<ElectricWire> wires;

    public ElectricWire() {
	this.endpoints = new ElectricComponent[2];
	this.wires = new HashSet<>();
    }

    public ElectricComponent[] endpoints() {
	return endpoints;
    }

    public Set<ElectricWire> wires() {
	return wires;
    }
    
}
