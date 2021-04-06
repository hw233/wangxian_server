package com.fy.engineserver.activity.refreshbox;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
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
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
/**
 * 大使技能排行榜活动
 * 
 *
 */
public class BillbordActivityOfSkillHoner extends ActivityConfig implements MConsole{

	public static String [] datas = {Translate.世界};
	
	public static String  rewardnames_[] = {"大师技能排行榜1","大师技能排行榜2","大师技能排行榜3"};
	
	public static String  rewardnames__[] = {"低调奢华有内涵","高端大气上档次","忧郁深沉无所谓"};
	
	public static String[][] rewardnames = {rewardnames_};
	public static String[][] rewardnames2 = {rewardnames__};
	
	public int 参与奖励名次 = 3;
	
	@ChangeAble("周一是2，周日是1")
	public long 星期几发奖 =  1;
	
	@ChangeAble("发奖时间小时")
	public long 发奖时间小时 = 23;
	
	@ChangeAble("发奖时间点")
	public long 发奖时间点 = 0;
	
	@ChangeAble("是否已经发奖")
	public boolean issendprice = false;
	
	public String keyname;	//用来记录上次获取称号的玩家key，不同平台唯一
	
	private LinkedHashMap<String, Long[]> records = new LinkedHashMap<String, Long[]>();
	
