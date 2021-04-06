package com.fy.engineserver.operating.activitybuff;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chat.ChatMessageTask;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.XmlUtil;


public class ActivityFlushBossItemManager implements Runnable{
//    private  static Logger log = Logger.getLogger(ActivityFlushBossItemManager.class);
public	static Logger log = LoggerFactory.getLogger(ActivityFlushBossItemManager.class);
	static ActivityFlushBossItemManager self;
	
	public static ActivityFlushBossItemManager getInstance(){
		return self;
	}
	
	private LinkedHashMap<Integer,ActivityFlushBossItem> map = new  LinkedHashMap<Integer,ActivityFlushBossItem>();
	
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
		
		thread = new Thread(this,Translate.text_5551);
		thread.start();
		
		self = this;
//		log.info("ActivityFlushBossItemManager init success ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-old)+"]");
		if(log.isInfoEnabled())
			log.info("ActivityFlushBossItemManager init success [{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-old)});
	}
	
	public boolean isExists(int id){
		return getActivityFlushBossItem(id) != null;
	}
	
	/**
	 * 创建一个新的ActivityBuffItem，id可以采用随机id。
	 * 
	 * @param id
	 * @param name
	 * @param descp
	 * @return
	 */
	public synchronized ActivityFlushBossItem createActivityBuffItem(String desp){
		int id = (int)(Math.random() * Integer.MAX_VALUE);
		while(isExists(id)){
			id = (int)(Math.random() * Integer.MAX_VALUE);
		}
	
		ActivityFlushBossItem item = new ActivityFlushBossItem();
		item.setId(id);
		item.setDesp(desp);
		map.put(id, item);
//		log.info("add success |id:"+id+" |descp: "+desp);
		if(log.isInfoEnabled())
			log.info("add success |id:{} |descp: {}", new Object[]{id,desp});
		return item;
	}
	
	
	public void deleteActivityFlushBossItem(int id){
		map.remove(id);
//		log.info("delete success id:"+id);
		if(log.isInfoEnabled())
			log.info("delete success id:{}", new Object[]{id});
	}
	
	public ActivityFlushBossItem getActivityFlushBossItem(int id){
		return map.get(id);
	}
	
	public ActivityFlushBossItem[] getActivityFlushBossItems(){
		return map.values().toArray(new ActivityFlushBossItem[0]);
	}
//	
//	/**
//	 * 得到BOSS的spriteType
//	 * @param mapName
//	 * @return
//	 */
//	public Byte[] getBossSpriteType(String mapName){
//		ActivityFlushBossItem[] bitems = this.getActivityFlushBossItems();
//		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
//		MemoryNPCManager mnm = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
//		
//		ArrayList<Byte> al = new ArrayList<Byte>();
//		for(int i = 0 ; i < bitems.length ; i++){
//			ActivityFlushBossItem b = bitems[i];
//			if(b.getMapName().equals(mapName)){
//				if(b.isMonsterFlag()){
//					MonsterTempalte t = mmm.getMonsterTempalteByCategoryId(b.bossId);
//					if (t != null && t.monster != null && t.monster.getAnimationType() == 0) {
////						al.add(t.monster.getType());
//					}
//					
//					if(t != null && t.monster instanceof BossMonster){
//						BossMonster bm = (BossMonster)t.monster;
//						
//						bm.init();
//						
//						BossExecuteItem items[] = bm.getBossExecuteItems();
//						for(int j = 0 ; items != null && j < items.length ; j++){
//							BossExecuteItem ei = items[j];
//							BossAction ba = null; 
//							if(ei != null)
//								ba = ei.getAction();
//							if(ba != null && ba instanceof BossCloseDoor){
//								BossCloseDoor bcd = (BossCloseDoor)ba;
//								int doorIds[] = bcd.getDoorNpcCategoryId();
//								for(int k = 0 ; doorIds != null && k < doorIds.length ; k++){
//									NPCTempalte nt = mnm.getNPCTempalteByCategoryId(doorIds[k]);
//									if(nt != null && nt.npc != null && nt.npc.getAnimationType() == 0){
//										al.add(nt.npc.getType());
//									}
//								}
//							}else if(ba != null && ba instanceof FlushMonster){
//								FlushMonster fm = (FlushMonster)ba;
//								int monsterIds[] = fm.getMonsterCategoryId();
//								for(int k = 0 ; monsterIds != null && k < monsterIds.length ; k++){
//									MonsterTempalte tt = mmm.getMonsterTempalteByCategoryId(monsterIds[k]);
//									// 0表示普通动画 1表示avatar动画
//									if (tt != null && tt.monster != null && tt.monster.getAnimationType() == 0) {
//										al.add(tt.monster.getType());
//									}
//								}
//							}else if(ba != null && ba instanceof BossFlushCaijiNpc){
//								BossFlushCaijiNpc bcd = (BossFlushCaijiNpc)ba;
//								int doorIds[] = bcd.getCaijiNpcCategoryId();
//								for(int k = 0 ; doorIds != null && k < doorIds.length ; k++){
//									NPCTempalte nt = mnm.getNPCTempalteByCategoryId(doorIds[k]);
//									if(nt != null && nt.npc != null && nt.npc.getAnimationType() == 0){
//										al.add(nt.npc.getType());
//									}
//								}
//							}
//						}
//					}
//				}else{ //npc
//					NPCTempalte t = mnm.getNPCTempalteByCategoryId(b.bossId);
//					if (t != null && t.npc != null && t.npc.getAnimationType() == 0) {
//						al.add(t.npc.getType());
//					}
//				}
//			}
//		}
//		return (Byte[])al.toArray(new Byte[0]);
//	}
	
