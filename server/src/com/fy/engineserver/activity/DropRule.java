package com.fy.engineserver.activity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * boss掉落规则
 * 
 */
public interface DropRule {
	void doDrop(Monster monster, Game game);
}
