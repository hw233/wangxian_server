package com.fy.engineserver.jsp.propertyvalue;

public class SkillField {
	/** 编辑界面的表示形式 **/
	int inputType;
	
	public static int INPUTTYPE_INPUT_TEXT = 0;
	//public static int INPUTTYPE_INPUT_TEXT_AREA = 1;
	public static int INPUTTYPE_INPUT_SELECT = 2;
	//public static int INPUTTYPE_INPUT_NUMBER = 3;
	//public static int INPUTTYPE_INPUT_CHECK_BOX = 4;
	public static int INPUTTYPE_INPUT_SELECT_ITEM = 5;
	public static int INPUTTYPE_INPUT_LEVEL = 6;
	public static int INPUTTYPE_INPUT_MAP = 7;
	
	/** 选择的值 **/
	String selectValues[];
	
	/** 选择的值的显示 **/
	String selectDisplayValues[];
	
	/** 类中的成员变量名 **/
	String fieldName;
	
	/** 显示的名字 **/
	String displayName;
	
	/** 字段的描述 **/
	String description;
	
	/** 字段的类型 **/
	Class type;
	
	/** 字段的默认值 **/
	String defaultValue;
	
	public int indexOf(String value){
		if(value == null){
			return -1;
		}
		
		for (int i = 0; i < selectValues.length; i++) {
			if(selectValues[i].equals(value.trim())){
				return i;
			}
		}
		
		return -1;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
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

	public void setSelectDisplayValues(String[] selectDisplayValues) {
		this.selectDisplayValues = selectDisplayValues;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
