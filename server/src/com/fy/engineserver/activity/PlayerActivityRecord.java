package com.fy.engineserver.activity;

import java.io.Serializable;

public class PlayerActivityRecord implements Serializable{

	private static final long serialVersionUID = 1L;

	
	public long dafengsongTime = 0;

	//最后一次在线领取奖品时间
	public long lastOnlineReceiveTime = 0;
	public boolean firstReceive = false;
	public boolean secondReceive = false;
	public boolean thirdReceive = false;
	public boolean fourthReceive = false;

	
	public boolean dirty = true;
	
	
	
	public long getDafengsongTime() {
		return dafengsongTime;
	}
	public void setDafengsongTime(long dafengsongTime) {
		this.dafengsongTime = dafengsongTime;
		setDirty(true);
	}
	public long getLastOnlineReceiveTime() {
		return lastOnlineReceiveTime;
	}
	public void setLastOnlineReceiveTime(long lastOnlineReceiveTime) {
		this.lastOnlineReceiveTime = lastOnlineReceiveTime;
		setDirty(true);
	}

	public boolean isFirstReceive() {
		return firstReceive;
	}

	public void setFirstReceive(boolean firstReceive) {
		this.firstReceive = firstReceive;
		setDirty(true);
	}

	public boolean isSecondReceive() {
		return secondReceive;
	}

	public void setSecondReceive(boolean secondReceive) {
		this.secondReceive = secondReceive;
		setDirty(true);
	}

	public boolean isThirdReceive() {
		return thirdReceive;
	}

	public void setThirdReceive(boolean thirdReceive) {
		this.thirdReceive = thirdReceive;
		setDirty(true);
	}

	public boolean isFourthReceive() {
		return fourthReceive;
	}

	public void setFourthReceive(boolean fourthReceive) {
		this.fourthReceive = fourthReceive;
		setDirty(true);
	}
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
}
