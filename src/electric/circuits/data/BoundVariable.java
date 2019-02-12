package electric.circuits.data;

import electric.circuits.Utils;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Tomer Moran
 */
public class BoundVariable extends Variable {

    private final Map<Variable, Double> bindings;

    public BoundVariable() {
	this.bindings = new IdentityHashMap<>();
    }

    public void add(Variable var, Double coefficient) {
	if (var == this || var == null || Utils.equals(coefficient, 0))
	    throw new IllegalArgumentException();
	
	bindings.compute(var, (v, c) -> c + coefficient);
    }

    @Override
    public boolean resolve() {
	if (isSolved())
	    return true;
	
	double result = 0;
	
	// Iterate over all the dependencies
	for (Entry<Variable, Double> entry : bindings.entrySet()) {
	    Variable var = entry.getKey();
	    
	    // If the dependency is not yet solved, try to solve it.
	    if (!var.resolve()) {
		return false;
	    }
	    
	    // Add the result 
	    result += (entry.getValue() * var.get());
	}
	
	// Mark as solved
	solve(result);
	return true;
    }
}
