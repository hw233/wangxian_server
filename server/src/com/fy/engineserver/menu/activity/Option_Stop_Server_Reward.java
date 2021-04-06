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
public class Option_Stop_Server_Reward extends Option implements NeedCheckPurview , MConsole{
	
	/**
	 * 对于这一类点击NPC领取奖励的，可以统一由一个manager去管理
	 * 以后简单配置就能用
	 */
	@ChangeAble("startTime")
	private String startTime = "2013-08-12 00:00:00";
	@ChangeAble("endTime")
	private String endTime = "2013-10-07 23:59:59";
	@ChangeAble("openplats")
	private Platform[] openplats = { Platform.官方,Platform.腾讯 };
	@ChangeAble("limitservers")
	private Set<String> limitservers = new HashSet<String>();
	@ChangeAble("背包满提示") private String 背包满提示 = Translate.背包空间不足;
	private String appServers[] = {"pan3","西方灵山","飞瀑龙池","玉幡宝刹","问天灵台","雪域冰城","白露横江","左岸花海","裂风峡谷","右道长亭","永安仙城","霹雳霞光","对酒当歌","独霸一方","独步天下","飞龙在天","九霄龙吟","万象更新","春风得意","天下无双","仙子乱尘","幻灵仙境","梦倾天下","再续前缘","兰若古刹","权倾皇朝","诸神梦境","倾世仙缘","傲啸封仙","一统江湖","龙隐幽谷","前尘忆梦","国色天香","天上红绯"};
	
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
		
		if(player.getLevel()<10){
			return;
		}
		
		boolean isappserver = false;
		String servername = GameConstants.getInstance().getServerName();
		for(String name : ActivityManagers.getInstance().appServers){
			if(name.equals(servername)){
				isappserver = true;
				break;
			}
		}
		
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		Long nowtiem = (Long)ddc.get(player.getId()+"20130930维护");
		
		if(nowtiem==null){
			if(isappserver){
				String [] names = {"强化石","500两工资转换卡"};
				int [] counts = {20,1};
				ArticleEntity aes [] = new ArticleEntity[names.length];
				for(int i=0;i<names.length;i++){
					Article a = ArticleManager.getInstance().getArticle(names[i]);
					if(a!=null){
						ArticleEntity ae;
						try {
							int color = 0;
							if(names[i].equals("强化石")){
								color = 0;
							}else{
								color = a.getColorType();
							}
							ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.维护补偿, player, color, counts[i], true);
							aes[i] = ae;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(aes.length>0){
					try {
						MailManager.getInstance().sendMail(player.getId(), aes,new int[]{20,1}, "领取补偿", "针对本次维护服务器，而给玩家造成的无法正常游戏的困扰，我们诚挚的向您致歉，因延误大家的宝贵时间，特将为广大的玩家提供了补偿奖励。", 0, 0, 0, "维护补偿");
						player.sendError("恭喜您领取成功");
						ddc.put(player.getId()+"20130930维护", new Long(player.getId()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			
			}else{
				String [] names = {"玉液锦囊(绿色)","绿色封魔录锦囊"};
				int [] counts = {4,4};
				ArticleEntity aes [] = new ArticleEntity[names.length];
				ArticleEntity ae;
				for(int i=0;i<names.length;i++){
					Article a = ArticleManager.getInstance().getArticle(names[i]);
					if(a!=null){
						try {
							ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.维护补偿, player, a.getColorType(), counts[i], true);
							aes[i] = ae;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(aes.length>0){
					try {
						MailManager.getInstance().sendMail(player.getId(), aes, new int[]{4,4},"领取补偿", "针对本次维护服务器，而给玩家造成的无法正常游戏的困扰，我们诚挚的向您致歉，因延误大家的宝贵时间，特将为广大的玩家提供了补偿奖励。", 0, 0, 0, "维护补偿");
						player.sendError("恭喜您领取成功");
						ddc.put(player.getId()+"20130930维护", new Long(player.getId()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}else{
			player.sendError(Translate.text_5158);
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
		
		return true;
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


