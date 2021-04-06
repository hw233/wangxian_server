package com.fy.engineserver.activity;

import java.util.Date;

import org.slf4j.Logger;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.Utils;


/**
 * 为活动单开的一个线程
 * @author Administrator
 *
 */
public class ActivityThread implements Runnable{

	public static Logger logger = ActivitySubSystem.logger;
	
	
	public static int 最少下限 = 9999;
	
	public String 临时日期 = "2012-11-07 23:59:00";
	
	//设置那天可以发邮件，打印日志
	public String[] dateString = {临时日期,"2012-11-11 23:59:00","2012-11-12 23:59:00","2012-11-13 23:59:00"};
	long[] times = null;
	boolean[] effects = null;
	
	
	//设置那天可以发滚屏
	public int[] 坐骑特殊日期 = new int[]{2012,10,07};
	public int[][] 指定日期 = {坐骑特殊日期,{2012,10,11},{2012,10,12},{2012,10,13}};
	
	//设置那天的那个时间发滚屏
	public int[][] 指定时间 = {{12,0},{12,30},{22,0},{22,30}};
	public boolean[] 指定时间生效 = null;
	
	
//	public String 坐骑名称  = Translate.坐骑名称;
	public String 坐骑名称  = Translate.坐骑名称半月;
	
	public Article horseArticle = null;
	
	private static ActivityThread instance = new ActivityThread();
	public static ActivityThread getInstance() {
		return instance;
	}
	
	
	public void init() throws Exception{
		
		long now = SystemTime.currentTimeMillis();
		
		times = new long[dateString.length];
		effects = new boolean[dateString.length];
		
		for(int i = 0;i<dateString.length;i++){
			Date date = null;
			date = LogRecordManager.sdf.parse(dateString[i]);
			long time = date.getTime();
			times[i] = time;
			if(time > now){
				effects[i] = true;
				logger.error("[加载定时查询当日鲜花糖果排行榜有效] ["+dateString[i]+"]");
			}else{
				effects[i] = false;
				logger.error("[加载定时查询当日鲜花糖果排行榜失效] ["+dateString[i]+"]");
			}
		}
		
		指定时间生效 = new boolean[指定时间.length];
		for(int i= 0;i<指定时间.length;i++){
			指定时间生效[i] = true;
		}
		logger.error("[加载指定时间生效]");
		
		horseArticle = ArticleManager.getInstance().getArticle(坐骑名称);
		if(horseArticle == null){
			logger.error("活动配置的飞行坐骑不对"+坐骑名称);
			System.out.println("活动配置的飞行坐骑不对"+坐骑名称);
		}
		
	}
	
	public static boolean running = true;
	
