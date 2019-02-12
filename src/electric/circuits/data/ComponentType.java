package electric.circuits.data;

/**
 *
 * @author cstuser
 */
public enum ComponentType {

    BATTERY,
    LED,
    RESISTANCE;

    public ElectricComponent create() {
	switch (this) {
	    case BATTERY:
		return new BatteryComponent();
	    case LED:
		return new LedComponent();
	    case RESISTANCE:
		return new Resistance();
	    default:
		throw new AssertionError("Unimplemented case " + this);
	}
    }

}
