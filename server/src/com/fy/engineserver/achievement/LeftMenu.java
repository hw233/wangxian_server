package com.fy.engineserver.achievement;

import java.util.Arrays;

/**
 * 左侧菜单
 * 
 */
public class LeftMenu {

	private String mainName;
	private String[] subNames;

	public LeftMenu() {

	}

	public LeftMenu(String mainName, String[] subNames) {
		this.mainName = mainName;
		this.subNames = subNames;
	}

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}

	public String[] getSubNames() {
		return subNames;
	}

	public void setSubNames(String[] subNames) {
		this.subNames = subNames;
	}

	@Override
	public String toString() {
		return "LeftMenu [mainName=" + mainName + ", subNames=" + Arrays.toString(subNames) + "]";
	}
}