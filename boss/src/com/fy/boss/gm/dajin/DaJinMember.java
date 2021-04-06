package com.fy.boss.gm.dajin;

public class DaJinMember {
	
	private long id;
	
	private int downNums;
	
	private int maxLayers;
	
	private String serverName;
	
	private long silerCounts;

	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDownNums() {
		return downNums;
	}

	public void setDownNums(int downNums) {
		this.downNums = downNums;
	}

	public int getMaxLayers() {
		return maxLayers;
	}

	public void setMaxLayers(int maxLayers) {
		this.maxLayers = maxLayers;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public long getSilerCounts() {
		return silerCounts;
	}

	public void setSilerCounts(long silerCounts) {
		this.silerCounts = silerCounts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
