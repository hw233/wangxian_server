/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MailSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * @author Administrator
 *
 */
public class Option_SendMailConfirm extends Option {

	RequestMessage message;
	/**
	 * 
	 */
	public Option_SendMailConfirm(RequestMessage message) {
		// TODO Auto-generated constructor stub
		this.message=message;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MailSubSystem mss = MailSubSystem.getInstance();
		if(mss != null){
			mss.confirmCreateMail(message, player);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_SEND_MAIL_CONFIRM;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4942 ;
	}

}
