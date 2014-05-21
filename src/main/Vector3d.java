package main;

import org.lwjgl.util.vector.Vector;
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

	public Vector3d(Vector3d a) {
		this.x = a.x;
		this.y = a.y;
		this.z = a.z;
	}

	public Vector3d(Vector3d a, Vector3d b) {
		this.x = a.x+b.x;
		this.y = a.y+b.y;
		this.z = a.z+b.z;
	}

	private void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d invert() {
		x=-x;
		y=-y;
		z=-z;
		return this;
	}

	public void set(Vector3d position) {
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

	public Vector3d mult(double i) {
		return new Vector3d(x*i, y*i, z*i);
	}

	public static Vector3d sub(Vector3d left, Vector3d right, Vector3d dest) {
		if (dest == null)
			return new Vector3d(left.x - right.x, left.y - right.y, left.z - right.z);
		else {
			dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
			return dest;
		}
	}
	
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	public Vector3d normalise() {
		double l = length();

		return new Vector3d(x / l, y / l, z / l);
	}
	
	public static Vector3d cross(
			Vector3d left,
			Vector3d right,
			Vector3d dest)
	{

		if (dest == null)
			dest = new Vector3d();

		dest.set(
				left.y * right.z - left.z * right.y,
				right.x * left.z - right.z * left.x,
				left.x * right.y - left.y * right.x
				);

		return dest;
	}

	public Vector3d scale(double scale) {

		x *= scale;
		y *= scale;
		z *= scale;

		return this;

	}
	
	public static Vector3d add(Vector3d left, Vector3d right, Vector3d dest) {
		if (dest == null)
			return new Vector3d(left.x + right.x, left.y + right.y, left.z + right.z);
		else {
			dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
			return dest;
		}
	}

	public static double dot(Vector3d left, Vector3d right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}

	public Vector3d mult(Vector3f other) {
		return new Vector3d(other.x*x, other.y*y, other.z*z);
	}

	public Vector3d round(double l) {
		return new Vector3d(round(x, l), round(y, l), round(z, l));
	}

	private double round(double v, double l) {
		return (Math.round(v/l))*l;
	}
}
