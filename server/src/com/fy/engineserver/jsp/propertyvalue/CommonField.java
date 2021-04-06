package com.fy.engineserver.jsp.propertyvalue;

public class CommonField {
	public static int INPUTTYPE_INPUT_TEXT = 0;
	public static int INPUTTYPE_INPUT_SELECT = 1;
	public static int INPUTTYPE_INPUT_CHECKBOX = 2;
	/*
	 * 按等级区分
	 */
	public static int INPUTTYPE_INPUT_LEVEL = 3;
	
	//java类中的属性名字
	String name;
	
	//显示的名字
	String displayName;
	
	//输入类型
	int inputType;
	
	Class type;
	
	//值
	String value;

	String defaultValue;
	
	//选择的值
	String selectValues[];
	
	//选择的值的显示
	String selectDisplayValues[];
	
	String description = "";
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String[] getSelectValues() {
		return selectValues;
	}

	public void setSelectValues(String[] selectValues) {
		this.selectValues = selectValues;
	}

	public String[] getSelectDisplayValues() {
		return selectDisplayValues;
	}

	public void setSelectDisplayValues(String[] selectDisplayValue) {
		this.selectDisplayValues = selectDisplayValue;
	}
	
	
	
}
