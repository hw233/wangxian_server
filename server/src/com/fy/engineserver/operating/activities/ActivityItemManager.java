/**
 * 
 */
package com.fy.engineserver.operating.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * @author Administrator
 *
 */
public class ActivityItemManager implements Runnable{

	File activitiesInfoFile;
	
//	public static Logger log=Logger.getLogger(ActivityItemManager.class);
public	static Logger log = LoggerFactory.getLogger(ActivityItemManager.class);
	
	ArrayList<ActivityItem>[] activities;
	
	static ActivityItemManager self;
	
	final String[] WEEK={"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
	
	public static final int MONDAY=0;
	
	public static final int TUESDAY=1;
	
	public static final int WEDNESDAY=2;
	
	public static final int THURSDAY=3;
	
	public static final int FRIDAY=4;
	
	public static final int SATURDAY=5;
	
	public static final int SUNDAY=6;
	
	public static long ChristmasActivityStartTime;
	
	public static long ChristmasActivityEndTime;
	
	public static long NewYearActivityStartTime;
	
	public static long NewYearActivityEndTime;
	
	File christmasActivityFile;
	
	File newYearActivityFile;
	
	public DefaultDiskCache ddc;
	
	public DefaultDiskCache newYearddc;
	
	public static boolean isAlive=true;
	
	/**
	 * 
	 */
	public ActivityItemManager() {
		// TODO Auto-generated constructor stub
		ActivityItemManager.self=this;
	}
	
	public void init(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try{
//			this.setDaemon(true);
//			this.setName("ChargeInfo-Watchlog");
//			this.addFile(new File(fileDir,fileName));
//			this.start();
			this.activities=new ArrayList[this.WEEK.length];
			for(int i=0;i<this.activities.length;i++){
				this.activities[i]=new ArrayList<ActivityItem>();
			}
			this.readDataFile();
			
			this.ddc=new DefaultDiskCache(this.christmasActivityFile, null,
					"ChristmasActivity", 100L * 365 * 24 * 3600 * 1000L);
			
			this.newYearddc=new DefaultDiskCache(this.newYearActivityFile, null,
					"NewYearActivityFile", 100L * 365 * 24 * 3600 * 1000L);
			
			Thread th=new Thread(this);
			th.setName(Translate.text_5523);
			th.start();
			
		}catch(Exception e){
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[读取运营活动信息] [失败] [发生错误] [错误："+e+"]",e);
		}
		if(log.isInfoEnabled()){
//			log.info("[初始化运营活动信息] [结束] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
			if(log.isInfoEnabled())
				log.info("[初始化运营活动信息] [结束] [耗时：{}毫秒]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
		}
		
		Calendar ca=Calendar.getInstance();
		ca.clear();
//		ca.set(2010, Calendar.DECEMBER, 24, 0, 0, 0);
//		ActivityItemManager.ChristmasActivityStartTime=ca.getTimeInMillis();
//		ca.clear();
//		ca.set(2010, Calendar.DECEMBER, 28, 0, 0, 0);
//		ActivityItemManager.ChristmasActivityEndTime=ca.getTimeInMillis();
		
		ca.set(2011, Calendar.FEBRUARY, 2, 0, 0, 0);
		ActivityItemManager.NewYearActivityStartTime=ca.getTimeInMillis();
		ca.clear();
		ca.set(2011, Calendar.FEBRUARY, 8, 9, 0, 0);
		ActivityItemManager.NewYearActivityEndTime=ca.getTimeInMillis();
		
		System.out.println("[系统初始化] [运营活动信息管理器] [初始化完成] ["+this.getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
	}
	
	public static void main(String[] args) {
		Calendar ca=Calendar.getInstance();
		ca.clear();
		ca.set(2011, Calendar.JANUARY, 1, 0, 0, 0);
//		System.out.println("time:"+ca.getTimeInMillis());
		
		ca.clear();
		ca.set(2011, Calendar.JANUARY, 4, 0, 0, 0);
//		System.out.println("time:"+ca.getTimeInMillis());
	}
	
	public static ActivityItemManager getInstance(){
		return ActivityItemManager.self;
	}

	public File getActivitiesInfoFile() {
		return activitiesInfoFile;
	}

	public void setActivitiesInfoFile(File activitiesInfoFile) {
		this.activitiesInfoFile = activitiesInfoFile;
	}

	private void outputDataFile() throws Exception{
		FileOutputStream output=null;
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gb2312'?>\n");
			sb.append("<activities time='"+new Date()+"'>"+'\n');
			for(int i=0;i<this.activities.length;i++){
				sb.append("<dailyActivities day='"+this.WEEK[i]+"'>"+'\n');
				for(int j=0;j<this.activities[i].size();j++){
					ActivityItem ai=this.activities[i].get(j);
					sb.append("<activityItem id='"+ai.getActivityId()+"' title='"+ai.getTitle()+"' time='"+ai.getTime()+"' startMapName='"+ai.getStartMapName()+"' startX='"+ai.getStartX()+"' startY='"+ai.getStartY()+"' needPathfinding='"+ai.isNeedPathfinding()+"' textColor='"+ai.getTextColor()+"' detail='"+ai.getDetail()+"' weight='"+ai.getWeight()+"' camp='"+ai.getCamp()+"' minimalLevelLimit='"+ai.getMinimalLevelLimit()+"' maximalLevelLimit='"+ai.getMaximalLevelLimit()+"'/>"+'\n');
				}
				sb.append("</dailyActivities>"+'\n');
			}
			sb.append("</activities>");
			
			output = new FileOutputStream(this.activitiesInfoFile);
//			if(TransferLanguage.characterTransferormFlag){
				output.write(sb.toString().getBytes("utf-8"));
//			}else{
//				output.write(sb.toString().getBytes());
//			}
			
			output.flush();
			output.close();
		}finally{
			if(output!=null){
				output.close();
			}
		}
	}
	
	private void saveActivities(){
		try{
			long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			this.outputDataFile();
			if(log.isInfoEnabled()){
//				log.info("[运营活动] [存储成功] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[运营活动] [存储成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[运营活动] [存储ID出错] [发生错误]",e);
		}
	}
	
	private long getRandomId(){
		return Long.parseLong(StringUtil.randomIntegerString(12));
	}
	
	public ArrayList<ActivityItem>[] getActivityItem(Player player){
		ArrayList<ActivityItem>[] als=new ArrayList[this.activities.length]; 
		for(int i=0;i<als.length;i++){
			als[i]=new ArrayList<ActivityItem>();
			for(int j=0;j<this.activities[i].size();j++){
				ActivityItem ai=this.activities[i].get(j);
				if(ai.getCamp()==0){
					if(player.getLevel()>=ai.getMinimalLevelLimit()&&player.getLevel()<=ai.getMaximalLevelLimit()){
						als[i].add(ai);
					}
				}else{
					if(player.getLevel()>=ai.getMinimalLevelLimit()&&player.getLevel()<=ai.getMaximalLevelLimit()&&player.getCountry()==ai.getCamp()){
						als[i].add(ai);
					}
				}
			}
		}
		return als;
	}
	public ArrayList<ActivityItem>[] getActivityItem(){
		
		return this.activities;
	}
	private void readDataFile() throws Exception{
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(this.activitiesInfoFile);
			Document doc = null;
//			if(TransferLanguage.characterTransferormFlag){
				doc=XmlUtil.load(fis,"utf-8");
//			}else{
//				doc=XmlUtil.load(fis);
//			}
			Element root = doc.getDocumentElement();
			Element[] dailyActivities=XmlUtil.getChildrenByName(root, "dailyActivities");
			for(int i=0;i<dailyActivities.length;i++){
				Element[] activityItem=XmlUtil.getChildrenByName(dailyActivities[i], "activityItem");
				for(int j=0;j<activityItem.length;j++){
					long id=XmlUtil.getAttributeAsLong(activityItem[j], "id",0);
					String title=XmlUtil.getAttributeAsString(activityItem[j], "title","", null);
					String time=XmlUtil.getAttributeAsString(activityItem[j], "time","", null);
					String startMapName=XmlUtil.getAttributeAsString(activityItem[j], "startMapName","", null);
					int startX=XmlUtil.getAttributeAsInteger(activityItem[j], "startX",0);
					int startY=XmlUtil.getAttributeAsInteger(activityItem[j], "startY",0);
					boolean needPathfinding=XmlUtil.getAttributeAsBoolean(activityItem[j], "needPathfinding",false);
					int textColor=XmlUtil.getAttributeAsInteger(activityItem[j], "textColor",0xffffff);
					String detail=XmlUtil.getAttributeAsString(activityItem[j], "detail", null);
					int weight=XmlUtil.getAttributeAsInteger(activityItem[j], "weight",0);
					int camp=XmlUtil.getAttributeAsInteger(activityItem[j], "camp",0);
					int minimalLevelLimit=XmlUtil.getAttributeAsInteger(activityItem[j], "minimalLevelLimit",0);
					int maximalLevelLimit=XmlUtil.getAttributeAsInteger(activityItem[j], "maximalLevelLimit",ExperienceManager.getInstance().maxLevel);
					
					ActivityItem ai=new ActivityItem();
					ai.setActivityId(id);
					ai.setTitle(title);
					ai.setTime(time);
					ai.setStartMapName(startMapName);
					ai.setStartX(startX);
					ai.setStartY(startY);
					ai.setNeedPathfinding(needPathfinding);
					ai.setTextColor(textColor);
					ai.setDetail(detail);
					ai.setWeight(weight);
					ai.setCamp(camp);
					ai.setMinimalLevelLimit(minimalLevelLimit);
					ai.setMaximalLevelLimit(maximalLevelLimit);
					
					this.activities[i].add(ai);
				}
			}
		}finally{
			if(fis!=null){
				fis.close();
			}
		}
	}
	
	public ActivityItem getById(long id){
		for(int i=0;i<this.activities.length;i++){
			for(int j=0;j<this.activities[i].size();j++){
				if(this.activities[i].get(j).getActivityId()==id){
					return this.activities[i].get(j);
				}
			}
		}
		return null;
	}
	
	private void sort(ArrayList<ActivityItem> al){
		Collections.sort(al, new Comparator<ActivityItem>(){
			public int compare(ActivityItem o1, ActivityItem o2) {
				// TODO Auto-generated method stub
				if(o1.getWeight()>o2.getWeight()){
					return -1;
				}else if(o1.getWeight()<o2.getWeight()){
					return 1;
				}
				return 0;
			}
			
		});
	}
	
	private boolean containsId(long id){
		for(int i=0;i<this.activities.length;i++){
			for(int j=0;j<this.activities[i].size();j++){
				if(this.activities[i].get(j).getActivityId()==id){
					return true;
				}
			}
		}
		return false;
	}
	
	public void creatActivityItem(int week, String title,String time,boolean needPathfinding, String startMapName,int startX,int startY,String detail,int weight,int color,int minlevel,int maxlevel,int camp){
		try{
			int index=0;
			long id=0;
			while(true){
				id=this.getRandomId();
				if(!this.containsId(id)){
					break;
				}
				index++;
				if(index>100){
//					log.warn("[增加运营活动] [存储ID出错] [活动信息："+title+" "+time+"]");
					if(log.isWarnEnabled())
						log.warn("[增加运营活动] [存储ID出错] [活动信息：{} {}]", new Object[]{title,time});
					return;
				}
			}
			ActivityItem ai=new ActivityItem();
			ai.setActivityId(id);
			ai.setTitle(title);
			ai.setTime(time);
			ai.setNeedPathfinding(needPathfinding);
			ai.setStartMapName(startMapName);
			ai.setStartX(startX);
			ai.setStartY(startY);
			ai.setDetail(detail);
			ai.setWeight(weight);
			ai.setTextColor(color);
			ai.setMaximalLevelLimit(maxlevel);
			ai.setMinimalLevelLimit(minlevel);
			ai.setCamp(camp);
			this.activities[week].add(ai);
			this.sort(this.activities[week]);
			this.saveActivities();
			if(log.isInfoEnabled()){
//				log.info("[增加运营活动] [成功] [ID="+id+"] [活动："+title+"] [星期"+this.WEEK[week]+"]");
				if(log.isInfoEnabled())
					log.info("[增加运营活动] [成功] [ID={}] [活动：{}] [星期{}]", new Object[]{id,title,this.WEEK[week]});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(log.isInfoEnabled()){
//				log.info("[增加运营活动] [错误] [活动："+title+"] [星期"+this.WEEK[week]+"]");
				if(log.isInfoEnabled())
					log.info("[增加运营活动] [错误] [活动：{}] [星期{}]", new Object[]{title,this.WEEK[week]});
			}
		}
	}
	
	public boolean editeActivityItem(int week, long id, String title,String time,boolean needPathfinding, String startMapName,int startX,int startY,String detail,int weight,int color,int minlevel,int maxlevel,int camp){
		try{
			ActivityItem ai=this.getById(id);
			if(ai!=null){
				ai.setActivityId(id);
				ai.setTitle(title);
				ai.setTime(time);
				ai.setNeedPathfinding(needPathfinding);
				ai.setStartMapName(startMapName);
				ai.setStartX(startX);
				ai.setStartY(startY);
				ai.setDetail(detail);
				ai.setWeight(weight);
				ai.setTextColor(color);
				ai.setMaximalLevelLimit(maxlevel);
				ai.setMinimalLevelLimit(minlevel);
				ai.setCamp(camp);
				this.sort(this.activities[week]);
				this.saveActivities();
				if(log.isInfoEnabled()){
//					log.info("[编辑运营活动] [成功] [ID="+id+"] [活动："+title+"] [星期"+this.WEEK[week]+"]");
					if(log.isInfoEnabled())
						log.info("[编辑运营活动] [成功] [ID={}] [活动：{}] [星期{}]", new Object[]{id,title,this.WEEK[week]});
				}
				return true;
			}else{
//				log.warn("[编辑运营活动] [失败] [没有这个活动] [ID="+id+"] [活动："+title+"]");
				if(log.isWarnEnabled())
					log.warn("[编辑运营活动] [失败] [没有这个活动] [ID={}] [活动：{}]", new Object[]{id,title});
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("[编辑运营活动] [错误] [ID="+id+"] [活动："+title+"] [错误："+e+"]",e);
		}
		return false;
	}
	
	public boolean removeActivityItem(long id){
		try{
			if(log.isInfoEnabled()){
//				log.info("[尝试删除运营活动] [ID="+id+"]");
				if(log.isInfoEnabled())
					log.info("[尝试删除运营活动] [ID={}]", new Object[]{id});
			}
			for(int i=0;i<this.activities.length;i++){
				for(int j=0;j<this.activities[i].size();j++){
					if(this.activities[i].get(j).getActivityId()==id){
						this.activities[i].remove(j);
						this.saveActivities();
						if(log.isInfoEnabled()){
//							log.info("[删除运营活动] [成功] [ID="+id+"]");
							if(log.isInfoEnabled())
								log.info("[删除运营活动] [成功] [ID={}]", new Object[]{id});
						}
						return true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("[删除运营活动] [错误] [ID="+id+"] [错误："+e+"]",e);
		}
		return false;
	}

	public File getChristmasActivityFile() {
		return christmasActivityFile;
	}

	public void setChristmasActivityFile(File christmasActivityFile) {
		this.christmasActivityFile = christmasActivityFile;
	}
	
	/**
	 * 得到领取圣诞奖励记录
	 * @param playerId
	 * @return 0：3小时礼物，1：2小时礼物，2：1小时礼物
	 */
	public long[] getTakeChristmasGiftRecord(long playerId){
		String s=(String)this.ddc.get(playerId);
		if(s!=null){
			String[] ss=s.split(":");
			long[] l=new long[ss.length];
			for(int i=0;i<ss.length;i++){
				l[i]=Long.parseLong(ss[i]);
			}
			return l;
		}else{
			return new long[]{0,0,0};
		}
	}

	public void putTakeChristmasGiftRecord(long playerId, long lastTakeThreeHoursGiftTime,long lastTakeTwoHoursGiftTime,long lastTakeOneHourGiftTime){
		this.ddc.put(playerId, lastTakeThreeHoursGiftTime+":"+lastTakeTwoHoursGiftTime+":"+lastTakeOneHourGiftTime);
	}
	
	public void putTakeNewYearGiftRecord(long playerId, long lastTakeThreeHoursGiftTime,long lastTakeTwoHoursGiftTime,long lastTakeOnlineGiftTime){
		this.newYearddc.put(playerId, lastTakeThreeHoursGiftTime+":"+lastTakeTwoHoursGiftTime+":"+lastTakeOnlineGiftTime);
	}
	
	/**
	 * 得到领取新年奖励记录
	 * @param playerId
	 * @return 0：2小时礼物，1：1小时礼物，2：上线礼物
	 */
	public long[] getTakeNewYearGiftRecord(long playerId){
		String s=(String)this.newYearddc.get(playerId);
		if(s!=null){
			String[] ss=s.split(":");
			long[] l=new long[ss.length];
			for(int i=0;i<ss.length;i++){
				l[i]=Long.parseLong(ss[i]);
			}
			return l;
		}else{
			return new long[]{0,0,0};
		}
	}

	public void run() {}

	public File getNewYearActivityFile() {
		return newYearActivityFile;
	}

	public void setNewYearActivityFile(File newYearActivityFile) {
		this.newYearActivityFile = newYearActivityFile;
	}
}
