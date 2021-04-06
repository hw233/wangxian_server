package com.fy.engineserver.menu;

import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.ACTIVENESS_LIST_REQ;
import com.fy.engineserver.message.ACTIVENESS_LIST_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_OPEN_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 充值提示弹窗
 * 
 * 
 *
 */
public class Option_Query_Activity extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		System.out.println("in Option_Query_Activity..");
		ActivenessManager am = ActivenessManager.getInstance();
		ACTIVENESS_LIST_RES res = am.getACTIVENESS_LIST_RES(player);
		player.addMessageToRightBag(res);
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
