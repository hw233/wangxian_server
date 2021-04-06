package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;


public class Option_Learn_Skill_Sure extends Option {
	
	int skillId;
	boolean isAuto;
	boolean learnByBook;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void setArgs(int skillId,boolean isAuto,boolean learnByBook){
		this.skillId = skillId;
		this.isAuto = isAuto;
		this.learnByBook = learnByBook;
	}
	
	
	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			player.tryToLearnSkill(skillId, isAuto, learnByBook, true);
		} catch (Exception e) {
			Skill.logger.error("[学习技能弹框提示] [异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] "+toString()+"",e);
		}
	}

	@Override
	public String toString() {
		return "[skillId=" + this.skillId + ", isAuto=" + this.isAuto + ", learnByBook=" + this.learnByBook + "]";
	}
	
}
