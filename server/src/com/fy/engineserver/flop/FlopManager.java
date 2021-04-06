package com.fy.engineserver.flop;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.USER_CLIENT_INFO_REQ;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;

public class FlopManager implements Runnable{

	public static Logger logger = LoggerFactory.getLogger(FlopManager.class.getName());
	private static FlopManager self;
	
	private FlopManager(){
		
	}
	
	public static FlopManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		
		long now = System.currentTimeMillis();
		self = this;
		load();
		Thread thread = new Thread(this, "FlopManager");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	
	public static boolean running = true;
	public File file;
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 物品每人掉落的最大值
	 * key为物品名和颜色组合 "手套_0"
	 * value为最大掉落数量
	 */
	public HashMap<String,Integer> articleFlopMaxValues = new HashMap<String,Integer>();
	
	/**
	 * 玩家每天的掉落集合
	 * key玩家id
	 * value掉落物品信息(key掉落物品名字颜色组合，value已经掉落个数)
	 */
	public Hashtable<Long,Hashtable<String,Integer>> flopCounts = new Hashtable<Long,Hashtable<String,Integer>>();

	
	/**
	 * ip每天的掉落集合
	 * key玩家ip
	 * value掉落物品信息(key掉落物品名字颜色组合，value已经掉落个数)
	 */
	public Hashtable<String,Hashtable<String,Integer>> ipFlopCounts = new Hashtable<String,Hashtable<String,Integer>>();
	
	/**
	 * 一个设备组号里的受到掉落限制影响
	 */
	public Hashtable<String,ArrayList<String>> 设备号组Map = new Hashtable<String,ArrayList<String>>();
	
	/**
	 * 一个设备号在哪个组KeyId里
	 * key为设备号,value为设备号组Map的包含这个设备号的key
	 */
	public Hashtable<String,String> 设备号Map = new Hashtable<String,String>();
	
	
	
	public static int IP_掉落倍数 = 5;

	public void load() throws Exception{
		if (file != null && file.isFile() && file.exists()) {
			HashMap<String,Integer> articleFlopMaxValues = new HashMap<String,Integer>();
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int num = sheet.getPhysicalNumberOfRows();
			for(int i = 1; i < num; i++){
				HSSFRow hr = sheet.getRow(i);
				String name = (hr.getCell(0).getStringCellValue().trim());
				int color = (int)hr.getCell(1).getNumericCellValue();
				int count = (int)hr.getCell(2).getNumericCellValue();
				articleFlopMaxValues.put(name+"_"+color, count);
			}
			this.articleFlopMaxValues = articleFlopMaxValues;
		}
	}
	/**
	 * 能否掉落物品，根据物品名字和颜色组合成关键字
	 * @param name
	 * @param color
	 * @param player
	 * @return
	 */
	public boolean canFlopArticle(String name, int color, Player player){
		synchronized(player){
			StringBuffer sb = new StringBuffer();
			String key = sb.append(name).append("_").append(color).toString();
			if(articleFlopMaxValues.get(key) == null){
				return true;
			}
			int maxValue = articleFlopMaxValues.get(key);
			if(maxValue <= 0){
				return false;
			}
			
			boolean ipFlop = canFlopByIP(key, player, IP_掉落倍数*maxValue);
			if(!ipFlop){
				return false;
			}
			if(flopCounts.get(player.getId()) == null){
				return true;
			}
			
			Hashtable<String,Integer> playerFlopMap = flopCounts.get(player.getId());
			if(playerFlopMap.get(key) == null){
				return true;
			}
			
			
			int currentCount = playerFlopMap.get(key);
			if(maxValue > currentCount){
				return true;
			}
		}
		return false;
	}

	public boolean canFlopByIP(String key, Player player, int maxValue){

//		String ip = 根据传进来的设备号得到一个标记字符串(player);
//		if(ip != null){
//			//判断掉落合法性
//			if(ipFlopCounts.get(ip) == null){
//				return true;
//			}
//			
//			Hashtable<String,Integer> ipFlopMap = ipFlopCounts.get(ip);
//			if(ipFlopMap.get(key) == null){
//				return true;
//			}
//		
//			int currentCount = ipFlopMap.get(key);
//			if(maxValue > currentCount){
//				return true;
//			}else{
//				FlopManager.logger.info("[今天IP掉落已满] [{}] [{}] [{}]", new Object[] { key, player.getLogString(), ip });
//				return false;
//			}
//		}

		return true;
	}
	
	public void addFlopArticle(String name, int color, Player player){
		synchronized(player){
			StringBuffer sb = new StringBuffer();
			String key = sb.append(name).append("_").append(color).toString();
			if(articleFlopMaxValues.get(key) == null){
				return;
			}
			
			Hashtable<String,Integer> playerFlopMap = flopCounts.get(player.getId());
			if(playerFlopMap == null){
				playerFlopMap = new Hashtable<String,Integer>();
				flopCounts.put(player.getId(),playerFlopMap);
			}
			
			if(playerFlopMap.get(key) == null){
				playerFlopMap.put(key, 1);
			}else{
				playerFlopMap.put(key, playerFlopMap.get(key)+1);
			}
			if(FlopManager.logger.isInfoEnabled()){
				FlopManager.logger.info("[添加掉落] [{}] [color:{}] [count:{}] [{}]",new Object[]{name, color, playerFlopMap.get(key), player.getLogString()});
			}
			
			addFlopByIP(key, player);
		}
	}

