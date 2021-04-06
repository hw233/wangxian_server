package com.fy.engineserver.menu.guozhan;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;


/**
 * 加入国战
 * 
 * 
 *
 */
public class Option_Attend_Guozhan extends Option {

	/**
	 * 玩家加入国战
	 * 1.判断当前国家有无国战，有国战发送确认消息
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		long start = System.currentTimeMillis();
		if(player.getLevel() < GuozhanOrganizer.getInstance().getConstants().国战等级限制) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.玩家等级不够);
			player.addMessageToRightBag(hintreq);
			return;
		}
		CountryManager cm = CountryManager.getInstance();
		GuozhanOrganizer org = GuozhanOrganizer.getInstance();
		Guozhan g = org.getPlayerGuozhan(player);
		if(g == null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.本国当前没有进行国战);
			player.addMessageToRightBag(hintreq);
			return;
		} else {
			String enemyCountryName = null;
			if(player.getCountry() == g.getAttacker().getCountryId()) {
				enemyCountryName = cm.得到国家名(g.getDefender().getCountryId());
			} else {
				enemyCountryName = cm.得到国家名(g.getAttacker().getCountryId());
			}
			org.sendConfirmFightToPlayer(player, enemyCountryName);
			if(GuozhanOrganizer.logger.isDebugEnabled())
				GuozhanOrganizer.logger.debug("[玩家请求参与国战] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
 		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}
}
