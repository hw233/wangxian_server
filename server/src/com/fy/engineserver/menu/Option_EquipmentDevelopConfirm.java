package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.EQUIPMENT_DEVELOP_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 炼化确认窗口
 * 
 * 
 *
 */
public class Option_EquipmentDevelopConfirm extends Option{

	EQUIPMENT_DEVELOP_REQ req;

	public EQUIPMENT_DEVELOP_REQ getReq() {
		return req;
	}

	public void setReq(EQUIPMENT_DEVELOP_REQ req) {
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
			am.confirmEquipmentDevelopReq(player, req.getEquipmentId(), req.getAssistEquipmentId());
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
		return Translate.服务器选项;
	}
}
