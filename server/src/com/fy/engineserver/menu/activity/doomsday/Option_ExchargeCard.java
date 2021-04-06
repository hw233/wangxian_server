package com.fy.engineserver.menu.activity.doomsday;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.activity.doomsday.DoomsdayManager.ExchageData;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 兑换贡献卡
 * 
 * 
 */
public class Option_ExchargeCard extends Option {

	private String requestPropName;// 要消耗的物品名字
	private int requestPropNum;// 要消耗的物品数量

	public Option_ExchargeCard(String requestPropName, int requestPropNum) {
		this.requestPropName = requestPropName;
		this.requestPropNum = requestPropNum;
	}

	@Override
	public void doSelect(Game game, Player player) {
		DoomsdayManager.getInstance().excharge(player, requestPropName, requestPropNum);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
