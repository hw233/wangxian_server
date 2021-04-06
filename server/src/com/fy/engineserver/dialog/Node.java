package com.fy.engineserver.dialog;

public abstract class Node {
	public static final int NORMAL = 0;
	public static final int EXIT = 1;

	abstract Node goNext(int enter);

	abstract int doNode(int strIndex);
}
