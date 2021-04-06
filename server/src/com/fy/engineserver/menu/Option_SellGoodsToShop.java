package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;

/**
 * 进入商店
 * 
 * 
 *
 */
public class Option_SellGoodsToShop extends Option{

	protected Shop shop;
	protected Player player;
	protected int knapsackIndex;
//	int knapsackType;
	boolean fangbaoFlag;
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getKnapsackIndex() {
		return knapsackIndex;
	}

	public void setKnapsackIndex(int knapsackIndex) {
		this.knapsackIndex = knapsackIndex;
	}

	public boolean isFangbaoFlag() {
		return fangbaoFlag;
	}

	public void setFangbaoFlag(boolean fangbaoFlag) {
		this.fangbaoFlag = fangbaoFlag;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		shop.doSellArticleEntity(player, fangbaoFlag, knapsackIndex, true, false);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_卖商店;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "";
	}
}
