package com.fy.engineserver.menu.activity;

import java.util.Arrays;
import java.util.Calendar;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.MConsole;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
/**
 * 每日领取活动
 * 
 */
public class Option_Everyday_Get extends Option implements NeedCheckPurview, MConsole{
	/** 开始和结束时间 */
	private String startTimeStr;
	private String endTimeStr;
	
	private String articleName;
	
	/**参加活动的服务器，逗号间隔，为空的话表示所有服都参加*/
	private String showServerNames;
	private String limitServerNames;

	@Override
	public void doSelect(Game game, Player player) {
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		Long lastTime = (Long)ddc.get(player.getId()+"每日领取活动"+ articleName);
		if(lastTime==null){
			ddc.put(player.getId()+"每日领取活动"+ articleName,0l);
		}else{
			if (!isSameDay(lastTime,System.currentTimeMillis())) {
				Article a = ArticleManager.getInstance().getArticle(articleName);
				if (a != null) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							if (player.getKnapsack_common().isFull()) {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, Translate.系统, Translate.由于您的背包已满您得到的部分物品通过邮件发送, 0, 0, 0, "每日领取活动");
								ActivitySubSystem.logger.error(player.getLogString() + "[每日领取活动] [发送邮件] [" + articleName + "]");
							} else {
								player.putToKnapsacks(ae, 1, "每日领取活动");
								ActivitySubSystem.logger.error(player.getLogString() + "[每日领取活动] [放入背包] [" + articleName + "]");
							}
							int colorValue = ArticleManager.getColorValue(a, ae.getColorType());
							player.sendError(Translate.translateString(Translate.恭喜您获得了, new String[][] { { Translate.COUNT_1, "<f color='"+colorValue+"'>"+articleName }, { Translate.STRING_1, 1+"</f>" }}));
							ddc.put(player.getId() +"每日领取活动"+ articleName, System.currentTimeMillis());
						}
					} catch (Throwable e) {
						e.printStackTrace();
						ActivitySubSystem.logger.error(player.getLogString() + "[每日领取活动] [创建物品] [异常] [" + articleName + "]", e);
					}
				} else {
					ActivitySubSystem.logger.error(player.getLogString() + "[每日领取活动] [物品不存在] [" + articleName + "]");
				}
			}else{
				player.sendError(Translate.您已经领取了今日奖励);
			}
		}
	}

	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);

		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
		return year1==year2&&month1==month2&&day1==day2;
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public boolean canSee(Player player) {
		boolean rightTime = false;
		boolean rightServer = false;
		
		//是否在活动时间内
		long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		long now = System.currentTimeMillis();
		if(startTime<=now && now<=endTime){
			rightTime = true;
		}
		//服务器是否参加活动
		String servername = GameConstants.getInstance().getServerName();
		if(!"".equals(showServerNames)&& showServerNames !=null){
			String showServername[] = showServerNames.trim().split(",");
			if(showServername!=null&&showServername.length>0){
				for (int i = 0; i < showServername.length; i++) {
					if (servername.trim().equals(showServername[i].trim())) {
						rightServer = true;
					} 
				}
				ActivitySubSystem.logger.warn("rightTime&rightServer1:"+rightTime+rightServer+Arrays.toString(showServername));
			}
		}else{
			rightServer = true; //所有服都参加
			ActivitySubSystem.logger.warn("rightTime&rightServer2:"+rightTime+rightServer);
		}
		if(!"".equals(limitServerNames)&& limitServerNames !=null){
			String limitServername[] = limitServerNames.trim().split(",");
			if(limitServername!=null&&limitServername.length>0){
				for (int i = 0; i < limitServername.length; i++) {
					if (servername.trim().equals(limitServername[i].trim())) {
						rightServer = false;
					}
				}
			}
		}
		
		ActivitySubSystem.logger.warn("rightTime&rightServer3:"+rightTime+rightServer);
		return rightTime&rightServer;
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

	public String getShowServerNames() {
		return showServerNames;
	}

	public void setShowServerNames(String showServerNames) {
		this.showServerNames = showServerNames;
	}

	public String getLimitServerNames() {
		return limitServerNames;
	}

	public void setLimitServerNames(String limitServerNames) {
		this.limitServerNames = limitServerNames;
	}

	@Override
	public String getMConsoleDescription() {
		return "属性值设置";
	}

	@Override
	public String getMConsoleName() {
		return "每日领取活动管理器";
	}
	
}
