package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_lineup_choosecity extends Option {
	
	String downCityName;

	public Option_lineup_choosecity(){
		
	}
	
	public String getDownCityName() {
		return downCityName;
	}

	public void setDownCityName(String downCityName) {
		this.downCityName = downCityName;
	}
	
	public Option_lineup_choosecity(String downCityName) {
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
		return OptionConstants.SERVER_FUNCTION_排队进入副本选择副本;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5114;
	}

}
