package electric.circuits.data;

import electric.circuits.component.DummyBatteryComponent;
import electric.circuits.component.DummyComponent;
import electric.circuits.simulation.SimulationContext;

/**
 * Represents a type of electric component that can be used to build circuits.
 *
 * @author Tomer Moran
 */
public enum ComponentType {

    /**
     * Provides a fixed voltage source.
     */
    BATTERY,
    /**
     * Produces light of a certain color.
     */
    LED,
    /**
     * Provides a resistance to the circuit; this reduces the voltage and the
     * current.
     */
    RESISTANCE;

    public ElectricComponent create(SimulationContext context) {
        // TODO: implement method
        switch (this) {
            case BATTERY:
                return new DummyBatteryComponent(context, "BATTERY", 15);
            case LED:
                return new DummyComponent(context, "LED");
            case RESISTANCE:
                return new DummyComponent(context, "RESISTOR");
            default:
                throw new AssertionError("Unimplemented case " + this);
        }
    }
}
