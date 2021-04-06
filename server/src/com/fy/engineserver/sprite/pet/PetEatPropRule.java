package com.fy.engineserver.sprite.pet;

public class PetEatPropRule {
	
	private int eatTime;		//第几次吃
	private int needEatNums;	//需要吃的数量
	private int costData;		//消化时间(单位：分)
	private int delCDMoney;		//清除cd需要的钱(单位：两)
	
	public int getEatTime() {
		return eatTime;
	}
	public void setEatTime(int eatTime) {
		this.eatTime = eatTime;
	}
	public int getNeedEatNums() {
		return needEatNums;
	}
	public void setNeedEatNums(int needEatNums) {
		this.needEatNums = needEatNums;
	}
	public int getCostData() {
		return costData;
	}
	public void setCostData(int costData) {
		this.costData = costData;
	}
	public int getDelCDMoney() {
		return delCDMoney;
	}
	public void setDelCDMoney(int delCDMoney) {
		this.delCDMoney = delCDMoney;
	}
	@Override
	public String toString() {
		return "PetEatPropRule [costData=" + costData + ", delCDMoney="
				+ delCDMoney + ", eatTime=" + eatTime + ", needEatNums="
				+ needEatNums + "]";
	}
	
}
