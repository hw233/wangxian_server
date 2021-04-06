package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.WangZheTransferPointOnMap;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.COUNTRY_WANGZHEZHIYIN_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
/**
 * 使用王者之印
 * 
 * 
 *
 */
public class Option_Country_wangzhezhiyin extends Option{

	String mapName;
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		if(cm != null){
			WangZheTransferPointOnMap wzt = cm.chinaMapNameTransferPoints2.get(new Integer(player.getCountry())).get(mapName);
			if(wzt != null){
				COUNTRY_WANGZHEZHIYIN_RES res = new COUNTRY_WANGZHEZHIYIN_RES(GameMessageFactory.nextSequnceNum(), wzt.mapName, wzt.displayMapName);
				player.addMessageToRightBag(res);
			}
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
