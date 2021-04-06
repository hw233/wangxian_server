package com.fy.engineserver.menu.activity.explore;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 兑换奖励
 * @author Administrator
 *
 */
public class Option_Exchange extends Option {
	
	private int type;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		ExploreManager.getInstance().convertFromArticle(player,type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}