	protected void load(File file) throws Exception{
		Document dom = XmlUtil.load(file.getAbsolutePath());
		Element root = dom.getDocumentElement();
		Element eles[] = XmlUtil.getChildrenByName(root, "activeityboss");
		
		LinkedHashMap<Integer,ActivityFlushBossItem> map2 = new  LinkedHashMap<Integer,ActivityFlushBossItem>();
		
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			ActivityFlushBossItem item = new ActivityFlushBossItem();
			item.id = XmlUtil.getAttributeAsInteger(e, "id");
			item.setEndTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "endTimeInDay"),"23:59", TransferLanguage.getMap()));
			item.setStartTimeInDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "startTimeInDay"),"00:00", TransferLanguage.getMap()));
			
			item.setTimeType(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "timeType"),0));
			item.setWeekDay(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "weekDay"),0));
			
			item.setFixDay(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "fixDay"),"2010-05-05", TransferLanguage.getMap()));
			item.setBetweenTime(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "betweenTime"),"2010-04-29-10-10~2010-06-29-10-10", TransferLanguage.getMap()));
			item.setIntervalTimeForBetweenTime(XmlUtil.getValueAsLong(XmlUtil.getChildByName(e, "intervalTimeForBetweenTime"),60));
			
			item.setBossId(XmlUtil.getValueAsInteger(XmlUtil.getChildByName(e, "bossid"),0));
			

			item.setMapName(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "mapname"), TransferLanguage.getMap()));
			item.setDesp(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "desp"),"", TransferLanguage.getMap()));
			item.setMonsterFlag(XmlUtil.getValueAsBoolean(XmlUtil.getChildByName(e, "monsterflag"),true));
			
			//二维数组赋值，格式0,0,0|0,0,0 转换为 a{{0,0,0},{0,0,0}}
