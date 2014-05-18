package main;

import org.lwjgl.opengl.GL11;

public class Structure {

	public Vbo vbo;
	public Camera camera = new Camera();
	
	public Structure(){
		
	}

	public void draw(Program program, boolean fill) {
		camera.applyInvertedMatrix();
		vbo.draw(fill);
		camera.applyMatrix();
	}

	public void initVBOs(Program program) {
		vbo = new Vbo(GL11.GL_QUADS);
		vbo.setColor(Color.blue);
		vbo.addRectPrism(new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		vbo.end();
	}

}
