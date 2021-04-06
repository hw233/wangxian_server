package com.fy.engineserver.sprite.pet;

import java.util.Arrays;

/**
 * 宠物飞升技能
 * 
 *
 */
public class PetFlySkillInfo {
	
	private int skillId;
	private String name = "";
	private String icon = "";
	private String skillDesc = "";
	public SkillProp skills[] = new SkillProp[0];
	
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public SkillProp[] getSkills() {
		return skills;
	}

	public void setSkills(SkillProp[] skills) {
		this.skills = skills;
	}

	public String getSkillDesc() {
		if(skills != null ) {
			if(skillDesc == null || skillDesc.isEmpty()){
				StringBuffer ss = new StringBuffer();
				ss.append("<f color='0xff8400'>").append(name).append("</f>").append("\n");
				for(SkillProp sk : skills){
					if(sk != null){
						ss.append(sk.skillDesc).append("\n");
					}
				}
				skillDesc = ss.toString();
			}
		}
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}

	@Override
	public String toString() {
		return "PetFlySkillInfo [icon=" + icon + ", name=" + name
				+ ", skillId=" + skillId + ", skills="
				+ Arrays.toString(skills) + "]";
	}
	
}
