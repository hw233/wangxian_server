package com.fy.engineserver.sprite.monster;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;

/**
 * Boss在特殊的条件下执行的动作
 * 
 *
 */
public interface BossAction {

	/**
	 * 动作的唯一编号
	 * @return
	 */
	public int getActionId();
	
	/**
	 * 关于此动作的描述
	 * @return
	 */
	public String getDescription();
	
	
	/**
	 * 判断此动作是否满足动作自身执行的条件，
	 * 包括，配置是否正确，技能是否已经不在CD中等等。
	 * 
	 * @param boss
	 * @param target
	 * @return
	 */
	public boolean isExeAvailable(BossMonster boss,Fighter target);
	
	/**
	 * 执行特殊的工作，要求实现者采用无状态的实现。
	 * 也就是任何有状态的数据，只能放到boss身上，
	 * 而不能放置在Action身上。
	 * 
	 * 因为同一个Action可能同时被很多Boss同时执行。
	 * 
	 * @param game
	 * @param boss
	 */
	public void doAction(Game game,BossMonster boss,Fighter target);
}
