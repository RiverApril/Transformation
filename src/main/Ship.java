package main;

import org.lwjgl.opengl.GL11;

public class Ship extends Area {
	
	private boolean needsUpdate = false;

	public Ship(){
		rectPrisms.add(new RectPrism(new Vector3d(0, -2, 0), new Vector3d(4, .1, 4)));
		rectPrisms.add(new RectPrism(new Vector3d(0, .1, -4.1), new Vector3d(4, 2, .1)));
		rectPrisms.add(new RectPrism(new Vector3d(0, .1, 4.1), new Vector3d(4, 2, .1)));
		rectPrisms.add(new RectPrism(new Vector3d(-4.1, .1, 0), new Vector3d(.1, 2, 4)));
		rectPrisms.add(new RectPrism(new Vector3d(4.1, .1, 0), new Vector3d(.1, 2, 4)));
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
	
	@Override
	public void update(Program program){
		if(needsUpdate){
			initVBOs(program);
			needsUpdate = false;
		}
		
	}

}
