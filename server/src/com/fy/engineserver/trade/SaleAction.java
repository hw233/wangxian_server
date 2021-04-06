package com.fy.engineserver.trade;

import java.util.List;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public interface SaleAction {

	/**
	 * 取消出售物品
	 * @param saler
	 * @param item
	 * @throws Throwable
	 */
	CompoundReturn cancel(Player saler , long itemId) throws Throwable;

	/**
	 * 物品出售过期
	 * @param saleItemId
	 * @throws Throwable
	 */
	CompoundReturn timeOut(long saleItemId) throws Throwable;

	CompoundReturn timeOut(List<Long> saleItemIds) throws Throwable;
	/**
	 * 购买
	 * @param purchaser 购买者
	 * @param saler 出售者
	 * @param item	物品
	 * @param num	交易数量
	 * @throws Throwable
	 */
	CompoundReturn trade(Player purchaser, Player saler, long itemId, int num) throws Throwable;

	/**
	 * 选择物品出售
	 * @param saler 出售者
	 * @param bagType 背包类型
	 * @param index	物品在背包中的位置
	 * @param num 物品数量
	 * @param perPrice 物品单价
	 * @throws Throwable
	 */
	CompoundReturn selectSale(Player saler, byte bagType, int index, int num, int perPrice) throws Throwable;

}
