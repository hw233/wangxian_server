package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 攻击触发的buff
 *
 */
@SimpleEmbeddable
public interface AttackBuff {

	/**
	 * 攻击谁
	 * @param target 攻击谁
	 * @param skill 使用了什么技能
	 */
	public void attack(Fighter target,ActiveSkill skill);
}
