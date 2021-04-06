package com.fy.engineserver.menu.qiling;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_QILING_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 打开器灵窗口
 * 
 * 
 *
 */
public class Option_OpenQilingWindow extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		OPEN_QILING_WINDOW_REQ req = new OPEN_QILING_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), (byte)0);
		player.addMessageToRightBag(req);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_绑定;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_472;
	}
}
