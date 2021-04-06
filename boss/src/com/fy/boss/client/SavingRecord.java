package com.fy.boss.client;

public class SavingRecord {
	
	private String statusDesp;
	
	private String orderId;
	
	private long createTime;
	
	private long savingAmount;
	
	private String savingMedium;

	public String getStatusDesp() {
		return statusDesp;
	}

	public void setStatusDesp(String statusDesp) {
		this.statusDesp = statusDesp;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getSavingAmount() {
		return savingAmount;
	}

	public void setSavingAmount(long savingAmount) {
		this.savingAmount = savingAmount;
	}

	public String getSavingMedium() {
		return savingMedium;
	}

	public void setSavingMedium(String savingMedium) {
		this.savingMedium = savingMedium;
	}
	
}
