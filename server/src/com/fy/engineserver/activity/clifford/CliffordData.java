package com.fy.engineserver.activity.clifford;

import java.util.HashMap;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class CliffordData {
	
	@SimpleId
	public long id = 1;
	
	@SimpleVersion
	protected int version2;
	
	/**
	 * 每天已经祈福次数
	 * key 玩家id，value 祈福次数
	 */
	@SimpleColumn(length=200000)
	public HashMap<Long,Integer> todayCliffordCountMap = new HashMap<Long,Integer>();

	/**
	 * 已开启的祈福物品id
	 * key 玩家id，value 已开启的祈福物品id
	 */
	@SimpleColumn(length=200000)
	public HashMap<Long,long[]> openedCliffordArticleIdMap = new HashMap<Long,long[]>();

	/**
	 * 已开启的祈福物品id
	 * key 玩家id，value 已开启的祈福物品id
	 */
	@SimpleColumn(length=200000)
	public HashMap<Long,int[]> openedCliffordArticleNumMap = new HashMap<Long,int[]>();

	public transient boolean dirty;
	
	public HashMap<Long, Integer> getTodayCliffordCountMap() {
		return todayCliffordCountMap;
	}

	public void setTodayCliffordCountMap(
			HashMap<Long, Integer> todayCliffordCountMap) {
		this.todayCliffordCountMap = todayCliffordCountMap;
	}

	public HashMap<Long, long[]> getOpenedCliffordArticleIdMap() {
		return openedCliffordArticleIdMap;
	}

	public void setOpenedCliffordArticleIdMap(
			HashMap<Long, long[]> openedCliffordArticleIdMap) {
		this.openedCliffordArticleIdMap = openedCliffordArticleIdMap;
	}

	public HashMap<Long, int[]> getOpenedCliffordArticleNumMap() {
		return openedCliffordArticleNumMap;
	}

	public void setOpenedCliffordArticleNumMap(
			HashMap<Long, int[]> openedCliffordArticleNumMap) {
		this.openedCliffordArticleNumMap = openedCliffordArticleNumMap;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
}
