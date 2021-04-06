package com.fy.engineserver.menu.cityfight;

import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 申请攻占城市
 * 
 * 
 *
 */
public class Option_CityFight_jinru extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if (player.getLevel() < 41) {
			player.sendError(Translate.等级不足不能参加城战);
			return ;
		}
		CityFightManager cfm = CityFightManager.getInstance();
		String transferGameName = cfm.得到城战地图(game.gi.name);
		if(transferGameName != null){
			cfm.传送进地图(game, transferGameName, game.gi.name, game.country, player);
		}else{
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.服务器出现错误);
			player.addMessageToRightBag(hreq);
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
