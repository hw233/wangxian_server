package com.fy.engineserver.datasource.skill2;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.jsp.propertyvalue.SkillManager;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 可以对攻击进行计数。
 * 
 *         create on 2013年9月3日
 */
public class CountTimesSkillAgent extends ActiveSkillAgent {
	public long atkTimes;

	public CountTimesSkillAgent(Fighter f) {
		super(f);
	}

	@Override
	public boolean usingSkill(ActiveSkill skill, int level, Fighter target, int x, int y, byte[] targetTypes, long[] targetIds, byte direction) {
		Logger log = Skill.logger;
 		if (target != null && target.canFreeFromBeDamaged(null)) {
			// TODO 2013-10-25 23:04:49修改,目标在安全区不可以使用技能
//			TaskManager.logger.warn(target.getName() + " 在安全区免疫技能", new Exception());
			return false;
		}
 		
		if (target != null && target instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
			Player p = (Player) target;
			if (p.getCountry() == p.getCurrentGameCountry()) {
				return false;
			}
		}
 		
		boolean ret = super.usingSkill(skill, level, target, x, y, targetTypes, targetIds, direction);
		if (log.isDebugEnabled()) {
			log.debug("{} skill {} ret {}", new Object[] { getOwner().getName(), skill.getName(), ret });
		}
		
		if (ret) {
			atkTimes += 1;
			Fighter o = getOwner();
			if (o != null && o.getSpriteType() == Sprite.SPRITE_TYPE_PET) {
				Pet pet = (Pet) o;
				pet.notifyUseSkill();
				if (log.isDebugEnabled()) {
					log.debug("pet {}", pet.getName());
				}
				GenericBuffCalc.getInst().procBuff(target, pet, atkTimes, log);
			} else if (log.isDebugEnabled()) {
				log.debug(" not pet {}", o.getName());
			}
		}
		return ret;
	}

}
