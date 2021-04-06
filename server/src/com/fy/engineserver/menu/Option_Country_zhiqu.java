package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 使用官员囚禁功能
 * 
 * 
 *
 */
public class Option_Country_zhiqu extends Option{

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
@Override
public void doInput(Game game, Player player, String inputContent) {
	// TODO Auto-generated method stub
	CountryManager cm = CountryManager.getInstance();
	if(cm != null){
		int count = 0;
		try{
			count = Integer.parseInt(inputContent);
		}catch(Exception ex){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.请输入数字);
			player.addMessageToRightBag(hreq);
			return;
		}
		cm.国王支取金库(player, count);
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
