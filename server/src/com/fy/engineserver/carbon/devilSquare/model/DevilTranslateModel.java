package com.fy.engineserver.carbon.devilSquare.model;

import java.util.Arrays;

public class DevilTranslateModel {
	/** 副本等级 */
	private int level;
	/** 玩法 */
	private String plays;
	/** 背景 */
	private String bcStory;
	/** 掉落物品 */
	private long[] dropProps;
	/** 掉率 */
	private String[] dropProbabbly;
	/** 门票 */
	private long costProps;
	/** 颜色 */
	private int color;
	/** 数量 */
	private int costNum;
	
	@Override
	public String toString() {
		return "DevilTranslateModel [level=" + level + ", plays=" + plays + ", bcStory=" + bcStory + ", dropProps=" + Arrays.toString(dropProps) + ", dropProbabbly=" + Arrays.toString(dropProbabbly) + ", costProps=" + costProps + ", color=" + color + ", costNum=" + costNum + "]";
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPlays() {
		return plays;
	}
	public void setPlays(String plays) {
		this.plays = plays;
	}
	public String getBcStory() {
		return bcStory;
	}
	public void setBcStory(String bcStory) {
		this.bcStory = bcStory;
	}
	public long[] getDropProps() {
		return dropProps;
	}
	public void setDropProps(long[] dropProps) {
		this.dropProps = dropProps;
	}
	public long getCostProps() {
		return costProps;
	}
	public void setCostProps(long costProps) {
		this.costProps = costProps;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getCostNum() {
		return costNum;
	}
	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}
	public String[] getDropProbabbly() {
		return dropProbabbly;
	}
	public void setDropProbabbly(String[] dropProbabbly) {
		this.dropProbabbly = dropProbabbly;
	}
	
	
	
}
