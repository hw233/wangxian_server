package com.fy.engineserver.activity.wafen.instacne.model;

import java.io.Serializable;

public class FenDiModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 坟地索引id */
	private int index;
	/** 挖出来的物品id(对应挖坟活动里边物品的id) */
	private int articleId;
	/** 领取状态 (1为未领取  2为已领取) */
	private byte reciveType;
	/** 挖出来的物品是否绑定(只有银铲子挖出来的是绑定的) */
	private boolean bind;
	
	@Override
	public String toString() {
		return "FenDiModel [index=" + index + ", articleId=" + articleId + ", reciveType=" + reciveType + "]";
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public byte getReciveType() {
		return reciveType;
	}
	public void setReciveType(byte reciveType) {
		this.reciveType = reciveType;
	}
	public boolean isBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	
	
}
