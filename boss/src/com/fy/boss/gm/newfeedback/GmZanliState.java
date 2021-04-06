package com.fy.boss.gm.newfeedback;

import com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * GM暂离统计
 * @author hkc
 */
@SimpleEntity
public class GmZanliState {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	@SimpleColumn(saveInterval=1) 
	private long zanlibegintime;
	@SimpleColumn(saveInterval=1) 
	private long zanliendtime;
	@SimpleColumn(saveInterval=1) 
	private String gmnum;
	@SimpleColumn(saveInterval=1) 
	private int duration;
	
	

	public long getZanlibegintime() {
		return zanlibegintime;
	}


	public void setZanlibegintime(long zanlibegintime) {
		this.zanlibegintime = zanlibegintime;
		NewFeedbackStateManager.getInstance().zlsem.notifyFieldChange(this, "zanbegintime");
	}


	public long getZanliendtime() {
		return zanliendtime;
	}


	public void setZanliendtime(long zanliendtime) {
		this.zanliendtime = zanliendtime;
		NewFeedbackStateManager.getInstance().zlsem.notifyFieldChange(this, "zanliendtime");
	}


	public String getGmnum() {
		return gmnum;
	}


	public void setGmnum(String gmnum) {
		this.gmnum = gmnum;
		NewFeedbackStateManager.getInstance().zlsem.notifyFieldChange(this, "gmnum");
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
		NewFeedbackStateManager.getInstance().zlsem.notifyFieldChange(this, "duration");
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
