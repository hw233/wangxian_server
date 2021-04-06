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
public class Option_StrongConfirm extends Option{

	EQUIPMENT_STRONG_REQ req;
	NEW_EQUIPMENT_STRONG_REQ req_new;
	boolean is20star = false;
	
	public NEW_EQUIPMENT_STRONG_REQ getReq_new() {
		return this.req_new;
	}

	public void setReq_new(NEW_EQUIPMENT_STRONG_REQ req_new) {
		this.req_new = req_new;
	}

	public boolean isIs20star() {
		return this.is20star;
	}

	public void setIs20star(boolean is20star) {
		this.is20star = is20star;
	}

	public EQUIPMENT_STRONG_REQ getReq() {
		return req;
	}

	public void setReq(EQUIPMENT_STRONG_REQ req) {
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
			if(req != null){
				am.confirmEquipmentStrongReq(player, req.getEquipmentId(), req.getStrongMaterialID(), req.getSequnceNum(), req.getStrongType());
			}
			if(req_new != null){
				am.confirmNewEquipmentStrongReq(player, req_new, is20star);
			}
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
}
