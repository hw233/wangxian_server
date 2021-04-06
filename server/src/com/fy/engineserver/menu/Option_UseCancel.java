package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 取消
 * 
 * 
 *
 */
public class Option_UseCancel extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
@Override
public void setText(String text) {
	// TODO Auto-generated method stub
	super.setText(Translate.取消);
}
	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_取消;
	}

	public void setOId(int oid) {
	}
	
	public String getIconId() {
		return "172";
	}

	public String toString(){
		return Translate.text_364;
	}
}