	public void addFlopByIP(String key, Player player){
		
//		String ip = 根据传进来的设备号得到一个标记字符串(player);
//		if(ip != null){
//			Hashtable<String,Integer> ipFlopMap = ipFlopCounts.get(ip);
//			if(ipFlopMap == null){
//				ipFlopMap = new Hashtable<String,Integer>();
//				ipFlopCounts.put(ip,ipFlopMap);
//			}
//			
//			if(ipFlopMap.get(key) == null){
//				ipFlopMap.put(key, 1);
//			}else{
//				ipFlopMap.put(key, ipFlopMap.get(key)+1);
//			}
//			if(FlopManager.logger.isInfoEnabled()){
//				FlopManager.logger.info("[添加IP掉落] [{}] [count:{}] [{}] [{}]",new Object[]{key, ipFlopMap.get(key), player.getLogString(), ip});
//			}
//		}else{
//			if(FlopManager.logger.isInfoEnabled()){
//				FlopManager.logger.info("[添加IP掉落失败] [不在监控范围内] [{}] [count:{}] [{}] [{}]",new Object[]{key, player.getLogString()});
//			}
//		}				
	}
	
	/**
	 * 根据设备号得到标记字符串，标记字符串不为空代表这个设备在监控范围内，为空代表不需要监控
	 * @return
	 */
	public String 根据传进来的设备号得到一个标记字符串(Player player){
		try{
			Connection conn = player.getConn();
			String deviceId = null;
			if(conn != null){
				Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
				if(o instanceof USER_CLIENT_INFO_REQ){
					USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ)o;
					if(req.getClientPlatform().toLowerCase().equals("android")){
						deviceId = req.getDEVICEID();
						if("".equals(deviceId)){
							deviceId = req.getClientId();
						}
					}
				}
			}
			
			if(deviceId != null && !deviceId.equals("")){
				if(设备号Map.get(deviceId) != null){
					return 设备号Map.get(deviceId);
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	public Random random = new Random();
	/**
	 * 组内的玩家将受到IP掉落规则限制
	 */
	public void 检测在线玩家按照规则分组(){
		PlayerManager pm = PlayerManager.getInstance();
		if(pm != null){
			Player[] ps = pm.getOnlinePlayers();
			if(ps != null){
				Hashtable<String,ArrayList<String>> table = new Hashtable<String,ArrayList<String>>();
				for(int i = 0; i < ps.length; i++){
					Player p = ps[i];
					if(p != null){
						try{
							Connection conn = p.getConn();
							if(conn != null){
								Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
								if(o instanceof USER_CLIENT_INFO_REQ){
									USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ)o;
									String network = req.getNetwork();
									if("wifi".equals(network.toLowerCase())){
										String identity = conn.getIdentity();
										if(identity != null){
											String[] ids = identity.split(":");
											if(ids != null && ids.length > 0){
												String deviceId = null;
												if(req.getClientPlatform().toLowerCase().equals("android")){
													deviceId = req.getDEVICEID();
													if("".equals(deviceId)){
														deviceId = req.getClientId();
													}
												}
												if(deviceId != null && !deviceId.equals("")){
													String ip = ids[0];
													ip = ip + req.getPhoneType();
													ArrayList<String> devices = table.get(ip);
													if(devices == null){
														devices = new ArrayList<String>();
														table.put(ip, devices);
													}
													devices.add(deviceId);
												}
											}
										}
									}
								}
							}
						}catch(Exception ex){
							logger.error("[搜集玩家信息异常] ["+p.getLogString()+"]",ex);
						}

					}
				}
				
				//对table进行处理
				if(设备号组Map.isEmpty()){
					if(table.keySet() != null){
						for(String key : table.keySet()){
							String k = "IP_LIMIT_"+random.nextInt(100000000);
							ArrayList<String> list = table.get(key);
							if(list != null){
								设备号组Map.put(k, list);
								for(String device : list){
									设备号Map.put(device, k);
								}
							}
						}
					}
				}else{
					if(table.keySet() != null){
						for(String key : table.keySet()){
							ArrayList<String> list = table.get(key);
							if(list != null){
								ArrayList<String> list2 = new ArrayList<String>();
								String k = null;
								for(String str : list){
									if(设备号Map.get(str) == null){
										list2.add(str);
									}else{
										if(k == null){
											k = 设备号Map.get(str);
										}
									}
								}
								if(k != null){
									ArrayList<String> l = 设备号组Map.get(k);
									if(l == null){
										l = new ArrayList<String>();
										设备号组Map.put(k, l);
									}
									for(String s : list2){
										l.add(s);
										设备号Map.put(s, k);
									}
								}else{
									k = "IP_LIMIT_"+random.nextInt(100000000);
									ArrayList<String> l = new ArrayList<String>();
									设备号组Map.put(k, l);
									for(String s : list2){
										l.add(s);
										设备号Map.put(s, k);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	DajingStudioManager djm;
	
	public DajingStudioManager getDjm() {
		return djm;
	}

	public void setDjm(DajingStudioManager djm) {
		this.djm = djm;
	}

	/**
	 *   public boolean 打怪是否可以掉落白酒(Player p){return 检测是否可以产出(p,每天打怪掉落白酒限制);}

    public boolean 打怪是否可以掉落绿酒(Player p){return 检测是否可以产出(p,每天打怪掉落绿酒限制);}

    public boolean 打怪是否可以掉落白帖(Player p){return 检测是否可以产出(p,每天打怪掉落白帖限制);}

    public boolean 打怪是否可以掉落绿帖(Player p){return 检测是否可以产出(p,每天打怪掉落绿帖限制);}

    public boolean 打怪是否可以掉落白装(Player p){return 检测是否可以产出(p,每天打怪掉落白装限制);}

    public boolean 打怪是否可以掉落绿装(Player p){return 检测是否可以产出(p,每天打怪掉落绿装限制);}

    public boolean 打怪是否可以掉落蓝装(Player p){return 检测是否可以产出(p,每天打怪掉落蓝装限制);}

    public boolean 打怪是否可以掉落紫装(Player p){return 检测是否可以产出(p,每天打怪掉落紫装限制);}

	 * @param a
	 * @return
	 */
	public boolean 打金工作室处理是否掉落(Article a, int color, Player p){
		try {
			if(RelationShipHelper.checkForbid(p)) {
				if(RelationShipHelper.logger.isDebugEnabled()) {
					RelationShipHelper.logger.debug("[玩家因为打金行为被限制掉落] ["+p.getLogString()+"]");
				}
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(djm != null){
			if(Translate.酒.equals(a.get物品二级分类())){
				if(color == 0){
					return djm.打怪是否可以掉落白酒(p);
				}else if(color == 1){
					return djm.打怪是否可以掉落绿酒(p);
				}
			}
			if(Translate.封魔录.equals(a.get物品二级分类())){
				if(color == 0){
					return djm.打怪是否可以掉落白帖(p);
				}else if(color == 1){
					return djm.打怪是否可以掉落绿帖(p);
				}
			}
			if(a instanceof Equipment){
				if(color == 0){
					return djm.打怪是否可以掉落白装(p);
				}else if(color == 1){
					return djm.打怪是否可以掉落绿装(p);
				}else if(color == 2){
					return djm.打怪是否可以掉落蓝装(p);
				}else if(color == 3){
					return djm.打怪是否可以掉落紫装(p);
				}
			}
		}
		return true;
	}
	
	/**
	 * public void notify_打怪掉落白装(Player p){notify_产出(p,每天打怪掉落白装限制,1);}

    public void notify_打怪掉落绿装(Player p){notify_产出(p,每天打怪掉落绿装限制,1);}

    public void notify_打怪掉落蓝装(Player p){notify_产出(p,每天打怪掉落蓝装限制,1);}

    public void notify_打怪掉落紫装(Player p){notify_产出(p,每天打怪掉落紫装限制,1);}

    public void notify_打怪掉落白酒(Player p){notify_产出(p,每天打怪掉落白酒限制,1);}

    public void notify_打怪掉落绿酒(Player p){notify_产出(p,每天打怪掉落绿酒限制,1);}

    public void notify_打怪掉落白帖(Player p){notify_产出(p,每天打怪掉落白帖限制,1);}

    public void notify_打怪掉落绿帖(Player p){notify_产出(p,每天打怪掉落绿帖限制,1);}

	 */
	public void 通知工作室打怪掉落(Article a, int color, Player p){
		if(djm != null){
			if(Translate.酒.equals(a.get物品二级分类())){
				if(color == 0){
					djm.notify_打怪掉落白酒(p);
				}else if(color == 1){
					djm.notify_打怪掉落绿酒(p);
				}
			}
			if(Translate.封魔录.equals(a.get物品二级分类())){
				if(color == 0){
					djm.notify_打怪掉落白帖(p);
				}else if(color == 1){
					djm.notify_打怪掉落绿帖(p);
				}
			}
			if(a instanceof Equipment){
				if(color == 0){
					djm.notify_打怪掉落白装(p);
				}else if(color == 1){
					djm.notify_打怪掉落绿装(p);
				}else if(color == 2){
					djm.notify_打怪掉落蓝装(p);
				}else if(color == 3){
					djm.notify_打怪掉落紫装(p);
				}
			}
		}
	}
	
	public static boolean IP_LIMIT_FLAG = false;
	int date;
	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		while(running){
			try {
				Thread.sleep(60000);
				
				calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				int date = calendar.get(Calendar.DATE);
				if (this.date != date) {
					flopCounts.clear();
					ipFlopCounts.clear();
					设备号组Map.clear();
					设备号Map.clear();
					this.date = date;
				}
				if(IP_LIMIT_FLAG){
					检测在线玩家按照规则分组();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("[掉落管理器异常]",e);
			}
		}
		
	}
}
