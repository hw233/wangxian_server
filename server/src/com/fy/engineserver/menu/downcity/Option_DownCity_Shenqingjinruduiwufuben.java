package com.fy.engineserver.menu.downcity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 申请打开创建副本房间窗口
 * 
 * 
 *
 */
public class Option_DownCity_Shenqingjinruduiwufuben extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		DownCityManager dcm = DownCityManager.getInstance();
		if(dcm != null){
			dcm.进入队伍副本(player);
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
