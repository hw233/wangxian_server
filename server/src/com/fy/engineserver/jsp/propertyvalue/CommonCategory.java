package com.fy.engineserver.jsp.propertyvalue;

import java.util.ArrayList;
import java.util.List;

public class CommonCategory {
	
	public final static int SKILL_TYPE_ACTIVE = 0;
	public final static int SKILL_TYPE_PASSIVE = 1;
	public final static int SKILL_TYPE_AURA = 2;

	//名字
	public String displayName;
	
	//服务器实现的java类名
	public String className;
	
	public int type;
	
	/**
	 * 此类中的所有属性名和值的List
	 */
	public List<CommonField> cfList = new ArrayList<CommonField>();

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<CommonField> getCfList() {
		return cfList;
	}

	public void setCfList(List<CommonField> cfList) {
		this.cfList = cfList;
	}
}
