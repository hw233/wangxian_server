package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 该选项代表要领取某个物品，当玩家身上该物品的数量少于
 * amountLimit的时候，可以领取该物品，否则不能领取
 * 
 * @author Administrator
 *
 */
public class Option_RequireArticle extends Option {
	int amountLimit = 1;
	String articleName;
	
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getAmountLimit() {
		return amountLimit;
	}

	public void setAmountLimit(int amountLimit) {
		this.amountLimit = amountLimit;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取物品;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void doSelect(Game game, Player player) {}
	
	
}
