package com.fy.engineserver.menu.sifang;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sifang.SiFangManager;
import com.fy.engineserver.sprite.Player;

//进入BOSS场景
public class Option_SiFang_BOSS extends Option implements NeedCheckPurview{

	int siFangType;
	public int getSiFangType() {
		return siFangType;
	}

	public void setSiFangType(int siFangType) {
		this.siFangType = siFangType;
	}

	public Option_SiFang_BOSS(){
		super();
	}
	public Option_SiFang_BOSS(int type){
		super();
		this.siFangType = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		SiFangManager.getInstance().opt_JingRu_BOSS(player, siFangType);
	}

	@Override
	public boolean canSee(Player player) {
		return SiFangManager.getInstance().isJiaZuWin(player, siFangType);
	}
	
}
