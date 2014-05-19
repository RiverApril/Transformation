package main;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Ship extends Area {
	
	public Ship(){
		rectPrisms.add(new RectPrism(new Vector3d(0, 0, 0), new Vector3d(5, .1, 1)));
	}

	@Override
	public void initVBOs(Program program) {
		
		vboQuads = new Vbo(GL11.GL_QUADS);
		
		vboQuads.setColor(new Color(.5, .5, .5));
		for(int i=0;i<rectPrisms.size();i++){
			vboQuads.addRectPrism(rectPrisms.get(i));
		}
		
		vboQuads.end();
	}

}
