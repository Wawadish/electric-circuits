package electric.circuits.simulation;

import electric.circuits.Utils;
import electric.circuits.component.BatteryComponent;
import electric.circuits.component.ElectricComponent;
import electric.circuits.component.ElectricConnection;
import electric.circuits.wire.WireGroup;
import electric.circuits.wire.WireMap;
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
public class SimulationContext {

	private final Map<ElectricComponent, Variable> currentVariables;

	public SimulationContext() {
		this.currentVariables = new IdentityHashMap<>();
	}

	public Variable getVariable(ElectricComponent comp) {
		if (comp == null) {
			throw new NullPointerException();
		}

		return currentVariables.computeIfAbsent(comp, c -> new Variable());
	}

	public void setVariable(ElectricComponent comp, Variable var) {
		if (comp == null || var == null) {
			throw new NullPointerException();
		}

		currentVariables.put(comp, var);
	}

	public void clearVariables() {
		currentVariables.clear();
	}

	public void runSimulation(BatteryComponent battery) {
		try {
			// 1. Find all paths
			WireMap wireMap = new WireMap();
			Set<CircuitPath> paths = explorePaths(battery, true, wireMap);
			paths.stream().forEach(Utils::debug);

			Utils.debug("Found " + paths.size() + " paths");

			paths.stream().flatMap(c -> c.wireGroups(wireMap)).forEach(wg -> {
				Utils.debug("Wire group: " + wg);
			});

			// 2. Reduce the unknowns
			paths.stream().flatMap(c -> c.wireGroups(wireMap))
					.distinct()
					.filter(WireGroup::hasTwoConnections)
					.forEach(wg -> {
						// Collect the two components
						Iterator<ElectricConnection> it = wg.connections().iterator();
						ElectricConnection conn1 = it.next();
						ElectricConnection conn2 = it.next();

						// Bind the two components together
						boolean bound = BoundVariable.bind(conn1.component(), conn2.component());
						Utils.debug("binding " + conn1.component() + " and " + conn2.component() + ": " + bound);
					});

			// Count wire groups
			int wireGroups = (int) paths.stream().flatMap(c -> c.wireGroups(wireMap)).count();

			// 3. Prepare the matrix equations
			Map<Variable, Integer> inverseIndex = new IdentityHashMap<>();
			List<Variable> index = paths.stream().flatMap(CircuitPath::components).map(ElectricComponent::current).filter(v -> !(v instanceof BoundVariable)).distinct().collect(Collectors.toList());
			for (int i = 0; i < index.size(); ++i) {
				inverseIndex.put(index.get(i), i);
			}

			// TODO: support multiple batteries
			SimpleMatrix matrix = new SimpleMatrix(paths.size() + wireGroups, index.size());
			SimpleMatrix constants = new SimpleMatrix(paths.size() + wireGroups, 1);

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

				double voltage = 0;
				for (ElectricConnection conn : path) {
					if (!(conn.component() instanceof BatteryComponent)) {
						continue;
					}

					double v = ((BatteryComponent) conn.component()).voltage();
					voltage += (conn.isLeft() ? -v : v);
				}

				constants.set(r, 0, voltage);
				Utils.debug("Total voltage: " + voltage);

				for (Iterator<WireGroup> it = path.wireGroups(wireMap).iterator(); it.hasNext();) {
					int r2 = row++;
					WireGroup wg = it.next();
					Utils.debug("Expanding " + wg);
					if (wg.hasTwoConnections()) {
						continue;
					}

					wg.connections().stream().forEach(conn -> {
						ElectricComponent comp = conn.component();
						Variable parent = comp.current().parent();
						Integer col = inverseIndex.get(parent);
						if (col == null) {
							return;
						}

						int x = (conn.isLeft()) ? -1 : 1;
						matrix.set(r2, col, x + matrix.get(r2, col));
						Utils.debug("Adding " + x + " for " + comp.getType() + ": " + (matrix.get(r2, col)) + " at " + r2 + ", " + col);
						constants.set(r2, 0, 0);
					});
				}

			}

			Utils.debug(matrix);
			Utils.debug(constants);

			// Solve the equations and set the variables
			SimpleMatrix x = matrix.solve(constants);

			for (int i = 0; i < index.size(); ++i) {
				double current = x.get(i, 0);
				index.get(i).solve(current);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Set<CircuitPath> explorePaths(ElectricComponent start, boolean left, WireMap wireMap) {

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
				if (path.isClosedWith(conn) && path.size() > 1) {
					solutions.add(new CircuitPath(path));
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

}
