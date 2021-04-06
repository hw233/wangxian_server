package com.fy.engineserver.ai;

import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.sprite.Fighter;

/**
 * 要求被控制者必须对外提供的接口
 * 
 * 已经在Fighter中包含的方法，此接口中不再包含
 * 
 *
 */
public interface AIControllable extends Fighter{

	/**
	 * 获得仇恨第一的人
	 * @return
	 */
	public Fighter getMaxHatredFighter();
	
	/**
	 * 获得第一个攻击的敌人
	 * 
	 * 如果第一个攻击的敌人为空，那么自动将当前的攻击目标设置为第一个攻击的人
	 */
	public Fighter getFirstEnemy();
	
	/**
	 * 设置第一个攻击的敌人
	 * @param f
	 */
	public void setFirstEnemy(Fighter f);
	
	/**
	 * 得到最后一个的敌人
	 * 
	 * 被控制的对象，能记录最有一个敌人是谁，
	 * 以方便进行过其他对队友的操作后，
	 * 自动锁定到敌人身上
	 */
	public void getLastEnemy();
	
	/**
	 * 将某个Fighter作为目标
	 * @param f
	 */
	public void setTarget(Fighter f);
	
	/**
	 * 获得当前的目标
	 * @return
	 */
	public Fighter getTarget();
	
	/**
	 * 开始攻击
	 */
	public void startAttack();
	
	/**
	 * 停止攻击
	 * 
	 */
	public void stopAttack();
	
	
	/**
	 * 使用某个技能攻击当前的目标
	 * @param skill
	 */
	public void cast(ActiveSkill skill);
	
	
}
