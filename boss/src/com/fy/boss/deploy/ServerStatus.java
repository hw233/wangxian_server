package com.fy.boss.deploy;

public class ServerStatus {
	private long serverid;

	private boolean agenton;
	
	private boolean installed;
	
	private boolean running;

	public long getServerid() {
		return serverid;
	}

	public void setServerid(long serverid) {
		this.serverid = serverid;
	}

	public boolean isAgenton() {
		return agenton;
	}

	public void setAgenton(boolean agenton) {
		this.agenton = agenton;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}
