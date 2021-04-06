package com.fy.engineserver.activity.treasure.instance;

import java.util.ArrayList;
import java.util.List;

public class Treasure {
	/** 宝藏类型 */
	private int type;
	/** 已经挖到的物品id */
	private List<Integer> goodIds = new ArrayList<Integer>();
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Integer> getGoodIds() {
		return goodIds;
	}
	public void setGoodIds(List<Integer> goodIds) {
		this.goodIds = goodIds;
	}
}
