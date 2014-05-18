package main;

import java.awt.geom.Line2D;

public class Line {

	public double x1;
	public double y1;
	public double x2;
	public double y2;

	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Line() {
		this(0, 0, 0, 0);
	}

	public boolean intersection(Line line) {
		return Line2D.linesIntersect(x1, y1, x2, y2, line.x1, line.y1, line.x2, line.y2);
	}

	public boolean intersection(double xx1, double yy1, double xx2, double yy2) {
		return Line2D.linesIntersect(x1, y1, x2, y2, xx1, yy1, xx2, yy2);
	}

	public boolean intersectionWithRectangle(double xx1, double yy1, double xx2, double yy2) {
		return intersection(xx1, yy1, xx2, yy1) || intersection(xx1, yy1, xx1, yy2) || intersection(xx1, yy2, xx2, yy2) || intersection(xx2, yy1, xx2, yy2);
	}

}
