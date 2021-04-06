package com.fy.engineserver.activity.refreshbox;

import java.util.Calendar;
import java.util.Set;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.boss.game.GameConstants;

public class BillbordActivity extends ActivityConfig implements MConsole{

	public static String [] datas = {Translate.当日鲜花,Translate.当日糖果};
	public static String  rewardnames[] = {Translate.飞行坐骑礼包30,Translate.飞行坐骑礼包15,Translate.飞行坐骑礼包7};
	public String hua = Translate.玫瑰花束;
	@ChangeAble("starthour")
	public int starthour = 23;
	@ChangeAble("startminute")
	public int startminute = 58;
	
	public BillbordActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		MConsoleManager.register(this);
	}
	
	public boolean isEffective() {
		if(!super.isEffective()){
			return false;
		}
		Calendar cl = Calendar.getInstance();
		if (cl.get(Calendar.HOUR_OF_DAY) == starthour) {//TODO
			/** 按日结算 **/
			if (cl.get(Calendar.MINUTE) == startminute || cl.get(Calendar.MINUTE) == (startminute+1)) {//TODO
				Long lastTime = (Long) ActivityManager.getInstance().disk.get("排行榜活动20130806");
				if (lastTime == null) {
					ActivityManager.getInstance().disk.put("排行榜活动20130806", System.currentTimeMillis());
					BillboardsManager.logger.error("【排行榜活动】[添加新的统计数据]");
					return true;
				} else {
					if (!isSameDay(lastTime.longValue(), System.currentTimeMillis())) {
						ActivityManager.getInstance().disk.put("排行榜活动20130806", System.currentTimeMillis());
						BillboardsManager.logger.error("【排行榜活动】[不是同一天的统计数据]");
						return true;
					}
				}
				return true;
			}
		}
		return false;
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
	public void heartbeat() {
		BillboardsManager.logger.error("【排行榜活动】[排行榜线程]");
		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ChatMessageService cms = ChatMessageService.getInstance();
		ArticleEntity ae = null;
		ArticleEntity ae_hua = null;
		Billboard dayFlower = null;
		Article huaarticle = ArticleManager.getInstance().getArticle(hua);
		for(String name:datas){
			dayFlower = BillboardsManager.getInstance().getBillboard(Translate.魅力, name);
			if (dayFlower != null) {
				BillboardDate[] data = dayFlower.getDates();
				if (data != null && data.length > 0) {
					if (data.length < 3) {
						BillboardsManager.logger.error("【排行榜活动】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜数量少于10，不符合活动规则] [数量：" + data.length + "]");
					} else {
						for (int i = 0; i < 3; i++) {
							String datevalue = data[i].getDateValues()[3]; 
							if (datevalue != null && datevalue.trim().length() > 0) {
								Player player = null;
								try {
									player = PlayerManager.getInstance().getPlayer(data[i].getDateId());
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if (player != null) {
									Article a = ArticleManager.getInstance().getArticle(rewardnames[i]);
									if (a == null) {
										BillboardsManager.logger.error("【排行榜活动】[玩家信息:"+player.getLogString()+"] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [物品不存在,物品名:" + rewardnames[i] + "] [数量：" + data.length + "]");
										continue;
									}
									if(huaarticle == null){
										BillboardsManager.logger.error("【排行榜活动】[玩家信息:"+player.getLogString()+"] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [物品不存在,物品名:" + hua + "] [数量：" + data.length + "]");
										continue;
									}
									try {
										ae = am.createEntity(a, true, ArticleEntityManager.活动棉花糖, player, a.getColorType(), 1, true);
										ae_hua = am.createEntity(huaarticle, true, ArticleEntityManager.活动棉花糖, player, a.getColorType(), 1, true);
										MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae ,ae_hua}, Translate.浪漫七夕, Translate.浪漫七夕内容, 0, 0, 0, "浪漫七夕活动");
										String msg = Translate.text_marriage_011 + "[" + player.getName() + Translate.text_3745 + Translate.浪漫七夕 + Translate.中获得了第 + (i + 1) + Translate.名 + "!";
										if (i <= 1) {
											ChatMessage chat = new ChatMessage();
											chat.setMessageText(msg);
											cms.sendMessageToSystem(chat);
										}
										BillboardsManager.logger.error("【排行榜活动】【获得奖励】 [服务器：" + GameConstants.getInstance().getServerName() + "] [帐号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + player.getId() + "] [第" + (i + 1) + "名] [奖励：" + ae.getArticleName() + "]");
									} catch (Exception e) {
										e.printStackTrace();
										BillboardsManager.logger.error("【排行榜活动】【异常】 [服务器：" + GameConstants.getInstance().getServerName() + "] [帐号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + player.getId() + "] [第" + (i + 1) + "名]", e);
									}
								}
							} else {
								BillboardsManager.logger.error("【排行榜活动】[没有获得"+name+"数据] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜中没有数量相关数据] [积分：" + datevalue + "] [数量：" + data.length + "]");
							}
						}
					}
				} else {
					BillboardsManager.logger.error("【排行榜活动】[没有获得"+name+"数据] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜数量为空]");
				}
			} else {
				BillboardsManager.logger.error("【排行榜活动】[没有获得"+name+"数据] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜为空]");
			}
		}
	}

	@Override
	public String getMConsoleName() {
		return "浪漫七夕——才子佳人天注定";
	}

	@Override
	public String getMConsoleDescription() {
		return "排行榜活动的一些控制";
	}

}
