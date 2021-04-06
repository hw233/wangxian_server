package com.fy.engineserver.menu.mail;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MailSubSystem;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.MAIL_CREATE_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_Mail_MONEY extends Option {

	private MAIL_CREATE_REQ req;
	
	public Option_Mail_MONEY(){};
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		MailSubSystem.getInstance().creatMail(req, player, true);
	}

	public void setReq(MAIL_CREATE_REQ req) {
		this.req = req;
	}

	public MAIL_CREATE_REQ getReq() {
		return req;
	}
	
}
