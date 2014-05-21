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

	public void setPosition(Vector3d pos) {
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

	public void setRadii(int x, int y, int z) {
		radii.x = x;
		radii.y = y;
		radii.z = z;
		this.min = position.sub(radii);
		this.max = position.add(radii);
		
	}

	public void setRadii(Vector3d radii) {
		this.radii.set(radii);
		this.min = position.sub(radii);
		this.max = position.add(radii);
	}

}
