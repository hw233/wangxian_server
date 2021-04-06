package com.fy.engineserver.menu.villagefight;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.OreNPC;
/**
 * 村庄战申请占领
 * 
 * 
 *
 */
public class Option_VillageFight_lingqujiazuzijin extends Option{

	public OreNPC oreNPC;
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(game == null || player == null){
			return;
		}
		if(oreNPC != null){
			oreNPC.族长领取家族资金(player);
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
