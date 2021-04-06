package com.fy.engineserver.datasource.article.data.equipments;

public class SuitEquipment {

	public static final int FIRE_INDEX = 0;
	public static final int ICE_INDEX = 1;
	public static final int WIND_INDEX = 2;
	public static final int THUNDER_INDEX = 3;
	/**
	 * 套装名
	 */
	protected String suitEquipmentName;

	/**
	 * 套装级别
	 */
	protected int suitLevel;
	
	/**
	 * 套装值，属性分别为火冰风雷，并且顺序不变
	 */
	protected int[] propertyValue = new int[0];
	
	/**
	 * 按照此顺序开启相应属性，数组的值为属性值数组的下标
	 * 如{3,1,2,0}表示，开启第一个属性时开启的propertyValue[3]，开启第二个属性时开启的propertyValue[1];
	 */
	protected int[] openPropertyIndexs = new int[0];
	
	/**
	 * 开启属性需要的装备数量
	 * 如{2,3,5,8}表示2件开启第一个属性，3件开启第二个属性，5件开启第3个属性
	 */
	protected int[] openPropertySuitCounts = new int[0];

	public String getSuitEquipmentName() {
		return suitEquipmentName;
	}

	public void setSuitEquipmentName(String suitEquipmentName) {
		this.suitEquipmentName = suitEquipmentName;
	}

	public int getSuitLevel() {
		return suitLevel;
	}

	public void setSuitLevel(int suitLevel) {
		this.suitLevel = suitLevel;
	}

	public int[] getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(int[] propertyValue) {
		this.propertyValue = propertyValue;
	}

	public int[] getOpenPropertyIndexs() {
		return openPropertyIndexs;
	}

	public void setOpenPropertyIndexs(int[] openPropertyIndexs) {
		this.openPropertyIndexs = openPropertyIndexs;
	}

	public int[] getOpenPropertySuitCounts() {
		return openPropertySuitCounts;
	}

	public void setOpenPropertySuitCounts(int[] openPropertySuitCounts) {
		this.openPropertySuitCounts = openPropertySuitCounts;
	}

}
