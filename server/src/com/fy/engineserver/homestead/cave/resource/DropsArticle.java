package com.fy.engineserver.homestead.cave.resource;

/**
 * 可掉落物品包装
 * 
 * 
 */
public class DropsArticle {
	private int color;
	private String name;
	private int num;
	private boolean bind;
	private double rate;

	public DropsArticle(int color, String name, int num, int bind, double rate) {
		this.color = color;
		this.name = name;
		this.num = num;
		this.bind = bind == 1;
		this.rate = rate;
	}

	public final int getColor() {
		return color;
	}

	public final void setColor(int color) {
		this.color = color;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final int getNum() {
		return num;
	}

	public final void setNum(int num) {
		this.num = num;
	}

	public final double getRate() {
		return rate;
	}

	public final void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}
}
