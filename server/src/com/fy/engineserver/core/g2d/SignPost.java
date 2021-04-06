package com.fy.engineserver.core.g2d;

import java.util.ArrayList;
import java.util.List;

/**
 * 路标
 * 
 */
public class SignPost {
	public int x;
	public int y;
	public int id;

	List<Road> roads = new ArrayList<Road>();

	transient int F, G;
	transient boolean inOPEN;
	transient boolean inCLOSE;
	transient SignPost parent;
	transient boolean selected;

	public SignPost() {
	}

	public SignPost(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public SignPost(int x, int y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public Road[] getRoads() {
		return roads.toArray(new Road[roads.size()]);
	}

	public void addRoad(Road road) {
		if (!roads.contains(road)) {
			roads.add(road);
		}
	}

	public boolean equals(Object o) {
		if (o instanceof SignPost) {
			SignPost p = (SignPost) o;
			if (p.x == x && p.y == y)
				return true;
		}
		return false;
	}
}
