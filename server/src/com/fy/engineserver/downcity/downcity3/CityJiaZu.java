package com.fy.engineserver.downcity.downcity3;

public class CityJiaZu {

	private long jiaZuId;
	private String jiazuName;
	private long jiaZuDamage;
	
	public long getJiaZuId() {
		return jiaZuId;
	}
	public void setJiaZuId(long jiaZuId) {
		this.jiaZuId = jiaZuId;
	}
	public long getJiaZuDamage() {
		return jiaZuDamage;
	}
	public void setJiaZuDamage(long jiaZuDamage) {
		this.jiaZuDamage = jiaZuDamage;
	}
	public String getJiazuName() {
		return jiazuName;
	}
	public void setJiazuName(String jiazuName) {
		this.jiazuName = jiazuName;
	}
	@Override
	public String toString() {
		return "[jiaZuDamage=" + jiaZuDamage + ", jiaZuId=" + jiaZuId
				+ ", jiazuName=" + jiazuName + "]";
	}
	
}
