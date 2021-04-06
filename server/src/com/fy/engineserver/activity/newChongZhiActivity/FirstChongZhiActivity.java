package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.HashSet;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.FIRST_CHARGE_STATE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 *	首充 充值活动
 */
public class FirstChongZhiActivity extends NewMoneyActivity {

	public static String DISK_KEY = "shouchong";
	public HashSet<Long> activityPlayers = new HashSet<Long>();
	
	private long needMoney;							//首充奖励需要的金钱数目
	private String[] rewardPropNames;				//奖励物品名字
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	
	public FirstChongZhiActivity(int configID, String name, int platform, String startTime,
			String endTime, String[] serverNames, String[] unServerNames,
			String mailTitle, String mailMsg, String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
		
	}

	public void doActivity(Player player, long yinzi) {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			//已经参加过首充
			if (activityPlayers.contains(player.getId())) {
				return;
			}
			//钱不够
			if (yinzi < getNeedMoney()) {
				return;
			}
			//给东西
			activityPlayers.add(player.getId());
			diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
			sendReward(player);
		}catch(Exception e) {
			logger.error("首充 doActivity", e);
		}
	}
	
	public void sendReward(Player player) {
		try{
			ArticleEntity[] entitys = new ArticleEntity[rewardPropNames.length];
			for (int i = 0 ; i < rewardPropNames.length; i++ ) {
				String propName = rewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getConfigID(), propName});
					return;
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, player, getRewardColors()[i], getRewardPropNums()[i], true);
			}
			player.setFirstCharge(true);
			FIRST_CHARGE_STATE_RES res = new FIRST_CHARGE_STATE_RES(GameMessageFactory.nextSequnceNum(), 1);
			player.addMessageToRightBag(res);
			MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			logger.warn("[首充得奖] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString()});
		}catch(Exception e) {
			logger.error("首发奖出错", e);
		}
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("] ");
		sb.append("[").append(getName()).append("] ");
		sb.append("[").append(getStartTime()).append("] ");
		sb.append("[").append(getEndTime()).append("] ");
		sb.append("[").append(getNeedMoney()).append("]");
		return sb.toString();
	}

	public void heatbeat() {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				cleanActivityData();
				return;
			}
		}catch(Exception e) {
			logger.error("首充 heatbeat", e);
		}
	}

	public void creatParameter(String[] parameters) {
		try{
			//金额
			setNeedMoney(Long.parseLong(parameters[0]));
			//奖励物品名字
			setRewardPropNames(parameters[1].split(","));
			//奖励物品数目
			String[] numString = parameters[2].split(",");
			rewardPropNums = new int[numString.length];
			for (int i = 0 ; i < numString.length; i++) {
				rewardPropNums[i] = Integer.parseInt(numString[i]);
			}
			//奖励物品颜色
			String[] colorString = parameters[3].split(",");
			rewardColors = new int[colorString.length];
			for (int i = 0 ; i < colorString.length; i++) {
				rewardColors[i] = Integer.parseInt(colorString[i]);
			}
			//奖励物品绑定
			String[] bingString = parameters[4].split(",");
			rewardBinds = new boolean[bingString.length];
			for (int i = 0 ; i < bingString.length; i++) {
				rewardBinds[i] = Boolean.parseBoolean(bingString[i]);
			}
		}catch(Exception e) {
			logger.error("首充 creatParameter", e);
		}
	}
	
	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		for (int i = 0; i < rewardPropNames.length; i++) {
			sb.append("[").append(rewardPropNames[i]).append("-").append(rewardPropNums[i]).append("-").append(rewardBinds[i]).append("]");
		}
		return sb.toString();
	}
	
	public void cleanActivityData() {
		if (activityPlayers.size() > 0) {
			logger.warn("[活动数据清除] {} [{}]", new Object[]{getLogString(), activityPlayers.size()});
			activityPlayers.clear();
			diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
		}
	}
	
	public String[] getActivityPartake() {
		String[] s = new String[activityPlayers.size()];
		int i = 0;
		for (Long pID : activityPlayers) {
			s[i] = pID.longValue() + "";
			i++;
		}
		return s;
	}

	public void loadDiskCache() {
		try{
			Object obj = diskcache.get(DISK_KEY + getConfigID());
			if (obj == null) {
				diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
			}else {
				activityPlayers = (HashSet<Long>)obj;
			}
		}catch(Exception e) {
			logger.error("首充 loadDiskCache", e);
		}
	}

	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}

	public long getNeedMoney() {
		return needMoney;
	}

	public void setRewardPropNames(String[] rewardPropNames) {
		this.rewardPropNames = rewardPropNames;
	}

	public String[] getRewardPropNames() {
		return rewardPropNames;
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

	public void setRewardPropNums(int[] rewardPropNums) {
		this.rewardPropNums = rewardPropNums;
	}

	public int[] getRewardPropNums() {
		return rewardPropNums;
	}

}
