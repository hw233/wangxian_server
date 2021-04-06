package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 设置回城点
 * 
 * 
 *
 */
public class Option_SetHomePoint extends Option{

	

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		player.setHomeMapName(player.getMapName());
		player.setHomeX((short)player.getX());
		player.setHomeY((short)player.getY());
		
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5421);
		player.addMessageToRightBag(hreq);
		
	}


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_设置回城点;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "" ;
	}
}
