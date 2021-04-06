package com.fy.engineserver.newBillboard;

public interface IBillboardPlayerInfo {
	
//	角色ID、国家、家族、职业、级别
	public long getId();
	public String getName();
	public byte getCountry();
	public long getJiazuId();
	public byte getMainCareer();
	public int getLevel();
	public long getExp();
	public long getGongxun();
	
	public long getLastUpdateExpTime();
	
	
	
}
