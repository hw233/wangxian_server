package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
/**
 * 使用官员托运功能
 * 
 * 
 *
 */
public class Option_Country_tuoyun_chuansong extends Option{

	String targetMapName;
	int targetMapX;
	int targetMapY;
	
	public String getTargetMapName() {
		return targetMapName;
	}

	public void setTargetMapName(String targetMapName) {
		this.targetMapName = targetMapName;
	}

	public int getTargetMapX() {
		return targetMapX;
	}

	public void setTargetMapX(int targetMapX) {
		this.targetMapX = targetMapX;
	}

	public int getTargetMapY() {
		return targetMapY;
	}

	public void setTargetMapY(int targetMapY) {
		this.targetMapY = targetMapY;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		if(cm != null){
			
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
