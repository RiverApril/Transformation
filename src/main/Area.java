package main;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Area {

	protected Vbo vboQuads = new Vbo();
	
	public ArrayList<RectPrism> rectPrisms = new ArrayList<RectPrism>();
	
	public boolean needsUpdate = false;
	
	public Area(){
		
	}

	public void createRandomBoxes() {
		Random rand = new Random();
		for(int i=0;i<100000;i++){
			rectPrisms.add(new RectPrism(new Vector3d(rand.nextInt(1000)-500, rand.nextInt(1000)-500, rand.nextInt(1000)-500), new Vector3d(rand.nextFloat()*5+1, rand.nextFloat()*5+1, rand.nextFloat()*5+1)));
		}
	}

	public void initVBOs(Program program) {
		
		vboQuads.begin(GL11.GL_QUADS);
		Random rand = new Random();
		for(int i=0;i<rectPrisms.size();i++){
			vboQuads.setColor(new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
			vboQuads.addRectPrism(rectPrisms.get(i));
		}
		
		vboQuads.end();
	}

	public void draw(Program program, boolean fill, int i) {
		vboQuads.draw(fill);
	}
	
	public void update(Program program){
		if(needsUpdate){
			needsUpdate = false;
			initVBOs(program);
		}
		
	}

}
