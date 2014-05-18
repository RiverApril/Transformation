package main;

import org.lwjgl.util.vector.Vector3f;

public class RectPrism {

	private Vector3d position;
	private Vector3d radii;
	private Vector3d min;
	private Vector3d max;

	public RectPrism(Vector3d position, Vector3d radii) {
		this.position = position;
		this.radii = radii;
		this.min = position.sub(radii);
		this.max = position.add(radii);
	}

	public boolean collides(RectPrism other) {
	    return(this.max.x > other.min.x && 
	    		this.min.x < other.max.x &&
	    		this.max.y > other.min.y &&
	    		this.min.y < other.max.y &&
	    		this.max.z > other.min.z &&
	    this.min.z < other.max.z);

	}

	public void setPosition(Vector3f pos) {
		this.position.set(pos);
		this.min = position.sub(radii);
		this.max = position.add(radii);
	}

	public Vector3d getPosition() {
		return position;
	}

	public Vector3d getRadii() {
		return radii;
	}

}
