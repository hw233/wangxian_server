package com.fy.engineserver.util.server;

import java.util.Arrays;

import com.fy.engineserver.platform.PlatformManager.Platform;

/**
 * 对服务器的配置
 * 
 * 
 */
public class ServerConfigure {

	private Platform platform;

	private String[] serverNames;

	public ServerConfigure(Platform platform, String[] serverNames) {
		super();
		this.platform = platform;
		this.serverNames = serverNames;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String[] getServerNames() {
		return serverNames;
	}

	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}

	@Override
	public String toString() {
		return "ServerConfigure [platform=" + platform + ", serverNames=" + Arrays.toString(serverNames) + "]";
	}

	public boolean contains(Platform platformInput, String serverName) {
		if (!platform.equals(platformInput)) return false;
		for (String configSerName : serverNames) {
			if (configSerName.equals(serverName)) {
				return true;
			}
		}
		return false;
	}
}
