package electric.circuits.data;

import java.util.Set;

public class WireGroup {
	private final Set<ElectricWire> wires;
	private final Set<ElectricConnection> connections;

	public WireGroup(Set<ElectricWire> wires, Set<ElectricConnection> connections) {
		this.wires = wires;
		this.connections = connections;
	}

	public Set<ElectricWire> wires() {
		return wires;
	}

	public Set<ElectricConnection> connections() {
		return connections;
	}
	
	public boolean sameCurrent() {
		return connections.size() == 2;
	}

	@Override
	public String toString() {
		return wires.toString();
	}
}
