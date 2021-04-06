package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
/**
 * 国王开启封印
 * 
 * 
 *
 */
public class Option_Seal_Open extends Option implements NeedCheckPurview{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		SealManager sm = SealManager.getInstance();
		if(sm != null){
			sm.openSeal(player);
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

	@Override
	public boolean canSee(Player player) {
//		if(SealManager.getInstance().openSeal()){
			return true;
//		}
//		return false;
	}
}
