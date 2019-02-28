package electric.circuits.data;

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
	 * Provides a resistance to the circuit; this reduces the voltage and the current.
	 */
	RESISTANCE;

	public ElectricComponent create() {
		// TODO: implement method
		switch (this) {
			case BATTERY:
			//return new BatteryComponent();
			case LED:
			//return new LedComponent();
			case RESISTANCE:
			//return new Resistance();
			default:
				throw new AssertionError("Unimplemented case " + this);
		}
	}
}
