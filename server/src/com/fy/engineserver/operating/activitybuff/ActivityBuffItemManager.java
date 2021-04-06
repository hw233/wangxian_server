package com.fy.engineserver.operating.activitybuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.XmlUtil;


public class ActivityBuffItemManager implements PlayerInOutGameListener,Runnable{
//    protected  static Logger log = Logger.getLogger(ActivityBuffItemManager.class);
public	static Logger log = LoggerFactory.getLogger(ActivityBuffItemManager.class);
	static ActivityBuffItemManager self;
	
	public static ActivityBuffItemManager getInstance(){
		return self;
	}
	
	private HashMap<Integer,ActivityBuffItem> map = new  HashMap<Integer,ActivityBuffItem>();
	
	File configFile;
	
	protected Thread thread;
	
	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception{
		long old = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(configFile != null && configFile.exists()){
			load(configFile);
		}
		
		thread = new Thread(this,Translate.text_5548);
		thread.start();
		
		self = this;
//		log.info("ActivityBuffItemManager init success ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-old)+"]");
		if(log.isInfoEnabled())
			log.info("ActivityBuffItemManager init success [{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-old)});
	}
	
	public boolean isExists(int id){
		return getActivityBuffItem(id) != null;
	}
	
	/**
	 * 创建一个新的ActivityBuffItem，id可以采用随机id。
	 * 
	 * @param id
	 * @param name
	 * @param descp
	 * @return
	 */
	public synchronized ActivityBuffItem createActivityBuffItem(String name,String descp){
		int id = (int)(Math.random() * Integer.MAX_VALUE);
		while(isExists(id)){
			id = (int)(Math.random() * Integer.MAX_VALUE);
		}
	
		ActivityBuffItem item = new ActivityBuffItem();
		item.setBuffName(name+Translate.text_5549);
		item.setId(id);
		map.put(id, item);
//		log.info("add success name:"+name+" |id:"+id+" |descp: "+descp);
		if(log.isInfoEnabled())
			log.info("add success name:{} |id:{} |descp: {}", new Object[]{name,id,descp});
		return item;
	}
	

	
	public void deleteActivityBuffItem(int id){
		map.remove(id);
//		log.info("delete success id:"+id);
		if(log.isInfoEnabled())
			log.info("delete success id:{}", new Object[]{id});
	}
	
	public ActivityBuffItem getActivityBuffItem(int id){
		return map.get(id);
	}
	
	public ActivityBuffItem[] getActivityBuffItems(){
		return map.values().toArray(new ActivityBuffItem[0]);
	}
	
	protected void load(File file) throws Exception{
		Document dom = null;
//		if(TransferLanguage.characterTransferormFlag){
			InputStream is = new FileInputStream(file);
			dom = XmlUtil.load(is,"utf8");
//		}else{
//			dom = XmlUtil.load(file.getAbsolutePath());
//		}
		Element root = dom.getDocumentElement();
		Element eles[] = XmlUtil.getChildrenByName(root, "activeitybuff");
		
		HashMap<Integer,ActivityBuffItem> map2 = new  HashMap<Integer,ActivityBuffItem>();
		
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			ActivityBuffItem item = new ActivityBuffItem();
			item.id = XmlUtil.getAttributeAsInteger(e, "id");
			item.setBuffName(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "buffName"),"", null));
			item.setBuffLevel(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "buffName"),0));
			item.setEnableMapLimit(XmlUtil.getValueAsBoolean(XmlUtil.getChildByName(e, "enableMapLimit"), false));
			item.setMapLimit(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "mapLimit"),"", null));
			item.setEndTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "endTimeInDay"),"23:59", null));
			item.setStartTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "startTimeInDay"),"00:00", null));
			
			item.setTimeType(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "timeType"),0));
			item.setWeekDay(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "weekDay"),0));
			item.setPlayerPoliticalCampLimit(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "playerPoliticalCampLimit"),0));
			item.setPlayerLevelLimit(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "playerLevelLimit"),0));
			
			item.setFixDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "fixDay"),"2010-05-05", null));
			item.setFixDayRange(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "fixDayRange"),"20100505~20100606", null));
			
			map2.put(item.getId(),item);
		}
		Element eless[] = XmlUtil.getChildrenByName(root, "activeitybuffbangpai");
		for(int i = 0 ; i < eless.length ; i++){
//			Element e = eless[i];
//			ActivityBuffBangPaiItem item = new ActivityBuffBangPaiItem();
//			item.id = XmlUtil.getAttributeAsInteger(e, "id");
//			item.bangPaiId = XmlUtil.getAttributeAsLong(e, "bangPaiId", -1);
//			item.setBuffName(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "buffName"),"", null));
//			item.setBuffLevel(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "buffName"),0));
//			item.setEnableMapLimit(XmlUtil.getValueAsBoolean(XmlUtil.getChildByName(e, "enableMapLimit"), false));
//			item.setMapLimit(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "mapLimit"),"", null));
//			item.setEndTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "endTimeInDay"),"23:59", null));
//			item.setStartTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "startTimeInDay"),"00:00", null));
//			
//			item.setTimeType(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "timeType"),0));
//			item.setWeekDay(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "weekDay"),0));
//			item.setPlayerPoliticalCampLimit(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "playerPoliticalCampLimit"),0));
//			item.setPlayerLevelLimit(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "playerLevelLimit"),0));
//			
//			item.setFixDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "fixDay"),"2010-05-05", null));
//			item.setFixDayRange(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "fixDayRange"),"20100505~20100606", null));
//			
//			map2.put(item.getId(),item);
		}
		map = map2;
	}
	/**
	 * 存盘
	 * 
	 * <activeitybuffs>
	 * 		<activeitybuff>
	 * 			<buffName></buffName>
	 * 			<buffLevel></buffLevel>
	 *  		<enableMapLimit></enableMapLimit>
	 * 			<mapLimit></mapLimit>
	 * 			<startTimeInDay></startTimeInDay>
	 *  		<endTimeInDay></endTimeInDay>
	 *  		<timeType></timeType>
	 * 			<weekDay></weekDay>
	 *  		<fixDay></fixDay>
	 *  		<playerPoliticalCampLimit></playerPoliticalCampLimit>
	 * 			<playerLevelLimit></playerLevelLimit>
	 * 		</activeitybuff>
	 * </activeitybuffs>
	 */
	public void saveAll() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='gb2312'?>\n");
		sb.append("<activeitybuffs>\n");
		ActivityBuffItem[] items = getActivityBuffItems();
		
		for(int i = 0 ; i < items.length ; i++){
			ActivityBuffItem e = items[i];
//			if(e instanceof ActivityBuffBangPaiItem){
//				sb.append("<activeitybuffbangpai id='"+e.getId()+"' bangPaiId='"+((ActivityBuffBangPaiItem)e).getBangPaiId()+"'>\n");
//				sb.append("<buffName>"+e.getBuffName()+"</buffName>\n");
//				sb.append("<buffLevel>"+e.getBuffLevel()+"</buffLevel>\n");
//				sb.append("<enableMapLimit>"+e.isEnableMapLimit()+"</enableMapLimit>\n");
//				sb.append("<mapLimit>"+e.getMapLimit()+"</mapLimit>\n");
//				sb.append("<startTimeInDay>"+e.getStartTimeInDay()+"</startTimeInDay>\n");
//				sb.append("<endTimeInDay>"+e.getEndTimeInDay()+"</endTimeInDay>\n");
//				sb.append("<timeType>"+e.getTimeType()+"</timeType>\n");
//				sb.append("<weekDay>"+e.getWeekDay()+"</weekDay>\n");
//				sb.append("<fixDay>"+e.getFixDay()+"</fixDay>\n");
//				sb.append("<fixDayRange>"+e.getFixDayRange()+"</fixDayRange>\n");
//				sb.append("<playerPoliticalCampLimit>"+e.getPlayerPoliticalCampLimit()+"</playerPoliticalCampLimit>\n");
//				sb.append("<playerLevelLimit>"+e.getPlayerLevelLimit()+"</playerLevelLimit>\n");
//				sb.append("</activeitybuffbangpai>\n");
//			}else{
				sb.append("<activeitybuff id='"+e.getId()+"'>\n");
				sb.append("<buffName>"+e.getBuffName()+"</buffName>\n");
				sb.append("<buffLevel>"+e.getBuffLevel()+"</buffLevel>\n");
				sb.append("<enableMapLimit>"+e.isEnableMapLimit()+"</enableMapLimit>\n");
				sb.append("<mapLimit>"+e.getMapLimit()+"</mapLimit>\n");
				sb.append("<startTimeInDay>"+e.getStartTimeInDay()+"</startTimeInDay>\n");
				sb.append("<endTimeInDay>"+e.getEndTimeInDay()+"</endTimeInDay>\n");
				sb.append("<timeType>"+e.getTimeType()+"</timeType>\n");
				sb.append("<weekDay>"+e.getWeekDay()+"</weekDay>\n");
				sb.append("<fixDay>"+e.getFixDay()+"</fixDay>\n");
				sb.append("<fixDayRange>"+e.getFixDayRange()+"</fixDayRange>\n");
				sb.append("<playerPoliticalCampLimit>"+e.getPlayerPoliticalCampLimit()+"</playerPoliticalCampLimit>\n");
				sb.append("<playerLevelLimit>"+e.getPlayerLevelLimit()+"</playerLevelLimit>\n");
				sb.append("</activeitybuff>\n");
//			}
		}
		sb.append("</activeitybuffs>\n");
		FileUtils.chkFolder(configFile.getAbsolutePath());
		FileOutputStream output = new FileOutputStream(configFile);
