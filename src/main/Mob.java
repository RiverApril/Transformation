package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Mob {

	protected RectPrism bounds;

	public Vbo vbo;
	
	public Vector3d motion = new Vector3d();
	
	public Mob(Vector3d position, Vector3d radii){
		bounds = new RectPrism(position, radii);
		vbo = new Vbo(GL11.GL_QUADS);
		vbo.addRectPrism(position, radii);
		vbo.end();
	}

	
	public void draw(Program program, boolean fill){
		//draw(program, fill);
		GL11.glTranslated(bounds.getPosition().x, bounds.getPosition().y, bounds.getPosition().z);
		vbo.draw(fill);
		GL11.glTranslated(-bounds.getPosition().x, -bounds.getPosition().y, -bounds.getPosition().z);
	}

	
	public void update(Program program) {
		
		
		for(int i=0;i<program.currentArea.mobs.size();i++){
			if(program.currentArea.mobs.get(i)!=this){
				applyGravity(program.currentArea.mobs.get(i).bounds);
			}
		}
		
		
		//camera.translateRelative(checkCollision(program.currentArea, newPlace));
		//motion.x += (newPlace.x * camera.l.x) + (newPlace.y * camera.u.x) + (newPlace.z * camera.f.x);
		//motion.y += (newPlace.x * camera.l.y) + (newPlace.y * camera.u.y) + (newPlace.z * camera.f.y);
		//motion.z += (newPlace.x * camera.l.z) + (newPlace.y * camera.u.z) + (newPlace.z * camera.f.z);
		checkCollisionAndMove(program.currentArea, motion);
		

	}

	protected void applyGravity(RectPrism rp) {
		double ds = MathUtils.squaredAbs(bounds.getPosition().x-rp.getPosition().x)+MathUtils.squaredAbs(bounds.getPosition().y-rp.getPosition().y)+MathUtils.squaredAbs(bounds.getPosition().z-rp.getPosition().z);
		if(ds==0){
			ds = .00001;
		}
		rp.getMass();
		double f = ((rp.getMass()*bounds.getMass())/ds)*.0005;
		//System.out.println("f: "+f+"  sub:"+rp.getPosition().sub(bounds.getPosition())+"  sub&norm:"+rp.getPosition().sub(bounds.getPosition()).normal()+"  sub&norm&mul:"+rp.getPosition().sub(bounds.getPosition()).normal().mul(f));
		motion = motion.add(rp.getPosition().sub(bounds.getPosition()).normal().mul(f));
	}


	protected void checkCollisionAndMove(Area area, Vector3d newPlace) {
		
		Vector3d friction = new Vector3d(1, 1, 1);
		double f = .95;
		
		RectPrism xBounds = new RectPrism(new Vector3d(bounds.getPosition().x+newPlace.x, bounds.getPosition().y, bounds.getPosition().z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(xBounds)){
				newPlace.x = 0;
				friction.y = f;
				friction.z = f;
				break;
			}
		}
		bounds.setPosition(bounds.getPosition().add(new Vector3d(newPlace.x, 0, 0)));
		
		RectPrism yBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y+newPlace.y, bounds.getPosition().z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(yBounds)){
				newPlace.y = 0;
				friction.x = f;
				friction.z = f;
				break;
			}
		}
		bounds.setPosition(bounds.getPosition().add(new Vector3d(0, newPlace.y, 0)));
		
		RectPrism zBounds = new RectPrism(new Vector3d(bounds.getPosition().x, bounds.getPosition().y, bounds.getPosition().z+newPlace.z), bounds.getRadii());
		for(int i=0;i<area.mobs.size();i++){
			RectPrism rectPrism = area.mobs.get(i).bounds;
			if(rectPrism.collides(zBounds)){
				newPlace.z = 0;
				friction.x = f;
				friction.y = f;
				break;
			}
		}
		bounds.setPosition(bounds.getPosition().add(new Vector3d(0, 0, newPlace.z)));
		motion = motion.mul(friction);
	}
}
