package com.fy.engineserver.menu.villagefight;

import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 比武进比武场
 * 
 * 
 *
 */
public class Option_VillageFight_ShenqingQueren extends Option {

	public int arrayIndex;
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == null){
			if(VillageFightManager.logger.isWarnEnabled())
				VillageFightManager.logger.warn("[申请攻打灵矿] [失败] [player == null]");
			return;
		}
		VillageFightManager vfm = VillageFightManager.getInstance();
		if(vfm != null){
			try{
				vfm.申请攻打灵矿确认(player, arrayIndex);
			}catch(Exception ex){
				if(VillageFightManager.logger.isWarnEnabled())
					VillageFightManager.logger.warn("[申请攻打灵矿] [{}] [{}] [{}]",new Object[]{player.getUsername(),player.getId(),player.getName()},ex);
			}
		}
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doInput(Game game, Player player, String inputContent){}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_比武进比武场;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5035 ;
	}

}
