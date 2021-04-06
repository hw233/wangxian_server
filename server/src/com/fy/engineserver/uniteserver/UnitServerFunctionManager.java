package com.fy.engineserver.uniteserver;

import static com.fy.engineserver.util.TimeTool.TimeDistance.DAY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 合区功能管理
 * 
 * 
 */
public class UnitServerFunctionManager {
	public static List<ServerCfg> cfgs = new ArrayList<ServerCfg>();

//	public static void createFunction() {
//		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
//			{ 
//				Set<String> set1 = new HashSet<String>();
//				 set1.add("碧海青天");
//				 set1.add("侠骨柔肠");
//				 set1.add("凌霜傲雪");
//				 set1.add("仙岛秘境");
//				 
//				 set1.add("崆峒灵宝");
//				 set1.add("万佛朝宗");
//				 set1.add("无量净土");
//				 set1.add("金宫银坊");
//				 
//				 set1.add("问鼎江湖");
//				 set1.add("笑傲江湖");
//				 set1.add("鱼跃龙门");
//				 set1.add("龙翔凤舞");
//				 
//				 set1.add("冲霄云楼");
//				 set1.add("王者之域");
//				 set1.add("独战群神");
//				 set1.add("傲剑凌云");
//				 ServerCfg sc1 = new ServerCfg(TimeTool.formatter.varChar19.parse("2013-11-15 15:00:00"), set1, Platform.官方, "2013-11-05 00:00:00", "2013-11-14 23:59:50", new
//				 boolean[]{true, true});
//				 cfgs.add(sc1);
//			}
//			 
//			 {
//				 Set<String> set2 = new HashSet<String>();
//				 set2.add("金霞玉鼎");
//				 set2.add("海天佛国");
//				 set2.add("百花深谷");
//				 set2.add("东极仙岛");
//				 
//				 set2.add("鹊桥仙境");
//				 set2.add("瑶台仙宫");
//				 set2.add("紫阳青峰");
//				 set2.add("普陀潮音");
//				 
//				 set2.add("雪域冰城");
//				 set2.add("白露横江");
//				 set2.add("左岸花海");
//				 set2.add("裂风峡谷");
//				 
//				 set2.add("蓬莱仙阁");
//				 set2.add("乾元金光");
//				 set2.add("北冥佛光");
//				 set2.add("七宝莲台");
//				 ServerCfg sc1 = new ServerCfg(TimeTool.formatter.varChar19.parse("2013-11-22 10:00:00"), set2, Platform.官方, "2013-11-13 00:00:00", "2013-11-21 23:59:50", new
//				 boolean[]{true, true});
//				 cfgs.add(sc1);
//			 }
//		}
//		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
//			Set<String> set2 = new HashSet<String>();
//			set2.add("雪域冰城");
//			set2.add("皇圖霸業");
//			ServerCfg sc1 = new ServerCfg(TimeTool.formatter.varChar19.parse("2013-09-20 20:00:00"), set2, Platform.台湾, "2013-09-17 00:00:00", "2013-09-20 23:59:50", new boolean[] { true, true });
//			cfgs.add(sc1);
//		}
//		// System.out.println("needCloseFunctuin----["+GameConstants.getInstance().getServerName()+"]~~~~~["+sc1.unitedTime+"] ["+(sc1.unitedTime -
//		// System.currentTimeMillis())+"]       ["+(DAY.getTimeMillis() * 8)+"]");
//	}

	public enum Function {
		拍卖(DAY.getTimeMillis() * 9),
		求购(DAY.getTimeMillis() * 9),
		邮件(DAY.getTimeMillis() * 9),
		武圣(DAY.getTimeMillis() * 9),
		城战(DAY.getTimeMillis() * 9),
		矿战(DAY.getTimeMillis() * 9),
		国战(DAY.getTimeMillis() * 9),
		仙府(DAY.getTimeMillis() * 9),
		五方(DAY.getTimeMillis() * 9),
		镖局(DAY.getTimeMillis() * 9),
		结婚(DAY.getTimeMillis() * 9),
		仙尊(DAY.getTimeMillis() * 9),
		仙灵大会(DAY.getTimeMillis() * 9),
		创建角色(DAY.getTimeMillis() * 14),
		武神之巅(DAY.getTimeMillis() * 9);

