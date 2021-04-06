package com.fy.engineserver.menu.horse;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HORSE_RANKSTAR_STRONG_RES;
import com.fy.engineserver.message.LEARN_HORSE_SKILL_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;

public class Option_ConfirmReplaceHighSkill extends Option{
	
	private long horseId;
	
	private int skillIndex;
	
	private int skillId;

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		boolean result = Horse2EntityManager.instance.learnSkill(player, horseId, skillIndex, skillId, true);
		LEARN_HORSE_SKILL_RES resp = new LEARN_HORSE_SKILL_RES(GameMessageFactory.nextSequnceNum(), horseId, (byte) (result?1:0));
		player.addMessageToRightBag(resp);
	}

	public long getHorseId() {
		return horseId;
	}

	public void setHorseId(long horseId) {
		this.horseId = horseId;
	}

	public int getSkillIndex() {
		return skillIndex;
	}

	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
}
