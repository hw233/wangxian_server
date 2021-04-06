package com.fy.engineserver.activity.godDown;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.Utils;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;


public class GodDwonManager implements Runnable {

	public static Logger logger = ActivitySubSystem.logger;
	
	private static GodDwonManager self;
	
	public static GodDwonManager getInstance(){
		return self;
	}
	
//	600051		斗战神将
//	600052		幻灵仙子
//	600053		隐匿神君
//	600054		巫毒圣皇
//	600055		节日兑换使者
	public static int[] npcIds = {600051,600052,600053,600054};
	public static int[] countrys = {0,1,2,3};
	
	public List<Integer> npcLists = new ArrayList<Integer>();
	public List<Integer> countryLists = new ArrayList<Integer>();
	
	//默认00时开始，结束
	public static int[] activityBegin = {2012,10,22};
	public static int[] activityEnd = {2012,10,27};
	
	public long beginTime;
	public long endTime;
	
	//开始时间早10分钟，他要广播在哪儿生成
	public static int[][] 指定开始时间 = {{9,50},{14,50},{21,20}};
	public static int[][] 指定结束时间 = {{12,0},{17,0},{23,30}};
	
	public static String gift = Translate.火鸡大餐礼券;
	public Article article;
	
	public String fileName;
	public static int 每天最多领取次数 = 4;
	
	public String diskFile;
	//key 为玩家id
	public DiskCache cache = null;
	
	public static long 十分钟 = 10*60*1000;
	public static long 半小时 = 30*60*1000;
	public static long 五分钟 = 5*60*1000;
	
	public void init()throws Exception{
		
		
		self = this;
		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			return;
		}
		for(int npcId : npcIds){
			boolean hava = false;
			NPCTempalte[] npcs = ((MemoryNPCManager)MemoryNPCManager.getNPCManager()).getNPCTemaplates();
			for(NPCTempalte t : npcs){
				if(t.NPCCategoryId == npcId){
					hava = true;
				}
			}
			if(!hava){
				throw new RuntimeException("[天神下凡配置错误，没对应的npc] ["+npcId+"]");
			}
			npcLists.add(npcId);
		}
		for(int country:countrys){
			countryLists.add(country);
		}
		
		article = ArticleManager.getInstance().getArticle(gift);
		if(article == null){
			throw new RuntimeException("[天神下凡配置错误，没对应的物品] ["+gift+"]");
		}
		loadExcel();
		for(GodDownTemplate gd:gdList){
			boolean isPeace = gd.isPeace;
			String map = gd.map;
			Game game = null;
			if(isPeace){
				game = GameManager.getInstance().getGameByName(map, 0);
			}else{
				game = GameManager.getInstance().getGameByName(map, 1);
			}
			if(game == null){
				System.err.println("[天神下凡配置错误，没对应的game] ["+gd.chinaMap+"] ["+gd.map+"] ["+gd.isPeace+"]");
				continue;
//				throw new RuntimeException("[天神下凡配置错误，没对应的game] ["+gd.chinaMap+"] ["+gd.map+"] ["+gd.isPeace+"]");
			}
		}
		
		
		File file = new File(diskFile);
		cache = new DefaultDiskCache(file,null,"godDown", 100L * 365 * 24 * 3600 * 1000L);
		
		Calendar cal = Calendar.getInstance();
		cal.set(activityBegin[0], activityBegin[1], activityBegin[2]);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		beginTime = cal.getTimeInMillis();
		
		cal.set(activityEnd[0], activityEnd[1], activityEnd[2]);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		endTime = cal.getTimeInMillis();
		
		
		Thread thread = new Thread(this,"天神下凡");
		thread.start();
		
