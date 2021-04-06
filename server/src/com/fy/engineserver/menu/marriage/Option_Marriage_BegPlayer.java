package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedInitialiseOption;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * NPC上用的，送花还是送糖
 * 
 *
 */
public class Option_Marriage_BegPlayer extends Option implements NeedCheckPurview {

	private byte beqType;			//送花还是糖
	public byte getBeqType() {
		return beqType;
	}

	public void setBeqType(byte beqType) {
		this.beqType = beqType;
	}
	
	public Option_Marriage_BegPlayer(){
		super();
	}

	public Option_Marriage_BegPlayer(byte type){
		super();
		this.beqType = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game,Player player){
		MarriageManager.getInstance().option_beqStart(beqType, player);
	}

	@Override
	public boolean canSee(Player player) {
		if (beqType == 0) {
			if (player.getSex() == 0) {
				return true;
			}
		}else if (beqType == 1) {
			if (player.getSex() == 1) {
				return true;
			}
		}
		return false;
	}
	
}
