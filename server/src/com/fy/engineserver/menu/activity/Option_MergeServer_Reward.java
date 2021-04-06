package com.fy.engineserver.menu.activity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 合服活动
 * 
 *
 */
public class Option_MergeServer_Reward extends Option implements NeedCheckPurview , MConsole{
	
	/**
	 * 对于这一类点击NPC领取奖励的，可以统一由一个manager去管理
	 * 以后简单配置就能用
	 */
	@ChangeAble("startTime")
	private String startTime = "2013-1-26 00:00:00";
	@ChangeAble("endTime")
	private String endTime = "2013-09-19 23:59:59";
	@ChangeAble("openplats")
	private Platform[] openplats = { Platform.台湾 };
	@ChangeAble("limitservers")
	private Set<String> limitservers = new HashSet<String>();
	@ChangeAble("openServers")
	private String[] openServers = {"仙尊降世","雪域冰城","皇圖霸業"};
	@ChangeAble("articlenames")
	private String articlenames [] = {"合服錦囊","合服錦囊","合服回饋錦囊"};
	@ChangeAble("mailtitle")
	private String mailtitle = Translate.text_4931;
	@ChangeAble("mailcount")
	private String mailcount = Translate.text_4931;
	@ChangeAble("背包满提示") private String 背包满提示 = Translate.背包空间不足;
	
	
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
	public void doSelect(Game game, Player player) {
		MConsoleManager.register(this);
		
		if(player.getLevel()<71){
			player.sendError("您的等級未達到71級,無法獲得合服補償哦,快快升級吧!");
			return;
		}
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		Long nowtiem = (Long)ddc.get(player.getId()+"合服活动_tw");
		if(nowtiem==null){
			ddc.put(player.getId()+"合服活动_tw", new Long(11));
			nowtiem = (Long)ddc.get(player.getId()+"合服活动_tw");
		}
		if(nowtiem!=null){
			if(!isSameDay(System.currentTimeMillis(),nowtiem.longValue())){
				GameConstants gc = GameConstants.getInstance();
				String servername = gc.getServerName();
				int index = -1;
				for(int i=0;i<openServers.length;i++){
					if(openServers[i].equals(servername)){
						index = i;
					}
				}
				if(index>=0){
					String articlename = articlenames[index];
					Article a = ArticleManager.getInstance().getArticle(articlename); 
					if(a==null){
						ActivitySubSystem.logger.warn("[合服活动_tw] [物品不存在] [玩家信息："+player.getLogString()+"] ["+articlename+"]");
						return;
					}
					
					ArticleEntity ae = null;
					
					try {
						ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 0, true);
					} catch (Exception e) {
						e.printStackTrace();
						ActivitySubSystem.logger.warn("[合服活动_tw] [创建物品异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
						return;
					}
					
					if(ae!=null){
						boolean issucc = false;
						if(player.getKnapsack_common().isFull()){
							MailManager mm = MailManager.getInstance();
							try {
								mm.sendMail(player.getId(), new ArticleEntity[]{ae}, mailtitle, mailcount, 0, 0, 0, "合服活动_tw");
								ActivitySubSystem.logger.warn("[合服活动_tw] [领取礼包成功] [邮件发送] ["+player.getLogString()+"]");
								issucc = true;
							} catch (Exception e) {
								e.printStackTrace();
								ActivitySubSystem.logger.warn("[合服活动_tw] [通过邮件发送奖励异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
							}
							player.sendError(背包满提示);
						}else{
							player.putToKnapsacks(ae, "合服活动_tw");
							ActivitySubSystem.logger.warn("[合服活动_tw] [领取礼包成功] [放入背包] ["+player.getLogString()+"]");
							issucc = true;
						}
						if(issucc){
							ddc.put(player.getId()+"合服活动_tw", new Long(System.currentTimeMillis()));
							player.sendError("恭喜您获得合服奖励！");
						}
					}
				}
					
			}else{
				player.sendError(Translate.今天已经领取);
			}
		}else{
			ActivitySubSystem.logger.warn("[合服活动_tw] [ddc==null] ["+player.getLogString()+"]");
		}
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		if(!PlatformManager.getInstance().isPlatformOf(openplats)){
			return false;
		}
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		
		if(limitservers.contains(gc.getServerName())){
			return false;
		}
		
		if(TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)){
			return false;
		}
		
		for(String s:openServers){
			if(s.equals(gc.getServerName())){
				return true;
			}
		}
		
		if(openServers.length==0){
			return true;
		}
		
		return false;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public String getMConsoleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMConsoleDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
}


