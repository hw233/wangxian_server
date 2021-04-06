package com.fy.engineserver.hotspot;

public class HotspotClientInfo {

	private int hotspotID;
	
	private String name;
	
	private String icon;
	
	private boolean isSee;
	
	private boolean isOver;
	
	private String hotspotMsg;

	public void setHotspotID(int hotspotID) {
		this.hotspotID = hotspotID;
	}

	public int getHotspotID() {
		return hotspotID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setIsSee(boolean isSee) {
		this.isSee = isSee;
	}

	public boolean isIsSee() {
		return isSee;
	}

	public void setIsOver(boolean isOver) {
		this.isOver = isOver;
	}

	public boolean isIsOver() {
		return isOver;
	}

	public void setHotspotMsg(String hotspotMsg) {
		this.hotspotMsg = hotspotMsg;
	}

	public String getHotspotMsg() {
		return hotspotMsg;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
}
