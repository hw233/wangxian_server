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
public class Option_TakePrenticeDisagree extends Option {

	Player master;
	/**
	 * 
	 */
	public Option_TakePrenticeDisagree(Player master) {
		// TODO Auto-generated constructor stub
		this.master=master;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,player.getName()+Translate.text_5459);
		this.master.addMessageToRightBag(req);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_DISAGREE_TO_TAKE_PRENTICE;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5460;
	}

}
