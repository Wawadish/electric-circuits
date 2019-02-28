package electric.circuits.simulation;

import electric.circuits.data.WireCrawler;
import electric.circuits.Utils;
import electric.circuits.component.BatteryComponent;
import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.data.BoundVariable;
import electric.circuits.component.DummyComponent;
import electric.circuits.data.ElectricComponent;
import electric.circuits.data.ElectricConnection;
import electric.circuits.data.ElectricWire;
import electric.circuits.data.Variable;
import electric.circuits.data.WireGroup;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Tomer Moran
 */
public class DummySimulation {

	public static void main(String[] args) {
		SimulationContext context = new SimulationContext();
		WireCrawler wireMap = new WireCrawler();
		
		ElectricComponent battery = new DummyBatteryComponent(context, "Battery", 10);
		ElectricComponent led1 = new DummyComponent(context, "LED1");
		ElectricComponent led2 = new DummyComponent(context, "LED2");
		ElectricComponent led3 = new DummyComponent(context, "LED3");
		ElectricComponent res = new DummyComponent(context, "RES1");

		ElectricWire wire1 = new ElectricWire();
		ElectricWire wire2 = new ElectricWire();
		ElectricWire wire3 = new ElectricWire();
		ElectricWire wire4 = new ElectricWire();
		ElectricWire wire5 = new ElectricWire();
		ElectricWire wire6 = new ElectricWire();
		ElectricWire wire7 = new ElectricWire();
		Utils.connect(wire1, wire3);
		Utils.connect(wire1, wire5);
		Utils.connect(wire2, wire4);
		Utils.connect(wire2, wire7);

		Utils.connect(battery, wire2, wire1);
		Utils.connect(led1, wire1, wire2);
		Utils.connect(led2, wire3, wire4);
		Utils.connect(led3, wire5, wire6);
		Utils.connect(res, wire6, wire7);

		led1.setResistance(2);
		led2.setResistance(3);
		led3.setResistance(5);
		res.setResistance(7);

		// 1. Find all paths
		Set<CircuitPath> paths = explorePaths(battery, true, wireMap);
		paths.stream().forEach(System.out::println);

		// 2. Reduce the unknowns
		paths.stream().flatMap(c -> c.wireGroups(wireMap))
				.filter(WireGroup::hasTwoConnections)
				.forEach(wg -> {
					// Collect the two components
					Iterator<ElectricConnection> it = wg.connections().iterator();
					ElectricConnection conn1 = it.next();
					ElectricConnection conn2 = it.next();

					// Bind the two components together
					System.out.println("binding " + conn1.component() + " and " + conn2.component());
					BoundVariable.bind(conn1.component(), conn2.component());
				});

		// 3. Prepare the matrix equations
		Map<Variable, Integer> inverseIndex = new IdentityHashMap<>();
		List<Variable> index = paths.stream().flatMap(CircuitPath::components).map(ElectricComponent::current).filter(v -> !(v instanceof BoundVariable)).distinct().collect(Collectors.toList());
		for (int i = 0; i < index.size(); ++i) {
			inverseIndex.put(index.get(i), i);
		}

		// TODO: support multiple batteries
		SimpleMatrix matrix = new SimpleMatrix(2 * paths.size(), index.size());
		SimpleMatrix constants = new SimpleMatrix(2 * paths.size(), 1);

		int row = 0;
		for (CircuitPath path : paths) {
			// Get current row
			int r = row++;

			// Add all the resistances
			path.components().forEach(comp -> {
				Variable parent = comp.current().parent();
				int col = inverseIndex.get(parent);
				matrix.set(r, col, comp.resistance() + matrix.get(r, col));
			});

			constants.set(r, 0, getVoltage(path));

			int r2 = row++;
			WireGroup wg = path.explore(wireMap);
			wg.connections().stream().map(ElectricConnection::component).forEach(comp -> {
				Variable parent = comp.current().parent();
				int col = inverseIndex.get(parent);
				int x = (comp instanceof BatteryComponent) ? -1 : 1;
				matrix.set(r2, col, x + matrix.get(r2, col));
			});

			constants.set(r2, 0, 0);
		}

		SimpleMatrix x = matrix.solve(constants);
		System.out.println("Coefficients");
		System.out.println("--------------------");
		System.out.println(matrix);

		System.out.println("Constants");
		System.out.println("--------------------");
		System.out.println(constants);

		System.out.println("Solutions");
		System.out.println("--------------------");
		System.out.println(x);

		for (int i = 0; i < index.size(); ++i) {
			double current = x.get(i, 0);
			index.get(i).solve(current);
		}

		printCurrents(battery, led1, led2, led3, res);

	}

	private static Set<CircuitPath> explorePaths(ElectricComponent start, boolean left, WireCrawler wireMap) {

		// Prepare the search
		Set<CircuitPath> solutions = new HashSet<>();
		Queue<CircuitPath> queue = new LinkedList<>();

		// Insert starting point
		queue.add(new CircuitPath(new ElectricConnection(start, left)));

		// Loop for as long as there are paths to expand
		while (!queue.isEmpty()) {
			CircuitPath path = queue.poll();

			// Check if this wire group was already traversed
			WireGroup group = path.explore(wireMap);
			if (path.contains(group, wireMap)) {
				continue;
			}

			// Loop through potential extensions to current path
			for (ElectricConnection conn : group.connections()) {
				// Check for solution
				if (path.isClosedWith(conn)) {
					solutions.add(path.copyAndAppend(conn));
					continue;
				}

				// Check for inner loop
				if (path.contains(conn.component())) {
					continue;
				}

				// Append to path and keep searching
				queue.add(path.copyAndAppend(conn));
			}
		}

		return solutions;
	}

	private static double getVoltage(CircuitPath path) {
		return ((BatteryComponent) path.components().findFirst().get()).voltage();
	}

	private static void printCurrents(ElectricComponent... comps) {
		for (ElectricComponent c : comps) {
			
			System.out.println(String.format("%s: %.2f", c, c.current().get()));
		}
	}
}
