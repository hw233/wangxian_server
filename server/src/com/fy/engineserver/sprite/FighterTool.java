package com.fy.engineserver.sprite;

/**
 * Fighter工具类,用于一些判断
 */
public class FighterTool {

	public static boolean isCanNotIncHp(Fighter fighter) {
		if (fighter == null) {
			return true;
		}
		if (fighter instanceof AbstractPlayer) {
			return ((AbstractPlayer) fighter).isCanNotIncHp();
		} else if (fighter instanceof AbstractSprite) {
			return ((AbstractSprite) fighter).isCanNotIncHp();
		}
		return false;
	}
}
