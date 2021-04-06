package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

/**
 * 点NPC，领取物品
 * @author Administrator
 * 
 */
public class Option_Npc_Get extends Option{

	/** 开始和结束时间 */
	private String startTimeStr;
	private String endTimeStr;
	
	private String articleName;
	private String activityName;//活动名称

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if(isOpen()){
			if (ActivityManagers.getInstance().getDdc().get(player.getId() + activityName) == null) {
				Article a = ArticleManager.getInstance().getArticle(articleName);
				if (a != null) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							if (player.getKnapsack_common().isFull()) {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, Translate.系统邮件提示, Translate.由于您的背包已满您得到的部分物品通过邮件发送, 0, 0, 0, activityName);
								ActivitySubSystem.logger.error(player.getLogString() + "["+ activityName + "] [发送邮件] [" + articleName + "]");
							} else {
								player.putToKnapsacks(ae, 1, activityName);
								ActivitySubSystem.logger.error(player.getLogString() + "["+ activityName + "] [放入背包] [" + articleName + "]");
							}
							player.sendError(articleName +"*1획득한것을 축하합니다.");
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
				player.sendError(Translate.您已经领取过此奖励);// 提示已经领取过了
			}
		}
	}

	public boolean isOpen() {
		long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		long now = SystemTime.currentTimeMillis();
		if (startTime <= now && now <= endTime) {
			return true;
		}
		return false;
	}
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
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

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	

}
