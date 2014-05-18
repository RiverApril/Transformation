package main;

import org.lwjgl.util.vector.Vector3f;

public class Vector3d {

	public double x;
	public double y;
	public double z;

	public Vector3d() {
		this(0, 0, 0);
	}
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(Vector3f p) {
		this(p.x, p.y, p.z);
	}

	public Vector3d invert() {
		x=-x;
		y=-y;
		z=-z;
		return this;
	}

	public void set(Vector3f position) {
		x = position.x;
		y = position.y;
		z = position.z;
	}

	public Vector3d sub(Vector3d other) {
		return new Vector3d(this.x-other.x, this.y-other.y, this.z-other.z);
	}

	public Vector3d add(Vector3d other) {
		return new Vector3d(this.x+other.x, this.y+other.y, this.z+other.z);
	}

}
