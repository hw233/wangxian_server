package com.fy.engineserver.activity.refreshbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.base.GodDownActivityConfig;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;

/**
 * 诸天守望——天降密藏
 * 
 * 
 */
public class RefreshBoxManager implements Runnable, EventProc {

	public static Logger log = ActivitySubSystem.logger;

	private static RefreshBoxManager self;

	public List<ActivityConfig> activitys;

	public List<Player> onlineplayers;

	public boolean isstart = true;
	
	public int lastminue = -1;	//上次有效的操作分钟记录数
	
	public GodDownReward godDownActivity;

	public static RefreshBoxManager getInstance() {
		return self;
	}

	public void init() {
		
		long now = System.currentTimeMillis();
		self = this;
		activitys = new ArrayList<ActivityConfig>();
		onlineplayers = new ArrayList<Player>();
//		initConfig();
		godDownActivity = new GodDownReward();
		new Thread(this, "RefreshBoxManager").start();
		doReg();
		ServiceStartRecord.startLog(this);
	}

	public void initConfig() {
		Set<String> set2 = new HashSet<String>();
		Set<String> notopen2 = new HashSet<String>();
//		GodDownReward activity2 = new GodDownReward(TimeTool.formatter.varChar19.parse("2015-01-30 00:00:00"), TimeTool.formatter.varChar19.parse("2015-02-05 23:59:59"), new Platform[] { Platform.官方,Platform.腾讯}, notopen2, set2);
//		activitys.add(activity2);
//		MConsoleManager.register(activity2);
//		Set<String> set_tw = new HashSet<String>();
//		Set<String> notopen_tw = new HashSet<String>();
//		GodDownReward activity_tw = new GodDownReward(TimeTool.formatter.varChar19.parse("2014-11-07 00:00:00"), TimeTool.formatter.varChar19.parse("2014-11-13 23:59:59"), new Platform[] { Platform.腾讯}, notopen_tw, set_tw);
//		activitys.add(activity_tw);

		Set<String> set_七夕 = new HashSet<String>();
		Set<String> notopen_七夕 = new HashSet<String>();
		BillbordActivity activity_七夕 = new BillbordActivity(TimeTool.formatter.varChar19.parse("2013-06-19 00:00:00"), TimeTool.formatter.varChar19.parse("2013-08-13 23:59:59"), new Platform[] { Platform.官方, Platform.腾讯 }, notopen_七夕, set_七夕);
		// activitys.add(activity_七夕);
 
		Set<String> set_累计在线 = new HashSet<String>();
		Set<String> notopen_累计在线 = new HashSet<String>();
		TotleOnlineTime activity_累计在线 = new TotleOnlineTime(TimeTool.formatter.varChar19.parse("2013-06-19 00:00:00"), TimeTool.formatter.varChar19.parse("2013-08-20 23:59:59"), new Platform[] { Platform.韩国 }, notopen_累计在线, set_累计在线);
		// activitys.add(activity_累计在线);

		Set<String> set_排行榜 = new HashSet<String>();
		Set<String> notopen_排行榜 = new HashSet<String>();
		BillbordActivityOfSkillHoner activity_排行榜 = new BillbordActivityOfSkillHoner("国服大使技能排行送称号活动",TimeTool.formatter.varChar19.parse("2013-06-19 00:00:00"), TimeTool.formatter.varChar19.parse("2115-08-19 23:59:59"), new Platform[] { Platform.官方 }, notopen_排行榜, set_排行榜);
		activitys.add(activity_排行榜);
	}

	@Override
	public void run() {
		int currminute = -1;
		while (isstart) {
			try {
				Thread.sleep(25 * 1000);
				int minute = Calendar.getInstance().get(Calendar.MINUTE);
				if(currminute!=minute){
					currminute = minute;
//					for (ActivityConfig config : activitys) {
//						if (config.isEffective()) {
//							config.heartbeat();
//						}
//					}
					try{
						if(ActivityManager.getInstance().godDownActivity.size() > 0){
							for(GodDownActivityConfig activity : ActivityManager.getInstance().godDownActivity){
								if(activity != null && activity.isEffectActivity() != -1){
									godDownActivity.开始小时 = activity.isEffectActivity();
									godDownActivity.startActivity(activity.getLiZiName(),activity.getRewardNames(),activity.isHasFlowerDown());
								}
							}	
						}
					}catch (Exception e) {
						ActivitySubSystem.logger.warn("[飘箱子活动] [异常:{}]",e);
					}
				
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("[活动管理RefreshBoxManager] [异常] [" + e + "]");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void joinActivity(Player p) {
		try {
			if (onlineplayers != null && !onlineplayers.contains(p)) {
				onlineplayers.add(p);
			}
		} catch (Exception e) {
			log.warn("[玩家进入游戏] [加入活动范围] [异常] [" + p.getLogString() + "]", e);
		}
	}

	public void leaveActivity(Player p) {
		try {
			if (onlineplayers != null && onlineplayers.contains(p)) {
				onlineplayers.remove(p);
			}
		} catch (Exception e) {
			log.warn("[玩家离开游戏] [不在活动范围] [异常] [" + p.getLogString() + "]", e);
		}
	}

	@Override
	public void proc(Event evt) {
		joinActivity(((EventPlayerLogin) evt).player);
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.PLAYER_LOGIN, this);
	}

}
