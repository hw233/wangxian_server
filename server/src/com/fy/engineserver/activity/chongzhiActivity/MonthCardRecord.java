package com.fy.engineserver.activity.chongzhiActivity;

import com.fy.engineserver.activity.ActivityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"haveDays"}),
	@SimpleIndex(members={"cardName"})
})
public class MonthCardRecord {

	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	private long pid;
	private String cardName;
	private long haveDays;		//失效时间
	private long lastRewardTime;
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getPid() {
		return this.pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
		ActivityManager.em_record.notifyFieldChange(this, "pid");
	}
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getCardName() {
		return this.cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
		ActivityManager.em_record.notifyFieldChange(this, "cardName");
	}
	public long getHaveDays() {
		return this.haveDays;
	}
	public void setHaveDays(long haveDays) {
		this.haveDays = haveDays;
		ActivityManager.em_record.notifyFieldChange(this, "haveDays");
	}
	public long getLastRewardTime() {
		return this.lastRewardTime;
	}
	public void setLastRewardTime(long lastRewardTime) {
		this.lastRewardTime = lastRewardTime;
		ActivityManager.em_record.notifyFieldChange(this, "lastRewardTime");
	}
	@Override
	public String toString() {
		return "MonthCardRecord [id=" + this.id + ", version=" + this.version + ", cardName=" + this.cardName + ", haveDays=" + this.haveDays + ", lastRewardTime=" + this.lastRewardTime + "]";
	}
	
}
