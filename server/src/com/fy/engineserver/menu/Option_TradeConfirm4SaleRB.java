package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;

public class Option_TradeConfirm4SaleRB extends Option {

	private long entityId;
	private int index;
	private int saleNum;
	private long rbId;

	public Option_TradeConfirm4SaleRB(long entityId, int index, int saleNum, long rbId) {
		this.entityId = entityId;
		this.index = index;
		this.saleNum = saleNum;
		this.rbId = rbId;

	}

	@Override
	public synchronized void doSelect(Game game, Player player) {
		RequestBuyManager.getInstance().releaseRequestSale(player, index, entityId, saleNum, rbId, true);
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public long getRbId() {
		return rbId;
	}

	public void setRbId(long rbId) {
		this.rbId = rbId;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
