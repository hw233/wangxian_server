package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_CleanSkillOK extends Option {
	private int skillID;
	
	public Option_CleanSkillOK() {
		super();
	}
	
	public Option_CleanSkillOK(int skillID) {
		this.skillID = skillID;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		player.tryCleanSkill(skillID);
	}
	
	public void setSkillID(int skillID) {
		this.skillID = skillID;
	}

	public int getSkillID() {
		return skillID;
	}
	
}
