package com.fy.engineserver.datasource.skill.master;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 大师技能bean
 * 
 * create on 2013年9月6日
 */
@SimpleEntity
public class SkBean implements Cacheable {
	@SimpleVersion
	private int version;
	@SimpleId
	public long pid;
	public int point;
	public int returnPointNum;
	/**
	 * 元神和本尊都有自己的等级，使用时按需要将对应的数组赋值给这个数组。
	 */
	public transient byte[] useLevels;
	public transient boolean isSoul;
	private byte[] levels;
	public byte[] soulLevels;
	public int exchangeTimes;
	public int lastExchangeDay;
	/**
	 * 总共加了多少点。
	 */
	public transient int sumAddPoint;
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
		SkEnhanceManager.em.notifyFieldChange(this, "point");
	}
	public byte[] getLevels() {
		return levels;
	}
	public void setLevels(byte[] levels) {
		this.levels = levels;
		SkEnhanceManager.em.notifyFieldChange(this, "levels");
	}
	public int getExchangeTimes() {
		return exchangeTimes;
	}
	public void setExchangeTimes(int exchangeTimes) {
		this.exchangeTimes = exchangeTimes;
		SkEnhanceManager.em.notifyFieldChange(this, "exchangeTimes");
	}
	public int getLastExchangeDay() {
		return lastExchangeDay;
	}
	public void setLastExchangeDay(int lastExchangeDay) {
		this.lastExchangeDay = lastExchangeDay;
		SkEnhanceManager.em.notifyFieldChange(this, "lastExchangeDay");
	}
	
	
	public int getReturnPointNum() {
		return returnPointNum;
	}
	public void setReturnPointNum(int returnPointNum) {
		this.returnPointNum = returnPointNum;
		SkEnhanceManager.em.notifyFieldChange(this, "returnPointNum");
	}
	@Override
	public int getSize() {
		return 30;
	}
	public byte[] getSoulLevels() {
		return soulLevels;
	}
	public void setSoulLevels(byte[] soulLevels) {
		this.soulLevels = soulLevels;
		SkEnhanceManager.em.notifyFieldChange(this, "soulLevels");
	}
}
