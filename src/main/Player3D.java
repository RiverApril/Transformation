package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Player3D extends Mob{

	//public Camera camera = new Camera();

	public float moveSpeed = .06f;
	public float rotSpeed = .05f;

	public Camera camera = new Camera();
	
	//public Vbo vbo;
	
	//public Structure structure = new Structure();

	//private RectPrism bounds = new RectPrism(new Vector3d(camera.getPosition()), new Vector3d(1, 1, 1));

	public Player3D(){
		super(new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		camera.setPosition(0, 0, 0);
	}
	
	@Override
	public void update(Program program) {
		int xdiff = (Display.getWidth() / 2) - Mouse.getX();
		int ydiff = (Display.getHeight() / 2) - Mouse.getY();
		
		if(Mouse.isGrabbed()){
			Mouse.setCursorPosition((Display.getWidth() / 2), (Display.getHeight() / 2));

			camera.pitch((float)Math.toRadians(-ydiff/2.0));
			camera.yaw((float)Math.toRadians(xdiff/2.0));
			
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
			camera.roll(rotSpeed);
		}
		
		if (KeyboardControl.down(KeyboardControl.keyRotateRight)) {
			camera.roll(-rotSpeed);
		}
		
		for(int i=0;i<program.currentArea.mobs.size();i++){
			applyGravity(program.currentArea.mobs.get(i).bounds);
		}
		
		/*for(int i=0;i<program.currentArea.rectPrisms.size();i++){
			RectPrism rp = program.currentArea.rectPrisms.get(i);
			double ds = MathUtils.squaredAbs(camera.getPosition().x-rp.getPosition().x)+MathUtils.squaredAbs(camera.getPosition().y-rp.getPosition().y)+MathUtils.squaredAbs(camera.getPosition().z-rp.getPosition().z);
			rp.getMass();
			double f = ((rp.getMass()*bounds.getMass())/ds)*.0005;
			//System.out.println("f: "+f+"  sub:"+rp.getPosition().sub(bounds.getPosition())+"  sub&norm:"+rp.getPosition().sub(bounds.getPosition()).normal()+"  sub&norm&mul:"+rp.getPosition().sub(bounds.getPosition()).normal().mul(f));
			motion = motion.add(rp.getPosition().sub(bounds.getPosition()).normal().mul(f));
		}*/
		
		
		//camera.translateRelative(checkCollision(program.currentArea, newPlace));
		motion.x += (newPlace.x * camera.l.x) + (newPlace.y * camera.u.x) + (newPlace.z * camera.f.x);
		motion.y += (newPlace.x * camera.l.y) + (newPlace.y * camera.u.y) + (newPlace.z * camera.f.y);
		motion.z += (newPlace.x * camera.l.z) + (newPlace.y * camera.u.z) + (newPlace.z * camera.f.z);
		checkCollisionAndMove(program.currentArea, motion);
		

	}

	@Override
	protected void checkCollisionAndMove(Area area, Vector3d newPlace) {
		
		Vector3d friction = new Vector3d(1, 1, 1);
		double f = .95;
		
		RectPrism xBounds = new RectPrism(new Vector3d(bounds.getPosition().x+newPlace.x, bounds.getPosition().y, bounds.getPosition().z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			if(area.mobs.get(i)!=this){
				continue;
			}
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(xBounds)){
				newPlace.x = 0;
				friction.y = f;
				friction.z = f;
				break;
			}
		}
		camera.translate(new Vector3d(newPlace.x, 0, 0));
		bounds.setPosition(camera.getPosition());
		
		RectPrism yBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y+newPlace.y, bounds.getPosition().z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			if(area.mobs.get(i)!=this){
				continue;
			}
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(yBounds)){
				newPlace.y = 0;
				friction.x = f;
				friction.z = f;
				break;
			}
		}
		camera.translate(new Vector3d(0, newPlace.y, 0));
		bounds.setPosition(camera.getPosition());
		
		RectPrism zBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y, bounds.getPosition().z+newPlace.z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			if(area.mobs.get(i)!=this){
				continue;
			}
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(zBounds)){
				newPlace.z = 0;
				friction.x = f;
				friction.y = f;
				break;
			}
		}
		camera.translate(new Vector3d(0, 0, newPlace.z));
		bounds.setPosition(camera.getPosition());
		motion = motion.mul(friction);
	}

	public RectPrism getRectPrism(){
		return bounds;
	}

	public void initVBOs(Program program) {
		//initVBOs(program);
		vbo = new Vbo(GL11.GL_QUADS);
		vbo.setColor(Color.blue);
		vbo.addRectPrism(new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		vbo.end();
	}

}
