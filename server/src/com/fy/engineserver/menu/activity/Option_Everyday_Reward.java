package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.model.Passport;

/**
 * 新服活动，领取礼包
 * @author Administrator
 *
 */
public class Option_Everyday_Reward {
	

	/** 开始和结束时间 */
	private String startTimeStr="2013-07-30 00:00:00";
	private String endTimeStr="2013-08-02 23:59:59";
	
	private String articleName = "美酒锦囊";
	
	public void doReward(Player player) {
		if(isEffective(player)){
			if (ActivityManagers.getInstance().getDdc().get(player.getId() + "用于360补偿活动") == null) {
				Article a = ArticleManager.getInstance().getArticle(articleName);
				if (a != null) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 3, true);
						if (ae != null) {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae,ae,ae}, new int[]{1,1,1}, "亲爱的玩家您好", "由于近期出现360客户端更换账号登录不了《飘渺寻仙曲》的问题，《飘渺寻仙曲》官方和360同表抱歉！并送出美酒锦囊*3，其中包含着我们对您的歉意。", 0, 0, 0, "用于360补偿活动");
							ActivityManagers.getInstance().getDdc().put(player.getId() + "用于360补偿活动", 1);
							ActivitySubSystem.logger.error(player.getLogString() + "[用于360补偿活动] [发送邮件] [" + articleName + "]");
						}
					} catch (Throwable e) {
						e.printStackTrace();
						ActivitySubSystem.logger.error(player.getLogString() + "[用于360补偿活动] [创建物品] [异常] [美酒锦囊*3] [" + articleName + "]", e);
					}
					
				} else {
					ActivitySubSystem.logger.error(player.getLogString() + "[用于360补偿活动] [物品不存在] [美酒锦囊*3] [" + articleName + "]");
				}
			}
		}
	}
	
	public boolean isEffective(Player player){
		boolean time = false;
		boolean qudao = false;
		long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		
		long now = SystemTime.currentTimeMillis();
		if(startTime <= now && now <= endTime){
			time = true;
		}
		
		Passport p = player.getPassport();
		if (p.getRegisterChannel() !=  null && (p.getRegisterChannel().matches("360JIEKOU\\d*_MIESHI"))) {
			ActivitySubSystem.logger.warn(player.getLogString()+"是360用户");
			qudao = true;
		}
		
		return time&&qudao;
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
	
}


