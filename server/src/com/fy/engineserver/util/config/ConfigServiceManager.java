package com.fy.engineserver.util.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;

public class ConfigServiceManager {
	public static Logger logger = Logger.getLogger(ConfigServiceManager.class);

	private static ConfigServiceManager self;

	private PlatformManager platformManager;

	public List<ServerConfig> configs = new ArrayList<ServerConfig>();

	public static ConfigServiceManager getInstance() {
		return self;
	}

	public void init() {
		
		long now = System.currentTimeMillis();
		self = this;
		configs = initNewConfig();
		System.out.println("[ConfigServiceManager] [初始化成功] [configs:" + configs.size() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]");
		ServiceStartRecord.startLog(this);
	}

	public PlatformManager getPlatformManager() {
		return platformManager;
	}

	public void setPlatformManager(PlatformManager platformManager) {
		this.platformManager = platformManager;
	}

	public List<ServerConfig> initNewConfig() {
		List<ServerConfig> list = new ArrayList<ServerConfig>();
		// 配置新资源，新资源名称必须contains老资源名字

		ServerConfig taiwan = new ServerConfig("2014-04-23 00:00:00", "2018-08-03 23:59:59", new String[] { "newService_shops.xls" }, new ServerFit() {
			@Override
			public boolean thisServerOpen() {
//				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					if ("九州仙境".equals(GameConstants.getInstance().getServerName())) {
						return true;
					}
//				}
				return false;
			}
		});
		list.add(taiwan);

		return list;
	}

	/**
	 * 某个平台下的某几个服务器下的某几个资源
	 * @param file
	 * @return
	 */
	public String getFilePath(File file) {
		logger.warn("[获得游戏配置路径] [platName:" + PlatformManager.getInstance().getPlatform().getPlatformName() + "] [servername:" + GameConstants.getInstance().getServerName() + "] [原始资源配置：" + file.getAbsolutePath() + "]");
		boolean isupdate = false;
		// 符合服务器的所有需要改动的资源
		String allconfigs[] = null;
		String newfilepath = "";
		if (configs != null) {
			for (ServerConfig config : configs) {
				if (config.isEffective() && config.thisServerOpen()) {
					allconfigs = config.getNeedupdateconfigs();
					if (allconfigs != null) {
						String filenames[] = file.getAbsolutePath().split("/");
						String filename = filenames[filenames.length - 1];
						logger.warn("原始文件名:" + filename + ">>>" + Arrays.toString(filenames));
						for (String con : allconfigs) {
							if (con.contains(filename)) {
								newfilepath = "";
								for (int i = 0; i < filenames.length - 1; i++) {
									newfilepath = newfilepath + filenames[i] + "/";
								}
								logger.warn("拼装文件名:" + newfilepath);
								isupdate = true;
								newfilepath = newfilepath + con;
								break;
							}
						}
						if (isupdate) {
							break;
						}
					}
				}
			}
			if (!isupdate) {
				return file.getAbsolutePath();
			}
		}
		logger.warn("[获得游戏配置路径] [资源是否改变:" + isupdate + "] [platName:" + PlatformManager.getInstance().getPlatform().getPlatformName() + "] [servername:" + GameConstants.getInstance().getServerName() + "] [原始资源配置：" + file.getAbsolutePath() + "] [新资源配置" + newfilepath + "]");
		return newfilepath;
	}

}
