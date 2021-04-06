package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.message.NEW_EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 强化确认窗口
 * 
 * 
 *
 */
public class Option_StrongConfirmNew extends Option{

	NEW_EQUIPMENT_STRONG_REQ req;
	
	boolean 是20星;

	public NEW_EQUIPMENT_STRONG_REQ getReq() {
		return req;
	}

	public void setReq(NEW_EQUIPMENT_STRONG_REQ req) {
		this.req = req;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		ArticleManager am = ArticleManager.getInstance();
		if(am != null){
			if(req == null){
				return;
			}
			am.confirmNewEquipmentStrongReq(player, req,是20星);
		}
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

	public boolean is是20星() {
		return 是20星;
	}

	public void set是20星(boolean 是20星) {
		this.是20星 = 是20星;
	}
	
	
}
