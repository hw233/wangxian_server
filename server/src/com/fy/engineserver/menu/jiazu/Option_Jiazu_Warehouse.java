package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

//npc查看家族仓库
public class Option_Jiazu_Warehouse extends Option{

	public Option_Jiazu_Warehouse(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		JiazuManager.getInstance().lookWareHouse(player);
	}
}