		private Function(long closeBeforeUnited) {
			this.closeBeforeUnited = closeBeforeUnited;
		}

		private long closeBeforeUnited;

		public long getCloseBeforeUnited() {
			return closeBeforeUnited;
		}

		public void setCloseBeforeUnited(long closeBeforeUnited) {
			this.closeBeforeUnited = closeBeforeUnited;
		}

	}

	public static boolean needCloseFunctuin(Function function) {
		CompoundReturn cr = needCloseForServer();
		// System.out.println("needCloseFunctuin---- ["+cr.getBooleanValue()+"] ["+function.getCloseBeforeUnited()+"] ["+function.name()+"]");
		if (cr.getBooleanValue()) {
			long now = System.currentTimeMillis();
			long unitedTime = cr.getLongValue();

			long distanct = unitedTime - now;
			// System.out.println("needCloseFunctuin---- ["+distanct+"] ["+function.getCloseBeforeUnited()+"] ["+function.name()+"]");
			return function.getCloseBeforeUnited() > distanct;
		}
		return false;
	}

	private static CompoundReturn needCloseForServer() {
		if (cfgs == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		for (ServerCfg cfg : cfgs) {
			if (cfg.isUnitServer()) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(cfg.needUnit()).setLongValue(cfg.unitedTime);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public static int ACTIVITY_ZHONGPAI_MONEY = 0;
	public static float ZONGPAI_ZHEKOU = 0.5f;
	public static int ACTIVITY_JIEYI_MONEY = 1;
	public static float JIEYI_ZHEKOU = 0.5f;

	public static boolean isActvity(int activity_index) {
		for (int i = 0; i < cfgs.size(); i++) {
			ServerCfg sc = cfgs.get(i);
			if (sc.isUnitServer()) {
				try {
					long now = System.currentTimeMillis();
					if (sc.activityStartTimeLong <= now && sc.activityEndTimeLong >= now) {
						if (activity_index < sc.activityOpens.length && activity_index >= 0) {
							return sc.activityOpens[activity_index];
						}
					}
				} catch (Exception e) {
					UnitedServerManager.logger.error("查询合服活动出错", e);
				}
				break;
			}
		}
		return false;
	}
}

class ServerCfg {
	long unitedTime;// 合服时间
	Set<String> serverNames;// 要合并的伺服器
	Platform platform;// 平台

	// 时间格式 2013-01-01 01:01:01
	String activityStartTime;
	long activityStartTimeLong;
	String activityEndTime;
	long activityEndTimeLong;

	// 此数组标记哪个功能开活动
	// 0 宗派费用打 5折
	// 1 结婚费用打 5折
	// 2 结义费用打 5折
	boolean[] activityOpens;

	public ServerCfg(long unitedTime, Set<String> serverNames, Platform platform, String activityStartTime, String activityEndTime, boolean[] activityOpens) {
		this.unitedTime = unitedTime;
		this.serverNames = serverNames;
		this.platform = platform;
		this.activityStartTime = activityStartTime;
		this.activityEndTime = activityEndTime;
		this.activityOpens = activityOpens;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			activityStartTimeLong = format.parse(activityStartTime).getTime();
			activityEndTimeLong = format.parse(activityEndTime).getTime();
		} catch (Exception e) {
			UnitedServerManager.logger.error("ServerCfg创建失败", e);
		}
	}

	public boolean isUnitServer() {
		Platform thisplatform = PlatformManager.getInstance().getPlatform();
		if (!thisplatform.equals(platform)) {
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null) {
			return false;
		}
		if (!serverNames.contains(gc.getServerName())) {
			return false;
		}
		return true;
	}

	public boolean needUnit() {
		if (isUnitServer()) {
			return System.currentTimeMillis() < unitedTime;
		}
		return false;
	}
}