package com.fy.engineserver.marriage.manager;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

public class MarriageActivity {
	
	private int sendType;

	private long startTime;
	private long endTime;
	
	private int platformType;			//0国服  1台服  2腾讯  3马来  4韩服
	private String[] serverNames;
	private String[] unServerNames;
	
	private String mailTitle;
	private String mailMsg;
	
	private String[] rewardNames;
	private int[] rewardNums;
	private int[] rewardColors;
	private boolean[] rewardBinds;
	
	public MarriageActivity (long startTime, long endTime, int sendType, 
			int platformType, String[] serverNames, String[] unServerNames, String mailTitle, String mailMsg,
			String[] rewardNames, int[] rewardNums, int[] rewardColors, boolean[] rewardBinds) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.sendType = sendType;
		this.platformType = platformType;
		this.serverNames = serverNames;
		this.unServerNames = unServerNames;
		this.mailTitle = mailTitle;
		this.mailMsg = mailMsg;
		this.rewardNames = rewardNames;
		this.rewardNums = rewardNums;
		this.rewardColors = rewardColors;
		this.rewardBinds = rewardBinds;
	}
	
	public boolean isStart (int sendType) {
		if (getSendType() != sendType) {
			return false;
		}
		if (getPlatformType() == 0) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return false;
			}
		}else if (getPlatformType() == 1) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
				return false;
			}
		}else if (getPlatformType() == 2) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
				return false;
			}
		}else if (getPlatformType() == 3) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.马来)) {
				return false;
			}
		}else if (getPlatformType() == 4) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				return false;
			}
		}
		String serverName = GameConstants.getInstance().getServerName();
		if (getUnServerNames() != null) {
			for (String s : getUnServerNames()) {
				if (s.equals(serverName)) {
					return false;
				}
			}
		}
		//如果是allserver 的  或者是这个活动的就参加
		boolean isServer = false;
		if (getServerNames() != null) {
			for (String s : getServerNames()) {
				if (s.equals("ALL_SERVER")) {
					isServer = true;
				}else if (s.equals(serverName)) {
					isServer = true;
				}
			}
		}
		if (isServer) {
			long now = System.currentTimeMillis();
			if (getStartTime() <= now && getEndTime() >= now) {
				return true;
			}
		}
		return false;
	}
	
	public void sendReward (Player p) {
		try{
			ArticleEntity[] entitys = new ArticleEntity[rewardNames.length];
			for (int i = 0 ; i < rewardNames.length; i++ ) {
				String propName = rewardNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					QianCengTa_Ta.logger.warn("[物品不存在] [{}] [{}]", new Object[]{p.getLogString(), propName});
					return;
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, p, getRewardColors()[i], getRewardNums()[i], true);
			}
			MailManager.getInstance().sendMail(p.getId(), entitys, getRewardNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			MarriageManager.logger.warn("[结婚送花活动奖励] {} [{}]", new Object[]{Arrays.toString(rewardNames), p.getLogString()});
		}catch(Exception e) {
			MarriageManager.logger.error("结婚送花活动奖励出错", e);
		}
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getStartTime() {
		return startTime;
	}
	
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getPlatformType() {
		return platformType;
	}
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	public String[] getServerNames() {
		return serverNames;
	}
	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}
	public String[] getUnServerNames() {
		return unServerNames;
	}
	public void setUnServerNames(String[] unServerNames) {
		this.unServerNames = unServerNames;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailMsg() {
		return mailMsg;
	}
	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}
	public String[] getRewardNames() {
		return rewardNames;
	}
	public void setRewardNames(String[] rewardNames) {
		this.rewardNames = rewardNames;
	}
	public int[] getRewardNums() {
		return rewardNums;
	}
	public void setRewardNums(int[] rewardNums) {
		this.rewardNums = rewardNums;
	}
	public int[] getRewardColors() {
		return rewardColors;
	}
	public void setRewardColors(int[] rewardColors) {
		this.rewardColors = rewardColors;
	}
	public boolean[] getRewardBinds() {
		return rewardBinds;
	}
	public void setRewardBinds(boolean[] rewardBinds) {
		this.rewardBinds = rewardBinds;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getSendType() {
		return sendType;
	}
		
}
