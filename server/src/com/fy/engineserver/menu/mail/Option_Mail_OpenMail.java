package com.fy.engineserver.menu.mail;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 打开邮件界面
 * 
 * 
 *
 */
public class Option_Mail_OpenMail extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),
					(byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(
					GameMessageFactory.nextSequnceNum(), (byte) 0,
					Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return;
		}
		
		DefaultMailManager mm = (DefaultMailManager)MailManager.getInstance();
		mm.addPlayerMailRequestToQueue(new DefaultMailManager.PlayerMailRequest("Option_Mail_OpenMail",player));
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
