package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class SuitSkillGroup {
	@SimpleKey
	private int skillGroup;
	private int[] skillId;
	private long skillCD;

	public int getSkillGroup() {
		return skillGroup;
	}

	public void setSkillGroup(int skillGroup) {
		this.skillGroup = skillGroup;
	}

	public int[] getSkillId() {
		return skillId;
	}

	public void setSkillId(int[] skillId) {
		this.skillId = skillId;
	}

	public long getSkillCD() {
		return skillCD;
	}

	public void setSkillCD(long skillCD) {
		this.skillCD = skillCD;
	}

}
