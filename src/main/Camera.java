package main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3d centerPosition = new Vector3d(0, 0, -1);  // world space coordinates.
	private Vector3d eyePosition = new Vector3d(0, 0, 0);      // world space coordinates.

	public Quaternion orientation = new Quaternion(0, 0, 0, 1); // Identity.

	public static final Vector3d X_AXIS = new Vector3d(1, 0, 0);
	public static final Vector3d Y_AXIS = new Vector3d(0, 1, 0);
	public static final Vector3d Z_AXIS = new Vector3d(0, 0, 1);

	public Vector3d l = new Vector3d(-1, 0, 0);
	public Vector3d f = new Vector3d(0, 0, -1);
	public Vector3d u = new Vector3d(0, 1, 0);

	public void setPosition(double x, double y, double z) {
		eyePosition.x = x;
		eyePosition.y = y;
		eyePosition.z = z;
	}

	public void setPosition(Vector3d position) {
		setPosition(position.x, position.y, position.z);
	}

	public void translate(double x, double y, double z) {
		eyePosition.x += x;
		eyePosition.y += y;
		eyePosition.z += z;
	}

	public void translate(Vector3d vec) {
		translate(vec.x, vec.y, vec.z);
	}

	public void translateRelative(double left, double up, double forward) {
		eyePosition.x += (left * l.x) + (up * u.x) + (forward * f.x);
		eyePosition.y += (left * l.y) + (up * u.y) + (forward * f.y);
		eyePosition.z += (left * l.z) + (up * u.z) + (forward * f.z);
	}

	public void translateRelative(Vector3f vec) {
		translateRelative(vec.x, vec.y, vec.z);
	}

	public Vector3d getPosition() {
		return new Vector3d(eyePosition.x, eyePosition.y, eyePosition.z);
	}

	public void getPosition(Vector3d dest) {
	    dest.x = eyePosition.x;
	    dest.y = eyePosition.y;
	    dest.z = eyePosition.z;
	}

	public void lookAt(double eyeX, double eyeY, double eyeZ, double centerX,
			double centerY, double centerZ, double upX, double upY, double upZ) {

		centerPosition.x = centerX;
		centerPosition.y = centerY;
		centerPosition.z = centerZ;

		eyePosition.x = eyeX;
		eyePosition.y = eyeY;
		eyePosition.z = eyeZ;

		// f = centerPosition - eyePosition.
		Vector3d.sub(centerPosition, eyePosition, f);

		// Do nothing if centerPosition ~= eyePostion.
		if (f.length() <= MathUtils.EPSILON) return;

		f.normalise();

		u.x = upX;
		u.y = upY;
		u.z = upZ;

		// l = u x f
		Vector3d.cross(u, f, l);
		l.normalise();

		// u = f x l
		Vector3d.cross(f, l, u);

		// Flip f so it points along camera's local z-axis.
		f.scale(-1f);

		// Flip l so it points along camera's local x-axis.
		l.scale(-1f);

		// Construct orientation from the basis vectors s, u, f.
		orientation.fromAxes(l, u, f);

		// Reset camera's f and l vectors.
		f.scale(-1f);
		l.scale(-1f);
	}

	public void lookAt(Vector3d eyePos, Vector3d centerPos, Vector3d upDir) {
		lookAt(eyePos.x, eyePos.y, eyePos.z, centerPos.x, centerPos.y,
				centerPos.z, upDir.x, upDir.y, upDir.z);
	}


	public void lookAt(double centerX, double centerY, double centerZ) {
		centerPosition.x = centerX;
		centerPosition.y = centerY;
		centerPosition.z = centerZ;

		// f = center - eye.
		Vector3d.sub(centerPosition, eyePosition, f);

		// If center ~= eyePosition, do nothing.
		if (f.lengthSquared() <= MathUtils.EPSILON) return;

		f.normalise();

		// The following projects u onto the plane defined by the point eyePosition,
		// and the normal f. The goal is to rotate u so that it is orthogonal to f,
		// while attempting to keep u's orientation close to its previous direction.
		{
			// Borrow l vector for calculation, so we don't have to allocate a
			// new vector.
			// l = eye + u
			Vector3d.add(eyePosition, u, l);

			// t = -1 * (f dot u)
			double t = -1f * Vector3d.dot(f, u);

			// Move point l in the normal direction, f, by t units so that it is
			// on the plane.
			l.x += t * f.x;
			l.y += t * f.y;
			l.z += t * f.z;

			// u = l - eye.
			Vector3d.sub(l, eyePosition, u);
			u.normalise();
		}

		// Update l vector given new f and u vectors.
		// l = u x f
		Vector3d.cross(u, f, l);

		// If f and u are no longer orthogonal, make them so.
		if (Vector3d.dot(f, u) > MathUtils.EPSILON) {
			// u = f x l
			Vector3d.cross(f, l, u);
			u.normalise();
		}

		// Flip f so it points along camera's local z-axis.
		f.scale(-1f);

		// Flip l so it points along camera's local x-axis.
		l.scale(-1f);

		// Construct orientation from the basis vectors l, u, f, which make up the
		// camera's local right handed coordinate system.
		orientation.fromAxes(l, u, f);

		// Reset camera's f and l vectors, so f points forward, and l points to the
		// left, as seen by the camera.
		f.scale(-1f);
		l.scale(-1f);
	}

	public void lookAt(Vector3d centerPos) {
		lookAt(centerPos.x, centerPos.y, centerPos.z);
	}

	public void rotate(Vector3d axis, double angle) {
		Quaternion p = new Quaternion(axis, angle);
		Quaternion.mult(p, orientation, orientation);
		orientation.normalize();
	}

	public void roll(double angle) {
		Vector3d localZAxis = new Vector3d(Z_AXIS);
		orientation.rotate(localZAxis);

		Quaternion q = new Quaternion(localZAxis, angle);

		// Update camera's local left and up vectors.
		q.rotate(l);
		q.rotate(u);

		// orientation = q * orientation.
		Quaternion.mult(q, orientation, orientation);
	}

	public void pitch(double angle) {
		Vector3d localXAxis = new Vector3d(X_AXIS);
		orientation.rotate(localXAxis);

		Quaternion q = new Quaternion(localXAxis, angle);

		// Update camera's local up and forward vectors.
		q.rotate(u);
		q.rotate(f);

		// orientation = q * orientation.
		Quaternion.mult(q, orientation, orientation);
	}

	public void yaw(double angle) {
		Vector3d localYAxis = new Vector3d(Y_AXIS);
		orientation.rotate(localYAxis);

		Quaternion q = new Quaternion(localYAxis, angle);

		// Update camera's local left and forward vectors.
		q.rotate(l);
		q.rotate(f);

		// orientation = q * orientation.
		Quaternion.mult(q, orientation, orientation);
	}
	
	public Matrix4f getViewMatrix() {
		orientation.normalize();

		// Each column of the viewMatrix describes a camera basis vector using
		// world space coordinates.
		//
		// | s_x | u_x | f_x | 0 |
		// | s_y | u_y | f_y | 0 |
		// | s_z | u_z | f_z | 0 |
		// |  0  |  0  |  0  | 1 |
		Matrix4f viewMatrix = orientation.toRotationMatrix();

		// Transpose the viewMatrix so that each column describes a world space
		// basis vector using camera space coordinates.
		//
		// | s_x | s_y | s_z | 0 |
		// | u_x | u_y | u_z | 0 |
		// | f_x | f_y | f_z | 0 |
		// |  0  |  0  |  0  | 1 |
		viewMatrix.transpose();

		// Apply inverse translation from world space origin to
		// the camera's eye position.
		Vector3f dist = new Vector3f();
		dist.x = (float)((-1) * eyePosition.x);
		dist.y = (float)((-1) * eyePosition.y);
		dist.z = (float)((-1) * eyePosition.z);
		viewMatrix.translate(dist);


		return viewMatrix;
	}
	
	public Vector3d getRay(){
		return new Vector3d(f.x, f.y, f.z).normalise();
	}

	public void applyMatrix() {
		Matrix4f matrix = getViewMatrix();
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);
		matrix.store(matrixBuffer);
		matrixBuffer.rewind();
		GL11.glMultMatrix(matrixBuffer);
		
	}

	public void applyInvertedMatrix() {
		Matrix4f matrix = (Matrix4f) getViewMatrix().invert();
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);
		matrix.store(matrixBuffer);
		matrixBuffer.rewind();
		GL11.glMultMatrix(matrixBuffer);
		
	}
}