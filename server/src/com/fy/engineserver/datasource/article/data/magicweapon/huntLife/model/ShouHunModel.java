package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model;

import java.util.Arrays;

public class ShouHunModel {
	/** 兽魂等级 */
	private int level;
	/** 升级所需经验 */
	private long exp4Up;
	/** 被吞噬给予基础经验（需要额外加上本身带有的经验） */
	private long exp4Give;
	/** 增加属性数值 */
	private int[] addAttrNums;
	/** 底框 */
	private String icon1;
	/** 角标 */
	private String icon2;
	
	@Override
	public String toString() {
		return "ShouHunModel [level=" + level + ", exp4Up=" + exp4Up + ", exp4Give=" + exp4Give + ", addAttrNums=" + Arrays.toString(addAttrNums) + ", icon1=" + icon1 + ", icon2=" + icon2 + "]";
	}

	public String getIcon1() {
		return icon1;
	}

	public void setIcon1(String icon1) {
		this.icon1 = icon1;
	}

	public String getIcon2() {
		return icon2;
	}

	public void setIcon2(String icon2) {
		this.icon2 = icon2;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp4Up() {
		return exp4Up;
	}

	public void setExp4Up(long exp4Up) {
		this.exp4Up = exp4Up;
	}

	public long getExp4Give() {
		return exp4Give;
	}

	public void setExp4Give(long exp4Give) {
		this.exp4Give = exp4Give;
	}


	public int[] getAddAttrNums() {
		return addAttrNums;
	}


	public void setAddAttrNums(int[] addAttrNums) {
		this.addAttrNums = addAttrNums;
	}
	
	
}
