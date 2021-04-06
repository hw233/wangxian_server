package com.fy.engineserver.soulpith.property;
/**
 * 增加属性类型
 * 
 * @date on create 2016年3月22日 下午4:24:08
 */
public enum AddPropertyTypes {
	ADD_B_NUM(1, "增加B值(数值)"),
	ADD_C_NUM(2, "增加C值(百分比)"),
	ADD_X_NUM(3, "增加X值(元神加成)"),
	;
	private int index;
	private String name;
	
	private AddPropertyTypes(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public static AddPropertyTypes valueOf(int index) {
		for (AddPropertyTypes t : AddPropertyTypes.values()) {
			if (t.getIndex() == index) {
				return t;
			}
		}
		return null;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
