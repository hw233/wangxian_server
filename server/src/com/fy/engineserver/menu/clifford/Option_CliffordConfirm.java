package com.fy.engineserver.menu.clifford;


import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.OreNPC;
/**
 * 祈福扣费确认
 * 
 * 
 *
 */
public class Option_CliffordConfirm extends Option{

	public int index;
	
	public byte cliffordType;
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(game == null || player == null){
			return;
		}
		CliffordManager cm = CliffordManager.getInstance();
		if(cm != null){
			player.setCliffordNotifyFlag(true);
			cm.祈福(index, cliffordType, player, false);
		}
	}
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub

	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
