package main;

import org.lwjgl.opengl.GL11;

public class Ship extends Area {

	public Ship(){
		
		double s = 100;
		double w = .1;
		double d = s+w;
		
		rectPrisms.add(new RectPrism(new Vector3d(0, -d, 0), new Vector3d(s, w, s)));
		rectPrisms.add(new RectPrism(new Vector3d(0, d, 0), new Vector3d(s, w, s)));
		
		rectPrisms.add(new RectPrism(new Vector3d(-d, 0, 0), new Vector3d(w, s, s)));
		rectPrisms.add(new RectPrism(new Vector3d(d, 0, 0), new Vector3d(w, s, s)));
		
		rectPrisms.add(new RectPrism(new Vector3d(0, 0, -d), new Vector3d(s, s, w)));
		rectPrisms.add(new RectPrism(new Vector3d(0, 0, d), new Vector3d(s, s, w)));
		
		//rectPrisms.add(new RectPrism(new Vector3d(2, 0, 0), new Vector3d(1, 1, 1)));
	}

	@Override
	public void initVBOs(Program program) {
		
		vboQuads.begin(GL11.GL_QUADS);
		
		vboQuads.setColor(new Color(.5, .5, .5));
		for(int i=0;i<rectPrisms.size();i++){
			vboQuads.addRectPrism(rectPrisms.get(i));
		}
		
		vboQuads.end();
	}

}
