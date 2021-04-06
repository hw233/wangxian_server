package com.fy.engineserver.activity.TransitRobbery.model;

public class AwardModel {
	private int id;
	/** 奖励类型 */
	private int awardType;
	/** 奖励*/
	private String awardItem;
	/** 奖励数量 */
	private int amount;
	
	public String toString(){
		return "id=" + id + " awardType=" + awardType + " awardItem=" + awardItem + " amount=" + amount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAwardType() {
		return awardType;
	}
	public void setAwardType(int awardType) {
		this.awardType = awardType;
	}
	public String getAwardItem() {
		return awardItem;
	}
	public void setAwardItem(String awardItem) {
		this.awardItem = awardItem;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
