package com.fy.engineserver.activity.treasure.model;

import java.util.ArrayList;
import java.util.List;

public class TreasureModel {
	/** 宝藏类型 */
	private int type;
	
	private boolean isopen;
	/** 宝藏名 */
	private String name;
	/** 参与等级限制 */
	private int levelLimit;
	/** 此类型寻宝可产物物品列表 */
	private List<Integer> goodIds = new ArrayList<Integer>();
	/** 挖宝次数 */
	private List<Integer> digTimes = new ArrayList<Integer>();
	/** 对应需要的银子 */
	private List<Long> costS = new ArrayList<Long>();
	/** 对应需要的免费道具统计名 */
	private List<String> costArticles = new ArrayList<String>();
	/** 特殊产出物品列表  需要展示给玩家 */
	private List<Integer> needShowGoods = new ArrayList<Integer>();
	/** 玩法说明描述 */
	private String description;
	/**
	 * 
	 * @param costType
	 * @return   小于零代表代表出错
	 */
	public long getCostSiliver(int costType) {
		for (int i=0; i<digTimes.size(); i++) {
			if (digTimes.get(i) == costType) {
				return costS.get(i);
			}
		}
		return -1;
	}
	/**
	 * 
	 * @param costType
	 * @return   null代表出错
	 */
	public String getCostArticles(int costType) {
		if (costArticles == null || costArticles.size() <= 0) {
			return null;
		}
		for (int i=0; i<digTimes.size(); i++) {
			if (digTimes.get(i) == costType && costArticles.size() > i) {
				return costArticles.get(i);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "TreasureModel [type=" + type + ", name=" + name + ", levelLimit=" + levelLimit + ", goodIds=" + goodIds + ", digTimes=" + digTimes + ", costS=" + costS + ", costArticles=" + costArticles + ", needShowGoods=" + needShowGoods + ", description=" + description + "]";
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getGoodIds() {
		return goodIds;
	}
	public void setGoodIds(List<Integer> goodIds) {
		this.goodIds = goodIds;
	}
	public List<Integer> getDigTimes() {
		return digTimes;
	}
	public void setDigTimes(List<Integer> digTimes) {
		this.digTimes = digTimes;
	}
	public List<Long> getCostS() {
		return costS;
	}
	public void setCostS(List<Long> costS) {
		this.costS = costS;
	}
	public List<String> getCostArticles() {
		return costArticles;
	}
	public void setCostArticles(List<String> costArticles) {
		this.costArticles = costArticles;
	}
	public List<Integer> getNeedShowGoods() {
		return needShowGoods;
	}
	public void setNeedShowGoods(List<Integer> needShowGoods) {
		this.needShowGoods = needShowGoods;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public boolean isIsopen() {
		return isopen;
	}
	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}
	
	
}
