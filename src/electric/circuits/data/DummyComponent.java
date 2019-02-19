package electric.circuits.data;

/**
 *
 * @author Tomer Moran
 */
public class DummyComponent extends ElectricComponent {

	private final String name;
	
	public DummyComponent(String name) {
		super(null);
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
