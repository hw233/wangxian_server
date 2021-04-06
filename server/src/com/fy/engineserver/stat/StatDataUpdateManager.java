package com.fy.engineserver.stat;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class StatDataUpdateManager {
//	Logger log=Logger.getLogger("StatData");
public	static Logger log = LoggerFactory.getLogger("StatData");
	
	PlayerManager pm;
	
	static StatDataUpdateManager self;
	
	public StatDataUpdateManager(){
		this.pm=PlayerManager.getInstance();
		self=this;
	}
	
	public void init(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		System.out.println("[系统初始化] [统计管理器] [初始化完成] [com.fy.engineserver.stat.StatDataUpdateManager] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
	}
	
	public void update(Player player, int statId, long value, long updateTime) {
		try {
			synchronized (player) {
				StatData data = this.getById(player.getId(), statId);
				long oldValue = 0;
				long oldTime = 0;
				if (data != null) {
					switch (statId) {
					case StatData.STAT_BATTLE_FIELD_GLORY:
					case StatData.STAT_BATTLE_FIELD_KILLING_NUM:
						if (player.isInBattleField()) {
							oldValue = data.getValue();
							data.setValue(oldValue + value);
							data.setLastUpdateTime(updateTime);
							data.setDirty(true);
						}
						break;

					case StatData.STAT_BATTLE_FIELD_GLORY_WEEKLY:
					case StatData.STAT_BATTLE_FIELD_KILLING_NUM_WEEKLY:
						if (player.isInBattleField()) {
							oldTime = data.getLastUpdateTime();
							if (!StatDataUpdateManager.isSameWeek(oldTime,
									updateTime)) {
								data.setValue(0);
							}
							oldValue = data.getValue();
							data.setValue(oldValue + value);
							data.setLastUpdateTime(updateTime);
							data.setDirty(true);
						}
						break;

					case StatData.STAT_INCOME_FORM_AU:
					case StatData.STAT_ONLINE_TIME:
					case StatData.STAT_CURRENT_LEVEL_ONLINE_TIME:
					case StatData.STAT_DUEL_TIMES:
					case StatData.STAT_DUEL_LOSE_TIMES:
					case StatData.STAT_DUEL_WIN_TIMES:
					case StatData.STAT_HONOR_KILLING_NUM:
					case StatData.STAT_ARATHI_WIN_TIMES:
					case StatData.STAT_COMBAT_5V5_WIN_TIMES:
					case StatData.STAT_COMBAT_10V10_WIN_TIMES:
					case StatData.STAT_PRENTICE_NUM:
						oldValue = data.getValue();
						data.setValue(oldValue + value);
						data.setLastUpdateTime(updateTime);
						data.setDirty(true);
						break;

					case StatData.STAT_INCOME_FORM_AU_WEEKLY:
						oldTime = data.getLastUpdateTime();
						if (!StatDataUpdateManager.isSameWeek(oldTime,
								updateTime)) {
							data.setValue(0);
						}
						oldValue = data.getValue();
						data.setValue(oldValue + value);
						data.setLastUpdateTime(updateTime);
						data.setDirty(true);
						break;

					case StatData.STAT_KILLING_NUM:
						oldValue = data.getValue();
						if (!player.isInBattleField()) {
							data.setValue(oldValue + value);
							data.setLastUpdateTime(updateTime);
							data.setDirty(true);
						}
						break;

					case StatData.STAT_ONLINE_DAYS:
						oldTime = data.getLastUpdateTime();
						oldValue = data.getValue();
						StatData cdot=this.getById(player.getId(), StatData.STAT_CURRENT_DAY_ONLINE_TIME);
						if(cdot!=null){
							if (!StatDataUpdateManager.isSameDay(oldTime,
									updateTime)) {
								if (cdot.getValue() > 60 * 60 * 1000) {
									data.setValue(oldValue + value);
									data.setLastUpdateTime(updateTime);
									data.setDirty(true);
								}
							}
						}
						break;
						
					case StatData.STAT_QUIT_GANG_TIME:
					case StatData.STAT_PLAYER_CREATE_TIME:
						oldValue = data.getValue();
						data.setValue(value);
						data.setLastUpdateTime(updateTime);
						data.setDirty(true);
						break;

					}
					if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [成功] [内容："
//+ StatData.STAT_NAMES[data.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [玩家等级："+player.getLevel()+"] [oldValue：" + oldValue
//+ "] [newValue：" + data.getValue() + "]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [玩家等级：{}] [oldValue：{}] [newValue：{}]", new Object[]{StatData.STAT_NAMES[data.getStatId()],player.getName(),player.getId(),player.getLevel(),oldValue,data.getValue()});
					}
				} else {
					if(statId!=StatData.STAT_BATTLE_FIELD_KILLING_NUM&&statId!=StatData.STAT_BATTLE_FIELD_KILLING_NUM_WEEKLY){
						data = new StatData();
						data.setPlayerId(player.getId());
						data.setDirty(true);
						data.setLastUpdateTime(updateTime);
						data.setStatId(statId);
						data.setValue(value);
						player.addStatData(data);
						if (log.isInfoEnabled()) {
//log.info("[添加玩家统计数据] [成功] [名字：" + player.getName()
//+ "] [玩家id：" + player.getId() + "] [玩家等级："+player.getLevel()+"] [内容：" + StatData.STAT_NAMES[statId] + "]");
if(log.isInfoEnabled())
	log.info("[添加玩家统计数据] [成功] [名字：{}] [玩家id：{}] [玩家等级：{}] [内容：{}]", new Object[]{player.getName(),player.getId(),player.getLevel(),StatData.STAT_NAMES[statId]});
						}
					}else{
						if (player.isInBattleField()) {
							data = new StatData();
							data.setPlayerId(player.getId());
							data.setDirty(true);
							data.setLastUpdateTime(updateTime);
							data.setStatId(statId);
							data.setValue(value);
							player.addStatData(data);
							if (log.isInfoEnabled()) {
//log.info("[添加玩家统计数据] [成功] [名字：" + player.getName()
//+ "] [玩家id：" + player.getId() + "] [玩家等级："+player.getLevel()+"] [内容：" + StatData.STAT_NAMES[statId] + "]");
if(log.isInfoEnabled())
	log.info("[添加玩家统计数据] [成功] [名字：{}] [玩家id：{}] [玩家等级：{}] [内容：{}]", new Object[]{player.getName(),player.getId(),player.getLevel(),StatData.STAT_NAMES[statId]});
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//log.warn("[更新玩家统计数据] [失败] [发生错误] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [内容：" + StatData.STAT_NAMES[statId] + "] [错误："+e+"]",e);
if(log.isWarnEnabled())
	log.warn("[更新玩家统计数据] [失败] [发生错误] [玩家：" + player.getName() + "] [玩家id："+ player.getId() + "] [内容：" + StatData.STAT_NAMES[statId] + "] [错误："+e+"]",e);
		}
	}
	
	public void updateCurrentDayOnlineTime(Player player,long onlineTime,long updateTime){}
	
	private void addCurrentWeekOnlineDays(Player player,long updateTime){
		StatData cwod = this.getById(player.getId(),
				StatData.STAT_CURRENT_WEEK_ONLINE_DAYS);
//		StatData cwod = this.getById2(player,
//				StatData.STAT_CURRENT_WEEK_ONLINE_DAYS);
		if (cwod != null) {
			long oldValue=cwod.getValue();
			if(isSameWeek(cwod.getLastUpdateTime(), updateTime)){
				if (!isSameDay(cwod.getLastUpdateTime(), updateTime)||cwod.getValue()==0) {
					cwod.setValue(cwod.getValue() + 1);
					cwod.setLastUpdateTime(updateTime);
					cwod.setDirty(true);
					if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [成功] [内容："
//+ StatData.STAT_NAMES[cwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + cwod.getValue() + "]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}]", new Object[]{StatData.STAT_NAMES[cwod.getStatId()],player.getName(),player.getId(),oldValue,cwod.getValue()});
					}
				}
			}else{
				StatData lwod=this.getById(player.getId(), StatData.STAT_LAST_WEEK_ONLINE_DAYS);
//				StatData lwod=this.getById2(player, StatData.STAT_LAST_WEEK_ONLINE_DAYS);
				if(lwod!=null){
					long lOldvalue=lwod.getValue();
					lwod.setValue(cwod.getValue());
					lwod.setLastUpdateTime(updateTime);
					lwod.setDirty(true);
					if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [成功] [内容："
//+ StatData.STAT_NAMES[lwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + lOldvalue
//+ "] [newValue：" + lwod.getValue() + "]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}]", new Object[]{StatData.STAT_NAMES[lwod.getStatId()],player.getName(),player.getId(),lOldvalue,lwod.getValue()});
					}
				}else{
					lwod=new StatData();
					lwod.setStatId(StatData.STAT_LAST_WEEK_ONLINE_DAYS);
					lwod.setPlayerId(player.getId());
					lwod.setLastUpdateTime(updateTime);
					lwod.setValue(cwod.getValue());
					lwod.setDirty(true);
					player.addStatData(lwod);
					if (log.isInfoEnabled()) {
//log.info("[添加玩家统计数据] [成功] [名字：" + player.getName()
//+ "] [内容：" + StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS] + "]");
if(log.isInfoEnabled())
	log.info("[添加玩家统计数据] [成功] [名字：{}] [内容：{}]", new Object[]{player.getName(),StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS]});
					}
				}
				cwod.setValue(1);
				cwod.setLastUpdateTime(updateTime);
				cwod.setDirty(true);
				if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [成功] [内容："
//+ StatData.STAT_NAMES[cwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + cwod.getValue() + "]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}]", new Object[]{StatData.STAT_NAMES[cwod.getStatId()],player.getName(),player.getId(),oldValue,cwod.getValue()});
				}
			}
		} else {
			cwod = new StatData();
			cwod.setPlayerId(player.getId());
			cwod.setLastUpdateTime(updateTime);
			cwod.setValue(1);
			cwod
					.setStatId(StatData.STAT_CURRENT_WEEK_ONLINE_DAYS);
			cwod.setDirty(true);
			player.addStatData(cwod);
			if (log.isInfoEnabled()) {
//log.info("[添加玩家统计数据] [成功] [名字：" + player.getName()
//+ "] [内容：" + StatData.STAT_NAMES[StatData.STAT_CURRENT_WEEK_ONLINE_DAYS] + "]");
if(log.isInfoEnabled())
	log.info("[添加玩家统计数据] [成功] [名字：{}] [内容：{}]", new Object[]{player.getName(),StatData.STAT_NAMES[StatData.STAT_CURRENT_WEEK_ONLINE_DAYS]});
			}
		}
	}
	
	/**
	 * 玩家上线时调用
	 * @param player
	 * @param updateTime
	 */
	public void updateLastWeekOnlineDays(Player player,long updateTime){
		try{
			StatData cwod = this.getById(player.getId(),
					StatData.STAT_CURRENT_WEEK_ONLINE_DAYS);
			StatData lwod = this.getById(player.getId(),
					StatData.STAT_LAST_WEEK_ONLINE_DAYS);
			
//			StatData cwod = this.getById2(player,
//					StatData.STAT_CURRENT_WEEK_ONLINE_DAYS);
//			StatData lwod = this.getById2(player,
//					StatData.STAT_LAST_WEEK_ONLINE_DAYS);
			if (cwod != null) {
				int weeks=StatDataUpdateManager.computeWeek(new Date(cwod.getLastUpdateTime()), new Date(updateTime));
				
//				//-------------------------
//				//testing
//				weeks=1;
//				//-------------------------
				
				
				if(weeks==1){
					if(lwod!=null){
						long oldValue=lwod.getValue();
						lwod.setValue(cwod.getValue());
						lwod.setLastUpdateTime(updateTime);
						lwod.setDirty(true);
						if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [玩家上线] [成功] [内容："
//+ StatData.STAT_NAMES[lwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + lwod.getValue() + "]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [玩家上线] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}]", new Object[]{StatData.STAT_NAMES[lwod.getStatId()],player.getName(),player.getId(),oldValue,lwod.getValue()});
						}
					}else{
						lwod=new StatData();
						lwod.setStatId(StatData.STAT_LAST_WEEK_ONLINE_DAYS);
						lwod.setPlayerId(player.getId());
						lwod.setLastUpdateTime(updateTime);
						lwod.setValue(cwod.getValue());
						lwod.setDirty(true);
						player.addStatData(lwod);
						if (log.isInfoEnabled()) {
//log.info("[添加玩家统计数据] [玩家上线] [成功] [名字：" + player.getName()
//+ "] [内容：" + StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS] + "] [WEEK="+weeks+"]");
if(log.isInfoEnabled())
	log.info("[添加玩家统计数据] [玩家上线] [成功] [名字：{}] [内容：{}] [WEEK={}]", new Object[]{player.getName(),StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS],weeks});
						}
					}
					long oldValue=cwod.getValue();
					cwod.setValue(0);
					cwod.setLastUpdateTime(updateTime);
					cwod.setDirty(true);
					if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [玩家上线] [成功] [内容："
//+ StatData.STAT_NAMES[cwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + cwod.getValue() + "] [WEEK="+weeks+"]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [玩家上线] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}] [WEEK={}]", new Object[]{StatData.STAT_NAMES[cwod.getStatId()],player.getName(),player.getId(),oldValue,cwod.getValue(),weeks});
					}
				}else if(weeks>1){
					if(lwod!=null){
						long oldValue=lwod.getValue();
						lwod.setValue(0);
						lwod.setLastUpdateTime(updateTime);
						lwod.setDirty(true);
						if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [玩家上线] [成功] [内容："
//+ StatData.STAT_NAMES[lwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + lwod.getValue() + "] [WEEK="+weeks+"]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [玩家上线] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}] [WEEK={}]", new Object[]{StatData.STAT_NAMES[lwod.getStatId()],player.getName(),player.getId(),oldValue,lwod.getValue(),weeks});
						}
					}
					long oldValue=cwod.getValue();
					cwod.setValue(0);
					cwod.setLastUpdateTime(updateTime);
					cwod.setDirty(true);
					if (log.isInfoEnabled()) {
//log.info("[更新玩家统计数据] [玩家上线] [成功] [内容："
//+ StatData.STAT_NAMES[cwod.getStatId()]
//+ "] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [oldValue：" + oldValue
//+ "] [newValue：" + cwod.getValue() + "] [WEEK="+weeks+"]");
if(log.isInfoEnabled())
	log.info("[更新玩家统计数据] [玩家上线] [成功] [内容：{}] [玩家：{}] [玩家id：{}] [oldValue：{}] [newValue：{}] [WEEK={}]", new Object[]{StatData.STAT_NAMES[cwod.getStatId()],player.getName(),player.getId(),oldValue,cwod.getValue(),weeks});
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
//log.error("[更新玩家统计数据] [失败] [发生错误] [玩家：" + player.getName() + "] [玩家id："
//+ player.getId() + "] [内容：" + StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS] + "] [错误："+e+"]",e);
log.error("[更新玩家统计数据] [失败] [发生错误] [玩家：" + player.getName() + "] [玩家id："+ player.getId() + "] [内容：" + StatData.STAT_NAMES[StatData.STAT_LAST_WEEK_ONLINE_DAYS] + "] [错误："+e+"]",e);
		}
	}
	
	public StatData getByName(String playerName,int statId){
		StatData data=null;
		try{
			Player p=this.pm.getPlayer(playerName);
			if(p!=null){
				data=p.getStatData(statId);
				if(log.isInfoEnabled()){
//					log.info("[获取玩家统计数据] [成功] [名字："+playerName+"] [statId="+statId+"]");
					if(log.isInfoEnabled())
						log.info("[获取玩家统计数据] [成功] [名字：{}] [statId={}]", new Object[]{playerName,statId});
				}
			}else{
//				log.warn("[获取玩家统计数据] [失败] [玩家不存在] [名字："+playerName+"]");
				if(log.isWarnEnabled())
					log.warn("[获取玩家统计数据] [失败] [玩家不存在] [名字：{}]", new Object[]{playerName});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[获取玩家统计数据] [失败] [发生错误] [错误："+e+"]",e);
		}
		return data;
	}
	
	public StatData getById(long playerId,int statId){
		StatData data=null;
		try{
			Player p=this.pm.getPlayer(playerId);
			if(p!=null){
				data=p.getStatData(statId);
				if(log.isInfoEnabled()){
//					log.info("[获取玩家统计数据] [成功] [ID："+playerId+"] [statId="+statId+"]");
					if(log.isInfoEnabled())
						log.info("[获取玩家统计数据] [成功] [ID：{}] [statId={}]", new Object[]{playerId,statId});
				}
			}else{
//				log.warn("[获取玩家统计数据] [失败] [玩家不存在] [ID："+playerId+"]");
				if(log.isWarnEnabled())
					log.warn("[获取玩家统计数据] [失败] [玩家不存在] [ID：{}]", new Object[]{playerId});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[获取玩家统计数据] [失败] [发生错误] [错误："+e+"]",e);
		}
		return data;
	}
	
//	public StatData getById2(Player player,int statId){
//		StatData data=null;
//		try{
//			Player p=player;
//			if(p!=null){
//				data=p.getStatData(statId);
//				
//			}else{
//				
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			log.warn("[获取玩家统计数据] [失败] [发生错误] [错误："+e+"]",e);
//		}
//		return data;
//	}
	
	public static boolean isSameWeek(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.setTimeInMillis(time1);
		int week1=ca.get(Calendar.WEEK_OF_YEAR);
		
		ca.setTimeInMillis(time2);
		int week2=ca.get(Calendar.WEEK_OF_YEAR);
		
		return week1==week2; 
	}
	
	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
		
		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
		
		return year1==year2&&month1==month2&&day1==day2; 
	}
	
	public static StatDataUpdateManager getInstance(){
		return StatDataUpdateManager.self;
	}
	
	public void killingUpdate(Player player,long value,long updateTime){
		int[] ids={StatData.STAT_BATTLE_FIELD_KILLING_NUM,StatData.STAT_BATTLE_FIELD_KILLING_NUM_WEEKLY,StatData.STAT_KILLING_NUM};
		for(int i=0;i<ids.length;i++){
			this.update(player, ids[i], value, updateTime);
		}
	}
	
	public void incomeUpdate(Player player,long value,long updateTime){
		int[] ids={StatData.STAT_INCOME_FORM_AU,StatData.STAT_INCOME_FORM_AU_WEEKLY};
		for(int i=0;i<ids.length;i++){
			this.update(player, ids[i], value, updateTime);
		}
	}
	
	/**  
     * 计算两个日期间相隔的周数  
     *   
     * @param startDate  
     *            开始日期  
     * @param endDate  
     *            结束日期  
     * @return  
     */  
    public static int computeWeek(Date startDate, Date endDate) {   
  
        int weeks = 0;   
  
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        beginCalendar.setTime(startDate);   
  
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        endCalendar.setTime(endDate);
        while (beginCalendar.before(endCalendar)) {   
            if (beginCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar   
                            .get(Calendar.WEEK_OF_YEAR)) {   
                break;   
  
            } else {   
  
                beginCalendar.add(Calendar.DAY_OF_YEAR, 7);   
                weeks += 1;   
            }   
        }   
        return weeks;   
    }
    
    public static void main(String[] args) {
//		StatDataUpdateManager sdum=new StatDataUpdateManager();
//		Calendar ca=Calendar.getInstance();
//		ca.set(2010, 5, 6, 20, 0);
//		Player player=new Player();
//		player.setId(132);
//		StatData cdot=new StatData();
//		cdot.setPlayerId(player.getId());
//		cdot.setValue(100000);
//		cdot.setLastUpdateTime(ca.getTimeInMillis());
//		cdot.setDirty(true);
//		cdot.setStatId(StatData.STAT_CURRENT_DAY_ONLINE_TIME);
//		player.addStatData(cdot);
//		
//		ca.set(2010, 5, 6, 22, 0);
//		long loginTime=ca.getTimeInMillis();
//		ca.set(2010, 5, 7, 5, 0);
//		long logoutTime=ca.getTimeInMillis();
//		sdum.updateCurrentDayOnlineTime(player, loginTime, logoutTime, logoutTime);
//		
//		ca.set(2010, 5, 10, 5, 0);
//		loginTime=ca.getTimeInMillis();
//		ca.set(2010, 5, 10, 6, 0);
//		logoutTime=ca.getTimeInMillis();
//		sdum.updateCurrentDayOnlineTime(player, loginTime, logoutTime, logoutTime);
//		
//		ca.set(2010, 5, 10, 22, 50);
//		sdum.updateLastWeekOnlineDays(player, ca.getTimeInMillis());
//		loginTime=ca.getTimeInMillis();
//		ca.set(2010, 5, 11, 1, 0);
//		logoutTime=ca.getTimeInMillis();
//		sdum.updateCurrentDayOnlineTime(player, loginTime, logoutTime, logoutTime);
//		
//		ca.set(2010, 5, 11, 5, 0);
//		loginTime=ca.getTimeInMillis();
//		ca.set(2010, 5, 12, 6, 0);
//		logoutTime=ca.getTimeInMillis();
//		sdum.updateCurrentDayOnlineTime(player, loginTime, logoutTime, logoutTime);
//		
//		ca.set(2010, 5, 12, 23, 0);
//		loginTime=ca.getTimeInMillis();
//		ca.set(2010, 5, 13, 5, 0);
//		logoutTime=ca.getTimeInMillis();
//		sdum.updateCurrentDayOnlineTime(player, loginTime, logoutTime, logoutTime);
//		
//		
//		ca.set(2010, 5, 14, 5, 0);
//		sdum.updateLastWeekOnlineDays(player, ca.getTimeInMillis());
    	
    	Calendar ca=Calendar.getInstance();
    	ca.set(2010, 11, 27, 5, 0);
    	Date d1=ca.getTime();
    	ca.set(2011, 0, 1, 6, 0);
    	Date d2=ca.getTime();
//    	System.out.println("weeks="+StatDataUpdateManager.computeWeek(d1, d2));
//    	System.out.println("same week="+StatDataUpdateManager.isSameWeek(d1.getTime(), d2.getTime()));
    	
	}
}
