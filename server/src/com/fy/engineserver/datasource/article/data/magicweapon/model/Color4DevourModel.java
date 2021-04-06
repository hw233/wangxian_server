package com.fy.engineserver.datasource.article.data.magicweapon.model;
/**
 * 吞噬基础需要经验及消耗金钱
 * @author Administrator
 *
 */
public class Color4DevourModel {
	/** 颜色 */
	private int color;
	/** 消耗金钱 */
	private int costSiliver;
	/** 升级所需经验 */
	private int exp4Levelup;
	
	@Override
	public String toString() {
		return "Color4DevourModel [costSiliver=" + costSiliver + ", exp4Levelup=" + exp4Levelup + "]";
	}
	
	public int getCostSiliver() {
		return costSiliver;
	}
	public void setCostSiliver(int costSiliver) {
		this.costSiliver = costSiliver;
	}
	public int getExp4Levelup() {
		return exp4Levelup;
	}
	public void setExp4Levelup(int exp4Levelup) {
		this.exp4Levelup = exp4Levelup;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	
}
