package com.fy.engineserver.menu.chuangong;

import com.fy.engineserver.chuangong.ChuanGongManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 传功同意
 * @author Administrator
 *
 */
public class Option_Chuangong_Agree extends Option {
	
	private Player active;


	public Player getActive() {
		return active;
	}


	public void setActive(Player active) {
		this.active = active;
	}


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		String result = ChuanGongManager.getInstance().agreeChuangong(player,active);
		if(!result.equals("")){
			player.send_HINT_REQ(result);
		}
	}

}
