package com.fy.engineserver.activity.wafen.instacne.model;

public class FenDiModel4Client {
	private int fendiIndex;				//坟地索引
	private long articleId;				//临时物品id
	private byte receiveType;			//领取状态
	
	public FenDiModel4Client(){}
	
	public FenDiModel4Client(int fendiIndex, long articleId, byte receiveType) {
		super();
		this.fendiIndex = fendiIndex;
		this.articleId = articleId;
		this.receiveType = receiveType;
	}



	public int getFendiIndex() {
		return fendiIndex;
	}

	public void setFendiIndex(int fendiIndex) {
		this.fendiIndex = fendiIndex;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public byte getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(byte receiveType) {
		this.receiveType = receiveType;
	}

	
}
