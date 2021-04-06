package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Invite_disagree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	Player p;
	
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		ZongPaiManager.getInstance().disagreeInvite(player, p);
	}

	
	
}
