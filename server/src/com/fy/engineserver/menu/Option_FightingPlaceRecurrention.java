package com.fy.engineserver.menu;

import com.fy.engineserver.core.FightingPlace;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 进入商店
 * 
 * 
 *
 */
public class Option_FightingPlaceRecurrention extends Option{


	
	
	FightingPlace fightingPlace;
	
	
	public FightingPlace getFightingPlace() {
		return fightingPlace;
	}

	public void setFightingPlace(FightingPlace fightingPlace) {
		this.fightingPlace = fightingPlace;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_战场复活;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4838 + ":" + fightingPlace.getName();
	}


}
