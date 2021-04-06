package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 输入password
 * 
 * 
 *
 */
public class Option_Cangku_InputShedingPassword extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		if(inputContent == null || inputContent.trim().equals("")){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.设定密码不能为空);
			player.addMessageToRightBag(hreq);
			return;
		}
		String s = inputContent.trim();
		if(s.length() < 4){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.密码长度最短位数);
			player.addMessageToRightBag(hreq);
			return;
		}
		if(s.length() > 12){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.密码长度最长位数);
			player.addMessageToRightBag(hreq);
			return;
		}
		if((player.getCangkuPassword() == null || player.getCangkuPassword().trim().equals(""))){
			player.setCangkuPassword(s);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translateString(Translate.设定密码成功,new String[][]{{Translate.STRING_1,s}}));
			player.addMessageToRightBag(hreq);
			return;
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
