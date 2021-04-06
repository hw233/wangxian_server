package com.fy.engineserver.shortcut;

public class SystemShortcut extends Shortcut {
	/**
	 * 快捷键的功能类型，其中0-3是行走方向，分别代表上下左右。4及4以上代表背包、任务、技能、寻路等功能
	 */
	public int functionType;

	public SystemShortcut(int actionType) {
		this.functionType = actionType;
	}
}
