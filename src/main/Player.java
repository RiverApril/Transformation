package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Player {

	//public Camera camera = new Camera();

	public float moveSpeed = .2f;
	public float rotSpeed = .05f;
	
	public Structure structure = new Structure();

	private RectPrism bounds = new RectPrism(new Vector3d(structure.camera.getPosition()), new Vector3d(.5, .5, .5));
	
	private boolean thirdPersonMode = false;
	
	private double reach = 10;
	
	private Vbo placeVbo = new Vbo();
	
	private Vector3d placeRadii = new Vector3d(1, 1, 1);

	double l = .2;
	double l2 = l/2.0;
	
	public Player(){
		structure.camera.setPosition(0, 0, 0);
	}
	
	public void draw(Program program, boolean fill){
		if(thirdPersonMode){
			structure.draw(program, fill);
		}
		//placeVbo.draw(fill);
	}
	
	public void update(Program program) {
		int xdiff = (Display.getWidth() / 2) - Mouse.getX();
		int ydiff = (Display.getHeight() / 2) - Mouse.getY();
		
		if(Mouse.isGrabbed()){
			Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));

			structure.camera.pitch((float)Math.toRadians(-ydiff/2.0));
			structure.camera.yaw((float)Math.toRadians(xdiff/2.0));
			
			/*if(thirdPersonMode){
				program.viewOffset.x += ((xdiff/100.0)-program.guiViewOffset.x)/10.0;
				program.viewOffset.y += ((ydiff/100.0)-program.guiViewOffset.y)/10.0;
				program.viewOffset.z = -10;
				
				program.guiViewOffset.x = 0;
				program.guiViewOffset.y = 0;
				program.guiViewOffset.z = 0;
			}else{
				program.viewOffset.x = 0;
				program.viewOffset.y = 0;
				program.viewOffset.z = 0;

				program.guiViewOffset.x += ((xdiff/100.0)-program.guiViewOffset.x)/10;
				program.guiViewOffset.y += ((ydiff/100.0)-program.guiViewOffset.y)/10;
				program.guiViewOffset.z = 0;
			}*/
			program.viewOffset.z = thirdPersonMode?-10:0;
			
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
		
		Vector3d a = new Vector3d();
		//camera.translateRelative(checkCollision(program.currentArea, newPlace));
		a.x = (newPlace.x * structure.camera.l.x) + (newPlace.y * structure.camera.u.x) + (newPlace.z * structure.camera.f.x);
		a.y = (newPlace.x * structure.camera.l.y) + (newPlace.y * structure.camera.u.y) + (newPlace.z * structure.camera.f.y);
		a.z = (newPlace.x * structure.camera.l.z) + (newPlace.y * structure.camera.u.z) + (newPlace.z * structure.camera.f.z);
		structure.camera.translate(checkCollision(program.currentArea, a));
		
		bounds.setPosition(structure.camera.getPosition());
		
		
		if(KeyboardControl.pressed(KeyboardControl.keyToggleThirdPerson)){
			thirdPersonMode = !thirdPersonMode;
		}
		
		/*if(MouseControl.pressed(MouseControl.ButtonLeft) || MouseControl.pressed(MouseControl.ButtonRight)){
			double i;
			double lastGoodLength = 0;
			boolean exit = false;
			int hit = -1;
			RectPrism rp = new RectPrism(new Vector3d(), new Vector3d(0, 0, 0));
			for(i=0;i<reach;i+=.0001){
				rp.setPosition(new Vector3d(structure.camera.getRay().mult(i), new Vector3d(structure.camera.getPosition())));
				for(int j=0;j<program.currentArea.rectPrisms.size();j++){
					RectPrism o = program.currentArea.rectPrisms.get(j);
					if(!rp.collides(o)){
						lastGoodLength = i;
					}else{
						exit = true;
						hit = j;
						break;
					}
				}
				if(exit){
					break;
				}
			}

			placeVbo.begin(GL11.GL_QUADS);
			placeVbo.end();

			boolean xDown = KeyboardControl.down(KeyboardControl.keyEditX);
			boolean yDown = KeyboardControl.down(KeyboardControl.keyEditY);
			boolean zDown = KeyboardControl.down(KeyboardControl.keyEditZ);
			
			if(lastGoodLength<reach-.01 && lastGoodLength>0){
				
				if(MouseControl.pressed(MouseControl.ButtonLeft) && hit>=0){
					program.currentArea.rectPrisms.remove(hit);
					program.currentArea.needsUpdate = true;
				}
				
				rp.setRadii(placeRadii);
				
				exit = false;
				
				for(double k=i;k>0;k-=.0001){
					rp.setPosition(new Vector3d(structure.camera.getRay().mult(k), new Vector3d(structure.camera.getPosition())).round(l2));
					boolean c = false;
					for(int j=0;j<program.currentArea.rectPrisms.size();j++){
						RectPrism o = program.currentArea.rectPrisms.get(j);
						if(rp.collides(o)){
							c = true;
						}
					}
					if(c){
						c = false;
					}else{
						exit = true;
						break;
					}
					if(exit){
						break;
					}
				}
				
				Color o;
				if(!rp.collides(bounds)){
					
					if(MouseControl.pressed(MouseControl.ButtonRight)){
						program.currentArea.rectPrisms.add(rp);
						program.currentArea.needsUpdate = true;
						
					}
					o = Color.tGray;
				}else{
					o = Color.tViolet;
				}
				placeVbo.begin(GL11.GL_QUADS);
				placeVbo.addRectPrism(rp, xDown?Color.tRed:o, yDown?Color.tGreen:o, zDown?Color.tBlue:o);
				placeVbo.end();

				
				int dWheel = Mouse.getDWheel();
				if(xDown){
					if(dWheel>0){
						placeRadii.x+=l2;
					}
					if(dWheel<0){
						placeRadii.x-=l2;
					}
				}
				
				if(yDown){
					if(dWheel>0){
						placeRadii.y+=l2;
					}
					if(dWheel<0){
						placeRadii.y-=l2;
					}
				}
				
				if(zDown){
					if(dWheel>0){
						placeRadii.z+=l2;
					}
					if(dWheel<0){
						placeRadii.z-=l2;
					}
				}
				
				if(placeRadii.x<l2){
					placeRadii.x=l2;
				}
				if(placeRadii.y<l2){
					placeRadii.y=l2;
				}
				if(placeRadii.z<l2){
					placeRadii.z=l2;
				}
				
				if(placeRadii.x>3){
					placeRadii.x=3;
				}
				if(placeRadii.y>3){
					placeRadii.y=3;
				}
				if(placeRadii.z>3){
					placeRadii.z=3;
				}
				
			}
		}*/

	}

	private Vector3d checkCollision(Area area, Vector3d newPlace) {
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