	@Override
	public void run() {
		while(running){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				long now = SystemTime.currentTimeMillis();
				for(int j = 0;j<times.length;j++){
					if(now > times[j] && effects[j]){
						effects[j] = false;
						打印鲜花(dateString[j]);
						打印糖果(dateString[j]);
					}
				}
				
				
				boolean specialDay = false;
				boolean specialTime = false;
				try{
					for(int[] ints : 指定日期){
						specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
						if(specialDay){
							for(int j =0;j<指定时间.length;j++){
								if(指定时间生效[j]){
									int[] intss = 指定时间[j];
									specialTime = Utils.isSpecialSameTime(intss[0], intss[1]);
									if(specialTime){
										sendNotice();
										for(int z= 0;z<指定时间生效.length;z++){
											指定时间生效[z] = true;
										}
										指定时间生效[j] = false;
										break;
									}
								}
							}
							break;
						}
					}
				}catch(Exception e){
					ActivitySubSystem.logger.error("[发送活动通知异常]",e);
				}
			}catch(Throwable t){
				logger.error("[活动线程异常]",t);
			}
		}
	}
	
	public void 打印鲜花(String datast){
		Billboard dayFlower = BillboardsManager.getInstance().getBillboard("魅力","当日鲜花");
		if(dayFlower != null){
			BillboardDate[] data = dayFlower.getDates();
			if(data != null && data.length > 0){
				sendWinPiaze(0, data[0].getDateId(),data[0].getDateValues()[3]);
				for(BillboardDate bbd : data){
					BillboardsManager.logger.error("["+datast+"] [当日鲜花打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"] ["+bbd.getDateValues()[3]+"]");
				}
			}else{
				BillboardsManager.logger.error("["+datast+"] [后台打印当日鲜花失败] [数据为null]");
			}
		}else{
			BillboardsManager.logger.error("["+datast+"] [后台打印当日鲜花失败] [没有排行榜]");
		}
	}
	
	
	public void 打印糖果(String datast){
		Billboard daySweet = BillboardsManager.getInstance().getBillboard("魅力","当日糖果");
		if(daySweet != null){
			BillboardDate[] data = daySweet.getDates();
			if(data != null && data.length > 0){
				sendWinPiaze(1, data[0].getDateId(),data[0].getDateValues()[3]);
				for(BillboardDate bbd : data){
					BillboardsManager.logger.error("["+datast+"] [当日糖果打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"] ["+bbd.getDateValues()[3]+"]");
				}
			}else{
				BillboardsManager.logger.error("["+datast+"] [后台打印当日糖果失败] [数据为null]");
			}
		}else{
			BillboardsManager.logger.error("["+datast+"] [后台打印当日糖果失败] [没有排行榜]");
		}
	}
	
	
	
//	a)	邮件标题：恭喜您成为飘渺寻仙曲万人迷
//	b)	邮件内容：恭喜您在当日鲜花/当日糖果排行榜中获得冠军，赢得拉风的风行坐骑。请注意查收附件！
	public void sendWinPiaze(int type,long playerId,String nums){
		try{
//			if(!ActivityManager.getInstance().分服活动open){
//				logger.error("[服务器不符]");
//				return;
//			}
			Player player = PlayerManager.getInstance().getPlayer(playerId);
			Long num = Long.parseLong(nums); 
			if(num >= 最少下限){
				String 当日 = "";
				if(type == 0){
					当日 = Translate.当日鲜花;
				}else{
					当日 = Translate.当日糖果;
				}
				String des = Translate.translateString(Translate.恭喜您在xx排行榜中获得冠军, new String[][]{{Translate.STRING_1,当日}});
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(horseArticle, true, ArticleEntityManager.运营发放活动奖励, player, horseArticle.getColorType(), 1, true);
				MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{1}, Translate.恭喜您成为飘渺寻仙曲万人迷, des, 0, 0, 0, "在线活动");
				ActivitySubSystem.logger.warn("[给第一名邮件发物品] [第一名:"+player.getLogString()+"]");
			}else{
				ActivitySubSystem.logger.warn("[给第一名邮件发物品] [少于9999] ["+num+"] [第一名:"+player.getLogString()+"]");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	3.	活动期间，每天12:00、12:30、22:00、22:30自动系统滚屏进行活动宣传
//	谁是万众瞩目的飘渺寻仙曲万人迷？今天[当日鲜花榜]和[当日糖果榜]的冠军，且积分不低于9999即可获得极度拉风的飞行坐骑哦。快把鲜花和糖果送给心仪的TA吧！
	public void sendNotice(){
//		if(!ActivityManager.getInstance().分服活动open){
//			logger.error("[服务器不符]");
//			return;
//		}
		try{
			ChatMessageService cms = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			msg.setMessageText("<f color='0x14ff00'>"+Translate.活动邮件内容+"</f>");
			cms.sendRoolMessageToSystem(msg);
			
			ActivitySubSystem.logger.warn("[发送活动详细通知] ["+Translate.活动邮件内容+"]");
		}catch(Exception ex){
			ActivitySubSystem.logger.error("[活动发送邮件异常]",ex);
		}
		
	}
	
}
