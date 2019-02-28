package electric.circuits.simulation;

import electric.circuits.data.ElectricComponent;
import electric.circuits.data.Variable;
import java.util.IdentityHashMap;
import java.util.Map;

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
}
