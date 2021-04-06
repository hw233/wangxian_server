package com.fy.engineserver.activity.loginActivity;

import java.util.Set;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 额外的一些奖励
 * 
 *
 */
public class PrizeMore {

	public PrizeMore(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers,String[] articlenames,int [] articlenums,int [] articlecolor){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		this.articlenames = articlenames;
		this.articlecolor = articlecolor;
		this.articlenums = articlenums;
	}
	
	private long startTime;
	
	private long endTime;
	
	public Platform[] platforms;
	
	private Set<String> notOpenServers;
	
	private Set<String> openServers;
	
	private String[] articlenames;
	
	private int [] articlenums;
	
	private int [] articlecolor;
	
	public boolean isEffective() {
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(getPlatforms())) {
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

	public Platform[] getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Platform[] platforms) {
		this.platforms = platforms;
	}

	public Set<String> getNotOpenServers() {
		return notOpenServers;
	}

	public void setNotOpenServers(Set<String> notOpenServers) {
		this.notOpenServers = notOpenServers;
	}

	public Set<String> getOpenServers() {
		return openServers;
	}

	public void setOpenServers(Set<String> openServers) {
		this.openServers = openServers;
	}

	public String[] getArticlenames() {
		return articlenames;
	}

	public void setArticlenames(String[] articlenames) {
		this.articlenames = articlenames;
	}

	public int[] getArticlenums() {
		return articlenums;
	}

	public void setArticlenums(int[] articlenums) {
		this.articlenums = articlenums;
	}

	public int[] getArticlecolor() {
		return articlecolor;
	}

	public void setArticlecolor(int[] articlecolor) {
		this.articlecolor = articlecolor;
	}
	
}
