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
public class Option_BecomePrenticeDisagree extends Option {

	Player prentice;
	/**
	 * 
	 */
	public Option_BecomePrenticeDisagree(Player prentice) {
		// TODO Auto-generated constructor stub
		this.prentice=prentice;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,player.getName()+Translate.text_5123);
		this.prentice.addMessageToRightBag(req);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_DISAGREE_TO_BECOME_PRENTICE;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5124;
	}

}
