package main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Structure {

	public Vbo vbo = new Vbo();
	public Camera camera = new Camera();
	
	public Structure(){
		
	}

	public void draw(Program program, boolean fill) {
		//camera.applyInvertedMatrix();
		GL11.glTranslated(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
		vbo.draw(fill);
		GL11.glTranslated(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		//camera.applyMatrix();
	}

	public void initVBOs(Program program) {
		vbo.begin(GL11.GL_QUADS);
		vbo.setColor(new Color(0, 0, 1, .1));
		vbo.addRectPrism(new Vector3d(0, 0, 0), new Vector3d(.5, .5, .5));
		vbo.end();
	}

}
