package com.fy.engineserver.septbuilding.entity;

/**
 * 家族徽章
 * 
 * 
 */
public class JiazuBedge {

	/** 切换徽章 */
	public static final int switchCost = 200;
	/** 徽章类型 - 基础 */
	public static final int TYPE_BASE = 0;
	/** 徽章类型 - 扩展 */
	public static final int TYPE_EXTEND = 1;

	private int id;
	private String name;
	private String resName;
	private int color;
	private int price;
	private int levelLimit;
	private String des;
	private int type;

	public JiazuBedge() {

	}

	public JiazuBedge(int id, String name, String resName, int color, int price, int levelLimit, String des, int type) {
		this.id = id;
		this.name = name;
		this.resName = resName;
		this.color = color;
		this.price = price;
		this.levelLimit = levelLimit;
		this.des = des;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "JiazuBedge [id=" + id + ", name=" + name + ", resName=" + resName + ", color=" + color + ", price=" + price + ", levelLimit=" + levelLimit + ", des=" + des + ", type=" + type + "]";
	}
}
