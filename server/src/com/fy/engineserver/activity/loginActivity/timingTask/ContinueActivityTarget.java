package com.fy.engineserver.activity.loginActivity.timingTask;

import java.util.Calendar;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 连登活动定时任务
 * 
 *
 */
public class ContinueActivityTarget extends TaskTarget{

	public ContinueActivityTarget(String times[]){
		this.times = times;
	}
	
	@Override
	public void doTarget() {
		// TODO 
		Player ps [] = PlayerManager.getInstance().getOnlinePlayers();
		try{
			for(Player p : ps){
				ActivityManagers.getInstance().noticePlayerActivity(p);
			}
		}catch(Exception e){
			Game.logger.warn("[连登活动定时任务] [异常]",e);
		}
	}

	@Override
	public boolean isEffection() {
		// TODO Auto-generated method stub
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		if(!gc.getServerName().equals("国内本地测试")){
			return false;
		}
		Calendar cl = Calendar.getInstance();
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		int minue = cl.get(Calendar.MINUTE);
		for(String t:times){
			String [] ts = t.split(":");
			if(ts.length<2){
				return false;
			}
			if(ts[0].equals(hour+"") && ts[1].equals(minue+"")){
				ActivitySubSystem.logger.warn("[执行定时任务] [连登活动] [ContinueActivityTarget]");
				return true;
			}
		}
		return false;
	}

}
