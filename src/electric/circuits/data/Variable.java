package electric.circuits.data;

import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Tomer Moran
 */
public class Variable {
    
    private boolean solved;
    private final SimpleDoubleProperty value;

    public Variable() {
	this.value = new SimpleDoubleProperty();
    }
    
    public double get() {
	if (solved)
	    return value.get();
	
	throw new IllegalStateException();
    }
    
    public boolean isSolved() {
	return solved;
    }
    
    public void solve(double v) {
	if (solved)
	
	solved = true;
	value.set(v);
    }
    
    public boolean resolve() {
	return false;
    }
}
