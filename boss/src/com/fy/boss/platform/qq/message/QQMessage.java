package com.fy.boss.platform.qq.message;

public class QQMessage {

	/**
	 * 命令类型，登录通知
	 */
	public static final int 登录通知=1;
	
	/**
	 * 命令类型，QB支付
	 */
	public static final int Q币支付通知=2;

	/**
	 * 命令类型，神州付支付
	 */
	public static final int 神州付支付通知=3;
	
	private int ver;
	
	private int seqNum;
	
	private int cmd;
		
	public QQMessage(int ver, int seqNum, int cmd) {
		this.ver = ver;
		this.seqNum = seqNum;
		this.cmd = cmd;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

}
