package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 物理攻击和法术攻击
 * 
 * 
 */
@SimpleEmbeddable
public class Buff_GongQiangFangYu extends Buff {

	public int physicalDamage = 0;
	public int magicDamage = 0;
	public int phyDefence = 0;
	public int magicDefence = 0;

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner) {

		if (owner instanceof Player) {
			Player p = (Player) owner;
			BuffTemplate_GongQiangFangYu bt = (BuffTemplate_GongQiangFangYu) this.getTemplate();
			/*
			 * 大师技能修改，创建buff时就计算好数值，2013年9月17日16:39:13，康建虎
			 * if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
			 * physicalDamage = bt.physicalDamage[getLevel()];
			 * }
			 * if(bt.phyDefence != null && bt.phyDefence.length > this.getLevel()){
			 * phyDefence = bt.phyDefence[getLevel()];
			 * }
			 * if(bt.magicDamage != null && bt.magicDamage.length > this.getLevel()){
			 * magicDamage = bt.magicDamage[getLevel()];
			 * }
			 * if(bt.magicDefence != null && bt.magicDefence.length > this.getLevel()){
			 * magicDefence = bt.magicDefence[getLevel()];
			 * }
			 */
			if (physicalDamage != 0) {
				p.setPhyAttackB((p.getPhyAttackB() + physicalDamage));
			}
			if (phyDefence != 0) {
				p.setPhyDefenceB((p.getPhyDefenceB() + phyDefence));
			}
			if (magicDamage != 0) {
				p.setMagicAttackB((p.getMagicAttackB() + magicDamage));
			}
			if (magicDefence != 0) {
				p.setMagicDefenceB((p.getMagicDefenceB() + magicDefence));
			}
		} else if (owner instanceof Sprite) {
			Sprite s = (Sprite) owner;
			/*
			 * 大师技能修改，创建buff时就计算好数值，2013年9月17日16:39:13，康建虎
			 * BuffTemplate_GongQiangFangYu bt = (BuffTemplate_GongQiangFangYu)this.getTemplate();
			 * if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
			 * physicalDamage = bt.physicalDamage[getLevel()];
			 * }
			 * if(bt.phyDefence != null && bt.phyDefence.length > this.getLevel()){
			 * phyDefence = bt.phyDefence[getLevel()];
			 * }
			 * if(bt.magicDamage != null && bt.magicDamage.length > this.getLevel()){
			 * magicDamage = bt.magicDamage[getLevel()];
			 * }
			 * if(bt.magicDefence != null && bt.magicDefence.length > this.getLevel()){
			 * magicDefence = bt.magicDefence[getLevel()];
			 * }
			 */
			if (physicalDamage != 0) {
				s.setPhyAttackB((s.getPhyAttackB() + physicalDamage));
			}
			if (phyDefence != 0) {
				s.setPhyDefenceB((s.getPhyDefenceB() + phyDefence));
			}
			if (magicDamage != 0) {
				s.setMagicAttackB((s.getMagicAttackB() + magicDamage));
			}
			if (magicDefence != 0) {
				s.setMagicDefenceB((s.getMagicDefenceB() + magicDefence));
			}
		}
	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			if (physicalDamage != 0) {
				p.setPhyAttackB((p.getPhyAttackB() - physicalDamage));
			}
			if (phyDefence != 0) {
				p.setPhyDefenceB((p.getPhyDefenceB() - phyDefence));
			}
			if (magicDamage != 0) {
				p.setMagicAttackB((p.getMagicAttackB() - magicDamage));
			}
			if (magicDefence != 0) {
				p.setMagicDefenceB((p.getMagicDefenceB() - magicDefence));
			}
		} else if (owner instanceof Sprite) {
			Sprite s = (Sprite) owner;
			if (physicalDamage != 0) {
				s.setPhyAttackB((s.getPhyAttackB() - physicalDamage));
			}
			if (phyDefence != 0) {
				s.setPhyDefenceB((s.getPhyDefenceB() - phyDefence));
			}
			if (magicDamage != 0) {
				s.setMagicAttackB((s.getMagicAttackB() - magicDamage));
			}
			if (magicDefence != 0) {
				s.setMagicDefenceB((s.getMagicDefenceB() - magicDefence));
			}
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

	public void fixEnhance(int point) {
		if (physicalDamage != 0) {
			physicalDamage += point;
		} else if (phyDefence != 0) {
			phyDefence += point;
		} else if (magicDamage != 0) {
			magicDamage += point;
		} else if (magicDefence != 0) {
			magicDefence += point;
		}
		setDescription(getDescription() + "(+" + point + ")");
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("Buff_GongQiangFangYu.fixEnhance: {" + getDescription() + "}");//, new Exception());
		}
	}

}
