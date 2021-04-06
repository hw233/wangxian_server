package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_lineup_listherocity extends Option {
	
	public static final int PAGENUM = 5;
	
	public int index = 0;
	
	@Override
	public void doSelect(Game game, Player player) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_排队副本列表;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5309;
	}

}
