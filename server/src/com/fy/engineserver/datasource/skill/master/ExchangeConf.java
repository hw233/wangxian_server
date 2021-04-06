package com.fy.engineserver.datasource.skill.master;

/**
 * 
 * create on 2013年9月7日
 */
public class ExchangeConf implements Cloneable {
	public int startTimes;
	public int endTimes;
	public long needExp;
	public int needMoney;//两
	public String desc;
	public int index;
	public int getStartTimes() {
		return startTimes;
	}
	public void setStartTimes(int startTimes) {
		this.startTimes = startTimes;
	}
	public int getEndTimes() {
		return endTimes;
	}
	public void setEndTimes(int endTimes) {
		this.endTimes = endTimes;
	}
	public long getNeedExp() {
		return needExp;
	}
	public void setNeedExp(long needExp) {
		this.needExp = needExp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public ExchangeConf clone() {
		try{
			return (ExchangeConf) super.clone();
		}catch ( CloneNotSupportedException e){
			SkEnhanceManager.log.error("ExchangeConf.clone: {}", e);
			return null;
		}
	}
}
