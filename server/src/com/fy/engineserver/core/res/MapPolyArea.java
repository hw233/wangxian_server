package com.fy.engineserver.core.res;

import com.fy.engineserver.core.g2d.Polygon;

public class MapPolyArea {
	public String name;
	public short type;
	Polygon poly;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public Polygon getPoly() {
		return poly;
	}
	public void setPoly(Polygon poly) {
		this.poly = poly;
	}
	
}
