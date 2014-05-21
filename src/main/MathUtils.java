package main;



public class MathUtils {
	
	public static final double EPSILON = Math.ulp(1.0);
	
	public static boolean doubleEqualsUlp(double a, double b) {
		return  Math.abs(a - b) <= 5 * Math.ulp(0.5f * (a + b));
	}
	
	public static boolean doubleEqualsUlp(double a, double b, int numUlp) {
		return  Math.abs(a - b) <= numUlp * Math.ulp(0.5f * (a + b));
	}
	
	public static boolean doubleEqualsAbs(double a, double b, double epsilon) {
	    return Math.abs(a - b) <= epsilon;
	}
	
	public static boolean doubleEqualsAbs(double a, double b) {
	    return Math.abs(a - b) <= EPSILON;
	}
	
	public static boolean doubleEqualsRel(double a, double b) {
	    return Math.abs(a - b) <= EPSILON * (Math.abs(a) + Math.abs(b) + 1f);
	}

}