package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Invite_agree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	ZongPai zp;
	public ZongPai getZp() {
		return zp;
	}
	public void setZp(ZongPai zp) {
		this.zp = zp;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		ZongPaiManager.getInstance().agreeInvite(player, zp);
	}

	
	
}
