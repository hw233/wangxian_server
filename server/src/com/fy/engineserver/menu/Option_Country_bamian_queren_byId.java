package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
/**
 * 使用官员囚禁功能
 * 
 * 
 *
 */
public class Option_Country_bamian_queren_byId extends Option{

	long playerId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	int guanzhi;
	
	public int getGuanzhi() {
		return guanzhi;
	}

	public void setGuanzhi(int guanzhi) {
		this.guanzhi = guanzhi;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if(cm != null && pm != null){
			try{
				Player playerB = pm.getPlayer(playerId);
				cm.罢免确认(player, playerB, guanzhi);
			}catch(Exception ex){
				
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
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
