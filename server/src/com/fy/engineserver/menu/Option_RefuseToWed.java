/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_RefuseToWed extends Option {

	/**
	 * 求婚者
	 */
	Player suitor;
	
	/**
	 * 
	 */
	public Option_RefuseToWed(Player suitor) {
		// TODO Auto-generated constructor stub
		this.suitor=suitor;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,player.getName()+Translate.text_5387);
		this.suitor.addMessageToRightBag(req);
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_REFUSE_TO_WED;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4861;
	}

}
