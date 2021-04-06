package com.fy.engineserver.menu.qianCeng;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Player;

public class Option_NewQianCengFlush extends Option{
	int daoIndex;
	int nandu;
	public Option_NewQianCengFlush(){
		super();
	}
	public Option_NewQianCengFlush(int nandu, int daoIndex){
		super();
		this.daoIndex = daoIndex;
		this.nandu = nandu;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		QianCengTaManager.getInstance().getTa(player.getId()).ope_flush(player, nandu, daoIndex);
	}
}
