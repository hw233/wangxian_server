package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.NEW_EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_BaodiStronger extends Option{
	
	private boolean 是20星 ;
	
	private NEW_EQUIPMENT_STRONG_REQ req;
	
	public void doSelect(Game game,Player player){
		ArticleManager.getInstance().confirmNewEquipmentStrongReq(player, req, 是20星, true);
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public boolean is是20星() {
		return 是20星;
	}

	public void set是20星(boolean 是20星) {
		this.是20星 = 是20星;
	}

	public NEW_EQUIPMENT_STRONG_REQ getReq() {
		return req;
	}

	public void setReq(NEW_EQUIPMENT_STRONG_REQ req) {
		this.req = req;
	}

}
