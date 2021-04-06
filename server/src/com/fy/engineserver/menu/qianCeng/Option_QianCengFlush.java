package com.fy.engineserver.menu.qianCeng;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Player;

public class Option_QianCengFlush extends Option{
	int daoIndex;
	public Option_QianCengFlush(){
		super();
	}
	public Option_QianCengFlush(int daoIndex){
		super();
		this.daoIndex = daoIndex;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		QianCengTaManager.getInstance().getTa(player.getId()).ope_flush(player, 0, daoIndex);
	}
}
