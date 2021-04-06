package com.fy.engineserver.activity;

import static com.fy.engineserver.util.TimeTool.formatter.varChar19;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.droprule.DropRuleForSqage2015;
import com.fy.engineserver.activity.droprule.DropRuleForSqage2015_2;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.config.ServerFit;
import com.fy.engineserver.util.config.ServerFit4Activity;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.fy.engineserver.util.time.DailyTimeConf;
import com.fy.engineserver.util.time.TimeConf;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 刷新NPC/怪物管理
 * 
 */
public class RefreshSpriteManager implements Runnable {

	private static RefreshSpriteManager instance;
	
	private String fileName;
	
	public static RefreshSpriteManager getInstance() {
		return instance;
	}

	public static boolean running = true;
	public static boolean opening = true;
	public static long sleep = 1000;
	
	private Map<String, RefreshSpriteData> configedDatas = new LinkedHashMap<String, RefreshSpriteData>();

	public Map<String, RefreshSpriteData> getConfigedDatas() {
		return configedDatas;
	}

	public void setConfigedDatas(Map<String, RefreshSpriteData> configedDatas) {
		this.configedDatas = configedDatas;
	}

	GameConstants gc = GameConstants.getInstance();

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				ActivitySubSystem.logger.error("[RefreshSpriteManager] [心跳异常]", e);
			}
			if (opening) {
				long startTime = SystemTime.currentTimeMillis();
				for (Iterator<String> itor = configedDatas.keySet().iterator(); itor.hasNext();) {
					String name = itor.next();
					RefreshSpriteData data = configedDatas.get(name);
					data.doRefresh();
				}
				long cost = System.currentTimeMillis() - startTime;
				if (cost > 100) {
					ActivitySubSystem.logger.warn("[刷新精灵] [耗时:" + (cost) + "ms]");
				}
			}
		}
		ActivitySubSystem.logger.error("[RefreshSpriteManager] [心跳结束]", new Exception());
	}

	// 所有的配置
	private void initAllRefreshSpriteData() {
		{
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("kunlunshengdian", new Point(1333, 721), -1));
				mapPointList.add(new MapPoint("miemoshenyu", new Point(1414, 584), 0));
				mapPointList.add(new MapPoint("kunhuagucheng", new Point(2398, 882), -1));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 3);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 60, new int[] { 8, 12, 19 }, new int[] { 0, 0, 0 });
			RefreshSpriteData data = new RefreshSpriteData(("2013-02-09 00:00:00"), ("2013-02-12 00:00:00"), refreshMapDatas, conf, 0, 600096, "年年有鱼");
			configedDatas.put(data.getName(), data);
		}
		{
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("kunlunshengdian", new Point(3084, 1759), -1));
				mapPointList.add(new MapPoint("miemoshenyu", new Point(2374, 1073), 0));
				mapPointList.add(new MapPoint("kunhuagucheng", new Point(2136, 1370), -1));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 3);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 60, new int[] { 8, 12, 19 }, new int[] { 0, 0, 0 });
			RefreshSpriteData data = new RefreshSpriteData(("2013-02-09 00:00:00"), ("2013-02-12 00:00:00"), refreshMapDatas, conf, 0, 600097, "喜气洋洋");
			configedDatas.put(data.getName(), data);
		}
		{
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("kunlunshengdian", new Point(3909, 2523), -1));
				mapPointList.add(new MapPoint("miemoshenyu", new Point(3754, 1782), 0));
				mapPointList.add(new MapPoint("kunhuagucheng", new Point(3407, 1155), -1));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 3);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 60, new int[] { 8, 12, 19 }, new int[] { 0, 0, 0 });
			RefreshSpriteData data = new RefreshSpriteData(("2013-02-09 00:00:00"), ("2013-02-12 00:00:00"), refreshMapDatas, conf, 0, 600098, "吉祥如意");
			configedDatas.put(data.getName(), data);
		}
		{
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("kunlunshengdian", new Point(503, 2609), -1));
				mapPointList.add(new MapPoint("miemoshenyu", new Point(2439, 1757), 0));
				mapPointList.add(new MapPoint("kunhuagucheng", new Point(2779, 385), -1));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 3);
			refreshMapDatas.add(refreshMapData);

			TimeConf conf = new DailyTimeConf(1000 * 60 * 60, new int[] { 8, 12, 19 }, new int[] { 0, 0, 0 });
			RefreshSpriteData data = new RefreshSpriteData(("2013-02-09 00:00:00"), ("2013-02-12 00:00:00"), refreshMapDatas, conf, 0, 600099, "珠联璧合");
			configedDatas.put(data.getName(), data);
		}
		int[] taiwanHour = new int[] { 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 00 };
		int[] taiwanMinu = new int[] { 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 };
		// int[] taiwanHour = new int[] { 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 };
		// int[] taiwanMinu = new int[] { 30, 35, 40, 45, 50, 55, 00, 05, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 };
		int[] ceshiHour = new int[] { 11, 11, 11, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13 };
		int[] ceshiMinu = new int[] { 30, 40, 50, 00, 10, 20, 30, 40, 50, 00, 10, 20, 30, 40, 50 };
		{
			// 测试服活动-天魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2107, 606), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 5, ceshiHour, ceshiMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131177, "官方-天魂", "万圣复活，大量魔鬼涌入人间，仙界至尊召集各国人士于“边境”地图进行封魂，协助者可获得仙尊赠送的鬼蜮丰厚奖励。", new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-天魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2107, 606), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130926, "韩国-天魂", "귀역지문이 열리면서 대량의 혼백들이 인간세상에 들어왔습니다. 종규를 도와서 각 나라 변강 맵에서 보스를 사냥하고 봉혼을 진행하면 [종규]NPC 로부터 풍부한 보상을 받을 수 있습니다.", new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-天魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2290, 557), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130927, "韩国-地魂", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-命魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2407, 664), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130928, "韩国-命魂", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-天冲
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2211, 739), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130929, "韩国-天冲", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-灵慧
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2528, 645), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130930, "韩国-灵慧", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-为气
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2416, 809), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130931, "韩国-为气", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-为力
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2357, 421), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130932, "韩国-为力", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-中枢
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2089, 624), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130933, "韩国-中枢 ", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-为精
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2071, 784), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130934, "韩国-为精", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 台湾活动-为英
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2335, 548), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-09-09 00:00:00"), ("2013-09-17 23:59:59"), refreshMapDatas, conf, 1, 20130935, "韩国-为英", null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国) || TestServerConfigManager.isTestServer()) {
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-天魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2107, 606), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131177, "官方-天魂", "万圣复活，大量魔鬼涌入人间，仙界至尊召集各国人士于“边境”地图进行封魂，协助者可获得仙尊赠送的鬼蜮丰厚奖励。", new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}

		{
			// 国服活动-地魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2290, 557), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131178, "官方-地魂", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-命魂
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2407, 664), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131179, "官方-命魂", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-天冲
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2211, 739), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131180, "官方-天冲", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-灵慧
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2528, 645), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131181, "官方-灵慧", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-为气
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2416, 809), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131182, "官方-为气", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-为力
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2357, 421), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131183, "官方-为力", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-中枢
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2089, 624), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131184, "官方-中枢", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-为精
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2071, 784), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131185, "官方-为精", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}
		{
			// 国服活动-为英
			List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
			List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
			{
				mapPointList.add(new MapPoint("bianjing", new Point(2335, 548), -2));
			}
			RefreshMapData refreshMapData = new RefreshMapData(mapPointList, 1);
			refreshMapDatas.add(refreshMapData);
			TimeConf conf = new DailyTimeConf(1000 * 60 * 40, taiwanHour, taiwanMinu);
			RefreshSpriteData data = new RefreshSpriteData(("2013-10-30 00:00:00"), ("2013-11-07 23:59:59"), refreshMapDatas, conf, 1, 20131186, "官方-为英", null, new ServerFit() {
				@Override
				public boolean thisServerOpen() {
					if (PlatformManager.getInstance().isPlatformOf(Platform.官方) || TestServerConfigManager.isTestServer()) {
						if (isUnOpenServers(gc.getServerName())) {
							return false;
						}
						return true;
					}
					return false;
				}
			});
			data.setDropRule(new DropRuleForTW99());
			configedDatas.put(data.getName(), data);
		}

	}

	public void init() throws Exception {
		
		instance = this;
//		initAllRefreshSpriteData();
		initRefreashDataFile();
		Thread thread = new Thread(instance, "刷新精灵管理");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	
	private RefreshSpriteData getRefreshSpriteData(HSSFRow row) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		String refreashHour = "";		//需要判断与分钟是否对应。不对应不让起服
		String refreashMin = "";
		
		int spriteType = 0;
		int spriteId = 0;
		String spriteName = "";
		
		String refreashMaps = "";
		String refreashPoints = "";
		
		int maxNum = 1;
		
		int dorpType = 0;			//0代表没有掉落规则
		int country = -2;
		String notice = null;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if(cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if(cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		refreashHour = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		refreashMin = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		spriteType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		spriteId = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		spriteName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		refreashMaps = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		refreashPoints = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		maxNum = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		if(cell != null) {
			dorpType = (int) cell.getNumericCellValue();
		}
		
		cell = row.getCell(rowNum++);
		country = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		if(cell != null) {
			notice = cell.getStringCellValue().trim();
		}
		
		List<RefreshMapData> refreshMapDatas = new ArrayList<RefreshSpriteManager.RefreshMapData>();
		List<MapPoint> mapPointList = new ArrayList<RefreshSpriteManager.MapPoint>();
		ServerFit serverFit = new ServerFit4Activity(platForm, openServerName, notOpenServerName);
		
		int[] timeHour = parse2Int(refreashHour.split(","));
		int[] timeMin = parse2Int(refreashMin.split(","));
		if(timeHour.length != timeMin.length) {
			throw new Exception("刷新时间对应错误！");
		}
		mapPointList = this.parse2MapPoint(refreashMaps, refreashPoints, country);
		RefreshMapData refreshMapData = new RefreshMapData(mapPointList, maxNum);
		refreshMapDatas.add(refreshMapData);
		
		TimeConf conf = new DailyTimeConf(1000 * 60 * 20, timeHour, timeMin);
		
		RefreshSpriteData data = new RefreshSpriteData(startTime, endTime, refreshMapDatas, conf, spriteType,
				spriteId, spriteName, notice, serverFit);
		data.setDropRule(this.getDropRule(dorpType, platForm));
		Platform p = getPlatForm(platForm);
		ArrayList<Integer> tt = aa.get(p);
		if(tt == null) {
			tt = new ArrayList<Integer>();
		}
		tt.add(spriteId);
		aa.put(p, tt);
		
		return data;
	}
	
	private Platform getPlatForm(String pN) throws Exception {
		for(Platform p : Platform.values()) {
			if(p.getPlatformName().equals(pN)) {
				return p;
			}
		}
		throw new Exception("配置了错误的平台名{}" + pN);
	}
	
	private DropRule getDropRule(int dropType, String platForm) {
		if(dropType == 1) {
			if("sqage".equals(platForm) || "tencent".equals(platForm)) {
				return new DropRuleForSqage99();
			} else if("taiwan".equals(platForm)) {
				return new DropRuleForTW99();
			}
		} else if (dropType == 2) {	//2015年怪物掉落规则
			if("sqage".equals(platForm) || "tencent".equals(platForm)) {
				return new DropRuleForSqage2015();
			}
		} else if (dropType == 3) {	//2015年年兽怪物掉落规则
			if("sqage".equals(platForm) || "tencent".equals(platForm)) {
				return new DropRuleForSqage2015_2();
			}
		}
		return null;
	}
	
	private List<MapPoint> parse2MapPoint(String maps, String points, int country) throws Exception {
		String[] temp1 = maps.split(",");
		String[] tempP1 = points.split("\\|");
		if(temp1.length != tempP1.length) {
			throw new Exception("地图坐标与地图数量不匹配[maps=" + maps + ",points=" + points + "]");
		}
		List<MapPoint> tempR = new ArrayList<RefreshSpriteManager.MapPoint>();
		for(int i=0; i< temp1.length; i++) {
			String[] pp = tempP1[i].split(",");
			MapPoint mp = new MapPoint(temp1[i], new Point(Integer.parseInt(pp[0].trim()), Integer.parseInt(pp[1].trim())), country);
			tempR.add(mp);
		}
		return tempR;
	}
	
	
	public static int[] parse2Int(String[] strs) {
		int[] result = new int[strs.length];
		for(int i=0; i<result.length; i++) {
			result[i] = Integer.parseInt(strs[i].trim().replace(".0", ""));
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
//		RefreshSpriteManager r = new RefreshSpriteManager();
//		r.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//refreashSpriteActivities.xls");
//		r.initRefreashDataFile();
		String aa = getReplaceNotice("xxx在@@的%%刷新", "昆仑", "王城");
		System.out.println(aa);
	}
	
	public static String replaceStr1 = "@@";
	public static String replaceStr2 = "%%";
	
	public static String getReplaceNotice(String notice, String country, String mapName) {
		if(notice == null) {
			return null;
		}
		String result = notice.replaceAll(replaceStr1, country).replaceAll(replaceStr2, mapName);
		return result;
	}
	
	private void initRefreashDataFile() throws Exception {
		File f = new File(fileName);
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if(!f.exists()){
			throw new Exception("refreashSpriteActivities.xls配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);			
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				RefreshSpriteData temp = getRefreshSpriteData(row);
				if(configedDatas.containsKey(temp.getName())) {
					throw new Exception("有重复的活动名：" + temp.getName());
				}
				configedDatas.put(temp.getName(), temp);
			}catch(Exception e) {
				throw new Exception("refreashSpriteActivities第" + (i+1) + "行异常！！", e);
			}
		}
//		addSpriteId(aa);
		aa = null;
	}
	private Map<Platform, ArrayList<Integer>> aa = new HashMap<PlatformManager.Platform, ArrayList<Integer>>();

	public void addSpriteId(Map<Platform, ArrayList<Integer>> map) {
		ArrayList<Integer> aa = map.get(Platform.官方);
		if(aa != null && aa.size() > 0) {
			int[] ii = new int[aa.size()];
			for(int i=0; i<ii.length; i++) {
				ii[i] = aa.get(i);
			}
			DropRuleForSqage99.monsterIds = ii;
		}
		ArrayList<Integer> bb = map.get(Platform.台湾);
		if(bb != null && bb.size() > 0) {
			int[] ii = new int[bb.size()];
			for(int i=0; i<ii.length; i++) {
				ii[i] = bb.get(i);
			}
			DropRuleForTW99.monsterIds = ii;
		}
	}
	
	/**
	 * 刷新精灵数据,每个都是一个所谓的活动配置,一次多个�就配置多个活�
	 * 
	 * 
	 */
	public static class RefreshSpriteData {
		static String[] spriteTypeArr = { "NPC", "MONSTER" };
		static Random random = new Random();
		private long startTime;
		private long endTime;

		private long lastRefreshTime; // 最后一次刷新时�结束�0
		private List<MapPoint> lastRefreshPoint;// 最后一次刷新的数据.结束�null
		private List<Game> lastGames;

		private List<RefreshMapData> refreshMapDatas;// 每个数据都产生一个随机结�
		private TimeConf conf;
		private ServerFit serverFit;

		private int spriteType;// 精灵类型.0:npc,1:monster
		private int spriteId;// 精灵的Id

		private String name;

		private DropRule dropRule;// 掉落规则

		private String notice = null;
		
		public RefreshSpriteData(String startTimeStr, String endTimeStr, List<RefreshMapData> refreshMapDatas, TimeConf conf, int spriteType, int spriteId, String name) {
			this(startTimeStr, endTimeStr, refreshMapDatas, conf, spriteType, spriteId, name, null, new ServerFit() {

				@Override
				public boolean thisServerOpen() {
					return true;
				}
			});
		}

		public RefreshSpriteData(String startTimeStr, String endTimeStr, List<RefreshMapData> refreshMapDatas, TimeConf conf, int spriteType, int spriteId, String name, String notice, ServerFit serverFit) {
			this.startTime = varChar19.parse(startTimeStr);
			this.endTime = varChar19.parse(endTimeStr);
			this.refreshMapDatas = refreshMapDatas;
			this.conf = conf;
			this.spriteType = spriteType;
			this.spriteId = spriteId;
			this.name = name;
			this.notice = notice;
			this.serverFit = serverFit;
		}

		private RefreshMapData getOneMapData() {
			return refreshMapDatas.get(random.nextInt(refreshMapDatas.size()));
		}

		/**
		 * 刷新怪物
		 */
		public void doRefresh() {
			if (serverFit.thisServerOpen()) {
				Calendar calendar = Calendar.getInstance();
				if (calendar.getTimeInMillis() >= startTime && calendar.getTimeInMillis() <= endTime) {
					CompoundReturn inConfTimedistance = conf.inConfTimedistance(calendar, lastRefreshTime);
//					ActivitySubSystem.logger.error("[刷新精灵] [" + getName() + "] " + inConfTimedistance.getBooleanValue() + "," + inConfTimedistance.getStringValue());
					boolean needRefresh = false;
					if (inConfTimedistance.getBooleanValue()) {
						needRefresh = "refresh".equals(inConfTimedistance.getStringValue().trim());
					}
					// 需要刷新
					if (needRefresh) {
						realRefresh();
						ActivitySubSystem.logger.error("[刷新精灵] [" + getName() + "] [需要刷新] [" + inConfTimedistance.toString() + "]");
					} else {
						// 判断是否要移除
						if (lastRefreshPoint != null) {
							if ("remove".equals(inConfTimedistance.getStringValue())) {
								ActivitySubSystem.logger.error("[刷新精灵] [" + getName() + "] [需要移除] [" + inConfTimedistance.toString() + "]");
								removeLastRefresh();
							}
						}
					}
				}
			}
		}

		/**
		 * 移除上次刷新的结
		 */
		private synchronized void removeLastRefresh() {
			long startTime = SystemTime.currentTimeMillis();
			ActivitySubSystem.logger.warn(" [刷新精灵活动] [开始移除] [" + spriteTypeArr[spriteType] + "] [" + spriteId + "] [" + lastRefreshPoint.size() + "个] [this.lastGames:" + this.lastGames + "]");
			List<Game> games = this.lastGames;
			if (games == null) {
				return;
			}
			ActivitySubSystem.logger.warn(" [刷新精灵活动] [开始移除] [" + spriteTypeArr[spriteType] + "] [" + spriteId + "] [地图个数:" + games.size() + "个]");
			for (Game game : games) {
				LivingObject[] los = game.getLivingObjects();
				for (LivingObject lo : los) {
					if (lo != null) {
						if (lo instanceof NPC) {
							if (spriteType == 0) {
								if (((NPC) lo).getnPCCategoryId() == spriteId) {
									game.removeSprite((NPC) lo);
									noticeSpriteDisappearBySystem((Sprite) lo);
									ActivitySubSystem.logger.warn(" [刷新精灵活动] [真正移除" + spriteTypeArr[spriteType] + "] [" + ((NPC) lo).getName() + "/" + ((NPC) lo).getnPCCategoryId() + "/" + ((NPC) lo).getId() + "] [成功] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
								} else {
									ActivitySubSystem.logger.warn(" [刷新精灵活动] [真正移除" + spriteTypeArr[spriteType] + "] [" + ((NPC) lo).getName() + "/" + ((NPC) lo).getnPCCategoryId() + "] [不是目标] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
								}
							}
						} else if (lo instanceof Monster) {
							if (spriteType == 1) {
								ActivitySubSystem.logger.warn(" [刷新精灵活动] [测试移除] [" + ((Monster) lo).getSpriteCategoryId() + "/" + spriteId + "]");
								if (((Monster) lo).getSpriteCategoryId() == spriteId) {
									noticeSpriteDisappearBySystem((Sprite) lo);
									game.removeSprite((Monster) lo);
									ActivitySubSystem.logger.warn(" [刷新精灵活动] [真正移除" + spriteTypeArr[spriteType] + "] [" + ((Monster) lo).getName() + "] [成功] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
								}
							}
						}
					}
				}
			}
			lastRefreshTime = 0;
			lastRefreshPoint = null;
			this.lastGames = new ArrayList<Game>();
			ActivitySubSystem.logger.warn(" [刷新精灵活动] [移除完毕] [" + spriteTypeArr[spriteType] + "] [" + spriteId + "] [" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
		}

		public void realRefresh() {
			if (lastRefreshTime != 0 || lastRefreshPoint != null) {
				throw new IllegalStateException();
			}
			RefreshMapData oneMapData = getOneMapData();
			lastRefreshTime = SystemTime.currentTimeMillis();
			lastRefreshPoint = oneMapData.getOnceRandom();

			ActivitySubSystem.logger.warn("[刷新活动] [" + getName() + "] [lastRefreshPoint:" + lastRefreshPoint + "]");

			StringBuffer logSbf = new StringBuffer("[" + name + "]");
			logSbf.append(" [" + spriteTypeArr[spriteType] + "]");
			this.lastGames = new ArrayList<Game>();

			for (MapPoint mp : lastRefreshPoint) {
				List<Game> games = mp.getGames();
				ActivitySubSystem.logger.warn("[刷新活动] [" + getName() + "]"+ mp.getMapname() +" [MapPoint:" + mp.toString() + "] [返回地图:" + games + "]");
				if (games != null && games.size() > 0) {
					lastGames.addAll(games);
					for (Game game : games) {
						Sprite sprite = null;
						if (spriteType == 0) {//
							sprite = MemoryNPCManager.getNPCManager().createNPC(spriteId);
						} else if (spriteType == 1) {
							sprite = MemoryMonsterManager.getMonsterManager().createMonster(spriteId);
						}
						if (sprite != null) {
							sprite.setX(mp.getPoint().getX());
							sprite.setY(mp.getPoint().getY());

							sprite.setBornPoint(new com.fy.engineserver.core.g2d.Point(mp.getPoint().getX(), mp.getPoint().getY()));

							sprite.setRefreshSpriteData(this);

							game.addSprite(sprite);
							if (notice != null) {
								String country = CountryManager.得到国家名(game.country);
								String mapDisName = game.gi.displayName;
								String resultNotice = RefreshSpriteManager.getReplaceNotice(notice, country, mapDisName);
								ChatMessage msg = new ChatMessage();
								msg.setMessageText(resultNotice);
								try {
									ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
									logSbf.append("[系统消息:" + msg + "]");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							logSbf.append("[" + spriteTypeArr[spriteType] + "] [" + spriteId + "] [创建成功] [" + sprite.getName() + "(" + sprite.getX() + "," + sprite.getY() + "),id:" + sprite.getId() + "] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
						} else {
							logSbf.append("[" + spriteTypeArr[spriteType] + "] [" + spriteId + "] [创建失败]");
						}
					}
				} else {
					logSbf.append("[地图没取到]");
				}
			}
			ActivitySubSystem.logger.warn("[刷新活动] [" + this.getName() + "] [刷出结果:" + logSbf.toString() + "]");

		}
		

		/**
		 * 当精灵死亡时调用
		 * @param sprite
		 */
		public void noticeSpriteDead(Sprite sprite, Game game) {
			if (dropRule != null) {
				ActivitySubSystem.logger.warn("[怪物死亡回调] [" + sprite.getName() + "] [" + sprite.getClass().toString() + "] [" + dropRule.getClass().toString() + "] [是否是怪:" + (sprite instanceof Monster) + "]");
				if (sprite instanceof Monster) {
					dropRule.doDrop((Monster) sprite, game);
				}
			}
		}

		/**
		 * 当精灵被系统回收时调�
		 * @param sprite
		 */
		public void noticeSpriteDisappearBySystem(Sprite sprite) {
			// TODO DO NOTHING
			if(dropRule != null) {
				ActivitySubSystem.logger.warn("[怪物消失回调] [" + sprite.getName() + "] [" + sprite.getClass().toString() + "] [" + dropRule.getClass().toString() + "] [是否是怪:" + (sprite instanceof Monster) + "]");
			} else {
				ActivitySubSystem.logger.warn("[怪物消失回调] [" + sprite.getName() + "] [" + sprite.getClass().toString() + "] [" + dropRule + "] [是否是怪:" + (sprite instanceof Monster) + "]");
			}

		}

		public String getNotice() {
			return notice;
		}

		public void setNotice(String notice) {
			this.notice = notice;
		}

		public static String[] getSpriteTypeArr() {
			return spriteTypeArr;
		}

		public static void setSpriteTypeArr(String[] spriteTypeArr) {
			RefreshSpriteData.spriteTypeArr = spriteTypeArr;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		public long getLastRefreshTime() {
			return lastRefreshTime;
		}

		public void setLastRefreshTime(long lastRefreshTime) {
			this.lastRefreshTime = lastRefreshTime;
		}

		public List<MapPoint> getLastRefreshPoint() {
			return lastRefreshPoint;
		}

		public void setLastRefreshPoint(List<MapPoint> lastRefreshPoint) {
			this.lastRefreshPoint = lastRefreshPoint;
		}

		public List<RefreshMapData> getRefreshMapDatas() {
			return refreshMapDatas;
		}

		public void setRefreshMapDatas(List<RefreshMapData> refreshMapDatas) {
			this.refreshMapDatas = refreshMapDatas;
		}

		public TimeConf getConf() {
			return conf;
		}

		public void setConf(TimeConf conf) {
			this.conf = conf;
		}

		public int getSpriteType() {
			return spriteType;
		}

		public void setSpriteType(int spriteType) {
			this.spriteType = spriteType;
		}

		public int getSpriteId() {
			return spriteId;
		}

		public void setSpriteId(int spriteId) {
			this.spriteId = spriteId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ServerFit getServerFit() {
			return serverFit;
		}

		public void setServerFit(ServerFit serverFit) {
			this.serverFit = serverFit;
		}

		public DropRule getDropRule() {
			return dropRule;
		}

		public void setDropRule(DropRule dropRule) {
			this.dropRule = dropRule;
		}

	}

	/**
	 * 要刷新的地图位置配置
	 * 
	 * 
	 */
	public static class RefreshMapData {
		static Random random = new Random();
		private List<MapPoint> mapPoints; // 从各个数据中随机出X个结�
		private int maxnum;// 一次刷新的个数

		public RefreshMapData(List<MapPoint> mapPoints, int maxnum) {
			super();
			this.mapPoints = mapPoints;
			this.maxnum = maxnum;
		}

		public List<MapPoint> getMapPoints() {
			return mapPoints;
		}

		public void setMapPoints(List<MapPoint> mapPoints) {
			this.mapPoints = mapPoints;
		}

		public int getMaxnum() {
			return maxnum;
		}

		public void setMaxnum(int maxnum) {
			this.maxnum = maxnum;
		}

		public List<MapPoint> getOnceRandom() {
			if (maxnum >= mapPoints.size()) {
				return mapPoints;
			}
			List<MapPoint> result = new ArrayList<MapPoint>();
			List<MapPoint> temp = new ArrayList<MapPoint>(mapPoints);
			while (result.size() < maxnum) {
				result.add(temp.remove(random.nextInt(temp.size())));
			}
			return result;
		}
	}

	public static class MapPoint {

		private String mapname;// 要刷的出来的地图�拼音名字)
		private Point point;// 要刷出来的位�
		private int country;// -2[随机一个国家] -1(所有国家[1,2,3]) 0-中立 1昆仑,2九州,3万法

		public MapPoint(String mapname, Point point, int country) {
			this.mapname = mapname;
			this.point = point;
			this.country = country;
		}

		public List<Game> getGames() {
			List<Game> games = new ArrayList<Game>();
			int[] country = null;

			switch (getCountry()) {
			case -1:
				country = new int[] { 1, 2, 3 };
				break;
			case -2:
				country = new int[] { new Random().nextInt(3) + 1 };
				break;
			case 0:
				country = new int[] {0 };
				break;
			default:
				country = new int[] { getCountry() };
			}
			if (country != null) {
				for (int i = 0; i < country.length; i++) {
					Game game = GameManager.getInstance().getGameByName(mapname, country[i]);
					if (game != null) {
						games.add(game);
					}
				}
			}
			return games;
		}

		public int getCountry() {
			return country;
		}

		public void setCountry(int country) {
			this.country = country;
		}

		public String getMapname() {
			return mapname;
		}

		public void setMapname(String mapname) {
			this.mapname = mapname;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

	}

	// TODO 注意在此处配置需要屏蔽的服务器
	String unOpenServers[] = { "清风怡江", "风雨钟山", "龙骨平原" };

	public boolean isUnOpenServers(String serverName) {
		for (String unOpenserverName : unOpenServers) {
			if (serverName.equals(unOpenserverName)) {
				return true;
			}
		}
		return false;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}