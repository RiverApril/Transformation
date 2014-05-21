package main;

public class Vector2d{

	public double x;
	public double y;

	public Vector2d() {
		this(0, 0);
	}
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2d other) {
		x+=other.x;
		y+=other.y;
	}

}
