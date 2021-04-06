package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_FairyRobbery_Start extends Option implements NeedCheckPurview{
	
	private String robberyName;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		FairyRobberyManager.inst.notifyPlayerEnterRobbery(player, robberyName);
	}
	
	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		String result = FairyRobberyManager.inst.validPlayerEnter(player, getRobberyName());
		return result == null;
	}

	public String getRobberyName() {
		return robberyName;
	}

	public void setRobberyName(String robberyName) {
		this.robberyName = robberyName;
	}
}
