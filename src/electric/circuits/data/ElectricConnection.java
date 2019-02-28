package electric.circuits.data;

/**
 * Represents a connection to an electric component. An
 * {@code ElectricConnection} stores the side of the component on which the
 * connection is taking place. Such information is crucial in circuit analysis,
 * as current must flow <em>through</em> the component.
 *
 * Assuming that the current comes <em>from</em> the direction stored, a few
 * helper methods are available to determine the wire the current comes from,
 * and the wire it will flow to after flowing through the component.
 *
 * @author Tomer Moran
 */
public class ElectricConnection {

	/**
	 * Whether or not the connection comes from the left of the component. A
	 * value of {@code true} means left, while {@code false} means right.
	 */
	private final boolean left;

	/**
	 * The component in this connection.
	 */
	private final ElectricComponent component;

	public ElectricConnection(ElectricComponent component, boolean left) {
		this.component = component;
		this.left = left;
	}

	/**
	 * Returns from which direction the current is coming from. This method is
	 * effectively the negation of {@link #isRight}.
	 *
	 * @return {@code true} if this connection is coming from the left of the
	 * component, and {@code false} if it comes from the right.
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * Returns from which direction the current is coming from. This method is
	 * effectively the negation of {@link #isLeft}.
	 *
	 * @return {@code false} if this connection is coming from the left of the
	 * component, and {@code true} if it comes from the right.
	 */
	public boolean isRight() {
		return !left;
	}

	/**
	 *
	 * @return the component of this {@code ElectricConnection}.
	 */
	public ElectricComponent component() {
		return component;
	}

	/**
	 *
	 * @return the {@code ElectricWire} from which the current is coming from
	 * before arriving at the component.
	 */
	public ElectricWire wirePrevious() {
		return left ? component.leftWire() : component.rightWire();
	}

	/**
	 *
	 * @return the {@code ElectricWire} towards which the current is flowing to
	 * after having passed through the component.
	 */
	public ElectricWire wireNext() {
		return !left ? component.leftWire() : component.rightWire();
	}

	@Override
	public String toString() {
		return (left ? ">" + component : component + "<");
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.left ? 1 : 0);
		hash = 23 * hash + component.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof ElectricConnection))
			return false;

		ElectricConnection other = (ElectricConnection) obj;
		return this.component == other.component && this.left == other.left;
	}

}
