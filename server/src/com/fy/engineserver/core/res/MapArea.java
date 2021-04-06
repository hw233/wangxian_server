package com.fy.engineserver.core.res;

public class MapArea {

	public String name;

	public short x, y, width, height,type;
	public static final short TYPE_TANSUO = 1;//探索
	public static final short TYPE_BAITAN = 2;//摆摊
	public static final short TYPE_BORN_POINT = 0;//出生点

	public static final short TYPE_SAFE_1 = 7;//安全区(对本国安全)
	public static final short TYPE_SAFE_2 = 8;//安全区(对所有人安全)

	/**
	 * 类型为7,8的区域为安全区
	 * 安全区级别为数值7为安全级别较低的安全区，外国人不可以杀本国人，本国人可以杀外国人。8为安全级别高的安全区，任何人禁止pk
	 */
	public static final int[] 区域为安全区的类型 = new int[]{TYPE_SAFE_1,TYPE_SAFE_2};
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public short getX() {
		return x;
	}
	public void setX(short x) {
		this.x = x;
	}
	public short getY() {
		return y;
	}
	public void setY(short y) {
		this.y = y;
	}
	public short getWidth() {
		return width;
	}
	public void setWidth(short width) {
		this.width = width;
	}
	public short getHeight() {
		return height;
	}
	public void setHeight(short height) {
		this.height = height;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}	
	
	
	
	
	
}
