package com.fy.engineserver.qiancengta;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

public class QianCengTaActivity_RefDao {

	private int refNandu;
	private long refMoney;
	
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
	
	public QianCengTaActivity_RefDao (long startTime, long endTime, int refNandu, long refMoney, 
			int platformType, String[] serverNames, String[] unServerNames, String mailTitle, String mailMsg,
			String[] rewardNames, int[] rewardNums, int[] rewardColors, boolean[] rewardBinds) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.refNandu = refNandu;
		this.refMoney = refMoney;
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
	
	public boolean isStart (int nandu) {
		if (getRefNandu() != nandu) {
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
	
	public void sendReward (Player p, long money) {
		try{
			//如果没有给过，且金额大于需要金额
			int[] nums = new int[getRewardNums().length];
			for (int i = 0; i < nums.length; i++) {
				nums[i] = getRewardNums()[i]*(int)(money/refMoney);
			}
			ArticleEntity[] entitys = new ArticleEntity[rewardNames.length];
			for (int i = 0 ; i < rewardNames.length; i++ ) {
				String propName = rewardNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					QianCengTa_Ta.logger.warn("[物品不存在] [{}] [{}]", new Object[]{p.getLogString(), propName});
					return;
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, p, getRewardColors()[i], nums[i], true);
			}
			MailManager.getInstance().sendMail(p.getId(), entitys, nums, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			QianCengTa_Ta.logger.warn("[千层塔通过活动奖励] {} [{}]", new Object[]{Arrays.toString(rewardNames), p.getLogString()});
		}catch(Exception e) {
			QianCengTa_Ta.logger.error("千层塔通过活动奖励出错", e);
		}
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}

	public String getMailMsg() {
		return mailMsg;
	}

	public void setRewardNames(String[] rewardNames) {
		this.rewardNames = rewardNames;
	}

	public String[] getRewardNames() {
		return rewardNames;
	}

	public void setRewardNums(int[] rewardNums) {
		this.rewardNums = rewardNums;
	}

	public int[] getRewardNums() {
		return rewardNums;
	}

	public void setRewardColors(int[] rewardColors) {
		this.rewardColors = rewardColors;
	}

	public int[] getRewardColors() {
		return rewardColors;
	}

	public void setRewardBinds(boolean[] rewardBinds) {
		this.rewardBinds = rewardBinds;
	}

	public boolean[] getRewardBinds() {
		return rewardBinds;
	}

	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}

	public int getPlatformType() {
		return platformType;
	}

	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}

	public String[] getServerNames() {
		return serverNames;
	}

	public void setUnServerNames(String[] unServerNames) {
		this.unServerNames = unServerNames;
	}

	public String[] getUnServerNames() {
		return unServerNames;
	}

	public void setRefNandu(int refNandu) {
		this.refNandu = refNandu;
	}

	public int getRefNandu() {
		return refNandu;
	}

	public void setRefMoney(long refMoney) {
		this.refMoney = refMoney;
	}

	public long getRefMoney() {
		return refMoney;
	}
	
}