//		if(TransferLanguage.characterTransferormFlag){
			output.write(sb.toString().getBytes("utf-8"));
//		}else{
//			output.write(sb.toString().getBytes());
//		}
		
		output.close();
//		log.info(" 保存任务buff success 长度为"+items.length);
		if(log.isInfoEnabled())
			log.info(" 保存任务buff success 长度为{}", new Object[]{items.length});
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	// 以下方法监听用户进入和退出游戏场景
	
	public void run(){
		int step = 5000;
		
		while(Thread.currentThread().isInterrupted() == false){
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			try{
				
				heartbeat();
				
				long ll = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if(ll - now < step){
					Thread.sleep(step - (ll - now));
				}
				
			}catch(Throwable e){
				e.printStackTrace(System.out);
			}
		}
	}
	
	protected synchronized void heartbeat(){
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance() ;
		if (pm == null || btm == null) return;
		
		Player players[] = PlayerManager.getInstance().getOnlinePlayers();
		
		
		ActivityBuffItem[] items = getActivityBuffItems();
		
		for(int i = 0 ;i < items.length ; i++){
			ActivityBuffItem b = items[i];
			String dayStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");

			Date endTime = DateUtil.parseDate(dayStr+" " + b.endTimeInDay,"yyyy-MM-dd HH:mm");
			if(b.getTimeType() == ActivityBuffItem.TIME_TYPE_FIXDAY_RANGE){
				if(b.getFixDayRange() != null){
					String[] dayStrs = b.getFixDayRange().split("~");
					if(dayStrs.length != 2){
						dayStrs = b.getFixDayRange().split("~");
					}
					if(dayStrs.length == 2){
						try{
							endTime = DateUtil.parseDate(dayStrs[1]+" " + b.endTimeInDay,"yyyyMMdd HH:mm");
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}

			BuffTemplate bt = btm.getBuffTemplateByName(b.getBuffName());
			
			if(bt != null && b.isActive(now)){
				int level = b.getBuffLevel();
				if(level >= 1) level = level -1;
				
				
				for(int j = 0 ; j < players.length ; j++){
					if(b.isAvaiableForPlayer(players[j])){
						Buff buff = players[j].getBuff(bt.getId());
						if(buff == null){
							buff = bt.createBuff(level);
							buff.setBufferType(bt.getBuffType());
							buff.setFightStop(bt.isFightStop());
							buff.setGroupId(bt.getGroupId());
							buff.setCauser(players[j]);
							buff.setIconId(bt.getIconId());
							buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							buff.setSyncWithClient(true);
							buff.setInvalidTime(endTime.getTime());
							buff.setNotSaveFlag(true);
							
							players[j].placeBuff(buff);
						}
					}
				}
				
			}
		}
	}
	
	public synchronized void enterGame(Game game, Player player) {
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		ActivityBuffItem[] items = getActivityBuffItems();
		for(int i = 0 ;i < items.length ; i++){
			ActivityBuffItem b = items[i];
			String dayStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			Date endTime = DateUtil.parseDate(dayStr+" " + b.endTimeInDay,"yyyy-MM-dd HH:mm");
			if(b.getTimeType() == ActivityBuffItem.TIME_TYPE_FIXDAY_RANGE){
				if(b.getFixDayRange() != null){
					String[] dayStrs = b.getFixDayRange().split("~");
					if(dayStrs.length != 2){
						dayStrs = b.getFixDayRange().split("~");
					}
					if(dayStrs.length == 2){
						try{
							endTime = DateUtil.parseDate(dayStrs[1]+" " + b.endTimeInDay,"yyyyMMdd HH:mm");
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}

			BuffTemplate bt = btm.getBuffTemplateByName(b.getBuffName());
			
			if(bt != null && b.isActive(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())){
				int level = b.getBuffLevel();
				if(level >= 1) level = level -1;
				
				if(b.isAvaiableForPlayer(player)){
					Buff buff = player.getBuff(bt.getId());
					if(buff == null){
						buff = bt.createBuff(level);
						buff.setBufferType(bt.getBuffType());
						buff.setFightStop(bt.isFightStop());
						buff.setGroupId(bt.getGroupId());
						buff.setCauser(player);
						buff.setIconId(bt.getIconId());
						buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						buff.setSyncWithClient(true);
						buff.setInvalidTime(endTime.getTime());
						buff.setNotSaveFlag(true);
						
						player.placeBuff(buff);
					}
				}
			}
		}
	}

	public synchronized void leaveGame(Game game, Player player) {
		// TODO Auto-generated method stub
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		ActivityBuffItem[] items = getActivityBuffItems();
		for(int i = 0 ;i < items.length ; i++){
			ActivityBuffItem b = items[i];
			
			BuffTemplate bt = btm.getBuffTemplateByName(b.getBuffName());
			
			if(bt != null && b.isActive(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())){
				if(b.enableMapLimit && b.mapLimit.equals(game.getGameInfo().getName())){
					Buff buff = player.getBuff(bt.getId());
					if(buff != null){
						buff.setInvalidTime(0);
					}
				}
			}
		}
	}
	
}
