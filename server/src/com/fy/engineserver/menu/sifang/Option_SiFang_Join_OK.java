package com.fy.engineserver.menu.sifang;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sifang.SiFangManager;
import com.fy.engineserver.sprite.Player;

//确定报名
public class Option_SiFang_Join_OK extends Option{
	int type;
	public Option_SiFang_Join_OK(int type) {
		super();
		this.type = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		SiFangManager.getInstance().opt_BaoMingOK(player, type);
	}
}
