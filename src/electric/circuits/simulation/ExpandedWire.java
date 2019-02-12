package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricWire;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class ExpandedWire {

    private final Set<ElectricWire> wires;
    private final Set<ElectricComponent> connections;

    public ExpandedWire() {
	this.connections = new HashSet<>();
	this.wires = new HashSet<>();
    }

    public Set<ElectricComponent> connections() {
	return connections;
    }

    public void connect(ElectricComponent comp) {
	connections.add(comp);
    }

    public void connect(ElectricWire wire) {
	ElectricComponent[] endpoints = wire.endpoints();
	connections.add(endpoints[0]);
	connections.add(endpoints[1]);
    }

    public void connect(ExpandedWire wire) {
	connections.addAll(wire.connections);
    }
}
