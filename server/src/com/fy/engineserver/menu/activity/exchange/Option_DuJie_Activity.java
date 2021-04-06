package com.fy.engineserver.menu.activity.exchange;

import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.CleConditionModel;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class Option_DuJie_Activity extends Option implements NeedCheckPurview{
	
	private String activityName;
	
	/**
	 * 每天领取是实时的
	 * eg：今天是渡劫3层，能领取3层的，如果4层，还能领取4层的
	 */
	@Override
	public void doSelect(Game game, Player player) {
		ExchangeActivityManager emm = ExchangeActivityManager.getInstance();
		List<DuJieActivity> dujieActivitys = emm.dujieActivitys;
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		
		if(dujieActivitys==null || dujieActivitys.size()==0){
			player.sendError(Translate.活动已过期+"!");
			return;
		}
		
		DuJieActivity currActivity = null;
		for(DuJieActivity a : dujieActivitys){
			if(a.activityName.equals(activityName)){
				currActivity = a;
			}
		}
		
		
		if(currActivity==null){
			player.sendError(Translate.活动已过期);
			return;
		}
		
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity==null){
			player.sendError(Translate.获取渡劫信息出错);
			return;
		}
		int	currChong = entity.getCurrentLevel();
		if(currChong<0){
			player.sendError(Translate.渡劫没开启);
			return;
		}
		
		int rightChong = currChong - 1;
		if(rightChong<0){
			rightChong = 0;
		}else if(rightChong>8){
			rightChong = 8;
		}
		if(currActivity!=null){
			if(currActivity.type==0){ 	//永久
				if(ddc.get(currActivity.activityName+player.getId())==null){
					if(currActivity.iseffect(player).getBooleanValue()==false){
						player.sendError(currActivity.iseffect(player).getStringValue());
					}else{
						if(currActivity.doPrice(player,rightChong)){
							player.sendError(Translate.领取成功);
							ddc.put(currActivity.activityName+player.getId(), System.currentTimeMillis());
						}
					}
				}else{
					player.sendError(Translate.您已经领取过此奖励);	
				}
			}else if(currActivity.type==1){	
				if(ddc.get(currActivity.activityName+player.getId())==null){
					ddc.put(currActivity.activityName+player.getId(), 11l);
				}
				Long lasttime = (Long)ddc.get(currActivity.activityName+player.getId());
				if(isSameDay(System.currentTimeMillis(), lasttime.longValue())){
					player.sendError(Translate.今天已经领取);
				}else{
					if(currActivity.iseffect(player).getBooleanValue()==false){
						player.sendError(currActivity.iseffect(player).getStringValue());
					}else{
						if(currActivity.doPrice(player,rightChong)){
							player.sendError(Translate.领取成功);
							ddc.put(currActivity.activityName+player.getId(), System.currentTimeMillis());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 重新计算当前重数
	 * @param p
	 * @param currChong
	 * @return
	 */
	public int getCurrChongNum(Player p , int currChong){
		int maxCeng [] = {1,2,2,5,2,7,8,2,2};
		//过关条件
		CleConditionModel ccm = TransitRobberyManager.getInstance().getCleConditionMap().get(new Integer(currChong));
		int chong = currChong;
		if(chong<0){
			chong = 0;
		}else if(chong > 8){
			chong = 8;
		}
		
		int 当前的层数 = maxCeng[chong];
		int rightChong = currChong;
		if(rightChong==1){
			rightChong = 0;
		}else{
			if(ccm.getLevelDetails().size()+1<当前的层数){
				rightChong -=1; 
			}
		}
		if(rightChong<0){
			rightChong = 0;
		}else if(rightChong > 8){
			rightChong = 8;
		}
		return rightChong;
	}
	
	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
//		 int hour1=ca.get(Calendar.HOUR_OF_DAY);
//		 int min1=ca.get(Calendar.MINUTE);

		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
//		 int hour2=ca.get(Calendar.HOUR_OF_DAY);
//		 int min2=ca.get(Calendar.MINUTE);
		
		return year1==year2&&month1==month2&&day1==day2;//&&hour1==hour2&&min1==min2;
	}


	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel()<110){
			return false;
		}
		return true;
	}

}
