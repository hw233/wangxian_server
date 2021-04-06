package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.WAREHOUSE_MODIFY_PASSWORD_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开仓库界面
 * 
 * 
 *
 */
public class Option_Cangku_XiugaiPassword extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

		if(player.getCangkuPassword() != null && !player.getCangkuPassword().trim().equals("")){
			WAREHOUSE_MODIFY_PASSWORD_RES res = new WAREHOUSE_MODIFY_PASSWORD_RES(GameMessageFactory.nextSequnceNum());
			player.addMessageToRightBag(res);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_OPEN_CANGKU;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