		ServiceStartRecord.startLog(this);
	}
	
	//每次改info数据调用这个
	public void setGodDownInfo(Player player,GodDownInfo info){
		
		this.getCache().put(player.getId(), info);
		logger.warn("[玩家更新diskcache数据] ["+player.getLogString()+"] ["+info.getLogString()+"]");
		
	}
	
	public static boolean running = true;
	
	public GodDownFlushEntity flushEntity;
	
	
	public void flushGodDown(){
		if(flushEntity == null){
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int size = 指定开始时间.length;
			for(int j =0;j<size;j++){
				int[] ints = 指定开始时间[j];
				if(hour == ints[0]){
					int minute = cal.get(Calendar.MINUTE);
					if(minute >= ints[1]){
						if(hour <指定结束时间[j][0]){
							//产生新的实体
							flushEntity = new GodDownFlushEntity(j);
							flushEntity.init();
							return;
						}else if(hour <= 指定结束时间[j][0] && minute < 指定结束时间[j][1]){
							//产生新的实体
							flushEntity = new GodDownFlushEntity(j);
							flushEntity.init();
							return;
						}
					}
				}
			}
		}else{
			flushEntity.heartBeat();
			if(SystemTime.currentTimeMillis() > flushEntity.本次刷新实体的结束时间){
				logger.warn("[本次刷新结束]");
				flushEntity = null;
			}
			
		
		}
	}
	
	@Override
	public void run() {

		while(running){
			
			try {
				Thread.sleep(1000l);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try{
				flushGodDown();
			}catch(Throwable t){
				logger.error("[天神下凡心跳异常]",t);
			}
			
			if(SystemTime.currentTimeMillis() >= endTime){
				break;
			}
		}
		
	}
	
	public String getDiskFile() {
		return diskFile;
	}
	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public DiskCache getCache() {
		return cache;
	}

	public void setCache(DiskCache cache) {
		this.cache = cache;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	Random random = new Random();
	public GodDownTemplate flushTemplate(int country){
		
		List<GodDownTemplate> list = new ArrayList<GodDownTemplate>();
		if(country != 0){
			for(GodDownTemplate t :gdList){
				if(!t.isPeace){
					list.add(t);
				}
			}
		}else{
			for(GodDownTemplate t :gdList){
				if(t.isPeace){
					list.add(t);
				}
			}
		}
		
		int max = list.size();
		int r = random.nextInt(max);
		GodDownTemplate template = list.get(r);
		if(template != null){
			logger.warn("[得到刷新模板] ["+template.chinaMap+"] ["+template.isPeace+"] ["+template.map+"] ["+template.x+"] ["+template.y+"]");
		}else{
			logger.warn("[得到刷新模板 null]");
		}
		return template;
	}
	
	
	public List<GodDownTemplate> gdList = new ArrayList<GodDownTemplate>();
	
	public void loadExcel() throws Exception {

		 File file = new File(fileName);
		 HSSFWorkbook workbook = null;
		
		 InputStream is = new FileInputStream(file);
		 POIFSFileSystem pss = new POIFSFileSystem(is);
		 workbook = new HSSFWorkbook(pss);
		 
		 int 是否中立 = 0;
		 int 区域名 = 1;
		 int 地图名中文 = 2;
		 int 地图名 = 3;
		 int 坐标x = 4;
		 int 坐标y = 5;
		 
		 HSSFSheet sheet = null;
		 sheet = workbook.getSheetAt(0);
		 if (sheet == null) throw new RuntimeException("初始化天神下凡异常，没有表");
		 int rows = sheet.getPhysicalNumberOfRows();
		 HSSFRow row = null;
		 HSSFCell cell = null;
		 boolean isPeace = false;
		 String region = null;
		 String chinaMap = null;
		 String map = null;
		
		 for(int i= 1;i<rows;i++){
			 int x = 0;
			 int y = 0;
			 row = sheet.getRow(i);
			 if(row != null){
				 GodDownTemplate gd = new GodDownTemplate();
				 cell = row.getCell(是否中立);
				 if(cell != null){
					 isPeace = cell.getBooleanCellValue();
				 }
				 
				 cell = row.getCell(区域名);
				 if(cell != null){
					 region = cell.getStringCellValue();
				 }
				
				 cell = row.getCell(地图名中文);
				 if(cell != null){
					 chinaMap = cell.getStringCellValue();
				 }
				 cell = row.getCell(地图名);
				 if(cell != null){
					 map = cell.getStringCellValue();
				 }
				 cell = row.getCell(坐标x);
				 x = (int)cell.getNumericCellValue();
				 cell = row.getCell(坐标y);
				 y = (int)cell.getNumericCellValue();

				 gd.isPeace = isPeace;
				 gd.region = region.trim();
				 gd.chinaMap = chinaMap.trim();
				 gd.map = map.trim();
				 gd.x = x;
				 gd.y = y;
				 
				 gdList.add(gd);
			 }
		 }
	}
	
	//电视通知
	public static void noticeTelevision(String content){
		ChatMessage msg = new ChatMessage();
		StringBuffer sb = new StringBuffer();
		sb.append("<f color=' ");
		sb.append(ArticleManager.COLOR_PURPLE);
		sb.append("'>");
		sb.append(content);
		sb.append("</f>");
		
		msg.setMessageText(sb.toString());
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			cms.sendMessageToSystem(msg);
			logger.warn("[发送电视广播] ["+content+"]");
		} catch (Exception e) {
			logger.error("[发送电视广播异常] ["+content+"]",e);
		}
		
	}
	
	
	public void receiveGodDown(Player player){
		
		if(player.getLevel() >= 40){
			Object object = cache.get(player.getId());
			GodDownInfo info = null;
			if(object != null){
				info = (GodDownInfo) object;
			}else{
				info = new GodDownInfo();
			}
			
			if(flushEntity != null){

				int npcId = flushEntity.nowNpc.getnPCCategoryId();
				long now = SystemTime.currentTimeMillis();
				if(Utils.isSameDay(now, info.getLastReceiveTime())){
					if(info.getReceiveNpcList().contains(npcId)){
//						player.sendError(flushEntity.nowNpc.getName()+"已经领取过了，一天只能领取一次");
						String result = Translate.translateString(Translate.XX已经领取过了一天只能领取一次, new String[][]{{Translate.STRING_1,flushEntity.nowNpc.getName()}});
						player.sendError(result);
						return;
					}
				}else{
					info.receiveNpcList.clear();
				}
				
				if(player.getKnapsack_common().isFull()){
					player.sendError(Translate.背包已满请整理再来);
					return;
				}
				ArticleEntity ae = null;
				try {
					ae = ArticleEntityManager.getInstance().createEntity(article, false, ArticleEntityManager.天神下凡, player, article.getColorType(), 1, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(ae != null){
					info.getReceiveNpcList().add(npcId);
					info.setLastReceiveTime(System.currentTimeMillis());
					
					setGodDownInfo(player, info);
					
					player.sendError(Translate.天神领取成功);
					player.getKnapsack_common().put(ae, "天神下凡");
					logger.error("[领取成功] ["+player.getLogString()+"]");
				}else{
					logger.error("[领取失败] [ae null] ["+player.getLogString()+"]");
				}
			}else{
				player.sendError(Translate.npc刷新这个npc已经消失);
				logger.error("[领取失败] [npc已经刷新] ["+player.getLogString()+"]");
			}
		}else{
			player.sendError(Translate.请您升到40级再来吧);
			logger.error("[领取失败] [等级不够] ["+player.getLogString()+"]");
		}
	}
	
	
	
	public static void main(String[] args) {
		int[] activityBegin = {2012,10,22};
		Calendar cal = Calendar.getInstance();
		System.out.println("小时:"+cal.get(Calendar.HOUR_OF_DAY)+"分:"+cal.get(Calendar.MINUTE)+"秒:"+cal.get(Calendar.SECOND));
		
		cal.set(activityBegin[0], activityBegin[1], activityBegin[2]);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		long beginTime = cal.getTimeInMillis();
		
		long now = System.currentTimeMillis();
		
		System.out.println("指定时间:"+beginTime +" 现在时间:"+now +"间隔:"+(beginTime -now));
		
		long sel = beginTime -now;
		if(sel > 0){
			long s = 1000*60*60;
			System.out.println(sel/s+"  分:"+(sel%s/(1000*60)));
		}else{
			System.out.println("error");
		}
	}
}
