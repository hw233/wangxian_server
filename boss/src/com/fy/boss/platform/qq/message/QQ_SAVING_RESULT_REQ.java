package com.fy.boss.platform.qq.message;

import com.fy.boss.platform.qq.message.QQMessage;

public class QQ_SAVING_RESULT_REQ extends QQMessage {
	
	private String uid;
	
	private String linkId;
	
	private int buyId;
	
	private short goodsId;
	
	private int goodsCount;
	
	private int buyTime;
	
	public QQ_SAVING_RESULT_REQ(int ver, int seqNum, int cmd,String uid,String linkId,int buyId,short goodsId,int goodsCount,int buyTime)
	{
		super(ver,seqNum,cmd);
		this.uid = uid;
		this.linkId = linkId;
		this.buyId = buyId;
		this.goodsId = goodsId;
		this.goodsCount = goodsCount;
		this.buyTime = buyTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public int getBuyId() {
		return buyId;
	}

	public void setBuyId(int buyId) {
		this.buyId = buyId;
	}

	public short getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(short goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public int getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(int buyTime) {
		this.buyTime = buyTime;
	}
	
	


}
