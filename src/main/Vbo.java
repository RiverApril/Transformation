package main;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Vbo {

	private ArrayList<Vector3d> vertexList = new ArrayList<Vector3d>();
	private ArrayList<Vector3d> colorList = new ArrayList<Vector3d>();
	
	private DoubleBuffer vBuffer;
	private DoubleBuffer cBuffer;
	private int count = 0;
	private int mode;
	
	private Vector3d currentColor = Color.red;

	public Vbo(int mode) {
		this.mode = mode;
		count = 0;
	}

	public void end() {
		vBuffer = BufferUtils.createDoubleBuffer(vertexList.size()*3);
		for(Vector3d v : vertexList){
			vBuffer.put(v.x).put(v.y).put(v.z);
		}
		vertexList.clear();
		vBuffer.flip();
		
		cBuffer = BufferUtils.createDoubleBuffer(colorList.size()*3);
		for(Vector3d v : colorList){
			cBuffer.put(v.x).put(v.y).put(v.z);
		}
		colorList.clear();
		cBuffer.flip();
	}

	public void add(Vector3d vertex, Vector3d color) {
		colorList.add(color);
		vertexList.add(vertex);
		count++;
	}

	public void add(Vector3d vertex) {
		add(vertex, currentColor);
	}
	
	public void draw(boolean useColor, Camera offset) {
		offset.applyMatrix();
		draw(useColor);
		//offset.applyInvertedMatrix();
	}

	public void draw(boolean useColor) {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		if(useColor)GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		if(useColor)GL11.glColorPointer(3, 3<<3, cBuffer);
		GL11.glVertexPointer(3, 3<<3, vBuffer);
		GL11.glDrawArrays(mode, 0, count);
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		if(useColor)GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	}

	public void addXWall(double x, double y, double z, double halfSize, double halfHeight) {
		add(new Vector3d(x-halfSize, y-halfHeight, z));
		add(new Vector3d(x+halfSize, y-halfHeight, z));
		add(new Vector3d(x+halfSize, y+halfHeight, z));
		add(new Vector3d(x-halfSize, y+halfHeight, z));
	}

	public void addZWall(double x, double y, double z, double halfSize, double halfHeight) {
		add(new Vector3d(x, y-halfHeight, z-halfSize));
		add(new Vector3d(x, y-halfHeight, z+halfSize));
		add(new Vector3d(x, y+halfHeight, z+halfSize));
		add(new Vector3d(x, y+halfHeight, z-halfSize));
	}

	public void addWall(double y, Line a, double halfHeight) {
		add(new Vector3d(a.x1, y-halfHeight, a.y1));
		add(new Vector3d(a.x2, y-halfHeight, a.y2));
		add(new Vector3d(a.x2, y+halfHeight, a.y2));
		add(new Vector3d(a.x1, y+halfHeight, a.y1));
	}

	public void addPlane(double x, double y, double z, double halfXSize, double halfZSize) {
		add(new Vector3d(x-halfXSize, y, z-halfZSize));
		add(new Vector3d(x-halfXSize, y, z+halfZSize));
		add(new Vector3d(x+halfXSize, y, z+halfZSize));
		add(new Vector3d(x+halfXSize, y, z-halfZSize));
	}

	public void addRectPrism(RectPrism rectPrism) {
		addRectPrism(rectPrism.getPosition(), rectPrism.getRadii());
	}

	public void addRectPrism(Vector3d pos, Vector3d radii) {
		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z-radii.z));

		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z-radii.z));

		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z-radii.z));

		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z-radii.z));

		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z-radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z-radii.z));

		add(new Vector3d(pos.x-radii.x, pos.y-radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x-radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y+radii.y, pos.z+radii.z));
		add(new Vector3d(pos.x+radii.x, pos.y-radii.y, pos.z+radii.z));
	}
	
	public void setColor(Vector3d color){
		currentColor = color;
	}

}
