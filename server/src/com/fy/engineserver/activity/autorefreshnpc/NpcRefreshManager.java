package com.fy.engineserver.activity.autorefreshnpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ServiceStartRecord;

public class NpcRefreshManager implements Runnable{
	
	public static Logger log = ActivitySubSystem.logger;
	
	private static NpcRefreshManager self;
	
	public String fileName;
	
	public static final int 活动配置_sheet = 0;
	public static final int 初始化所有地图数据_sheet = 1;
	
	private List<MapMess> allMapMess;
	
	private List<NpcRefreshMess> configs;
	
	private boolean isstart = true;
	
	private long sleeptime = 60*1000;
	
	public static NpcRefreshManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		
		if(PlatformManager.getInstance().isPlatformOf(Platform.官方,Platform.腾讯)){
			long now = System.currentTimeMillis();
			self = this;
			allMapMess = new ArrayList<MapMess>();
			configs = new ArrayList<NpcRefreshMess>();
			loadFile();
			initAllMapData();
			if(allMapMess==null){
				throw new Exception("NpcRefreshManager..加载所有地图信息错误！");
			}
//			Set<String> set = new HashSet<String>();
//			set.add("倾世仙缘");
//			set.add("云游魂境");
//			set.add("仙山楼阁");
//			String servername = GameConstants.getInstance().getServerName();
//			if(!set.contains(servername)){
//			}
			log.warn("[刷怪管理加载] [allMapMess:"+allMapMess.size()+"] [configs:"+configs.size()+"] ["+(System.currentTimeMillis()-now)+"]");
			new Thread(this,"NpcRefreshManager").start();
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void loadFile()throws Exception{
		try{
			 File file = new File(fileName);
			 InputStream is = new FileInputStream(file);
			 POIFSFileSystem pss = new POIFSFileSystem(is);
			 HSSFWorkbook workbook = new HSSFWorkbook(pss);
			 HSSFSheet sheet = workbook.getSheetAt(活动配置_sheet);
			 int rows = sheet.getPhysicalNumberOfRows();
			 for(int i=1; i<rows; i++){
				 HSSFRow row = sheet.getRow(i);
				 if(row!=null){
					 NpcRefreshMess mm = new NpcRefreshMess();
					 int rownum = 0;
					 
					 HSSFCell cell = row.getCell(rownum++);
					 int spritetype = 0;
					 try {
						 spritetype = (int) cell.getNumericCellValue();
				  	 } catch (Exception ex) {
						 try {
							 spritetype = Integer.parseInt(cell.getStringCellValue().trim());
						 } catch (Exception e) {
							throw e;
						 }
					 } 
					 
					 cell = row.getCell(rownum++);
					 boolean isPeace = false;
					 try {
						 isPeace = cell.getBooleanCellValue();
				  	 } catch (Exception ex) {
				  		throw ex;
					 }
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String mapname = "";
					 try {
						 mapname = cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String mapnameen = "";
					 try {
						 mapnameen = cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String days = "";
					 try {
						 days = cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 //TODO
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String times = "";
					 try {
						 times = cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String npcs = "";
					 try {
						 npcs = cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 int npctimes = 0;
					 try {
						 npctimes = (int) cell.getNumericCellValue();
				  	 } catch (Exception ex) {
						 try {
							 npctimes = Integer.parseInt(cell.getStringCellValue().trim());
						 } catch (Exception e) {
							throw e;
						 }
					 }
					 
					 cell = row.getCell(rownum++);
					 boolean isallmap = false;
					 try {
						 isallmap = cell.getBooleanCellValue();
				  	 } catch (Exception ex) {
							throw ex;
					 } 
					 
					 row.getCell(rownum).setCellType(Cell.CELL_TYPE_STRING);
					 cell = row.getCell(rownum++);
					 String country = "";
					 try {
						 country =  cell.getStringCellValue().trim();
				  	 } catch (Exception ex) {
						throw ex;
					 } 
					 mm.setDays(days);
					 mm.setIsallmap(isallmap);
					 mm.setMapname(mapname);
					 mm.setMapnameen(mapnameen);
					 mm.setNpcs(npcs);
					 mm.setNpctimes(npctimes);
					 mm.setPeace(isPeace);
					 mm.setSpritetype(spritetype);
					 mm.setTimes(times);
					 mm.setCountry(country);
					 String str[] = npcs.split(",");
					 int npcid = Integer.parseInt(str[0]);
					 mm.setNextRefreshSptiteId(npcid);
					 mm.setLastRefreshSptiteId(npcid);
					 configs.add(mm);
				 }
			 }
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("随机刷新NPC，初始化配置文件异常:"+e+"");
		}
	}
	
	/**
	 * 初始化所有地图数据
	 */
	public void initAllMapData() throws Exception{
		try{
			 File file = new File(fileName);
			 InputStream is = new FileInputStream(file);
			 POIFSFileSystem pss = new POIFSFileSystem(is);
			 HSSFWorkbook workbook = new HSSFWorkbook(pss);
			 HSSFSheet sheet = workbook.getSheetAt(初始化所有地图数据_sheet);
			 int rows = sheet.getPhysicalNumberOfRows();
			 for(int i=1; i<rows; i++){
				 HSSFRow row = sheet.getRow(i);
				 if(row!=null){
					 MapMess mm = new MapMess();
					 int rownum = 0;
					 
					 HSSFCell cell = row.getCell(rownum++);
					 String name = "";
					 try {
						 name = (cell.getStringCellValue().trim());
					 } catch (Exception ex) {
						 throw new Exception("name");
					 }
					 
					 cell = row.getCell(rownum++);
					 String nameen = "";
					 try {
						 nameen = (cell.getStringCellValue().trim());
					 } catch (Exception ex) {
						 throw new Exception("nameen");
					 }
					 
					 cell = row.getCell(rownum++);
					 int xpoint = 0;
					 try {
						 xpoint = (int) cell.getNumericCellValue();
				  	 } catch (Exception ex) {
						 try {
							 xpoint = Integer.parseInt(cell.getStringCellValue().trim());
						 } catch (Exception e) {
							throw new Exception("xpoint");
						 }
					 }
					 
					 cell = row.getCell(rownum++);
					 int ypoint = 0;
					 try {
						 ypoint = (int) cell.getNumericCellValue();
				  	 } catch (Exception ex) {
						 try {
							 ypoint = Integer.parseInt(cell.getStringCellValue().trim());
						 } catch (Exception e) {
							throw new Exception("ypoint");
						 }
					 } 
					 
					 mm.setMapNameen(nameen);
					 mm.setMapName(name);
					 mm.setX(xpoint);
					 mm.setY(ypoint);
					 allMapMess.add(mm);
				 }
			 }
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("初始化所有地图数据，初始化所有地图数据异常:"+e+"");
		}
	}

	//刷新精灵
	public void refreshSprite(NpcRefreshMess npcmess){
		try{
			Sprite sprite = null;
			Random random = new Random();
			if (npcmess.getSpritetype() == 1) {
				sprite = MemoryNPCManager.getNPCManager().createNPC(npcmess.getNextRefreshSptiteId());
			} else if (npcmess.getSpritetype() == 0) {
				sprite = MemoryMonsterManager.getMonsterManager().createMonster(npcmess.getNextRefreshSptiteId());
			}
			if (sprite != null) {
				StringBuffer sb = new StringBuffer();
				if(npcmess.isIsallmap()){
					int mapIndex = random.nextInt(allMapMess.size());
					MapMess mapmess = allMapMess.get(mapIndex);
					
					sprite.setX(mapmess.getX());
					sprite.setY(mapmess.getY());
					String countrys[] = npcmess.getCountry().split(",");
					int index = random.nextInt(countrys.length);
					String refreshcountry = countrys[index];
					Game game = GameManager.getInstance().getGameByName(mapmess.getMapNameen(), index+1);
					game.addSprite(sprite);
					sb.append("三清<"+sprite.getName()+">出现在:"+refreshcountry+"的"+mapmess.getMapName()+"仅停留20分钟，之后将重返天庭！");
					noticeTelevision(sb.toString());
					
					int nextRefreshSptiteId = -1;
					int lastRefreshSptiteId = -1;
					String str[] = npcmess.getNpcs().split(",");
					for(int i =0;i<str.length;i++){
						if(Integer.parseInt(str[i]) == npcmess.getNextRefreshSptiteId()){
							if(i+1<str.length){
								nextRefreshSptiteId = Integer.parseInt(str[i+1]);
							}
							lastRefreshSptiteId = Integer.parseInt(str[i]);
						}
					}
					if(nextRefreshSptiteId>0){
						npcmess.setNextRefreshSptiteId(nextRefreshSptiteId);
					}else{
						log.warn("[获取下一个npcid] [error] ["+npcmess.getNpcs()+"]");
					}
					npcmess.setLastRefreshSptiteId(lastRefreshSptiteId);
					npcmess.setLastgame(game);
					log.warn("[创建成功] [npcmess:"+npcmess+"] [mapmess:"+mapmess+"]");
				}
			} else {
				log.warn("[创建失败] ["+npcmess+"]");
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * 移除上次刷新的npc
	 */
	private synchronized void removeLastRefresh(NpcRefreshMess npcmess) {
		try{
			Game game = npcmess.getLastgame();
			int lastnpcid = npcmess.getLastRefreshSptiteId();
			LivingObject[] los = game.getLivingObjects();
			for (LivingObject lo : los) {
				if (lo != null) {
					if (lo instanceof NPC) {
						if (((NPC) lo).getnPCCategoryId() == lastnpcid) {
							game.removeSprite((NPC) lo);
							ActivitySubSystem.logger.warn(" [移除精灵活动] [npcid" + lastnpcid + "] [" + ((NPC) lo).getName() + "/" + ((NPC) lo).getnPCCategoryId() + "/" + ((NPC) lo).getId() + "] [成功] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
						} 
					} else if (lo instanceof Monster) {
						if (((Monster) lo).getSpriteCategoryId() == lastnpcid) {
							game.removeSprite((NPC) lo);
							ActivitySubSystem.logger.warn(" [移除精灵活动] [npcid" + lastnpcid + "] [" + ((Monster) lo).getName() + "] [成功] [地图:" + game.getGameInfo().displayName + "/" + game.country + "]");
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			ActivitySubSystem.logger.warn(" [刷新精灵活动] [移除异常]");
		}
	}
	
	//通知
	public static void noticeTelevision(String content){
		ChatMessage msg = new ChatMessage();
		StringBuffer sb = new StringBuffer();
		sb.append("<f color=' ");
		sb.append(ArticleManager.COLOR_PURPLE);
		sb.append("'>");
		sb.append(content);
		sb.append("</f>");
		msg.setChatTime(5000);
		msg.setMessageText(sb.toString());
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			for(int i=0;i<3;i++){
				cms.sendRoolMessageToSystem(msg);
			}
			cms.sendHintMessageToSystem(msg);
			log.warn("[发送通知] ["+content+"]");
		} catch (Exception e) {
			log.error("[发送通知异常] ["+content+"]",e);
		}
	}

	@Override
	public void run() {
		while(isstart){
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}	
			heartbeat();
		}
	}
	
	private void heartbeat(){
		long now = System.currentTimeMillis();
		String currday =  new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm");
		long mintime = 0;
		long maxtime = 0;
		try{
			if(configs!=null){
				//day and time isfit
				for(NpcRefreshMess config:configs){
					String days [] = config.getDays().split(",");
					for(String day:days){
						if(day.equals(currday)){
							String hours [] = config.getTimes().split(",");
							for(String hour : hours){
								String hhs [] = hour.split("-");
								if(hhs.length==2){
									try {
										mintime = sdf.parse(currday+hhs[0]).getTime();
										maxtime = sdf.parse(currday+hhs[1]).getTime();
									} catch (ParseException e) {
										e.printStackTrace();
									}
									
									Calendar cl = Calendar.getInstance();
									int currminute = cl.get(Calendar.MINUTE);
									int currhour = cl.get(Calendar.HOUR_OF_DAY);
									cl.setTimeInMillis(mintime);
									int minminute = cl.get(Calendar.MINUTE);
									int minhour = cl.get(Calendar.HOUR_OF_DAY);
									cl.setTimeInMillis(maxtime);
									int maxminute = cl.get(Calendar.MINUTE);
									
									if(currhour==minhour){
										if(minminute-currminute==5){
											noticeTelevision("三清降世，赐福无限,5分钟后即将随机刷新“玉清原始混元至圣”、“上清灵宝混元至圣”、“太清道德混元至圣”之一,绑银宝库豪礼放送，银子宝库逆天再现！");
										}
										
										if(minminute == currminute && now < maxtime){
											refreshSprite(config);
										}
										
										if(currminute-minminute==20){
											removeLastRefresh(config);
										}
									}
//									if(minminute+config.getNpctimes()==currminute && now < maxtime){
//										refreshSprite(config);
//									}
//									if(maxminute==currminute){
//										removeLastRefresh(config);
//									}
								}else{
									log.warn("[获得小时] [hhs.length!=2]");
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<MapMess> getAllMapMess() {
		return allMapMess;
	}

	public void setAllMapMess(List<MapMess> allMapMess) {
		this.allMapMess = allMapMess;
	}

	public List<NpcRefreshMess> getConfigs() {
		return configs;
	}

	public void setConfigs(List<NpcRefreshMess> configs) {
		this.configs = configs;
	}

	public boolean isIsstart() {
		return isstart;
	}

	public void setIsstart(boolean isstart) {
		this.isstart = isstart;
	}

	public long getSleeptime() {
		return sleeptime;
	}

	public void setSleeptime(long sleeptime) {
		this.sleeptime = sleeptime;
	}
	
}
