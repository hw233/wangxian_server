package com.fy.engineserver.activity.refreshbox;

import java.util.Set;

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
	
	/**
	 * 活动是否在有效期
	 * @return
	 */
	public boolean isEffective(){
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(platforms)) {
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null || notOpenServers.contains(gc.getServerName())) {
			return false;
		}
		if(openServers.contains(gc.getServerName())){
			return true;
		}
		/**开放的服务器为空，该平台都符合**/
		if(openServers.size()==0){
			return true;
		}
		return false;
	}
	
	/**活动心跳**/
	public abstract void heartbeat();
	
}
