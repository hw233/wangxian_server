package com.fy.engineserver.activity.newChongZhiActivity;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

/**
 * 单笔消费，玩家单笔消费金额是设定金额的多少倍，给多少次奖励
 */
public class SingleXiaoFeiActivity extends NewXiaoFeiActivity {

	private long needMoney;							//首充奖励需要的金钱数目
	private String[] rewardPropNames;				//奖励物品名字
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	private boolean rewardMailIsOp;					//邮件是否优化
	
	public SingleXiaoFeiActivity(int configID, int[] xType, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg, String[] parameters)
			throws Exception {
		super(configID, xType, name, platform, startTime, endTime, serverNames, unServerNames,
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
			long rewardNum = yinzi/getNeedMoney();
			if (rewardMailIsOp && rewardPropNames.length == 1 && rewardNum > 1) {
				if (rewardNum > 0) {
					Article a = ArticleManager.getInstance().getArticle(rewardPropNames[0]);
					if (a == null) {
						logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getConfigID(), rewardPropNames[0]});
						return;
					}
					ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[0], ArticleEntityManager.活动, player, getRewardColors()[0], getRewardPropNums()[0], true);
					rewardNum = rewardNum * getRewardPropNums()[0];
					if (!a.isOverlap()) {
						while (rewardNum > 0) {
							if (rewardNum > 5) {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity,entity,entity,entity,entity}, new int[]{1,1,1,1,1}, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
								logger.warn("[单笔消费奖5个] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), 5});
							}else {
								ArticleEntity[] es = new ArticleEntity[(int)rewardNum];
								int[] ens = new int[(int)rewardNum];
								for (int kk = 0; kk < rewardNum; kk++) {
									es[kk] = entity;
									ens[kk] = 1;
								}
								MailManager.getInstance().sendMail(player.getId(), es, ens, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
								logger.warn("[单笔消费奖剩余] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), rewardNum});
							}
							rewardNum = rewardNum - 5;
						}
					}else {
						while (rewardNum > 0) {
							if (rewardNum > a.getOverLapNum()) {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{a.getOverLapNum()}, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
								logger.warn("[单笔消费奖N个] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), a.getOverLapNum()});
							}else {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{(int)rewardNum}, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
								logger.warn("[单笔消费奖剩余] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), rewardNum});
							}
							rewardNum = rewardNum - a.getOverLapNum();
						}
					}
				}
			}else {
				for (long i = 0 ; i < rewardNum; i++) {
					sendReward(player);
				}
			}
			logger.warn("[单笔消费] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString(), yinzi});
		}catch(Exception e) {
			logger.error("单笔消费 doActivity", e);
		}
	}

	public void heatbeat() {}

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
			
			//邮件是否优化
			if (parameters[5].length() > 0) {
				rewardMailIsOp = Boolean.parseBoolean(parameters[5]);
			}else {
				rewardMailIsOp = false;
			}
			
		}catch(Exception e) {
			logger.error("单笔消费 creatParameter", e);
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
			logger.warn("[单笔消费奖] {} [{}]", new Object[]{getLogString(), player.getLogString()});
		}catch(Exception e) {
			logger.error("单笔消费奖出错", e);
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

}
