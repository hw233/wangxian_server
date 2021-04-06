package com.fy.engineserver.activity.refreshbox;

import java.util.Calendar;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;

public class BillbordActivityHoner extends ActivityConfig implements MConsole{

	/**
	 * ！！！！这周比较忙，等腾讯，台湾，国服都维护了，再扩展吧
	 */
	public static String [] datas = {Translate.糖果活动,Translate.鲜花活动};
	
	public static String  rewardnames_糖果[] = {"할로윈 데이20","할로윈 데이21","할로윈 데이22"};
	public static String  rewardnames_鲜花[] = {"할로윈 데이17","할로윈 데이18","할로윈 데이19"};
	
	
	public static String  rewardnames_糖果2[] = {"인기 아이돌","비범한 인물","뛰어난 인재"};
	public static String  rewardnames_鲜花2[] = {"만인의 연인","매혹적인 여인","우아한 여인"};
	
	public static String[][] rewardnames = {rewardnames_糖果,rewardnames_鲜花};
	public static String[][] rewardnames2 = {rewardnames_糖果2,rewardnames_鲜花2};
	
	public int 参与奖励名次 = 3;
	
	@ChangeAble("每天公告时间")
	public int 每天公告时间 = 21;
	
	@ChangeAble("每天公告时间点")
	public int 每天公告时间点 = 0;
	
	@ChangeAble("发奖时间")
	public long 发奖时间 =  TimeTool.formatter.varChar19.parse("2013-11-05 18:00:00");
	
	@ChangeAble("发奖时间小时")
	public long 发奖时间小时 = 23;
	
	@ChangeAble("发奖时间点")
	public long 发奖时间点 = 0;
	
	public boolean issendprice = false;
	
	public BillbordActivityHoner(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		MConsoleManager.register(this);
	}
	
	
	public void sendeveryNotice() {
		Calendar cl = Calendar.getInstance();
		if (cl.get(Calendar.HOUR_OF_DAY) == 每天公告时间 && cl.get(Calendar.MINUTE) == 每天公告时间点) {//TODO
			for(String d:datas){
				try {
					//获得不同排行榜的第一人
					Player p = 获得有名次的排行榜(Translate.魅力,d,0);
					if(p!=null){
						ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] [in sendeveryNotice..] [有效] ["+d+"] [p:"+p.getLogString()+"]");
						ChatMessageService cm = ChatMessageService.getInstance();
						ChatMessage msg = new ChatMessage();
						msg.setSort(ChatChannelType.SYSTEM);
						String des = Translate.translateString(Translate.每日广播, new String[][] { { Translate.STRING_1, p.getName() },{ Translate.STRING_2, d }});
						msg.setMessageText(des);
						cm.sendMessageToWorld(msg);
						cm.sendMessageToSystem(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] 【异常】 "+e);
				}
			}
		}
	}
	
	public void sendPrice(){
		if(isSameDay(发奖时间,System.currentTimeMillis())){
			Calendar cl = Calendar.getInstance();
			if(cl.get(Calendar.HOUR_OF_DAY) == 发奖时间小时 && cl.get(Calendar.MINUTE) == 发奖时间点 && !issendprice) {//TODO
				try {
					for(int i=0;i<datas.length;i++){
						String d = datas[i];
						for(int j=0;j<参与奖励名次;j++){
							Player p = 获得有名次的排行榜(Translate.魅力,d,j);
							if(p!=null){
								ChatMessageService cm = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								msg.setSort(ChatChannelType.SYSTEM);
								String des = Translate.translateString(Translate.结束广播, new String[][] { { Translate.STRING_1, p.getName() },{ Translate.STRING_2, rewardnames2[i][j] }});
								msg.setMessageText(des);
								cm.sendMessageToWorld(msg);
								cm.sendMessageToSystem(msg);
								PlayerTitlesManager.getInstance().addTitle(p, rewardnames[i][j], true);
								ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] [in sendPrice..] [有效] [发送称号成功:"+rewardnames2[i][j]+"] [p:"+p.getLogString()+"]");
								issendprice = true;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public Player 获得有名次的排行榜(String menuname,String submenuname,int index){
		Billboard bb = BillboardsManager.getInstance().getBillboard(menuname, submenuname);
		int index2 = index;
		if(bb!=null){
			BillboardDate[] data = bb.getDates();
			if (data != null && data.length > 0) {
				try {
					if(index>=data.length){
						index2 = data.length-1;
					}
					Player player = PlayerManager.getInstance().getPlayer(data[index2].getDateId());
					ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] [成功] [menuname:"+menuname+"] [submenuname:"+submenuname+"] [名次："+(index+1)+"] [p:"+player.getLogString()+"]");
					return player;
				} catch (Exception e1) {
					e1.printStackTrace();
					ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] [异常] [data:"+data.length+"] ["+e1+"]");
				}
			}
		}else{
			ActivitySubSystem.logger.warn("[鲜花糖果排行榜公告] [in 活动排行榜第一] [menuname:"+menuname+"] [submenuname:"+submenuname+"] ["+bb+"]");
		}
		return null;
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
		sendeveryNotice();
		sendPrice();
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
