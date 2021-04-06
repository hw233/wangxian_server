package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.util.TimeTool;

/**
 * 腾讯蓝钻领取专属宠物
 * @author Administrator
 * 
 */
public class Option_OnlyForTecent_LanZuan extends Option implements NeedCheckPurview {

	/** 开始和结束时间 */
	private String startTimeStr = "";
	private String endTimeStr = "";
	private String uniqKey = "蓝钻专属宠物";

	private String articleName = "蓝钻小精灵蛋";

	public void doReward(Player player) {
		if (ActivityManagers.getInstance().getDdc().get(player.getId() + uniqKey) == null) {
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if (a != null) {
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
					if (ae != null) {
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, "亲爱的玩家您好", "恭喜您获得蓝钻小精灵。", 0, 0, 0, "蓝钻专属宠物");
						ActivityManagers.getInstance().getDdc().put(player.getId() + "蓝钻专属宠物", 1);
						ActivitySubSystem.logger.error(player.getLogString() + "[蓝钻专属宠物] [发送邮件] [" + articleName + "]");
					}
				} catch (Throwable e) {
					e.printStackTrace();
					ActivitySubSystem.logger.error(player.getLogString() + "[蓝钻专属宠物] [创建物品] [异常] [" + articleName + "]", e);
				}

			} else {
				ActivitySubSystem.logger.error(player.getLogString() + "[蓝钻专属宠物] [物品不存在] [" + articleName + "]");
			}
		} else {
			player.sendError("您已经领取过了");
		}
	}

	@Override
	public boolean canSee(Player player) {
		long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		long now = SystemTime.currentTimeMillis();
		return (startTime <= now && now <= endTime) && TengXunDataManager.instance.isGameLevel(player.getId());
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

	public String getUniqKey() {
		return uniqKey;
	}

	public void setUniqKey(String uniqKey) {
		this.uniqKey = uniqKey;
	}

}
