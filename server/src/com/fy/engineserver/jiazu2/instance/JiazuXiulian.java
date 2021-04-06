package com.fy.engineserver.jiazu2.instance;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class JiazuXiulian {
	/** 修炼技能id */
	private int skillId;
	/** 修炼技能等级 */
	private int skillLevel;
	/** 技能修炼经验 */
	private long skillExp;
	
	@Override
	public String toString() {
		return "JiazuXiulian [skillId=" + skillId + ", skillLevel=" + skillLevel + ", skillExp=" + skillExp + "]";
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	public long getSkillExp() {
		return skillExp;
	}
	public void setSkillExp(long skillExp) {
		this.skillExp = skillExp;
	}
	
	
}
