package com.fy.engineserver.playerAims.clientModel;

public class PlayerAims {
	private int id;
	private String aimName;
	private String description;
	private String icon;
	private int frameColor;
	private long aimNum;
	private long currentNum;
	private byte receiveStatus;
	private byte showProcess;
	private int vipReceiveLimit;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAimName() {
		return aimName;
	}
	public void setAimName(String aimName) {
		this.aimName = aimName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getFrameColor() {
		return frameColor;
	}
	public void setFrameColor(int frameColor) {
		this.frameColor = frameColor;
	}
	public long getAimNum() {
		return aimNum;
	}
	public void setAimNum(long aimNum) {
		this.aimNum = aimNum;
	}
	public long getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(long currentNum) {
		this.currentNum = currentNum;
	}
	public byte getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(byte receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public byte getShowProcess() {
		return showProcess;
	}
	public void setShowProcess(byte showProcess) {
		this.showProcess = showProcess;
	}
	public int getVipReceiveLimit() {
		return vipReceiveLimit;
	}
	public void setVipReceiveLimit(int vipReceiveLimit) {
		this.vipReceiveLimit = vipReceiveLimit;
	}
	
	
}
