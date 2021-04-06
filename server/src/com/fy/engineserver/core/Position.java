package com.fy.engineserver.core;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


@SimpleEmbeddable
public class Position {
	private int x;
	private int y;
	private String mapEName;
	private String mapCName;

	private String realSceneName;

	public Position() {

	}

	public Position(int x, int y, String mapEName, String mapCName) {
		super();
		this.x = x;
		this.y = y;
		this.mapEName = mapEName;
		this.mapCName = mapCName;
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

	public String getMapEName() {
		return mapEName;
	}

	public void setMapEName(String mapEName) {
		this.mapEName = mapEName;
	}

	public String getMapCName() {
		return mapCName;
	}

	public void setMapCName(String mapCName) {
		this.mapCName = mapCName;
	}

	public String getRealSceneName() {
		return realSceneName;
	}

	public void setRealSceneName(String realSceneName) {
		this.realSceneName = realSceneName;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + ", mapEName=" + mapEName + ", mapCName=" + mapCName + ", realSceneName=" + realSceneName + "]";
	}

}
