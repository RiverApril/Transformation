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
	
	public static Color tRed = new Color(1, 0, 0, .2);
	public static Color tGreen = new Color(0, 1, 0, .2);
	public static Color tBlue = new Color(0, 0, 1, .2);
	public static Color tGray = new Color(.5, .5, .5, .2);
	public static Color tViolet = new Color(1, 0, 1, .2);

}
