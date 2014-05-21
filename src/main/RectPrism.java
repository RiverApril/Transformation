package main;

import org.lwjgl.util.vector.Vector3f;

public class RectPrism {

	private static final double l = .001;
	
	private Vector3d position;
	private Vector3d radii;
	private Vector3d min;
	private Vector3d max;

	public RectPrism(Vector3d position, Vector3d radii) {
		this.position = position;
		this.radii = radii;
		setMinMax();
	}

	public boolean collides(RectPrism other) {
	    return(this.max.x-l > other.min.x+l && 
	    		this.min.x+l < other.max.x-l &&
	    		this.max.y-l > other.min.y+l &&
	    		this.min.y+l < other.max.y-l &&
	    		this.max.z-l > other.min.z+l &&
	    this.min.z+l < other.max.z-l);

	}

	public void setPosition(Vector3d pos) {
		this.position.set(pos);
		setMinMax();
	}

	private void setMinMax() {
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
		setMinMax();
		
	}

	public void setRadii(Vector3d radii) {
		this.radii.set(radii);
		setMinMax();
	}
	
	public boolean rayCollide(Vector3d o, Vector3d d, double minD, double maxD){
	    double tmin = (min.x - o.x) / d.x;
	    double tmax = (max.x - o.x) / d.x;
	    if (tmin > tmax) {double t = tmin;tmin = tmax;tmax=t;};
	    double tymin = (min.y - o.y) / d.y;
	    double tymax = (max.y - o.y) / d.y;
	    if (tymin > tymax) {double t = tymin;tymin = tymax;tymax=t;};
	    if ((tmin > tymax) || (tymin > tmax))
	        return false;
	    if (tymin > tmin)
	        tmin = tymin;
	    if (tymax < tmax)
	        tmax = tymax;
	    double tzmin = (min.z - o.z) / d.z;
	    double tzmax = (max.z - o.z) / d.z;
	    if (tzmin > tzmax) {double t = tzmin;tzmin = tzmax;tzmax=t;};
	    if ((tmin > tzmax) || (tzmin > tmax))
	        return false;
	    if (tzmin > tmin)
	        tmin = tzmin;
	    if (tzmax < tmax)
	        tmax = tzmax;
	    if ((tmin > maxD) || (tmax < minD)) return false;
	    if (minD < tmin) minD = tmin;
	    if (maxD > tmax) maxD = tmax;
	    return true;
	}

}
