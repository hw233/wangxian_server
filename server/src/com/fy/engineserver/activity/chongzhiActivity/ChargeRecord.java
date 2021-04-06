package com.fy.engineserver.activity.chongzhiActivity;

import com.fy.engineserver.activity.ActivityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"days"})
})
public class ChargeRecord {

	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	private int days;	//第几天连续充值
	private long lastChargeTime;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getDays() {
		return this.days;
	}

	public void setDays(int days) {
		this.days = days;
		ActivityManager.em_charge.notifyFieldChange(this, "days");
	}

	public long getLastChargeTime() {
		return this.lastChargeTime;
	}

	public void setLastChargeTime(long lastChargeTime) {
		this.lastChargeTime = lastChargeTime;
		ActivityManager.em_charge.notifyFieldChange(this, "lastChargeTime");
	}

	@Override
	public String toString() {
		return "ChargeRecord [id=" + this.id + ", version=" + this.version + ", days=" + this.days + ", lastChargeTime=" + this.lastChargeTime + "]";
	}

}
