package main;

public class Vector4d {

	public double w;
	public double x;
	public double y;
	public double z;

	public Vector4d(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector4d() {
		this(0, 0, 0, 0);
	}

}
