package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_lineup_entercity extends Option {
	
	String downCityName;

	public String getDownCityName() {
		return downCityName;
	}

	public void setDownCityName(String downCityName) {
		this.downCityName = downCityName;
	}

	public Option_lineup_entercity(String downCityName) {
		// TODO Auto-generated constructor stub
		this.downCityName=downCityName;
	}
	
	@Override
	public void doSelect(Game game, Player player) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_排队完成进入副本;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4682 + ":" + this.downCityName;
	}

}
