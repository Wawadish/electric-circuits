package electric.circuits.simulation;

import electric.circuits.Utils;
import electric.circuits.data.DummyComponent;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Tomer Moran
 */
public class DummySimulation {
	
	public static void main(String[] args) {
		DummyComponent battery = new DummyComponent();
		DummyComponent led = new DummyComponent();
		
		ElectricWire wire1 = new ElectricWire();
		ElectricWire wire2 = new ElectricWire();
		
		
		Utils.connect(battery, wire1, false, true);
		Utils.connect(battery, wire2, true, false);
		Utils.connect(led, wire1, true, false);
		Utils.connect(led, wire2, false, true);
		
		
	}
	
	private Set<CircuitPath> explorePaths(ElectricComponent start, boolean left) {
		ExpandedWireMap wireMap = new ExpandedWireMap();
		
		Set<CircuitPath> solutions = new HashSet<>();
		Queue<CircuitPath> queue = new LinkedList<>();
		queue.add(new CircuitPath(new ElectricConnection(start, left)));
		
		while (!queue.isEmpty()) {
			CircuitPath path = queue.poll();
			
			Set<ElectricConnection> connections = path.next(wireMap);
			if (connections.isEmpty())
				continue;
			
			
			for (ElectricConnection conn : connections) {
				if (start == conn.component()) {
					solutions.add(new CircuitPath(path));
					continue;
				}
				
				if (path.contains(conn.component()))
					continue;
				
				
			}
		}
	}
}
