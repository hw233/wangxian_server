package com.fy.boss.gm.newfeedback;

import com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 玩家反馈统计
 * @author wtx
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"recordid"})
})
public class FeedbackPlayerState {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	private long commitnums;
	
	private long deletenums;
	
	private long manyinums;
	
	private long yibannums;
	
	private long bumanyinums;
	
	private long talknums;
	
	//玩家操作时间
	private long recordtime;

	private String recordid;
	
	public long getRecordtime() {
		return recordtime;
	}

	public long getTalknums() {
		return talknums;
	}

	public void setTalknums(long talknums) {
		this.talknums = talknums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "talknums");
	}


	public void setRecordtime(long recordtime) {
		this.recordtime = recordtime;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "recordtime");
	}

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "recordid");
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

	public long getCommitnums() {
		return commitnums;
	}

	public void setCommitnums(long commitnums) {
		this.commitnums = commitnums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "commitnums");
	}

	public long getDeletenums() {
		return deletenums;
	}

	public void setDeletenums(long deletenums) {
		this.deletenums = deletenums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "deletenums");
	}

	public long getManyinums() {
		return manyinums;
	}

	public void setManyinums(long manyinums) {
		this.manyinums = manyinums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "manyinums");
	}

	public long getYibannums() {
		return yibannums;
	}

	public void setYibannums(long yibannums) {
		this.yibannums = yibannums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "yibannums");
	}

	public long getBumanyinums() {
		return bumanyinums;
	}

	public void setBumanyinums(long bumanyinums) {
		this.bumanyinums = bumanyinums;
		NewFeedbackStateManager.getInstance().psem.notifyFieldChange(this, "bumanyinums");
	}
	
	
}
