package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

/**
 * 单笔余额充值，玩家单笔充值金额是设定金额的多少倍，给多少次奖励,余额也给其他奖励
 */
public class SingleBalanceChongZhiActivity extends NewMoneyActivity {

	private long needMoney;							//奖励需要的金钱数目
	private long balanceMoney;						//余额
	
	private String[] rewardPropNames;				//奖励物品名字
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	
	private String[] balanceRewardPropNames;		//余额奖励物品名字
	private int[] balanceRewardPropNums;			//余额奖励物品数目
	private int[] balanceRewardColors;				//余额奖励物品颜色
	private boolean[] balanceRewardBinds;			//余额奖励绑定情况
	
	public SingleBalanceChongZhiActivity(int configID, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg, String[] parameters)
			throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
	}

	public void loadDiskCache() {
		//不需要存储什么数据
	}

	public void doActivity(Player player, long yinzi) {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			for (long i = 0 ; i < yinzi/getNeedMoney(); i++) {
				sendReward(player);
			}
			long balanceM = yinzi % getNeedMoney();
			for (long i = 0; i < (balanceM)/getBalanceMoney(); i++) {
				sendBalanceReward(player);
			}
			
			logger.warn("[单笔余额冲] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), yinzi});
		}catch(Exception e) {
			logger.error("单笔余额 doActivity", e);
		}
	}

	public void heatbeat() {}

	public void creatParameter(String[] parameters) {
		try{
			//金额
			String[] moneys = parameters[0].split(",");
			setNeedMoney(Long.parseLong(moneys[0]));
			setBalanceMoney(Long.parseLong(moneys[1]));
			
			//奖励物品名字
			String[] propNames = parameters[1].split("@@@@");
			
			setRewardPropNames(propNames[0].split(","));
			setBalanceRewardPropNames(propNames[1].split(","));
			
			//奖励物品数目
			String[] numStrings = parameters[2].split("@@@@");
			
			String[] numString = numStrings[0].split(",");
			rewardPropNums = new int[numString.length];
			for (int i = 0 ; i < numString.length; i++) {
				rewardPropNums[i] = Integer.parseInt(numString[i]);
			}
			
			String[] balanceNumString = numStrings[1].split(",");
			balanceRewardPropNums = new int[balanceNumString.length];
			for (int i = 0 ; i < balanceNumString.length; i++) {
				balanceRewardPropNums[i] = Integer.parseInt(balanceNumString[i]);
			}
			
			//奖励物品颜色
			String[] colorStrings = parameters[3].split("@@@@");
			
			String[] colorString = colorStrings[0].split(",");
			rewardColors = new int[colorString.length];
			for (int i = 0 ; i < colorString.length; i++) {
				rewardColors[i] = Integer.parseInt(colorString[i]);
			}
			
			String[] bcolorString = colorStrings[1].split(",");
			balanceRewardColors = new int[bcolorString.length];
			for (int i = 0 ; i < bcolorString.length; i++) {
				balanceRewardColors[i] = Integer.parseInt(bcolorString[i]);
			}
			//奖励物品绑定
			String[] bindStrings = parameters[4].split("@@@@");
			
			String[] bingString = bindStrings[0].split(",");
			rewardBinds = new boolean[bingString.length];
			for (int i = 0 ; i < bingString.length; i++) {
				rewardBinds[i] = Boolean.parseBoolean(bingString[i]);
			}
			
			String[] bbingString = bindStrings[1].split(",");
			balanceRewardBinds = new boolean[bbingString.length];
			for (int i = 0 ; i < bbingString.length; i++) {
				balanceRewardBinds[i] = Boolean.parseBoolean(bbingString[i]);
			}
		}catch(Exception e) {
			logger.error("单笔余额 creatParameter", e);
		}
	}

	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		for (int i = 0; i < rewardPropNames.length; i++) {
			sb.append("[").append(rewardPropNames[i]).append("-").append(rewardPropNums[i]).append("-").append(rewardBinds[i]).append("]");
		}
		sb.append("@@@@");
		sb.append("[").append(getBalanceMoney()).append("]");
		for (int i = 0; i < balanceRewardPropNames.length; i++) {
			sb.append("[").append(balanceRewardPropNames[i]).append("-").append(balanceRewardPropNums[i]).append("-").append(balanceRewardBinds[i]).append("]");
		}
		return sb.toString();
	}

	public String[] getActivityPartake() {
		String[] s = new String[0];
		return s;
	}
	
	public void cleanActivityData() {}

	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("] ");
		sb.append("[").append(getName()).append("] ");
		sb.append("[").append(getStartTime()).append("] ");
		sb.append("[").append(getEndTime()).append("] ");
		sb.append("[").append(getNeedMoney()).append("]");
		sb.append("[").append(getBalanceMoney()).append("]");
		return sb.toString();
	}

	public void sendReward(Player player) {
		try{
			//如果没有给过，且金额大于需要金额
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
			MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			logger.warn("[单笔奖] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), Arrays.toString(rewardPropNames)});
		}catch(Exception e) {
			logger.error("单笔余额奖出错", e);
		}
	}
	
	public void sendBalanceReward(Player player) {
		try{
			//如果没有给过，且金额大于需要金额
			ArticleEntity[] entitys = new ArticleEntity[balanceRewardPropNames.length];
			for (int i = 0 ; i < balanceRewardPropNames.length; i++ ) {
				String propName = balanceRewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getConfigID(), propName});
					return;
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getBalanceRewardBinds()[i], ArticleEntityManager.活动, player, getBalanceRewardColors()[i], getBalanceRewardPropNums()[i], true);
			}
			MailManager.getInstance().sendMail(player.getId(), entitys, getBalanceRewardPropNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			logger.warn("[余额奖] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), Arrays.toString(balanceRewardPropNames)});
		}catch(Exception e) {
			logger.error("单笔余额奖出错", e);
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

	public void setRewardPropNums(int[] rewardPropNums) {
		this.rewardPropNums = rewardPropNums;
	}

	public int[] getRewardPropNums() {
		return rewardPropNums;
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

	public void setBalanceMoney(long balanceMoney) {
		this.balanceMoney = balanceMoney;
	}

	public long getBalanceMoney() {
		return balanceMoney;
	}

	public void setBalanceRewardPropNames(String[] balanceRewardPropNames) {
		this.balanceRewardPropNames = balanceRewardPropNames;
	}

	public String[] getBalanceRewardPropNames() {
		return balanceRewardPropNames;
	}

	public void setBalanceRewardPropNums(int[] balanceRewardPropNums) {
		this.balanceRewardPropNums = balanceRewardPropNums;
	}

	public int[] getBalanceRewardPropNums() {
		return balanceRewardPropNums;
	}

	public void setBalanceRewardColors(int[] balanceRewardColors) {
		this.balanceRewardColors = balanceRewardColors;
	}

	public int[] getBalanceRewardColors() {
		return balanceRewardColors;
	}

	public void setBalanceRewardBinds(boolean[] balanceRewardBinds) {
		this.balanceRewardBinds = balanceRewardBinds;
	}

	public boolean[] getBalanceRewardBinds() {
		return balanceRewardBinds;
	}

}
