package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Dismiss_Confirm extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.CLIENT_FUNCTION_ZONGPAI_DISMISS;
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
		
		ZongPaiManager.getInstance().dismissZongPaiConfirm(player);
	}

	
	
}
