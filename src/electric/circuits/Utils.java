package electric.circuits;

/**
 *
 * @author Tomer Moran
 */
public class Utils {

    public static final double EPSILON = 0.001;

    public static boolean equals(double a, double b) {
	return Math.abs(a - b) <= EPSILON;
    }
}
