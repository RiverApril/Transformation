package main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

public class Program {

	public static void main(String[] args) {
		new Program(args);
	}
	
	private boolean exit = false;
	public long tick = 0;

	public Area currentArea;
	public Player player;
	
	public Vector3d viewOffset = new Vector3d();

	public Program(String[] args) {
		
		try {
			initDisplay(640, 480);
			init();
			initVBOs();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Mouse.setCursorPosition((Display.getWidth()/2), (Display.getHeight()/2));
		
		while(!exit){
			Display.update();
			update();
			tick++;
			Display.sync(60);
		}
	}

	private void init() throws LWJGLException {
		KeyboardControl.init();
		player = new Player();
		currentArea = new Area();
		//currentArea.createRandomBoxes();
	}

	private void initVBOs() {
		currentArea.initVBOs(this);
		player.initVBOs(this);
	}

	private void update() {
		KeyboardControl.update();
		
		if(Display.isCloseRequested()){
			exit = true;
		}
		if(Display.wasResized()){
			initGL();
			System.out.println("Resized to: "+Display.getWidth()+", "+Display.getHeight());
		}
		
		if(KeyboardControl.pressed(KeyboardControl.keyEscape)){
			Mouse.setGrabbed(!Mouse.isGrabbed());
			Mouse.setCursorPosition((Display.getWidth()/2), (Display.getHeight()/2));
			
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		
		player.update(this);

		GL11.glTranslated(viewOffset.x, viewOffset.y, viewOffset.z);
		
		player.structure.camera.applyMatrix();
		
		GL11.glPushMatrix();

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1, 1);
		GL11.glColor3f(1, 1, 1);
		currentArea.draw(this, true, 0);
		player.draw(this, true);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glColor3f(0, 0, 0);
		currentArea.draw(this, false, 0);
		player.draw(this, false);
		
		GL11.glPopMatrix();

		//player.structure.camera.applyInvertedMatrix();
	}

	private void initDisplay(int width, int height) throws LWJGLException {
		Display.setTitle("Transformation");
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setResizable(true);
		Display.create();
		Mouse.setGrabbed(true);
		
		initGL();
		
	}

	private void initGL() {
		
		GL11.glViewport(0, 0,  Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(90, Display.getWidth()/Display.getHeight(), .001f, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
	}

}
