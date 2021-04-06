package com.fy.engineserver.playerAims.instance;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlayerAim {
	/** 目标id */
	private int aimId;
	/** 完成时间 */
	private long completTime;
	/** 领取状态 (0未领取   1普通领取   2vip领取  3普通和vip均已领取)*/
	private byte reveiveStatus = -1;
	
	public int getAimId() {
		return aimId;
	}
	public void setAimId(int aimId) {
		this.aimId = aimId;
	}
	public long getCompletTime() {
		return completTime;
	}
	public void setCompletTime(long completTime) {
		this.completTime = completTime;
	}
	public byte getReveiveStatus() {
		return reveiveStatus;
	}
	public void setReveiveStatus(byte reveiveStatus) {
		this.reveiveStatus = reveiveStatus;
	}
	
}
