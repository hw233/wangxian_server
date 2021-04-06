package com.fy.engineserver.menu;

import com.fy.engineserver.battlefield.concrete.BattleFieldLineupService;
import com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_Battle_WaittingConfirm_Enter extends Option {

	WaittingItem wi;
	
	public Option_Battle_WaittingConfirm_Enter(WaittingItem wi) {
		this.wi = wi;
	}
	
	public void doSelect(Game game, Player player) {
		BattleFieldLineupService service = BattleFieldLineupService.getInstance();
		service.pushWaitingItemToQueue(wi);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_ENTER_BATTLE;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_215;
	}

}
