package com.fy.engineserver.marriage;


import com.fy.engineserver.marriage.manager.MarriageManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class MarriageBeq {

	public static final byte STATE_START = 0;					//初始
	public static final byte STATE_START1 = 100;				//初始 以发请求，但对方还未点
	public static final byte STATE_AGREE = 1;					//被求婚同意
	public static final byte STATE_REFUSE = 2;					//被求婚不同意
	public static final byte STATE_OVER = 3;					//求婚者收到通知，结束
	
	//求婚信息
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionMBeq;
	
	private long sendId;						//发起者ID
	
	private long toId;							//被求婚者ID
	
	@SimpleColumn(name="level2")
	private int level;							//求婚用花或用糖级别(个数)
	
	private byte state;							//状态
	
	private transient long sendTime;			//给被求婚者发提示的时间，或被求婚者同意不同意后给求婚者发提示的时间
	
	public MarriageBeq(){
		
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setSendId(long sendId) {
		this.sendId = sendId;
	}

	public long getSendId() {
		return sendId;
	}

	public void setToId(long toId) {
		this.toId = toId;
	}

	public long getToId() {
		return toId;
	}

	public void setState(byte state) {
		this.state = state;
		MarriageManager.getInstance().emBeq.notifyFieldChange(this, "state");
	}

	public byte getState() {
		return state;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setVersionMBeq(int versionMBeq) {
		this.versionMBeq = versionMBeq;
	}

	public int getVersionMBeq() {
		return versionMBeq;
	}
}
