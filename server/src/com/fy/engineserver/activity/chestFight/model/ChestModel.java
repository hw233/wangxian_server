package com.fy.engineserver.activity.chestFight.model;

import java.util.ArrayList;
import java.util.List;

public class ChestModel {
	/** 箱子类型 */
	private int chestType;
	/** 宝箱名 */
	private String chestName;
	/** 获取多久后开启 */
	private long openTime;
	/** 宝箱能开出来的物品列表 */
	private List<ChestArticleModel> articleList = new ArrayList<ChestArticleModel>();
	/** 一个玩家最多可获得几个此类宝箱 */
	private int maxObtain;
	/** 宝箱npcId */
	private int npcId;
	
	@Override
	public String toString() {
		return "ChestModel [chestType=" + chestType + ", chestName=" + chestName + ", openTime=" + openTime + ", articleList=" + articleList + ", maxObtain=" + maxObtain + ", npcId=" + npcId + "]";
	}
	public int getChestType() {
		return chestType;
	}
	public void setChestType(int chestType) {
		this.chestType = chestType;
	}
	public long getOpenTime() {
		return openTime;
	}
	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}
	public List<ChestArticleModel> getArticleList() {
		return articleList;
	}
	public void setArticleList(List<ChestArticleModel> articleList) {
		this.articleList = articleList;
	}
	public int getMaxObtain() {
		return maxObtain;
	}
	public void setMaxObtain(int maxObtain) {
		this.maxObtain = maxObtain;
	}
	public int getNpcId() {
		return npcId;
	}
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	public String getChestName() {
		return chestName;
	}
	public void setChestName(String chestName) {
		this.chestName = chestName;
	}
	
	
	
}
