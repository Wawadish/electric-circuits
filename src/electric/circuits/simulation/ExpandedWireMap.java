package electric.circuits.simulation;

import electric.circuits.data.ElectricWire;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author cstuser
 */
public class ExpandedWireMap {
    
    private final Map<ElectricWire, ExpandedWire> map = new IdentityHashMap<>();
    
    private ExpandedWire expand(ElectricWire wire) {
	ExpandedWire ew = map.get(wire);
	if (ew != null)
	    return ew;
	
	ew = new ExpandedWire();
	
	Queue<ElectricWire> queue = new LinkedList<>();
	Set<ElectricWire> visited = new HashSet<>();
	queue.add(wire);
	
	while (!queue.isEmpty()) {
	    ElectricWire w = queue.poll();
	    visited.add(w);
	    ew.connect(w);
	    
	    for (ElectricWire w2 : w.wires()) {
		ElectricWire ew2 = map.get(wire);
		if (ew2 != null)
		    
	    } 
	}
	
	
    }
    
}
