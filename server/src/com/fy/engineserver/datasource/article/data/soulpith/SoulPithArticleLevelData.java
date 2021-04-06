package com.fy.engineserver.datasource.article.data.soulpith;

import java.util.Arrays;

import com.fy.engineserver.soulpith.SoulPithTypes;

public class SoulPithArticleLevelData {
	/** 灵髓等级 */
	private int level;
	/** 附加的灵髓点类型 */
	private SoulPithTypes[] types;
	/** 对应类型的值 */
	private int[] soulNums;
	/** 附加属性类型 */
	private int[] properTypes;
	
	private int[] properNums;
	
	private String corner;
	
	@Override
	public String toString() {
		return "SoulPithArticleLevelData [level=" + level + ", types=" + Arrays.toString(types) + ", soulNums=" + Arrays.toString(soulNums) + ", properTypes=" + Arrays.toString(properTypes) + ", properNums=" + Arrays.toString(properNums) + "]";
	}

	public SoulPithArticleLevelData(int level, int[] types, int[] soulNums, int[] properTypes, int[] properNums, String corner) {
		this.level = level;
		this.soulNums = soulNums;
		this.types = new SoulPithTypes[types.length];
		for (int i=0; i<this.types.length; i++) {
			this.types[i] = SoulPithTypes.valueOf(types[i]);
		}
		this.properTypes = properTypes;
		this.properNums = properNums;
		this.corner = corner;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public SoulPithTypes[] getTypes() {
		return types;
	}
	public void setTypes(SoulPithTypes[] types) {
		this.types = types;
	}
	public int[] getSoulNums() {
		return soulNums;
	}
	public void setSoulNums(int[] soulNums) {
		this.soulNums = soulNums;
	}
	public int[] getProperTypes() {
		return properTypes;
	}
	public void setProperTypes(int[] properTypes) {
		this.properTypes = properTypes;
	}
	public int[] getProperNums() {
		return properNums;
	}
	public void setProperNums(int[] properNums) {
		this.properNums = properNums;
	}

	public String getCorner() {
		return corner;
	}

	public void setCorner(String corner) {
		this.corner = corner;
	}
}
