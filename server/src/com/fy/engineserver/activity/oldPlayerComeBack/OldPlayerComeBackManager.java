package com.fy.engineserver.activity.oldPlayerComeBack;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerActivity;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 无兄弟不飘渺寻仙曲——寻找遗失的兄弟
 * 
 *
 */
public class OldPlayerComeBackManager{
	
	public static Logger logger = LoggerFactory.getLogger(OldPlayerComeBackManager.class);
	
	private static OldPlayerComeBackManager self;
	
	private DefaultDiskCache ddc;
	
	public String activitykey;
	
	private List<ActivityConfig> activityConfigs;
	
	//召回id
//	private long playerBackId = 70000;
	
	public static OldPlayerComeBackManager getInstance(){
		return self;
	}
	
	public void init(){
		
		long now = System.currentTimeMillis(); 
		self = this;
		activityConfigs = new LinkedList<ActivityConfig>();
		ddc = ActivityManagers.getInstance().getDdc();
//		initActivitys();
		logger.warn("[OldPlayerComeBackManager] [启动OK] ["+(activitykey==null?"--":activitykey)+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		ServiceStartRecord.startLog(this);
	}
	
	public void initActivitys(){
		List<UnitServerActivity> usActivities= UnitedServerManager.getInstance().getUnitServerActivityByName("老玩家召回");
		if(usActivities!=null){
			for(UnitServerActivity usa:usActivities){
				Set<String> set = new HashSet<String>();
				if(null!=usa.getServerfit().getOpenServerNames()){
					for(String name:usa.getServerfit().getOpenServerNames()){
						set.add(name);
					}
				}
				Platform[] platform=usa.getServerfit().getPlatForms();
				Set<String> notopen = new HashSet<String>();
				OldPlayerActivity activity = new OldPlayerActivity(usa.getStartTime(), usa.getEndTime(), platform, notopen, set, new int[]{70,500});
				activityConfigs.add(activity);
			}
		}
//		Set<String> set = new HashSet<String>();
//		set.add("域外之战");
//		set.add("仙道独尊");
//		set.add("天道再临");
//		set.add("神魂不灭");
//		set.add("国内本地测试");
//		Set<String> notopen = new HashSet<String>();
//		OldPlayerActivity activity1 = new OldPlayerActivity(TimeTool.formatter.varChar19.parse("2013-10-30 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-02 23:59:59"), new Platform[] { Platform.官方 }, notopen, set, new int[]{70,500});
//		activityConfigs.add(activity1);
		
//		Set<String> set2 = new HashSet<String>();
//		Set<String> notopen2 = new HashSet<String>();//不开放的服
//		set2.add("国内本地测试");
////		set2.add("新功能测试");
//		DangLeOldPlayerActivity activity2 = new DangLeOldPlayerActivity(TimeTool.formatter.varChar19.parse("2013-06-19 00:00:00"), TimeTool.formatter.varChar19.parse("2013-08-11 23:59:59"), new Platform[] { Platform.官方 }, notopen2, set2, new int[]{150,500});
//		MConsoleManager.register(activity2);
//		activityConfigs.add(activity2);
	}
	
	public void noticeOldPlayer(Player p){
		try{
			for(ActivityConfig config : activityConfigs){
				if(config.isEffective(p)){
					config.doPrizes(p); 
				}
			}
		}catch(Exception e){
			logger.warn("[老玩家回归活动] [异常] ["+p.getLogString()+"] ["+e+"]");
		}
	}
	
	/**
	 * 召回id
	 * @return
	 */
	public synchronized long getPlayerBackId(Player p){
		String servername = GameConstants.getInstance().getServerName();
		UnitedServerManager usm=UnitedServerManager.getInstance();
		long playerBackId=usm.getPlayerBackId();
		String playerComebackKey=usm.getPlayerComebackKey();
		Long oldId = (Long)ddc.get(servername+playerComebackKey);
		if(oldId==null){
			ddc.put(servername+playerComebackKey, playerBackId);
			ddc.put(playerComebackKey+playerBackId, p.getId());
			return playerBackId;
		}
		long newId = oldId.longValue()+1;
		ddc.put(servername+playerComebackKey, newId);
		ddc.put(playerComebackKey+newId, p.getId());
//		Long oldId = (Long)ddc.get(servername+"召回id21031114");
//		if(oldId==null){
//			ddc.put(servername+"召回id21031114", playerBackId);
//			ddc.put("召回id21031114"+playerBackId, p.getId());
//			return playerBackId;
//		}
//		long newId = oldId.longValue()+1;
//		ddc.put(servername+"召回id21031114", newId);
//		ddc.put("召回id21031114"+newId, p.getId());
		return newId;
	} 
	
    public static long getContinueLoginDays(long start,long end){
    	Calendar ca=Calendar.getInstance();
    	ca.setTimeInMillis(start);
    	ca.set(Calendar.HOUR_OF_DAY, 0);
    	ca.set(Calendar.MINUTE, 0);
    	ca.set(Calendar.SECOND, 0);
    	ca.set(Calendar.MILLISECOND, 0);
    	long st=ca.getTimeInMillis();
    	
    	ca.clear();
    	ca.setTimeInMillis(end);
    	ca.set(Calendar.HOUR_OF_DAY, 0);
    	ca.set(Calendar.MINUTE, 0);
    	ca.set(Calendar.SECOND, 0);
    	ca.set(Calendar.MILLISECOND, 0);
    	long et=ca.getTimeInMillis();
    	
    	long days=Math.abs((et-st)/(24*60*60*1000));
    	
//    	long days=Math.abs((et-st)/(2*60*1000));
//    	if(days==0){
//    		days = 1;
//	    	boolean sameday = TimeTool.instance.isSame(start, end,TimeTool.TimeDistance.DAY, 1);
//	    	if(sameday){
//	    		days = 0;
//	    	}
//    	}
    	return days;
    }
    
	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}

	public List<ActivityConfig> getActivityConfigs() {
		return activityConfigs;
	}

}
