package main;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Program {

	public static void main(String[] args) {
		new Program(args);
	}
	
	private boolean exit = false;
	public long tick = 0;

	public Area currentArea;
	public Player player;
	
	public Vector3d viewOffset = new Vector3d();	
	public Vector3d guiViewOffset = new Vector3d();
	
	private Vbo guiVbo = new Vbo();

	public Program(String[] args) {

		setupGuiVbo();
		
		
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
		MouseControl.init();
		player = new Player();
		currentArea = new Ship();
		//currentArea.createRandomBoxes();
	}

	private void initVBOs() {
		currentArea.initVBOs(this);
		player.initVBOs(this);
	}

	private void update() {
		KeyboardControl.update();
		MouseControl.update();
		
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
		
		player.update(this);
		currentArea.update(this);
		
		drawGL();
	}

	private void drawGL() {
		GL11.glDepthMask(true);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glLoadIdentity();
		
		GL11.glTranslated(viewOffset.x, viewOffset.y, viewOffset.z);
		
		player.structure.camera.applyMatrix();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_EQUAL, 1);
		draw(true);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glAlphaFunc(GL11.GL_LESS, 1);
		draw(false);

		GL11.glLoadIdentity();
		//GL11.glTranslated(guiViewOffset.x, guiViewOffset.y, guiViewOffset.z);
		GL11.glRotated(guiViewOffset.x*100, 0, -1, 0);
		GL11.glRotated(guiViewOffset.y*100, 1, 0, 0);
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_EQUAL, 1);
		drawGui(true);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glAlphaFunc(GL11.GL_LESS, 1);
		drawGui(false);
		
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
	}

	private void setupGuiVbo() {
		guiVbo.begin(GL11.GL_QUADS);
		guiVbo.setColor(new Color(.5, .5, .5, .5));
		//guiVbo.addXWall(-1, 0, -1, .2, .8);
		//guiVbo.addXWall(1, 0, -1, .2, .8);
		guiVbo.addXWall(0, 0, -1, .01, .01);
		guiVbo.end();
	}

	private void drawGui(boolean fill) {
		//GL11.glLoadIdentity();
		
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		//GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		//GL11.glPolygonOffset(1, 1);
		//GL11.glColor4d(1, 1, 1, 1);

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1, 1);
		GL11.glColor3f(1, 1, 1);
		guiVbo.draw(true);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		if(fill){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glColor3f(0, 0, 0);
			guiVbo.draw(false);
		}
		
		//GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
	}

	private void draw(boolean fill) {
	
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1, 1);
		GL11.glColor3f(1, 1, 1);
		currentArea.draw(this, true, 0);
		player.draw(this, true);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			
		if(fill){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glColor3f(0, 0, 0);
			currentArea.draw(this, false, 0);
			player.draw(this, false);
		}
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
		GLU.gluPerspective(90, (float)Display.getWidth()/(float)Display.getHeight(), .001f, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glClearColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
	}

}
