package com.fy.engineserver.tune;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.res.AbstractResource;

public class GameMapManager {
	
//	private Logger logger = Logger.getLogger(GameMapManager.class);
public	static Logger logger = LoggerFactory.getLogger(GameMapManager.class);
	
	private static GameMapManager instance;
	
	private String appRoot;
	
	private LinkedHashMap<String, AbstractResource> gamemapsLow = new LinkedHashMap<String, AbstractResource>();// 低内存
	private LinkedHashMap<String, AbstractResource> gamemapsLowMapNameKey = new LinkedHashMap<String, AbstractResource>();// 低内存

	public static GameMapManager getInstance() {
		return instance;
	}
	
	public void init() {
		loadMap();
		instance = this;
		System.out.println("[===========================地图管理器加载成功]==========================");
		System.out.println("[===========================地图管理器加载成功]==========================");
		System.out.println("[===========================地图管理器加载成功]==========================");
	}
	
	public String getAppRoot() {
		return appRoot;
	}

	public void setAppRoot(String appRoot) {
		this.appRoot = appRoot;
	}

	private void loadMap() {
		FileFilter gamemapFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xmd");
			}
		};
		File dir = new File(appRoot + File.separator + "lowMap");
		File fs[] = dir.listFiles(gamemapFilter);
		if (fs == null) return;
		for (File f : fs) {
			try {
				String relatePath = f.getAbsolutePath().substring(appRoot.length());
//				logger.debug("[初始化地图数据] [" + f.getPath() + "] [" + relatePath + "] ");
//					if (logger.isDebugEnabled()) logger.debug("[初始化地图数据] [{}] [{}] ", new Object[]{f.getPath(),relatePath});
				GameInfo a = new GameInfo();
				a.load(f, relatePath);
				gamemapsLow.put(relatePath, a);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[初始化地图数据时出错] [" + f.getPath() + "]", e);
				System.out.println("[初始化地图数据时出错] [" + f.getPath() + "]");
			}
		}
		ArrayList<GameInfo> gis = new ArrayList<GameInfo>();
		HashMap<String, String> namemap = new HashMap<String, String>();

		Iterator it = gamemapsLow.values().iterator();
		while (it.hasNext()) {
			GameInfo gi = (GameInfo) it.next();
			gamemapsLowMapNameKey.put(gi.getName(), gi);
			gis.add(gi);
			namemap.put(gi.displayName, gi.name);
		}
		System.out.println("[初始化地图数据完毕] [地图数量:"+gamemapsLowMapNameKey.size()+"]");
	}
	
	public GameInfo getGameMap(String mapName) {
		return (GameInfo)gamemapsLowMapNameKey.get(mapName);
	}
}
