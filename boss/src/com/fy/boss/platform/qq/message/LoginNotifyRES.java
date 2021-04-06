package com.fy.boss.platform.qq.message;

import com.fy.boss.platform.qq.message.QQMessage;

public class LoginNotifyRES extends QQMessage {
	
	private int state;
	
	public LoginNotifyRES(int ver, int seqNum, int cmd, int state) {
		super(ver, seqNum, cmd);
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	
}
