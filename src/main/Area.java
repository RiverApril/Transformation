package main;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Area {

	private Vbo vboQuads;
	
	public ArrayList<RectPrism> rectPrisms = new ArrayList<RectPrism>();
	
	public ArrayList<Mob> mobs = new ArrayList<Mob>();

	private Random rand;
	
	public Area(){
		rand = new Random();
		//rectPrisms.add(new RectPrism(new Vector3d(0, 0, 0), new Vector3d(30, 30, 30)));
		for(int i=0;i<10000;i++){
			//rectPrisms.add(new RectPrism(new Vector3d(rand.nextInt(1000)-500, rand.nextInt(1000)-500, rand.nextInt(1000)-500), new Vector3d(rand.nextFloat()*5+1, rand.nextFloat()*5+1, rand.nextFloat()*5+1)));
		}
		for(int i=0;i<10;i++){
			mobs.add(new Mob(new Vector3d(rand.nextInt(100)-50, rand.nextInt(100)-50, rand.nextInt(100)-50), new Vector3d(1, 1, 1)));
		}
		
	}

	public void initVBOs(Program program) {
		
		vboQuads = new Vbo(GL11.GL_QUADS);
		
		for(int i=0;i<rectPrisms.size();i++){
			vboQuads.setColor(new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
			vboQuads.addRectPrism(rectPrisms.get(i));
		}
		
		vboQuads.end();
	}

	public void draw(Program program, boolean fill, int a) {
		vboQuads.draw(fill);
		for(int i=0;i<mobs.size();i++){
			mobs.get(i).draw(program, fill);
		}
	}

	public void update(Program program) {
		for(int i=0;i<mobs.size();i++){
			mobs.get(i).update(program);
		}
	}

}
