package com.fy.engineserver.util.server;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

public class TestServerConfigManager {

	public static boolean isTestServer = false;

	public static boolean inited = false;

	/** 测试服务器的列表 */
	public static List<ServerConfigure> testServerList = new ArrayList<ServerConfigure>();

	static {
		testServerList.add(new ServerConfigure(Platform.官方, new String[] {"测试服" ,"客户端测试","开发服"}));
		testServerList.add(new ServerConfigure(Platform.腾讯, new String[] { "化外之境","更端测试", "化外之境2" }));
		testServerList.add(new ServerConfigure(Platform.台湾, new String[] { "仙尊降世","奇緣仙侶" }));
		testServerList.add(new ServerConfigure(Platform.韩国, new String[] { "ST","S15-제천대성","st3","st2" }));
	}

	public static boolean isTestServer() {
		if (inited) {
			return isTestServer;
		}
		GameConstants gameConstants = GameConstants.getInstance();

		Platform platform = PlatformManager.getInstance().getPlatform();

		if (gameConstants == null) return false;

		String serverName = gameConstants.getServerName();

		for (ServerConfigure configure : testServerList) {
			if (configure.contains(platform, serverName)) {
				isTestServer = true;
				break;
			}
		}
		inited = true;
		return isTestServer;
	}
	
	public static boolean isTestServer(String serverName) {
		GameConstants gameConstants = GameConstants.getInstance();

		Platform platform = PlatformManager.getInstance().getPlatform();

		if (gameConstants == null) return false;

		for (ServerConfigure configure : testServerList) {
			if (configure.contains(platform, serverName)) {
				return true;
			}
		}
		return false;
	}
	
}
