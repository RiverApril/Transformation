package main;

public class Color extends Vector4d{

	public Color(double red, double green, double blue) {
		super(red, green, blue, 1);
	}
	
	public Color(double red, double green, double blue, double alpha) {
		super(red, green, blue, alpha);
	}

	public static Color red = new Color(1, 0, 0);
	public static Color green = new Color(0, 1, 0);
	public static Color blue = new Color(0, 0, 1);

}
