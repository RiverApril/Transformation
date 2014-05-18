package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Player {

	public double yaw = 0;
	public double pitch = 0;

	public Vector3d position = new Vector3d();

	public float walkSpeed = .2f;
	public Vector2d tempWalk = new Vector2d();
	private float size = 1;

	public void update(Program program) {
		int xdiff = (Display.getWidth() / 2) - Mouse.getX();
		int ydiff = (Display.getHeight() / 2) - Mouse.getY();
		
		if(Mouse.isGrabbed()){
			Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));

			yaw -= (xdiff)/2.0;
			pitch += (ydiff)/2.0;

			if (pitch > 90) pitch = 90;
			if (pitch < -90) pitch = -90;
			if (yaw > 360) yaw -= 360;
			if (yaw < 0) yaw += 360;
		}


		int angle = 0;
		boolean move = false;
		tempWalk.x=0;
		tempWalk.y=0;
		
		boolean left = KeyboardControl.down(KeyboardControl.keyMoveLeft);
		boolean right = KeyboardControl.down(KeyboardControl.keyMoveRight);

		if (KeyboardControl.down(KeyboardControl.keyMoveForward)) {
			move = true;
			if(left){
				angle = 90-45;
			}else if(right){
				angle = 90+45;
			}else{
				angle = 90;
			}
		}else if (KeyboardControl.down(KeyboardControl.keyMoveBackward)) {
			move = true;
			if(left){
				angle = 270+45;
			}else if(right){
				angle = 270-45;
			}else{
				angle = 270;
			}
		}else if(left){
			angle=0;
			move = true;
		}else if(right){
			angle=180;
			move = true;
		}
		
		if (move) {
			tempWalk.add(moveAngle(angle));
		}

		boolean moveX = true;
		boolean moveZ = true;
		
		if(!KeyboardControl.down(KeyboardControl.keySpace)){
			/*for (Line wall : program.currentLevel.walls[(int)position.y/program.currentLevel.floorSize]) {
				if(wall!=null){
					if (wall.intersectionWithRectangle(position.x - size + (tempWalk.x), position.z - size, position.x + size + (tempWalk.x), position.z + size)) {
						moveX = false;
					}
		
					if (wall.intersectionWithRectangle(position.x - size, position.z - size + (tempWalk.y), position.x + size, position.z + size + (tempWalk.y))) {
						moveZ = false;
					}
				}
			}*/
		}
		if(moveX){
			position.x += tempWalk.x;
		}
		if(moveZ){
			position.z += tempWalk.y;
		}

	}

	private Vector2d moveAngle(int angle) {
		double d = Math.toRadians(angle + yaw);
		return new Vector2d(Math.cos(d) * walkSpeed, Math.sin(d) * walkSpeed);
	}

}
