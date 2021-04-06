package com.fy.engineserver.datasource.article.data.horseInlay.module;

import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.util.SimpleKey;

public class InlayModule {
	/** 第几孔 */
	@SimpleKey
	private int index;
	/** 洗孔消耗  [0]物品名  [1]物品数量 */
	private String[] cost4punch;
	/** 刷新孔消耗  [0]物品名  [1]物品数量 */
	private String[] cost4reset;
	/** 各个颜色概率 */
	private int[] colorProb;
	
	private long tempAeId;
	
	public String[] getCostByType (int opt) {
		if (opt == HorseEquInlayEntityManager.开孔) {
			return cost4punch;
		} else if (opt == HorseEquInlayEntityManager.洗孔) {
			return cost4reset;
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String[] getCost4punch() {
		return cost4punch;
	}

	public void setCost4punch(String[] cost4punch) {
		this.cost4punch = cost4punch;
	}

	public String[] getCost4reset() {
		return cost4reset;
	}

	public void setCost4reset(String[] cost4reset) {
		this.cost4reset = cost4reset;
	}

	public int[] getColorProb() {
		return colorProb;
	}

	public void setColorProb(int[] colorProb) {
		this.colorProb = colorProb;
	}

	public long getTempAeId() {
		return tempAeId;
	}

	public void setTempAeId(long tempAeId) {
		this.tempAeId = tempAeId;
	}
	
}
