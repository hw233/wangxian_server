package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 被攻击触发的buff
 *
 */
@SimpleEmbeddable
public interface BeAttackedBuff {

	/**
	 * 被攻击，
	 * @param caster 谁攻击的
	 * @param skill 使用了什么技能
	 */
	public void beAttacked(Fighter caster,ActiveSkill skill);
}
