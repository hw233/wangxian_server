/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_UniqueGiftInput extends Option {

	/**
	 * 
	 */
	public Option_UniqueGiftInput() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doInput(Game game, Player player, String serial) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_UNIQUE_GIFT_INPUT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5492 ;
	}


}
