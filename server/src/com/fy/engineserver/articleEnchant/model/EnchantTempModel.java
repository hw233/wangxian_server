package com.fy.engineserver.articleEnchant.model;

public class EnchantTempModel {
	/** 附魔id */
	private int id;
	/** 装备id */
	private long equiptId;
	
	public EnchantTempModel(int id, long eid) {
		this.id = id;
		this.equiptId = eid;
	}
	
	@Override
	public String toString() {
		return "EnchantTempModel [id=" + id + ", equiptId=" + equiptId + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getEquiptId() {
		return equiptId;
	}
	public void setEquiptId(long equiptId) {
		this.equiptId = equiptId;
	}
	
	
}