//			String xs[] = XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "x"),"0").split(",");
//			String ys[] = XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "y"),"0").split(",");
//			int xx[] = new int[xs.length];
//			int yy[] = new int[ys.length];
//			for(int j = 0 ; j < xs.length ; j++){
//				xx[j] = Integer.parseInt(xs[j]);
//			}
//			for(int j = 0 ; j < ys.length ; j++){
//				yy[j] = Integer.parseInt(ys[j]);
//			}
			
			int xxx[][] = new int[][]{{0}};
			int yyy[][] = new int[][]{{0}};
			try{
				//0,0,0|0,0,0
				String xssTemp[] = XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "x"),"0", TransferLanguage.getMap()).split("\\|");
				String yssTemp[] = XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "y"),"0", TransferLanguage.getMap()).split("\\|");
				xxx = new int[xssTemp.length][];
				yyy = new int[yssTemp.length][];
				for(int j = 0; j < xssTemp.length; j++){
					String[] xsTemp = xssTemp[j].split(",");
					xxx[j] = new int[xsTemp.length];
					for(int k = 0; k < xsTemp.length; k++){
						xxx[j][k] = Integer.parseInt(xsTemp[k]);
					}
				}
				
				for(int j = 0; j < yssTemp.length; j++){
					String[] ysTemp = yssTemp[j].split(",");
					yyy[j] = new int[ysTemp.length];
					for(int k = 0; k < ysTemp.length; k++){
						yyy[j][k] = Integer.parseInt(ysTemp[k]);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				if(log.isWarnEnabled())
					log.warn("boss刷新位置设置出错",ex);
				xxx = new int[][]{{0}};
				yyy = new int[][]{{0}};
			}
			item.setX(xxx);
			item.setY(yyy);
			
			item.setSayContentToWorld(XmlUtil.getValueAsString(XmlUtil.getChildByName(e, "sayContentToWorld"),"", TransferLanguage.getMap()));
			item.lastFlushTime = XmlUtil.getValueAsLong(XmlUtil.getChildByName(e, "lastflush"));
			
			map2.put(item.getId(),item);
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
		sb.append("<activeityboss>\n");
		ActivityFlushBossItem[] items = this.getActivityFlushBossItems();
		
		for(int i = 0 ; i < items.length ; i++){
			ActivityFlushBossItem e = items[i];
			sb.append("<activeityboss id='"+e.getId()+"'>\n");
			sb.append("<lastflush>"+e.lastFlushTime+"</lastflush>\n");
			sb.append("<bossid>"+e.bossId+"</bossid>\n");
			sb.append("<monsterflag>"+e.monsterFlag+"</monsterflag>\n");
			sb.append("<mapname>"+e.mapName+"</mapname>\n");
			sb.append("<sayContentToWorld>"+e.sayContentToWorld+"</sayContentToWorld>\n");
			sb.append("<x>"+getArrayString(e.x)+"</x>\n");
			sb.append("<y>"+getArrayString(e.y)+"</y>\n");
			sb.append("<desp>"+e.desp+"</desp>\n");
			
			sb.append("<startTimeInDay>"+e.getStartTimeInDay()+"</startTimeInDay>\n");
			sb.append("<endTimeInDay>"+e.getEndTimeInDay()+"</endTimeInDay>\n");
			sb.append("<timeType>"+e.getTimeType()+"</timeType>\n");
			sb.append("<weekDay>"+e.getWeekDay()+"</weekDay>\n");
			sb.append("<fixDay>"+e.getFixDay()+"</fixDay>\n");
			sb.append("<betweenTime>"+e.getBetweenTime()+"</betweenTime>\n");
			sb.append("<intervalTimeForBetweenTime>"+e.getIntervalTimeForBetweenTime()+"</intervalTimeForBetweenTime>\n");

			sb.append("</activeityboss>\n");
		}
		sb.append("</activeityboss>\n");
		FileUtils.chkFolder(configFile.getAbsolutePath());
		FileOutputStream output = new FileOutputStream(configFile);
		output.write(sb.toString().getBytes());
		output.close();
//		log.info(" 保存活动Boss success 长度为"+items.length);
		if(log.isInfoEnabled())
			log.info(" 保存活动Boss success 长度为{}", new Object[]{items.length});
	}

	public static String getArrayString(int x[][]){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < x.length ; i++){
			int[] xx = x[i];
			for(int j = 0; j < xx.length; j++){
				sb.append(xx[j]);
				if(j < xx.length -1){
					sb.append(",");
				}
			}
			if(i < x.length -1){
				sb.append("|");
			}
		}
		return sb.toString();
	}
	public static void main(String args[]){
//		System.out.println(ActivityFlushBossItemManager.getArrayString(new int[][]{{0},{0}}));
		String a = "0,0,0|1,1";
//		System.out.println((a.split("\\|")).length);
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
		GameManager gm = GameManager.getInstance();
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		if (mmm == null || gm == null) return;
		
		ActivityFlushBossItem[] items = this.getActivityFlushBossItems();
		
		for(int i = 0 ;i < items.length ; i++){
			ActivityFlushBossItem b = items[i];
			
			Game game = gm.getGameByName(b.mapName,CountryManager.中立);
			
			if(b.isActive(now) && game != null && (b.sprite == null || b.sprite.isAlive() == false) ){
				//从没有刷新过
				if(b.lastFlushTime == 0){
					b.lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					try {
						this.saveAll();
					} catch (Exception e) {
						if(log.isWarnEnabled())
							log.warn("[定时刷精灵] [保存] [失败]",e);
					}
					flush(game,b);
				}else{
					if(b.getTimeType() == ActivityFlushBossItem.TIME_TYPE_EVERYDAY){
						if(b.isActive(b.lastFlushTime) == false){
							b.lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
							try {
								this.saveAll();
							} catch (Exception e) {
								if(log.isWarnEnabled())
									log.warn("[定时刷精灵] [保存] [失败]",e);
							}
							flush(game,b);
						}
					}else if(b.getTimeType() == ActivityFlushBossItem.TIME_TYPE_EVERYWEEKDAY){
						if(b.isActive(b.lastFlushTime) == false){
							b.lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
							try {
								this.saveAll();
							} catch (Exception e) {
								if(log.isWarnEnabled())
									log.warn("[定时刷精灵] [保存] [失败]",e);
							}
							flush(game,b);
						}
					}else if(b.getTimeType() == ActivityFlushBossItem.TIME_TYPE_BETWEEN_TIME){
						if((b.lastFlushTime+b.getIntervalTimeForBetweenTime()*1000) < now){
							b.lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
							try {
								this.saveAll();
							} catch (Exception e) {
								if(log.isWarnEnabled())
									log.warn("[定时刷精灵] [保存] [失败]",e);
							}
							flush(game,b);
						}
					}
				}
			}else if(b.isActive(now) && game != null && b.sprite != null){
				if(b.broadcastTimes > 0){
					if(now - b.lastBroadcastTime > 10000){
						b.lastBroadcastTime = now;
						b.broadcastTimes --;
						
						if(b.sayContentToWorld != null && b.sayContentToWorld.trim().length() > 0){
							 ChatMessageService cm = ChatMessageService.getInstance();
						     ChatMessage msg = new ChatMessage();
						     msg.setSort(ChatChannelType.SYSTEM);
						     
						     msg.setMessageText(b.sayContentToWorld.trim());
						   
						     msg.setAccessoryItem(new ChatMessageItem());
						     msg.setAccessoryTask(new ChatMessageTask());
						     msg.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						     try {
								cm.sendMessageToSystem(msg);
							} catch (Exception e) {
								if(log.isInfoEnabled())
									log.info("[刷出精灵] [世界喊话错误]",e);
							}
						}
					}
				}
			}
		}
	}
	
	private void flush(Game game,ActivityFlushBossItem b){
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		MemoryNPCManager mnm = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
		Random random = new Random();
		int index = random.nextInt(b.x.length);
		int[] xx = b.x[index];
		int[] yy = b.y[index];
		for(int i = 0 ; i < xx.length ; i++){
			Sprite sprite = null;
			if(b.isMonsterFlag()){
				sprite = mmm.createMonster(b.bossId);
			}else{
				sprite = mnm.createNPC(b.bossId);
			}
			
			if(sprite != null){
				sprite.setAlive(true);
				sprite.setX(xx[i]);
				sprite.setY(yy[i]);
				sprite.setBornPoint(new Point(xx[i],yy[i]));
				
				game.addSprite(sprite);
				
				b.sprite = sprite;
			}
			if(sprite != null)
//				log.info("[刷出精灵] [成功] ["+sprite.getName()+"] ["+game.getGameInfo().getName()+"] ["+sprite.getX()+"] ["+sprite.getY()+"]");
				if(log.isInfoEnabled())
					log.info("[刷出精灵] [成功] [{}] [{}] [{}] [{}]", new Object[]{sprite.getName(),game.getGameInfo().getName(),sprite.getX(),sprite.getY()});
			else
//				log.info("[刷出精灵] [失败] [配置错误："+b.bossId+","+b.isMonsterFlag()+"] ["+game.getGameInfo().getName()+"] [--] [--]");
				if(log.isInfoEnabled())
					log.info("[刷出精灵] [失败] [配置错误：{},{}] [{}] [--] [--]", new Object[]{b.bossId,b.isMonsterFlag(),game.getGameInfo().getName()});
		}
		
		b.broadcastTimes = 3;
		b.lastBroadcastTime = 0;
		
		
		
	}
	
}
