package com.fy.engineserver.menu.qianCeng;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Player;

//打开千层塔界面
public class Option_OpenQianCeng extends Option{
	public Option_OpenQianCeng(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		QianCengTaManager.getInstance().opt_open(player);
	}
}
