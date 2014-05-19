package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Player {

	//public Camera camera = new Camera();

	public float moveSpeed = .2f;
	public float rotSpeed = .05f;
	
	public Structure structure = new Structure();

	private RectPrism bounds = new RectPrism(new Vector3d(structure.camera.getPosition()), new Vector3d(1, 1, 1));

	public Player(){
		structure.camera.setPosition(10, 10, 10);
	}
	
	public void draw(Program program, boolean fill){
		structure.draw(program, fill);
	}
	
	public void update(Program program) {
		int xdiff = (Display.getWidth() / 2) - Mouse.getX();
		int ydiff = (Display.getHeight() / 2) - Mouse.getY();
		
		if(Mouse.isGrabbed()){
			Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));

			structure.camera.pitch((float)Math.toRadians(-ydiff/2.0));
			structure.camera.yaw((float)Math.toRadians(xdiff/2.0));
			
			program.viewOffset.x = (xdiff/100.0);
			program.viewOffset.y = (ydiff/100.0);
			program.viewOffset.z = -10;
		}
		
		Vector3f newPlace = new Vector3f();
		
		if (KeyboardControl.down(KeyboardControl.keyMoveForward)) {
			newPlace.z+=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyMoveBackward)) {
			newPlace.z-=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyMoveLeft)) {
			newPlace.x+=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyMoveRight)) {
			newPlace.x-=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyMoveUp)) {
			newPlace.y+=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyMoveDown)) {
			newPlace.y-=moveSpeed;
		}
		
		if (KeyboardControl.down(KeyboardControl.keyRotateLeft)) {
			structure.camera.roll(rotSpeed);
		}
		
		if (KeyboardControl.down(KeyboardControl.keyRotateRight)) {
			structure.camera.roll(-rotSpeed);
		}
		
		Vector3f a = new Vector3f();
		//camera.translateRelative(checkCollision(program.currentArea, newPlace));
		a.x = (newPlace.x * structure.camera.l.x) + (newPlace.y * structure.camera.u.x) + (newPlace.z * structure.camera.f.x);
		a.y = (newPlace.x * structure.camera.l.y) + (newPlace.y * structure.camera.u.y) + (newPlace.z * structure.camera.f.y);
		a.z = (newPlace.x * structure.camera.l.z) + (newPlace.y * structure.camera.u.z) + (newPlace.z * structure.camera.f.z);
		structure.camera.translate(checkCollision(program.currentArea, a));
		
		bounds.setPosition(structure.camera.getPosition());

	}

	private Vector3f checkCollision(Area area, Vector3f newPlace) {
		RectPrism xBounds = new RectPrism(new Vector3d(bounds.getPosition().x+newPlace.x, bounds.getPosition().y, bounds.getPosition().z), bounds.getRadii());
		RectPrism yBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y+newPlace.y, bounds.getPosition().z), bounds.getRadii());
		RectPrism zBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y, bounds.getPosition().z+newPlace.z), bounds.getRadii());
		
		for(int i=0;i<area.rectPrisms.size();i++){
			RectPrism rectPrism = area.rectPrisms.get(i);
			if(rectPrism.collides(xBounds)){
				newPlace.x = 0;
			}
			if(rectPrism.collides(yBounds)){
				newPlace.y = 0;
			}
			if(rectPrism.collides(zBounds)){
				newPlace.z = 0;
			}
		}
		
		
		return newPlace;
	}

	public RectPrism getRectPrism(){
		return bounds;
	}

	public void initVBOs(Program program) {
		structure.initVBOs(program);
	}

}
