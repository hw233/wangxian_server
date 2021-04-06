package com.fy.engineserver.sprite;

/**
 * 元神加成数据
 * 
 *
 */
public class SoulData {

	//等级限制
	private int level;
	
	//颜色值
	private String color;
	
	//标题
	private String title;
	
	//所占比例
	private int [] percent;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int[] getPercent() {
		return percent;
	}

	public void setPercent(int[] percent) {
		this.percent = percent;
	}
	
	
}
