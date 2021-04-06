package com.fy.engineserver.datasource.skill.passivetrigger;

import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Player;

/**
 * 被动触发免疫状态技能
 * 
 *
 */
public class PassiveTriggerImmune extends Skill{
	public static final byte 免疫减速 = 1;
	public static final byte 免疫定身 = 2;
	public static final byte 免疫晕眩 = 3;
	public static final byte 免疫控制 = 4;
	
	/** 触发概率(千分比) */
	private int[] probabbly;
	/** 持续时间(单位：秒) */
	private int[] lastTime;
	/** 触发buff名 */
	private String buffName;
	/** 对应buff级别 */
	private int[] buffLevel;
	/** 内置cd时间  单位 秒*/
	private int[] cds;
	
	public String getDescription(int level, Player player){
		if (level > probabbly.length) {
			return "仙界技能";
		}
		StringBuffer sb = new StringBuffer();
//		String temp = Translate.免疫状态buff[0];
//		if (buffLevel[level-1] == 2) {
//			temp = Translate.免疫状态buff[1];
//		} else if (buffLevel[level-1] == 3) {
//			temp = Translate.免疫状态buff[2];
//		}
		sb.append("\n<f color='0xfff600' size='28'>" + Translate.translateString(Translate.法术消耗点数, new String[][] { { Translate.COUNT_1, "0" } }) + "</f>");
		sb.append("\n<f color='0xfff600' size='28'>" + Translate.被动技能 + "</f>");
		sb.append("\n<f size='28'>");
		String des = String.format(this.getDescription(), (probabbly[level-1] / 10 + "%"));
		sb.append(des);
		sb.append("</f>");
		if (level < this.maxLevel) {
			sb.append("\n<f color='0xfff600' size='28'>");
			sb.append(Translate.下一级 + "</f>\n");
			sb.append("<f size='28'>");
			sb.append(String.format(Translate.下一级概率, (probabbly[level] / 10)+"%"));
			sb.append("</f>");
		}
		return sb.toString();
	}
	
	public void causeDamage(Player player) {
		try {
			long now = System.currentTimeMillis();
			byte level = player.getSkillLevel(this.getId());
			if (level > 0) {
				if (level > probabbly.length) {
					Skill.logger.warn("[仙界被动技能] [异常] [玩家技能等级超过配置] [" + player.getLogString() + "] [" + this.getName() + "_" + this.getId() 
							+ "] [skLevel:" + level + "]");
					return ;
				}
				if ((now - player.getLastImmuTime()) <= cds[level-1] * 1000) {
					if (Skill.logger.isDebugEnabled()) {
						Skill.logger.debug("[仙界被动技能] [内置cd时间，未触发] [" + player.getLogString() + "] [上次触发时间:" +  player.getLastImmuTime() + "]");
					} 
					return;
				}
				int ran = player.random.nextInt(1000);
				if (Skill.logger.isDebugEnabled()) {
					Skill.logger.debug("[仙界被动技能] [概率触发被动免疫] [ran:" + ran + "] [" + player.getLogString() + "]");
				}
				if (ran <= probabbly[level-1]) {
					player.setLastImmuTime(now);
					BuffTemplateManager btm = BuffTemplateManager.getInstance();
					BuffTemplate bt = btm.getBuffTemplateByName(buffName);
					if (bt == null) {
						Skill.logger.warn("[仙界被动技能] [buff不存在：" + buffName + "] [skillId:" + this.getId() + "] [" + player.getLogString() + "]");
						return ;
					}
					Buff buff = bt.createBuff(buffLevel[level-1] - 1);
					buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastTime[level-1] * 1000);
					buff.setCauser(player);
					player.placeBuff(buff);
					if (Skill.logger.isDebugEnabled()) {
						Skill.logger.debug("[仙界被动技能] [给玩家上buff] [成功] [" + player.getLogString() + "] [skId:" + this.getId() + "] [buffName:" + buffName + "] [免疫状态:" + player.getImmuType() + "]");
					}
				}
			}
		} catch (Exception e) {
			Skill.logger.error("[仙界被动技能] [异常] [" + player.getLogString() + "] [skId :" + this.getId() + "]", e);
		}
	}
	
	public int[] getProbabbly() {
		return probabbly;
	}
	public void setProbabbly(int[] probabbly) {
		this.probabbly = probabbly;
	}
	public int[] getLastTime() {
		return lastTime;
	}
	public void setLastTime(int[] lastTime) {
		this.lastTime = lastTime;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int[] getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int[] buffLevel) {
		this.buffLevel = buffLevel;
	}

	public int[] getCds() {
		return cds;
	}

	public void setCds(int[] cds) {
		this.cds = cds;
	}
	
}
