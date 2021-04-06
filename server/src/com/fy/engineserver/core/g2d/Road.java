package com.fy.engineserver.core.g2d;

/**
 * 路，表示两个路标之间的通路
 * 
 */

public class Road {
	public SignPost p1;
	public SignPost p2;
	public short len;

	public Road(SignPost start, SignPost end, short len) {
		this.p1 = start;
		this.p2 = end;
		this.len = len;
	}
}
