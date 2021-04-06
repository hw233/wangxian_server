package com.fy.engineserver.soulpith.module;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;

/**
 * 灵髓道具属性配置
 * 
 * @date on create 2016年3月23日 下午4:41:03
 */
public class SoulPithAritlcePropertyModule{
	private String articleCNNName;
	private int level;
	private int[] types;
	private int[] soulNums;
	private int[] properTypes;
	private int[] properNums;
	private String corner;
	
	@Override
	public String toString() {
		return "SoulPithAritlcePropertyModule [articleCNNName=" + articleCNNName + ", level=" + level + ", types=" + Arrays.toString(types) + ", soulNums=" + Arrays.toString(soulNums) + ", properTypes=" + Arrays.toString(properTypes) + ", properNums=" + Arrays.toString(properNums) + ", levelData=" + levelData + "]";
	}

	private Map<Integer, SoulPithArticleLevelData> levelData = new HashMap<Integer, SoulPithArticleLevelData>();
	
	public SoulPithArticleLevelData getSoulPithArticleLevelData() {
		return new SoulPithArticleLevelData(level, types, soulNums, properTypes, properNums, corner);
	}
	
	public String getArticleCNNName() {
		return articleCNNName;
	}

	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

	public void setSoulNums(int[] soulNums) {
		this.soulNums = soulNums;
	}

	public void setProperTypes(int[] properTypes) {
		this.properTypes = properTypes;
	}

	public void setProperNums(int[] properNums) {
		this.properNums = properNums;
	}

	public Map<Integer, SoulPithArticleLevelData> getLevelData() {
		return levelData;
	}

	public void setLevelData(Map<Integer, SoulPithArticleLevelData> levelData) {
		this.levelData = levelData;
	}

	public String getCorner() {
		return corner;
	}

	public void setCorner(String corner) {
		this.corner = corner;
	}
}
