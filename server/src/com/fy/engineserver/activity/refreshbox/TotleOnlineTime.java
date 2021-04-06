package com.fy.engineserver.activity.refreshbox;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
/**
 * 累计在线活动
 * 
 *
 */
public class TotleOnlineTime extends ActivityConfig implements MConsole{

	@ChangeAble("累计在线1小时")
	public static long 累计在线1小时 = 60*60*1000;
	@ChangeAble("累计在线1小时物品名")
	public static String articlename = Translate.宝珠;
	
	public static String title = Translate.光复节活动邮件标题;
	
	public TotleOnlineTime(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		MConsoleManager.register(this);
	}
	
	/**
	 * 返回给定累计时间的数量
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getContinueLoginDays(long start,long end){
    	Calendar ca=Calendar.getInstance();
    	ca.setTimeInMillis(start);
    	long st=ca.getTimeInMillis();
    	
    	ca.clear();
    	ca.setTimeInMillis(end);
    	long et=ca.getTimeInMillis();
    	
    	long days=Math.abs((et-st)/(累计在线1小时));////
    	return days;
    }
    
	@Override
	public void heartbeat() {
		long now = System.currentTimeMillis();
		List<Player> onlineplayers = RefreshBoxManager.getInstance().onlineplayers;
		if(onlineplayers!=null && onlineplayers.size()>0){
			for(Player p:onlineplayers){
				if(p!=null && p.isOnline()){
					OnlineStat stat = (OnlineStat)ActivityManagers.getInstance().getDdc().get(p.getId()+"累计在线1小时活动");
					if(stat==null){
						OnlineStat st = new OnlineStat();
						ActivityManagers.getInstance().getDdc().put(p.getId()+"累计在线1小时活动", st);
						stat = (OnlineStat)ActivityManagers.getInstance().getDdc().get(p.getId()+"累计在线1小时活动");
					}
					long logintime = p.getPlayerLastLoginTime();
					long lasttime = stat.getLasttime();
					if(now - logintime >= 累计在线1小时){
						ActivitySubSystem.logger.warn("[累计在线1小时] [onlineplayers:"+onlineplayers.size()+"] ["+p.getLogString()+"]");
						if(getContinueLoginDays(lasttime,now)==1){
							if(sendPrice(p)){
								int oldvalue = stat.getValue();
								stat.setLasttime(now);
								stat.setValue(oldvalue+1);
								ActivityManagers.getInstance().getDdc().put(p.getId()+"累计在线1小时活动", stat);
								ActivitySubSystem.logger.warn("[累计在线1小时] [更新统计] [累计"+(oldvalue+1)+"小时] ["+p.getLogString()+"]");
							}
						}else if(getContinueLoginDays(lasttime,now)>1){
							if(sendPrice(p)){
								stat.setLasttime(now);
								stat.setValue(1);
								ActivityManagers.getInstance().getDdc().put(p.getId()+"累计在线1小时活动", stat);
								ActivitySubSystem.logger.warn("[累计在线1小时] [新增统计] [累计1小时] ["+p.getLogString()+"]");
							}
						}
					}
				}
			}
		}
	}
	
	public boolean sendPrice(Player p){
		Article a = null;
		try {
			MailManager mm = MailManager.getInstance();
			a = ArticleManager.getInstance().getArticle(articlename);
			if(a==null){
				ActivitySubSystem.logger.warn("[累计在线1小时] [发送奖励] [物品不存在:"+articlename+"] ["+p.getLogString()+"]");
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.累计在线活动, p, a.getColorType(), 1, true);
			mm.sendMail(p.getId(), new ArticleEntity[]{ae},title, Translate.光复节活动邮件内容, 0, 0, 0, "累计在线1小时活动奖励");
			p.sendError(Translate.光复节活动邮件内容);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[累计在线1小时] [异常] [name:"+p.getName()+"] ["+e+"]");
			return false;
		}
	}


	@Override
	public String getMConsoleName() {
		return "累计在线活动";
	}

	@Override
	public String getMConsoleDescription() {
		return "累计在线活动发奖间隔";
	}

	public static long get累计在线1小时() {
		return 累计在线1小时;
	}

	public static void set累计在线1小时(long 累计在线1小时) {
		TotleOnlineTime.累计在线1小时 = 累计在线1小时;
	}

	public static String getArticlename() {
		return articlename;
	}

	public static void setArticlename(String articlename) {
		TotleOnlineTime.articlename = articlename;
	}

}
