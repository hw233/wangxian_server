package com.fy.engineserver.homestead.cave.resource;

import java.util.Arrays;

/**
 * 碰撞区配置(仙府的门)
 * 
 * 
 */
public class HitAreaCfg {

	private short[] x;
	private short[] y;

	public HitAreaCfg() {

	}

	public HitAreaCfg(short[] x, short[] y) {
		super();
		this.x = x;
		this.y = y;
	}

	public short[] getX() {
		return x;
	}

	public void setX(short[] x) {
		this.x = x;
	}

	public short[] getY() {
		return y;
	}

	public void setY(short[] y) {
		this.y = y;
	}

	public String toString() {
		return "HitAreaCfg [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + "]";
	}

}
