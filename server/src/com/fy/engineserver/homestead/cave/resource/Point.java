package com.fy.engineserver.homestead.cave.resource;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Point {

	private int x;
	private int y;

	public Point() {
		// TODO Auto-generated constructor stub
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}

	/**
	 * 是否是同一个点,此方法只适合用于同Game内的点判断
	 * @param point
	 * @return
	 */
	public boolean samePoint(Point point) {
		return x == point.x && y == point.y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
}
