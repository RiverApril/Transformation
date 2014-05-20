package main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Structure {

	public Vbo vbo;
	public Camera camera = new Camera();
	
	public Structure(){
		
	}

	public void draw(Program program, boolean fill) {
		//camera.applyInvertedMatrix();
		GL11.glTranslatef(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
		vbo.draw(fill);
		
		GL11.glTranslatef(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		//camera.applyMatrix();
	}

	public void initVBOs(Program program) {
		vbo = new Vbo(GL11.GL_QUADS);
		vbo.setColor(new Color(0, 0, 1, .1));
		vbo.addRectPrism(new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		vbo.end();
	}

}
