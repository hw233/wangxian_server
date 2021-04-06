package com.fy.engineserver.jiazu2.model;

public class BiaoCheQianghuaModel {
	/** 1强化血量   2强化双防 */
	private byte type;
	/** 强化等级 */
	private int level;
	/** 消耗家族资金数量 */
	private long costJiazuMoney;
	/** 消耗家族灵脉值数量 */
	private long costLingmai;
	/** 增加镖车数值百分比 */
	private int addNum;
	/** 需要家族等级 */
	private int needJiazuLevel;
	
	@Override
	public String toString() {
		return "BiaoCheQianghuaModel [type=" + type + ", level=" + level + ", costJiazuMoney=" + costJiazuMoney + ", costLingmai=" + costLingmai + ", addNum=" + addNum + ", needJiazuLevel=" + needJiazuLevel + "]";
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getCostJiazuMoney() {
		return costJiazuMoney;
	}
	public void setCostJiazuMoney(long costJiazuMoney) {
		this.costJiazuMoney = costJiazuMoney;
	}
	public long getCostLingmai() {
		return costLingmai;
	}
	public void setCostLingmai(long costLingmai) {
		this.costLingmai = costLingmai;
	}
	public int getAddNum() {
		return addNum;
	}
	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}
	public int getNeedJiazuLevel() {
		return needJiazuLevel;
	}
	public void setNeedJiazuLevel(int needJiazuLevel) {
		this.needJiazuLevel = needJiazuLevel;
	}
	
	
}
