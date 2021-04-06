package com.fy.engineserver.shortcut;

/**
 * 施放技能的快捷键。 主动技能与光环技能可能要分开
 * 
 * @author 
 * 
 */
public class SkillShortcut extends Shortcut {
	public int skillId;

	public SkillShortcut(int skillId) {
		this.skillId = skillId;
	}
}