	public BillbordActivityOfSkillHoner(String keyname,long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		this.keyname = keyname;
		MConsoleManager.register(this);
	}
	
	
	public void sendPrice(){
		Calendar cl = Calendar.getInstance();
		int day_of_week = cl.get(Calendar.DAY_OF_WEEK);
		if(day_of_week==星期几发奖){
			ActivitySubSystem.logger.warn("[大使技能排行榜活动] [issendprice:"+issendprice+"] [发奖时间小时:"+发奖时间小时+"] [发奖时间点:"+发奖时间点+"]");
			if(cl.get(Calendar.HOUR_OF_DAY) == 发奖时间小时 && cl.get(Calendar.MINUTE) == 发奖时间点 && !issendprice) {//TODO
				try {
					LinkedHashMap<String, Long[]> records = (LinkedHashMap<String, Long[]>)ActivityManagers.getInstance().getDdc().get(keyname);
					if(records==null){
						records = new LinkedHashMap<String, Long[]>();
						Long [] ids = new Long[参与奖励名次];
						ids[0] = -1l;ids[1] = -1l;ids[2] = -1l;
						records.put(keyname, ids);
						ActivityManagers.getInstance().getDdc().put(keyname, records);
						records = (LinkedHashMap<String, Long[]>)ActivityManagers.getInstance().getDdc().get(keyname);
					}
					Long [] oldids = records.get(keyname);
					if(oldids!=null && oldids.length>0){
						for(int i=0;i<oldids.length;i++){
							long id = oldids[i].longValue();
							if(id!=-1){
								try{
									Player p = PlayerManager.getInstance().getPlayer(id);
									for(int j=0;j<rewardnames_.length;j++){
										PlayerTitlesManager.getInstance().removeTitle(p, rewardnames_[j]);
										ActivitySubSystem.logger.warn("[大使技能排行榜活动] [删除上周排行榜] [成功] ["+p.getLogString()+"] [称号："+rewardnames_[j]+"]");
									}
								}catch(Exception e){
									ActivitySubSystem.logger.warn("[大使技能排行榜活动] [删除上周排行榜] [异常] [玩家id："+id+"] ["+e+"]");
									continue;
								}
							}
						}
					}
					
					for(int i=0;i<datas.length;i++){
						String d = datas[i];
						for(int j=0;j<参与奖励名次;j++){
							Player p = 获得有名次的排行榜(Translate.大使技能,d,j);
							if(p!=null){
								ChatMessageService cm = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								msg.setSort(ChatChannelType.SYSTEM);
								String des = Translate.translateString(Translate.大使技能结束广播, new String[][] { { Translate.STRING_1, p.getName() },{ Translate.STRING_2, rewardnames2[i][j] }});
								msg.setMessageText(des);
								cm.sendMessageToWorld(msg);
								cm.sendMessageToSystem(msg);
								PlayerTitlesManager.getInstance().addTitle(p, rewardnames[i][j], true);
								ActivitySubSystem.logger.warn("[大使技能排行榜活动] [in sendPrice..] [有效] [发送称号成功:"+rewardnames2[i][j]+"] [p:"+p.getLogString()+"]");
								issendprice = true;
								oldids[j] = p.getId();
								records.put(keyname, oldids);
								ActivityManagers.getInstance().getDdc().put(keyname, records);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			issendprice = false;
		}
	}
	
	public Player 获得有名次的排行榜(String menuname,String submenuname,int index){
		Billboard bb = BillboardsManager.getInstance().getBillboard(menuname, submenuname);
		if(bb!=null){
			BillboardDate[] data = bb.getDates();
			if (data != null && data.length > 0) {
				try {
					if(index>=data.length){
						ActivitySubSystem.logger.warn("[大使技能排行榜活动] [获得有名次的排行榜] [没有第"+index+1+"名] [menuname:"+menuname+"] [submenuname:"+submenuname+"] [名次："+(index+1)+"] ");
						return null;
					}
					Player player = PlayerManager.getInstance().getPlayer(data[index].getDateId());
					ActivitySubSystem.logger.warn("[大使技能排行榜活动] [获得有名次的排行榜] [成功] [menuname:"+menuname+"] [submenuname:"+submenuname+"] [名次："+(index+1)+"] [p:"+player.getLogString()+"]");
					return player;
				} catch (Exception e1) {
					e1.printStackTrace();
					ActivitySubSystem.logger.warn("[大使技能排行榜活动] [获得有名次的排行榜] [异常] [data:"+data.length+"] ["+e1+"]");
				}
			}
		}else{
			ActivitySubSystem.logger.warn("[大使技能排行榜活动] [in 活动排行榜第一] [menuname:"+menuname+"] [submenuname:"+submenuname+"] ["+bb+"]");
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
		sendPrice();
	}

	@Override
	public String getMConsoleName() {
		return "国服大师技能排行榜活动";
	}

	@Override
	public String getMConsoleDescription() {
		return "排行榜活动的一些控制";
	}


	public static String[] getDatas() {
		return datas;
	}


	public static void setDatas(String[] datas) {
		BillbordActivityOfSkillHoner.datas = datas;
	}


	public static String[] getRewardnames_() {
		return rewardnames_;
	}


	public static void setRewardnames_(String[] rewardnames_) {
		BillbordActivityOfSkillHoner.rewardnames_ = rewardnames_;
	}


	public static String[] getRewardnames__() {
		return rewardnames__;
	}


	public static void setRewardnames__(String[] rewardnames__) {
		BillbordActivityOfSkillHoner.rewardnames__ = rewardnames__;
	}


	public static String[][] getRewardnames() {
		return rewardnames;
	}


	public static void setRewardnames(String[][] rewardnames) {
		BillbordActivityOfSkillHoner.rewardnames = rewardnames;
	}


	public static String[][] getRewardnames2() {
		return rewardnames2;
	}


	public static void setRewardnames2(String[][] rewardnames2) {
		BillbordActivityOfSkillHoner.rewardnames2 = rewardnames2;
	}


	public int get参与奖励名次() {
		return 参与奖励名次;
	}


	public void set参与奖励名次(int 参与奖励名次) {
		this.参与奖励名次 = 参与奖励名次;
	}


	public long get星期几发奖() {
		return 星期几发奖;
	}


	public void set星期几发奖(long 星期几发奖) {
		this.星期几发奖 = 星期几发奖;
	}


	public long get发奖时间小时() {
		return 发奖时间小时;
	}


	public void set发奖时间小时(long 发奖时间小时) {
		this.发奖时间小时 = 发奖时间小时;
	}


	public long get发奖时间点() {
		return 发奖时间点;
	}


	public void set发奖时间点(long 发奖时间点) {
		this.发奖时间点 = 发奖时间点;
	}


	public boolean isIssendprice() {
		return issendprice;
	}


	public void setIssendprice(boolean issendprice) {
		this.issendprice = issendprice;
	}


	public String getKeyname() {
		return keyname;
	}


	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}


	public LinkedHashMap<String, Long[]> getRecords() {
		return records;
	}


	public void setRecords(LinkedHashMap<String, Long[]> records) {
		this.records = records;
	}


	
}
