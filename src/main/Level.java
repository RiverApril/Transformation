package main;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Level {

	private Vbo VBO = new Vbo();
	
	public Line[][] walls = new Line[0][0];

	private int ySize = 10;

	public int floorSize = 10;

	public Level() {
		init();
	}

	private void init() {
		Random rand = new Random();
		int n = 300;
		walls = new Line[ySize][n];
		for(int i=0;i<n;i++){
			int x = rand.nextInt(20)-10;
			int y = rand.nextInt(20)-10;
			int a;
			int b;
			if(rand.nextBoolean()){
				a = 10;
				b = 0;
			}else{
				a = 0;
				b = 10;
			}
			walls[0][i] = new Line(x*10, y*10, (x*10)+a, (y*10)+b);
		}
		//walls[0][0] = new Line(-10, -10, -10, 10);
		//walls[0][1] = new Line(-10, -10, 10, -10);
		//walls[0][2] = new Line(-10, 10, 10, 10);
		//walls[0][3] = new Line(10, -10, 10, 10);
		
	}

	public void initVBOs(Program program) {
		VBO.begin(GL11.GL_QUADS);
		for(int y=0;y<ySize;y++){
			for(Line wall : walls[y]){
				if(wall!=null){
					VBO.addWall(y*floorSize, wall, (floorSize/2));
				}
			}
			VBO.addPlane(0, (y*floorSize)-(floorSize/2), 0, 100, 100);
		}
		//VBO.addPlane(0, floorSize/2, 0, 40, 40);
		VBO.end();
		
	}

	public void draw(Program program, boolean fill, int i) {
		VBO.draw(fill);
	}

}
