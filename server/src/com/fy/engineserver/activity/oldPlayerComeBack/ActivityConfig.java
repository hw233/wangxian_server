package com.fy.engineserver.activity.oldPlayerComeBack;

import java.util.Arrays;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 活动配置
 * 
 *
 */
public abstract class ActivityConfig {

	public long startTime;
	
	public long endTime;
	
	public Platform[] platforms;
	
	public Set<String> notOpenServers;
	
	public Set<String> openServers;
	
	//活动开放的玩家等级范围
	public int levelLimit[];
	
	/**
	 * 活动是否在有效期
	 * @return
	 */
	public boolean isEffective(Player p){
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			ActivitySubSystem.logger.warn(p.getLogString()+"不在活动时间段内");
			return false;
		}
		try{
			if(levelLimit[0]>p.getLevel() || levelLimit[1]<p.getLevel()){
				ActivitySubSystem.logger.warn(p.getLogString()+"等级不满足活动要求");
				return false;
			}
		}catch(Exception e){
			ActivitySubSystem.logger.warn("[老玩家回归活动等级限制配置错误] ["+p.getLogString()+"] [levelLimit:"+Arrays.toString(levelLimit)+"] [异常："+e+"]");
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(platforms)) {
			ActivitySubSystem.logger.warn(p.getLogString()+"[当乐用户回归活动]该服不参与");
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null || notOpenServers.contains(gc.getServerName())) {
			ActivitySubSystem.logger.warn(p.getLogString()+"[当乐用户回归活动]该服不参与");
			return false;
		}
		if(openServers.contains(gc.getServerName())){
			ActivitySubSystem.logger.warn(p.getLogString()+"[当乐用户回归活动]该服参与");
			return true;
		}
		/**开放的服务器为空，该平台都符合**/
		if(openServers.size()==0){
			ActivitySubSystem.logger.warn(p.getLogString()+"[当乐用户回归活动]所有服都参与");
			return true;
		}
		return false;
	}
	
	/**活动奖励**/
	public abstract void doPrizes(Player player);
	
	/**
	 * 玩家活动记录key
	 */
	public abstract String activityKey(Player player);
}
