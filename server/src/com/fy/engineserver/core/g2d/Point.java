package com.fy.engineserver.core.g2d;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


/**
 * 多边形的点
 * 
 */
@SimpleEmbeddable
public class Point implements Cloneable {
	public int x;
	public int y;
	transient protected Edge tmp_edge;

	public Point() {

	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	protected Point clone() {
		return new Point(x, y);
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Point)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		Point p = (Point) obj;
		return x == p.x && y == p.y;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
