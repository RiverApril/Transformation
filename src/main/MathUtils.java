package main;



public class MathUtils {

    /**
     * Acceptable tolerance for comparisons between floats that are not
     * much larger than 1f.
     */
	public static final float EPSILON = Math.ulp(1f);

	/**
     * Checks the equality of two floats using ULP (units of least precision).
	 * 
	 * @param a - first float
	 * @param b - second float
	 * @return true if the distance between floats a and b is within 5 ULPs of
	 *         their average, false otherwise.
	 */
	public static boolean floatEqualsUlp(float a, float b) {
		return  Math.abs(a - b) <= 5 * Math.ulp(0.5f * (a + b));
	}

    /**
     * Checks the equality of two floats using adjustable ULP (units of least
     * precision).
     * 
     * @param a - first float
     * @param b - second float
     * @param numUlp - number of units of least precision to use to test equality
     * @return true if the distance between floats a and b is within
     *         <code>numUlps</code> ULPs of their average, false otherwise.
     */
	public static boolean floatEqualsUlp(float a, float b, int numUlp) {
		return  Math.abs(a - b) <= numUlp * Math.ulp(0.5f * (a + b));
	}

    /**
     * Absolute tolerance comparison.
     * 
     * @param a
     * @param b
     * @param epsilon
     * @return true if the absolute distance between the arguments
     *         <code>a</code> and <code>b</code> is less than or equal to
     *         epsilon, and false otherwise.
     */
	public static boolean floatEqualsAbs(float a, float b, float epsilon) {
	    return Math.abs(a - b) <= epsilon;
	}

    /**
     * Absolute tolerance comparison. Uses a predefined tolerance level of
     * <code>MathUtils.EPSILON</code>.
     * 
     * @param a
     * @param b
     * @return true if the absolute distance between the arguments
     *         <code>a</code> and <code>b</code> is less than or equal to
     *         <code>MathUtils.EPSILON</code>, and false otherwise.
     */
	public static boolean floatEqualsAbs(float a, float b) {
	    return Math.abs(a - b) <= EPSILON;
	}

    /**
     * Relative tolerance comparison.
     * 
     * The tolerance used in the comparison is automatically adjusted based on the
     * magnitudes of the compared floats.
     * 
     * @param a
     * @param b
     * @return true if the absolute distance between the arguments
     *         <code>a</code> and <code>b</code> is less than or equal to their
     *         relative tolerance, and false otherwise.
     */
	public static boolean floatEqualsRel(float a, float b) {
	    return Math.abs(a - b) <= EPSILON * (Math.abs(a) + Math.abs(b) + 1f);
	}

	public static double squaredAbs(double d) {
		return (d<0?-d:d)*(d<0?-d:d);
	}

}