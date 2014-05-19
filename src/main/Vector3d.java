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
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public void set(Vector3f position) {
		x = position.x;
		y = position.y;
		z = position.z;
	}

	public void set(Vector3d position) {
		x = position.x;
		y = position.y;
		z = position.z;
	}

	public Vector3d sub(Vector3d other) {
		return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	public Vector3d add(Vector3d other) {
		return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public Vector3d normal() {
		Vector3d n = new Vector3d();
		double length = Math.sqrt(abs(this.x * this.x) + abs(this.y * this.y) + abs(this.z * this.z));
		if (length != 0) {
			n.x = this.x / length;
			n.y = this.y / length;
			n.z = this.z / length;
		}
		return n;
	}

	private double abs(double d) {
		return d<0?-d:d;
	}

	public Vector3d mul(double v) {
		return new Vector3d(x*v, y*v, z*v);
	}

	public Vector3d mul(Vector3d v) {
		return new Vector3d(x*v.x, y*v.y, z*v.z);
	}
	
	@Override
	public String toString(){
		return x+", "+y+", "+z;
	}

}
