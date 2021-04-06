package com.fy.engineserver.datasource.skill2;

import com.fy.engineserver.datasource.skill.Skill;

/**
 * 
 * create on 2013年8月30日
 */
public class GenericSkill extends Skill{
	public GenericBuff buff;
	public byte getSkillType(){
		return Skill.SKILL_PASSIVE;
	}
}
