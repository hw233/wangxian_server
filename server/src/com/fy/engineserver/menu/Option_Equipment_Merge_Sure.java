package com.fy.engineserver.menu;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_SURE_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 装备合成
 * 
 *
 */
public class Option_Equipment_Merge_Sure extends Option{

	private GOD_EQUIPMENT_UPGRADE_SURE_REQ req;
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(req!=null){
			ArticleManager.getInstance().EquipmentUpgradeConfirm(player, req,true);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public GOD_EQUIPMENT_UPGRADE_SURE_REQ getReq() {
		return this.req;
	}

	public void setReq(GOD_EQUIPMENT_UPGRADE_SURE_REQ req) {
		this.req = req;
	}

	
	
}
