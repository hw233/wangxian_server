package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 新服活动，领取礼包
 * @author Administrator
 *
 */
public class Option_ActivityMergeServerReward implements NeedCheckPurview{
	

	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;
	private String endTimeStr;
	
	private String articleName;
	private String activityName;//活动名称
	
	/**服务器限制*/
	private String showServers;
	private String limitServers;
	
	/**等级限制*/
	private int levelLimit;
	
	/* 解析后的数据 */
	private long startTime;
	private long endTime;
	private String[] showServernames;
	private String[] limitServernames;
	
	public void doReward(Player player) {
		if (ActivityManagers.getInstance().getDdc().get(player.getId() + activityName) == null) {
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if (a != null) {
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
					if (ae != null) {
						if (player.getKnapsack_common().isFull()) {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae },Translate.系统邮件提示, Translate.由于您的背包已满您得到的部分物品通过邮件发送, 0, 0, 0, activityName);
							ActivitySubSystem.logger.error(player.getLogString() + "["+ activityName + "] [发送邮件] [" + articleName + "]");
						} else {
							player.putToKnapsacks(ae, 1, activityName);
							ActivitySubSystem.logger.error(player.getLogString() + "["+ activityName + "] [放入背包] [" + articleName + "]");
						}
						player.sendError("恭喜您获得了"+articleName+"*1");
						ActivityManagers.getInstance().getDdc().put(player.getId() + activityName, 1);
					}
				} catch (Throwable e) {
					e.printStackTrace();
					ActivitySubSystem.logger.error(player.getLogString() +"["+ activityName + "] [创建物品] [异常] [" + articleName + "]", e);
				}
				
			} else {
				ActivitySubSystem.logger.error(player.getLogString() +"["+ activityName + "] [物品不存在] [" + articleName + "]");
			}
		} else {
			player.sendError("您已经领取过了.");// 提示已经领取过了
		}
	}
	
	// 该活动在游戏服是否开放
	public boolean isServerShow() {
		String servername = GameConstants.getInstance().getServerName();
		if (!("".equals(showServers)||showServers == null) && showServernames.length > 0) {
			for (int i = 0; i < showServernames.length; i++) {
				if (servername.trim().equals(showServernames[i].trim())) {
					return true;
				}
			}
			return false;
		} else if (!("".equals(limitServers)||limitServers == null) && limitServernames.length > 0) {
			for (int j = 0; j < limitServernames.length; j++) {
				if (servername.trim().equals(limitServernames[j].trim())) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean canSee(Player player) {
		startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			return true;
		}
		long now = SystemTime.currentTimeMillis();
		return startTime <= now && now <= endTime && isServerShow()&& player.getSoulLevel()>=levelLimit;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getShowServers() {
		return showServers;
	}

	public void setShowServers(String showServers) {
		this.showServers = showServers;
	}

	public String getLimitServers() {
		return limitServers;
	}

	public void setLimitServers(String limitServers) {
		this.limitServers = limitServers;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
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

	public String[] getShowServernames() {
		return showServernames;
	}

	public void setShowServernames(String[] showServernames) {
		this.showServernames = showServernames;
	}

	public String[] getLimitServernames() {
		return limitServernames;
	}

	public void setLimitServernames(String[] limitServernames) {
		this.limitServernames = limitServernames;
	}


	
}


