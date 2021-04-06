package com.fy.engineserver.shortcut;

/**
 * 对于快捷键的抽象
 * 
 * @author 
 * 
 */
public class Shortcut {
	public static final int NONE = 0;
	public static final int SKILL_SHORTCUT = 1;
	public static final int PROPS_SHORTCUT = 2;
	public static final int SYSTEM_SHORTCUT = 3;
	public static final int GongFu_SHORTCUT = 4;

	public static final Shortcut UNDEFINED = new Shortcut();
}
