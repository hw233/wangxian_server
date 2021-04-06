package com.fy.engineserver.enterlimit;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.ANDROID_PROCESS_RES;
import com.fy.engineserver.message.DENY_USER_REQ;
import com.fy.engineserver.message.GET_RSA_DATA_REQ;
import com.fy.engineserver.message.GET_WAIGUA_PROCESS_NAMES_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.USER_CLIENT_INFO_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.validate.DefaultValidateManager;
import com.fy.engineserver.validate.UserData;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.Message;

/**
 * 登陆限制管理
 * 
 * 
 */
public class EnterLimitManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(EnterLimitManager.class);
	
	public static String[] noChongZhiMacs = new String[] {"C4:6A:B7:51:84:4D"};		//不显示充值的MD5
	public static String[] noChongZhiUserNames = new String[] {"xxlyai122"};		//不显示充值的用户名
	public static String[] noChongZhiClientIDs = new String[] {"04401663453703233332"};		//不显示充值的ClientID
	public static String[] noChongZhiIPs = new String[]{"125.39.108.73","219.239.197.33","124.202.191.140"};									//不显示充值的IP
	public static boolean isCanChongZhi = false;						//开关
	
	public static boolean isCheckIOS_Server = false;
	//是否检查银子发邮件
	public static boolean isCheckSilver = true;
	
	public static boolean canChongZhi (Connection conn) {
		try {
			if (!isCanChongZhi) {
				return true;
			}
			Object o = conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
			if (o != null) {
				NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)o;
				//MAC地址
				String md5 = req.getMACADDRESS();
				for (String m : noChongZhiMacs) {
					if (md5.equals(m)) {
						return false;
					}
				}
				//用户名
				String username = req.getUsername();
				for (String u : noChongZhiUserNames) {
					if (username.equals(u)) {
						return false;
					}
				}
				//ClientID
				String clientID = req.getClientId();
				for (String c : noChongZhiClientIDs) {
					if (clientID.equals(c)) {
						return false;
					}
				}
				//IP地址
				String ip = conn.getRemoteAddress();
				for (String i : noChongZhiIPs) {
					if (ip.contains(i)) {
						return false;
					}
				}
			}
		}catch (Exception e) {
			logger.error("isShowShop", e);
		}
		return true;
	}

	// 是否开启了限制
	public static boolean LIMIT = true;
	public static boolean isLimit_nickname = true;
	public static boolean RUNNING = true;

	private String android_rsa_path;
	private String android_rsa1_path;
	private String android_malai_path;
	private String android_huawei_path;
	// TODO:腾讯不开这个设置
	public static boolean isCompareRSA = true;
	public static boolean isSameRSAClose = false;
	public boolean isCloseConnet = true;
	public HashMap<Long, Integer> NO_RSA_REQNUM = new HashMap<Long, Integer>();
	public HashMap<Long, Integer> NO_PROCESS_REQNUM = new HashMap<Long, Integer>();
	public int noNum_MAX = 2;
	public boolean isRandomCompare = false;
	public byte[] rsaByte;
	public byte[] reaByte_uc;
	public byte[] reaByte_malai;
	public byte[] reaByte_huawei;
	public int rsaStringLength = 450;
	public String unAndroid = "UNANDROID";
  
	public static long sleepTime = 10000L;

	public DiskCache diskCache;

	public static String LIMIT_KEY = "LIMIT_USER_NAMES";

	public static String recordData_KEY = "RECORD_DATA_";
	public static String likeNeedLimit_KEY = "NEED_LIMIT_";

	public String dcFile = "";

	public String getDcFile() {
		return dcFile;
	}

	public void setDcFile(String dcFile) {
		this.dcFile = dcFile;
	}

	// 关心的联网方式,全是大写的
	public static List<String> attentionList = new ArrayList<String>();

	public static boolean attentionOpen = false;

	public static Map<Long, PlayerRecordData> recordData = new ConcurrentHashMap<Long, EnterLimitManager.PlayerRecordData>();

	// 玩家进程信息
	public static Map<Long, Player_Process> player_process = new ConcurrentHashMap<Long, Player_Process>();
	// 是否开启玩家进程获取协议
	public static boolean isGetPlayerProcess = true;
	//如果检查到外挂进程是否关闭连接
	public static boolean isWaiGuaProcessCloseConn = false;
	public static HashMap<Long, Integer> playerCloseNum = new HashMap<Long, Integer>();
	public static long lastGetProcessTime = 0L;
	public static long lastSendGetProcessTime = 0L;
	public static long GET_PROCESS_SPACE = 1000L * 60 * 10;	//10分钟取一次
	public static long GET_PLAYER_PROCESS_SPACE = 1000L * 60 * 5;	//5分钟取一次

	// 查看疑似的 //是否做短期内多次访问不变?
	public Map<String, EnterLimitData> likeNeedLimit = new HashMap<String, EnterLimitData>();

	static {
		attentionList.add("WIFI");
	}
	// public List<String> likeNeedLimitUsers = new ArrayList<String>();

	// 被限制的列表
	public List<String> limited = new ArrayList<String>();
	//玩家登陆错误的地图
	public static Map<Long, Integer> playerEnterErrorMapNums = new ConcurrentHashMap<Long, Integer>();
	//玩家发送多次错误的世界聊天
	public static Map<Long, Integer> playerSendErrorWorldChat = new ConcurrentHashMap<Long, Integer>();

	public static EnterLimitManager instance;

	public static EnterLimitManager getInstance() {
		return instance;
	}

	public void init() {
		
		
		
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			AndroidMsgManager.isAutoFeng = true;
		}
		diskCache = new DefaultDiskCache(new File(getDcFile()), "登陆限制", 1000L * 60 * 60 * 24 * 365 * 100);
		Object o = diskCache.get(LIMIT_KEY);
		if (o == null) {
			diskCache.put(LIMIT_KEY, (Serializable) limited);
		}
		limited = (List<String>) diskCache.get(LIMIT_KEY);

		// Object _likeNeedLimit = diskCache.get(likeNeedLimit_KEY);
		// if (_likeNeedLimit == null) {
		// diskCache.put(likeNeedLimit_KEY, (Serializable) likeNeedLimit);
		// }
		// likeNeedLimit = (Map<String, EnterLimitData>) diskCache.get(likeNeedLimit_KEY);

		// Object _recordData = diskCache.get(recordData_KEY);
		// if (_recordData == null) {
		// diskCache.put(recordData_KEY, (Serializable) recordData);
		// }
		// recordData = (Map<Long, PlayerRecordData>) diskCache.get(recordData_KEY);

		instance = this;
		try {
			File file = new File(getAndroid_rsa_path());
			if (!file.exists()) {
				System.out.println("安卓 的签名文件不存在!!!!!!!");
			} else {
				FileInputStream fis = new FileInputStream(file);
				rsaByte = new byte[fis.available()];
				fis.read(rsaByte);
				logger.warn("安卓签名文件大小:" + rsaByte.length);
			}
			file = new File(getAndroid_rsa1_path());
			if (!file.exists()) {
				System.out.println("安卓 UC的签名文件不存在!!!!!!!");
			} else {
				FileInputStream fis = new FileInputStream(file);
				reaByte_uc = new byte[fis.available()];
				fis.read(reaByte_uc);
				logger.warn("安卓UC签名文件大小:" + reaByte_uc.length);
			}
			file = new File(getAndroid_malai_path());
			if (!file.exists()) {
				System.out.println("马来的签名文件不存在!!!!!!!");
			} else {
				FileInputStream fis = new FileInputStream(file);
				reaByte_malai = new byte[fis.available()];
				fis.read(reaByte_malai);
				logger.warn("马来签名文件大小:" + reaByte_malai.length);
			}
			file = new File(getAndroid_huawei_path());
			if (!file.exists()) {
				System.out.println("华为的签名文件不存在!!!!!!!");
			} else {
				FileInputStream fis = new FileInputStream(file);
				reaByte_huawei = new byte[fis.available()];
				fis.read(reaByte_huawei);
				logger.warn("华为签名文件大小:" + reaByte_huawei.length);
			}
		} catch (Exception e) {
			logger.error("安卓签名文件获取出错", e);
		}

		PhoneNumMananger.getInstance().init();
		
		this.lastSaveTime = SystemTime.currentTimeMillis();
		Thread thread = new Thread(instance, "登陆限制管理");
		thread.start();
		
		if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			isWaiGuaProcessCloseConn = true;
		}
		
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			isCheckIOS_Server = true;
		}
		
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		long now = SystemTime.currentTimeMillis();
		if (recordData != null && recordData.size() > 0) {
			for (Iterator<Long> itor = recordData.keySet().iterator(); itor.hasNext();) {
				Long playerId = itor.next();
				PlayerRecordData playerRecordData = recordData.get(playerId);
				if (now - playerRecordData.lastModifyTime > REMOVE_SEPT) {
					itor.remove();
					logger.warn("[疑似外挂] [移除超时数据:" + playerId + "]");
					for (Iterator<String> iterator = likeNeedLimit.keySet().iterator(); iterator.hasNext();) {
						String ip = (String) iterator.next();
						EnterLimitData enterLimitData = likeNeedLimit.get(ip);
						if (enterLimitData.playerIds.contains(playerId)) {
							enterLimitData.playerIds.remove(playerId);
						}
					}
				}

			}
			diskCache.put(likeNeedLimit_KEY, (Serializable) likeNeedLimit);
			diskCache.put(recordData_KEY, (Serializable) recordData);
		}
	}

	Random random = new Random();
	int[] randomIndex = new int[10];

	public boolean compareRSA(byte[] values, String pla) {
		if (unAndroid.equals(pla)) {
			return true;
		}
		if (isCompareRSA) {
			if (values.length == rsaByte.length) {
				if (isRandomCompare) {
					for (int i = 0; i < 10; i++) {
						if (values[randomIndex[i]] != rsaByte[randomIndex[i]]) {
							return false;
						}
					}
					return true;
				} else {
					for (int i = 0; i < rsaStringLength; i++) {
						if (values[i] != rsaByte[i]) {
							return false;
						}
					}
					return true;
				}
			} else if (values.length == reaByte_uc.length) {
				if (isRandomCompare) {
					for (int i = 0; i < 10; i++) {
						if (values[randomIndex[i]] != reaByte_uc[randomIndex[i]]) {
							return false;
						}
					}
					return true;
				} else {
					for (int i = 0; i < rsaStringLength; i++) {
						if (values[i] != reaByte_uc[i]) {
							return false;
						}
					}
					return true;
				}
			} else if (values.length == reaByte_malai.length) {
				if (isRandomCompare) {
					for (int i = 0; i < 10; i++) {
						if (values[randomIndex[i]] != reaByte_malai[randomIndex[i]]) {
							return false;
						}
					}
					return true;
				} else {
					for (int i = 0; i < rsaStringLength; i++) {
						if (values[i] != reaByte_malai[i]) {
							return false;
						}
					}
					return true;
				}
			} else if (values.length == reaByte_huawei.length) {
				if (isRandomCompare) {
					for (int i = 0; i < 10; i++) {
						if (values[randomIndex[i]] != reaByte_huawei[randomIndex[i]]) {
							return false;
						}
					}
					return true;
				} else {
					for (int i = 0; i < rsaStringLength; i++) {
						if (values[i] != reaByte_huawei[i]) {
							return false;
						}
					}
					return true;
				}

			}
			return false;
		}
		return true;
	}

	// 踢下线
	public void offLine(String[] usernames) {
		logger.warn("[疑似外挂] [直接T玩家下线] [开始] [目标:" + usernames.length + "] [" + Arrays.toString(usernames) + "]");
		Set<String> set = new HashSet<String>();
		for (String username : usernames) {
			if (username != null) {
				set.add(username);
			}
		}
		int succNum = 0;
		Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
		for (Player p : players) {
			if (p != null) {
				Passport passport = p.getPassport();
				if (passport != null) {
					String userName = passport.getUserName();
					if (set.contains(userName)) {
						Connection conn = p.getConn();
						if (conn != null) {
							conn.close();
							succNum++;
							logger.warn("[疑似外挂] [直接T玩家下线] [" + p.toString() + "]");
						}
						set.remove(userName);
					}
				}
			}
		}
		logger.warn("[疑似外挂] [直接T玩家下线] [结束] [实际T下线:" + succNum + "]");

	}

	// 放入限制列表
	public void putTolimit(String ip, String[] usernames) {
		Set<String> usernameSet = new HashSet<String>();
		for (String username : usernames) {
			usernameSet.add(username);
		}
		boolean add = false;
		synchronized (limited) {
			for (String username : usernames) {
				if (!limited.contains(username)) {
					limited.add(username);
					add = true;
					logger.warn("[疑似外挂] [添加新用户] [" + username + "]");
				}
			}
			if (add) {
				diskCache.put(LIMIT_KEY, (Serializable) limited);
			}
		}
		// 在疑似列表中移除
		synchronized (likeNeedLimit) {
			for (Iterator<String> itor = likeNeedLimit.keySet().iterator(); itor.hasNext();) {
				String mapIp = itor.next();
				EnterLimitData enterLimitData = likeNeedLimit.get(mapIp);
				Iterator<Long> playerIdItor = enterLimitData.playerIds.iterator();
				while (playerIdItor.hasNext()) {
					Long playerId = playerIdItor.next();
					PlayerRecordData playerRecordData = getPlayerRecordData(playerId);
					if (playerRecordData != null) {
						if (usernameSet.contains(playerRecordData.username)) {
							// 移除数据
							playerIdItor.remove();
							removePlayerRecordData(playerId);
						}
					}
				}
			}
		}
		offLine(usernames);
	}

	// 从限制列表中移除
	public void removeFromlimit(String[] usernames) {
		boolean removed = false;
		synchronized (limited) {
			for (String username : usernames) {
				if (limited.contains(username)) {
					limited.remove(username);
					removed = true;
					logger.warn("[疑似外挂] [移除限制用户] [" + username + "]");
				}
			}
			if (removed) {
				diskCache.put(LIMIT_KEY, (Serializable) limited);
			}
		}
	}

	public static PlayerRecordData getPlayerRecordData(Long playerId) {
		return recordData.get(playerId);
	}

	public static void removePlayerRecordData(Long playerId) {
		recordData.remove(playerId);
	}

	/**
	 * 返回疑似外挂列表,
	 * 符合数量和等级
	 * @param maxSize同一个ip下用户至少要达到数量
	 *            (1级筛选)
	 * @param lowLevel同一个ip下用户至少达到的级别
	 *            (2级筛选)
	 * @return
	 */
	public EnterLimitData[] getLikeNeedLimitArray(int lowSize, int lowLevel) {
		Map<String, EnterLimitData> limit = getLikeNeedLimit(lowSize);
		List<EnterLimitData> list = new ArrayList<EnterLimitData>();

		for (Iterator<String> itor = limit.keySet().iterator(); itor.hasNext();) {
			String ip = itor.next();
			EnterLimitData data = limit.get(ip);
			EnterLimitData newData = new EnterLimitData();
			newData.copyOf(data, lowLevel);
			if (newData.size() > 0) {
				list.add(newData);
			}
		}
		return list.toArray(new EnterLimitData[0]);
	}

	// 返回疑似列表
	public Map<String, EnterLimitData> getLikeNeedLimit(int lowSize) {
		if (lowSize <= 1) {
			return likeNeedLimit;
		}
		Map<String, EnterLimitData> temp = new HashMap<String, EnterLimitData>();

		// map
		for (Iterator<String> itor = likeNeedLimit.keySet().iterator(); itor.hasNext();) {
			String ip = itor.next();
			if (likeNeedLimit.get(ip).size() >= lowSize) {
				EnterLimitData data = likeNeedLimit.get(ip);
				temp.put(ip, data);
				data.ip = ip;
				for (Long playerId : data.playerIds) {
					PlayerRecordData playerRecordData = getPlayerRecordData(playerId);
					if (playerRecordData != null) {
						playerRecordData.online = Boolean.FALSE;
						playerRecordData.online = GamePlayerManager.getInstance().isOnline(playerId);
					}
				}
			}
		}
		return temp;
	}

	public boolean inEnterLimit(String username, Connection conn) {
		if (!LIMIT || limited == null) {
			return false;
		}
		return limited.contains(username);
	}

	public long lastSaveTime = 0;

	public static long SAVE_SEPT = 1000 * 60 * 60;
	public static long REMOVE_SEPT = 1000 * 60 * 60 * 24 * 15;

	private final long MAX_SILVER = 111000000L;
	private final long MAX_SILVER_RMB = 20000L;
	private final long MAX_RMB = 200000L;
	private final long MAX_SILVER_TIME = 10 * 1000L;
	
	/***
	 * 1.先找到所有人,分配到IP组;
	 * 2.将所有人的数据更新到recordData
	 */
	@Override
	public void run() {
		while (RUNNING) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long start = SystemTime.currentTimeMillis();
			logger.warn("[疑似外挂] [心跳开始]");
			try{
				//去网关取外挂进程
				if (isWaiGuaProcessCloseConn) {
					if (start - lastGetProcessTime > GET_PROCESS_SPACE) {
						lastGetProcessTime = start;
						Message m = new GET_WAIGUA_PROCESS_NAMES_REQ(GameMessageFactory.nextSequnceNum());
						MieshiGatewayClientService.getInstance().sendMessageToGateway(m);
					}
					if (start - lastSendGetProcessTime > GET_PLAYER_PROCESS_SPACE) {
						lastSendGetProcessTime = start;
						Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
						for (int i = 0; i < ps.length; i++) {
							ANDROID_PROCESS_RES res = new ANDROID_PROCESS_RES(GameMessageFactory.nextSequnceNum());
							ps[i].addMessageToRightBag(res);
						}
					}
				}
			}catch (Exception e) {
				logger.warn("取进程信息出错:", e);
			}
			try{
				if (!PlatformManager.getInstance().platformOf(Platform.韩国)) {
					Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
					for (Player p : players) {
						if (start - p.lastHeatTime > MAX_SILVER_TIME) {
							if (p.getSilver() - p.lastHeatSilver > MAX_SILVER) {
								if ((p.getRMB() - p.lastHeatRmb < MAX_SILVER_RMB) && p.getRMB() < MAX_RMB ) {
									sendMail("银子异常，已永久封号", "玩家银子异常波动，出现10秒内50锭的银子变化:" + p.getLogString() + " [银子变化] [上次心跳银子=" + p.lastHeatSilver + "] [目前银子="+ p.getSilver() +"] [上次心跳rmb="+p.lastHeatRmb+"] ["+p.getRMB()+"] ["+GameConstants.getInstance().getServerName()+"]");
									DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", p.getUsername(), Translate.translateString(Translate.您在XX的银子金额异常增长可以通过邮件toususqagecom投诉, new String[][]{{Translate.STRING_1, GameConstants.getInstance().getServerName()}}) , GameConstants.getInstance().getServerName() + "-外挂检测模块", false, true, true, 0, 10000);
									MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
									p.getConn().close();
									EnterLimitManager.logger.warn("[出现金额异常，永久封号] [{}] [{}] [{}]", new Object[]{p.getLogString(), p.getSilver() + "-" + p.lastHeatSilver, p.getRMB() + "-" + p.lastHeatRmb});
								} else if (p.getRMB() - p.lastHeatRmb < MAX_SILVER_RMB){
									sendMail("银子异常，vip玩家封号12小时", "玩家银子异常波动，出现10秒内50锭的银子变化:" + p.getLogString() + " [银子变化] [上次心跳银子=" + p.lastHeatSilver + "] [目前银子="+ p.getSilver() +"] [上次心跳rmb="+p.lastHeatRmb+"] ["+p.getRMB()+"] ["+GameConstants.getInstance().getServerName()+"]");
									DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", p.getUsername(), Translate.translateString(Translate.您在XX的银子金额异常增长我们需要人工核实增长原因, new String[][]{{Translate.STRING_1, GameConstants.getInstance().getServerName()}}) , GameConstants.getInstance().getServerName() + "-外挂检测模块",
											false, true, false, 0, 12);
									MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
									p.getConn().close();
									EnterLimitManager.logger.warn("[出现金额异常，封号12小时] [{}] [{}] [{}]", new Object[]{p.getLogString(), p.getSilver() + "-" + p.lastHeatSilver, p.getRMB() + "-" + p.lastHeatRmb});
								}
							}
							p.lastHeatSilver = p.getSilver();
							p.lastHeatRmb = p.getRMB();
							p.lastHeatTime = start;
						}
					}
				}
			} catch (Exception e) {
				logger.error("自动封银子异常出错:", e);
			}
			
			try {
				for (int i = 0; i < 10; i++) {
					randomIndex[i] = random.nextInt(rsaStringLength);
				}
				if (LIMIT) {
					Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
					Map<String, EnterLimitData> temp = new HashMap<String, EnterLimitData>();

					// 角色数据

					for (Player p : players) {
						Connection conn = p.getConn();
						if (conn == null) {
							continue;
						}
						Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
						String pla = "";
						if (o instanceof USER_CLIENT_INFO_REQ) {
							USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) o;
							pla = req.getClientPlatform();
							String network = req.getNetwork();
							String ip = conn.getIdentity().split(":")[0];
							String phoneType = req.getPhoneType();
							if (ip != null) {
								if (!temp.containsKey(ip)) {
									EnterLimitData da = new EnterLimitData();
									da.ip = ip;
									temp.put(ip, da);
								}
								EnterLimitData data = temp.get(ip);
								data.playerIds.add(p.getId());

								PlayerRecordData playerRecordData = recordData.get(p.getId());
								if (playerRecordData == null) {
									playerRecordData = new PlayerRecordData(p, network, phoneType);
									logger.warn("[疑似外挂] [新增玩家数据] [" + p.getLogString() + "]");
									recordData.put(p.getId(), playerRecordData);
								}
								playerRecordData.channel = req.getChannel();
								playerRecordData.obj3 = req.getClientProgramVersion();
								if (player_process.containsKey(p.getId())) {
									playerRecordData.obj2 = player_process.get(p.getId()).isMoniqi();
								} else {
									playerRecordData.obj2 = null;
								}
								playerRecordData.obj4 = pla;
								playerRecordData.obj5 = (Integer) playerRecordData.obj5 + 1;
								playerRecordData.modifyData(p);
							}
							// }
						}

						if (isCompareRSA) {
							try {
								Object po = conn.getAttachmentData("ANDROID_PROCESS_REQ");
								if (po == null) {
									Integer NOnum = NO_PROCESS_REQNUM.get(p.getId());
									if (NOnum == null) {
										NOnum = new Integer(0);
									}
									NOnum = NOnum.intValue() + 1;
									NO_PROCESS_REQNUM.put(p.getId(), NOnum);
									logger.warn("[连接中未有ANDROID_PROCESS_REQ] [{}] [{}] [{}]", new Object[] { p.getLogString(), NOnum, conn.getRemoteAddress()});
									PlayerRecordData playerRecordData = recordData.get(p.getId());
									if (playerRecordData != null) {
											playerRecordData.obj7 = null;
									}
									if (EnterLimitManager.instance.isCloseConnet && NOnum >= noNum_MAX) {
										logger.warn("[ANDROID_PROCESS_REQ没返回] [{}] [{}] [{}]", new Object[] { p.getLogString(), NOnum, conn.getRemoteAddress()});
//										conn.close();
										if (playerRecordData != null) {
											playerRecordData.obj7 = Boolean.TRUE;
										}
									}
								}else {
									PlayerRecordData playerRecordData = recordData.get(p.getId());
									if (playerRecordData != null) {
										playerRecordData.obj7 = null;
								}
								}
							
								Object oo = conn.getAttachmentData("GET_RSA_DATA_REQ");
								if (oo == null) {
									Integer NOnum = NO_RSA_REQNUM.get(p.getId());
									if (NOnum == null) {
										NOnum = new Integer(0);
									}
									NOnum = NOnum.intValue() + 1;
									NO_RSA_REQNUM.put(p.getId(), NOnum);
									logger.warn("[连接中未有GET_RSA_DATA_REQ] [{}] [{}] [{}]", new Object[] { p.getLogString(), NOnum, conn.getRemoteAddress()});
									PlayerRecordData playerRecordData = recordData.get(p.getId());
									if (playerRecordData != null) {
										playerRecordData.obj1 = null;
										playerRecordData.obj6 = null;
									}
									if (EnterLimitManager.instance.isCloseConnet && NOnum >= noNum_MAX) {
										logger.warn("[GET_RSA_DATA_REQ没返回] [{}] [{}]", new Object[] { p.getLogString(), NOnum});
//										conn.close();
										if (playerRecordData != null) {
											playerRecordData.obj6 = Boolean.TRUE;
										}
									}
								} else {
									GET_RSA_DATA_REQ req = (GET_RSA_DATA_REQ) oo;
									String clientId = req.getClientID();
									String clientProgramVersion = req.getClientVersion();
									String platform = req.getPlatform();
									String realPlatform = req.getRealPlatform();
									String gpu = req.getGPU();
									String otherGpu = req.getOtherGPU();
									byte[] rsa_data = req.getRsa_data();
									if (p != null) {
										boolean isSame = EnterLimitManager.instance.compareRSA(rsa_data, platform);
										PlayerRecordData playerRecordData = EnterLimitManager.recordData.get(p.getId());
										if (playerRecordData != null) {
											playerRecordData.obj6 = null;
											if (playerRecordData.obj1 == null) {
												EnterLimitManager.logger.warn("[GET_RSA_DATA_REQ比较数据] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { p.getLogString(), clientId, clientProgramVersion, platform, realPlatform, gpu, otherGpu, rsa_data.length, isSame });
											} else if (Boolean.parseBoolean(playerRecordData.obj1.toString()) != isSame) {
												EnterLimitManager.logger.warn("[GET_RSA_DATA_REQ比较数据] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { p.getLogString(), clientId, clientProgramVersion, platform, realPlatform, gpu, otherGpu, rsa_data.length, isSame });
											}
											playerRecordData.obj1 = isSame;
										}
										if (!isSame) {
											if (EnterLimitManager.isSameRSAClose) {
												logger.warn("[GET_RSA_DATA_REQ不一致] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { p.getLogString(), clientId, clientProgramVersion, platform, realPlatform, gpu, otherGpu});
												conn.close();
											}
										}
									}
								}
							} catch (Exception e) {
								logger.error("isCompareRSA出错", e);
							}
						}
					}

					// 将新的数据放入老的列表
					for (Iterator<String> itor = temp.keySet().iterator(); itor.hasNext();) {
						String newIp = itor.next();
						EnterLimitData tempData = temp.get(newIp);
						if (likeNeedLimit.containsKey(newIp)) {
							EnterLimitData enterLimitData = likeNeedLimit.get(newIp);
							for (Long playerId : tempData.playerIds) {
								enterLimitData.playerIds.add(playerId);
							}
						} else {
							likeNeedLimit.put(newIp, tempData);
						}
					}
					temp.clear();

//					if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯) && !"百里沃野".equals(GameConstants.getInstance().getServerName())) {
//						{
//							// 自动封号
//
//							// A台湾版本,同组大于50个用户,封号
//							if (openOfsameIP && PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
//								try {
//									for (Iterator<String> itor = likeNeedLimit.keySet().iterator(); itor.hasNext();) {
//										String ip = itor.next();
//										EnterLimitData elds = likeNeedLimit.get(ip);
//										if (elds.playerIds.size() >= autoLimitEnter_insameIP) {
//											// 封所有的号
//											Set<String> usernames = new HashSet<String>();
//											for (PlayerRecordData prd : elds.recordDatas) {
//												String userName = prd.username;
//												usernames.add(userName);
//											}
//											String[] usernameArr = usernames.toArray(new String[0]);
//											putTolimit(ip, usernameArr);
//											logger.warn("[自动封号] [同一Ip下有太多用户] [" + usernameArr.length + "] [被封用户:" + Arrays.toString(usernameArr) + "]");
//										}
//									}
//								} catch (Exception e) {
//									logger.error("自动封同一IP用户异常", e);
//								}
//							}
//							// B 规则内的自动封号
//							// 检测是否要自动封号
//							// 只考虑在线用户即可,
//							for (Player p : players) {
//								if (p.getSoulLevel() > autoLimitEnter_maxLevel || p.getRMB() > autoLimitEnter_minRMB) {
//									continue;
//								}
//								PlayerRecordData playerRecordData = getPlayerRecordData(p.getId());
//								if (playerRecordData != null) {
//									boolean limitEnter = AutoLimitEnterManager.getInstance().needLimitEnter(playerRecordData);
//									if (limitEnter) {
//										logger.warn("[自动封号] [账号被封] [" + p.getLogString() + "]");
//										putTolimit("", new String[] { p.getUsername() });
//									}
//								}
//							}
//						}
//					}
				}
				// if (start - lastSaveTime > SAVE_SEPT) {
				// diskCache.put(likeNeedLimit_KEY, (Serializable) likeNeedLimit);
				// diskCache.put(recordData_KEY, (Serializable) recordData);
				// this.lastSaveTime = SystemTime.currentTimeMillis();
				// }
			} catch (Exception e) {
				logger.warn("[疑似外挂] [心跳结束] [异常] [cost:" + (SystemTime.currentTimeMillis() - start) + "ms]", e);
			}

			logger.warn("[疑似外挂] [心跳结束] [IP组:" + likeNeedLimit.size() + "个] [cost:" + (SystemTime.currentTimeMillis() - start) + "ms]");
		}
	}

	public static int autoLimitEnter_insameIP = 50;// 同ip用户数量
	public static int autoLimitEnter_runTimes = 5;// 心跳次数
	public static int autoLimitEnter_maxLevel = 110;// 监测的最高等级
	public static int autoLimitEnter_minRMB = 1000;// 监测的最少充值金额
	public static int autoLimitEnter_sellEmpty = 8;// 出售空格子数量

	public static boolean openOfsameIP = true;

	/**
	 * 进入限制数据集合
	 * 
	 * 
	 */
	public static class EnterLimitData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3166503960093753420L;

		public String ip;

		public Set<Long> playerIds = new HashSet<Long>();

		public List<PlayerRecordData> recordDatas = new ArrayList<EnterLimitManager.PlayerRecordData>();

		public void copyOf(EnterLimitData data, int lowLevel) {
			this.ip = data.ip;
			this.playerIds = new HashSet<Long>(data.playerIds);
			Iterator<Long> playerIdItor = this.playerIds.iterator();
			while (playerIdItor.hasNext()) {
				Long id = playerIdItor.next();
				PlayerRecordData playerRecordData = EnterLimitManager.getPlayerRecordData(id);
				if (playerRecordData != null) {
					if (playerRecordData.level < lowLevel) {
						playerIdItor.remove();// 移除不符合条件的ID
					} else {
						if (player_process.containsKey(id)) {
							playerRecordData.obj2 = player_process.get(id).isMoniqi();
						} else {
							playerRecordData.obj2 = null;
						}
					}
				}
			}
		}

		public int size() {
			return playerIds.size();
		}
	}

	/**
	 * 玩家的一些数据的记录
	 * 
	 * 
	 */
	public static class PlayerRecordData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3405504217346402997L;

		public PlayerRecordData(Player p, String network, String phoneType) {
			this.username = p.getUsername();
			this.playername = p.getName();
			this.network = network;
			this.playerId = p.getId();
			this.ua = phoneType;
			this.obj5 = Integer.valueOf(0);
			Passport passport = p.getPassport();
			if (passport != null) {
				// this.channel = passport.getLastLoginChannel();
				this.registerTime = TimeTool.formatter.varChar19.format(passport.getRegisterDate());
			}
			modifyDataFromConnection(p);
		}

		/**
		 * 只更新和玩家数据相关的
		 * @param p
		 */
		public void modifyData(Player p) {
			this.level = p.getLevel();
			Game game = p.getCurrentGame();
			if (game != null) {
				this.gameName = game.gi.displayName;
			}
			this.rmb = p.getRMB();
			this.jiazuName = p.getJiazuName();
			this.currentTili = p.getVitality();
			this.classLevel = p.getClassLevel();
			this.packageSize = p.getKnapsack_common().size();
			int currentPetNum = p.getPetKnapsack().size() - p.getPetKnapsack().getEmptyNum();
			this.maxPetNum = Math.max(currentPetNum, this.maxPetNum);
			int currentHorseNum = p.getMainSoul().getHorseArr().size();
			this.maxHorseNum = Math.max(currentHorseNum, this.maxHorseNum);
			int currentPurpleEquipmentNum = 0;
			int currentOrangeEquipmentNum = 0;
			EquipmentColumn equipmentColumns = p.getEquipmentColumns();
			for (Long equipId : equipmentColumns.getEquipmentIds()) {
				if (equipId != null && equipId > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equipId);
					if (ae != null) {
						int colorType = ae.getColorType();
						if (colorType >= 3) {
							currentPurpleEquipmentNum++;
						}
						if (colorType >= 5) {
							currentOrangeEquipmentNum++;
						}
					}
				}
			}
			this.purpleEquipmentNum = Math.max(currentPurpleEquipmentNum, this.purpleEquipmentNum);
			this.orangeEquipmentNum = Math.max(currentOrangeEquipmentNum, this.orangeEquipmentNum);
			List<TaskEntity> allTask = p.getAllTask();
			int mainTaskLevel = -1;
			if (allTask != null) {
				for (TaskEntity te : allTask) {
					if (te != null) {
						Task t = te.getTask();
						if (t != null) {
							if (t.getShowType() == 0) {// 主线
								mainTaskLevel = t.getGrade();
								break;
							}
						}
					}
				}
			}
			if (mainTaskLevel == -1) {
				List<Long> nextCanAcceptTasks = p.getNextCanAcceptTasks();
				if (nextCanAcceptTasks != null) {
					for (Long nextId : nextCanAcceptTasks) {
						Task task = TaskManager.getInstance().getTask(nextId);
						if (task != null) {
							if (task.getShowType() == 0) {// 主线
								mainTaskLevel = task.getGrade();
								break;
							}
						}
					}
				}
			}
			this.mainTaskLevel = mainTaskLevel;
			modifyDataFromConnection(p);

			UserData userData = DefaultValidateManager.getInstance().getUserData(p);
			if (userData != null) {
				this.answerRightTimes = userData._answerRightTimes;
				this.answerWrongTimes = userData._answerWrongTimes;
				this.answerAcceptTimes = userData._needAnswerTimes;
				this.answerNoresponseTimes = (this.answerAcceptTimes - this.answerRightTimes - this.answerWrongTimes);
			}
			changeModifyTime();
		}

		public void modifyDataFromConnection(Player player) {
			Connection conn = player.getConn();
			if (conn == null) {
				return;
			}
			Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
			if (o instanceof USER_CLIENT_INFO_REQ) {
				USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) o;
				this.network = req.getNetwork();
				this.obj0 = req.getGpuOtherName();
				this.ua = req.getPhoneType();
			}
		}

		public void changeModifyTime() {
			this.lastModifyTime = SystemTime.currentTimeMillis();
		}

		public long lastModifyTime = SystemTime.currentTimeMillis();
		// 初始化数据
		public String username;
		public String playername;
		public String network;
		public long playerId;
		public String ua;
		public String channel;
		// 账号注册时间
		public String registerTime;
		/* #################################################################################################################### */
		public boolean online;
		public int level;
		public String gameName;
		public long rmb;
		// 当前主线任务级别
		public int mainTaskLevel;
		// 家族名字
		public String jiazuName;
		// 当前体力
		public int currentTili;
		// 最大坐骑数量
		public int maxHorseNum;
		// 最大宠物数量
		public int maxPetNum;
		// 防外挂答题接收次数
		public int answerAcceptTimes;
		// 防外挂答题正确次数
		public int answerRightTimes;
		// 防外挂答题错误次数
		public int answerWrongTimes;
		// 防外挂答题未答次数
		public int answerNoresponseTimes;
		// 背包格子数量
		public int packageSize;
		// 玩家身上紫装数量(包括完美)
		public int purpleEquipmentNum;
		// 玩家身上橙装数量(包括完美)
		public int orangeEquipmentNum;
		// 当前境界
		public int classLevel;

		// #########################################需要另外记录的数据#########################################//
		// 聊天次数
		public int chatTimes;
		// 喝酒次数
		public int drinkTimes;
		// 出售空格子次数
		public int sealEmptyCellTimes;
		// 接受体力任务次数
		public int tiliTaskAcceptTimes;
		// 求购次数
		public int requestBuyTimes;
		// 摆摊次数
		public int boothSaleTimes;
		// 同意国战次数
		public int guozhanAcceptTimes;
		// 发送带附件的邮件数
		public int sendArticleMailTimes;
		// 快速出售次数
		public int fastSellTimes;
		// 种植次数
		public int plantNum;
		// 合成绑定次数
		public int composeBindTimes;

		public Object obj0; // cpuOther
		public Object obj1; // 是否签名一致
		public Object obj2; // 是否是模拟器
		public Object obj3; // 客户端版本
		public Object obj4; // ClientPlatform
		public Object obj5; // 心跳次数
		public Object obj6;	//是否发送签名协议
		public Object obj7;	//是否发送进程协议
		public Object obj8;
		public Object obj9;
	}

	public static enum PlayerRecordType {
		聊天次数,
		喝酒次数,
		出售空格子次数,
		完成体力任务次数,
		求购次数,
		摆摊次数,
		同意国战次数,
		发送带附件的邮件数,
		快速出售次数,
		种植次数,
		合成绑定次数, ;
	}

	public static void setValues(Player p, PlayerRecordType playerRecordType) {
		try {
			setValues(p, playerRecordType, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setValues(Player p, PlayerRecordType recordType, int value) {
		try {
			PlayerRecordData playerRecordData = getPlayerRecordData(p.getId());
			if (playerRecordData != null) {
				switch (recordType) {
				case 聊天次数:
					playerRecordData.chatTimes += value;
					break;
				case 喝酒次数:
					playerRecordData.drinkTimes += value;
					break;
				case 出售空格子次数:
					playerRecordData.sealEmptyCellTimes += value;
					break;
				case 完成体力任务次数:
					playerRecordData.tiliTaskAcceptTimes += value;
					break;
				case 求购次数:
					playerRecordData.requestBuyTimes += value;
					break;
				case 摆摊次数:
					playerRecordData.boothSaleTimes += value;
					break;
				case 同意国战次数:
					playerRecordData.guozhanAcceptTimes += value;
					break;
				case 发送带附件的邮件数:
					playerRecordData.sendArticleMailTimes += value;
					break;
				case 快速出售次数:
					playerRecordData.fastSellTimes += value;
					break;
				case 种植次数:
					playerRecordData.plantNum += value;
					break;
				case 合成绑定次数:
					playerRecordData.composeBindTimes += value;
					break;
				default:
					break;
				}
				playerRecordData.changeModifyTime();
			}
			logger.warn(p.getLogString() + "[外挂监控,增加记录] [" + recordType.name() + "] [" + value + "]");
		} catch (Exception e) {
			logger.error(p.getLogString() + "[外挂监控,增加记录] [异常]", e);
		}
	}

	public void setAndroid_rsa_path(String android_rsa_path) {
		this.android_rsa_path = android_rsa_path;
	}

	public String getAndroid_rsa_path() {
		return android_rsa_path;
	}

	public void setAndroid_rsa1_path(String android_rsa1_path) {
		this.android_rsa1_path = android_rsa1_path;
	}

	public String getAndroid_rsa1_path() {
		return android_rsa1_path;
	}

	public void setAndroid_malai_path(String android_malai_path) {
		this.android_malai_path = android_malai_path;
	}

	public String getAndroid_malai_path() {
		return android_malai_path;
	}

	public void setAndroid_huawei_path(String android_huawei_path) {
		this.android_huawei_path = android_huawei_path;
	}

	public String getAndroid_huawei_path() {
		return android_huawei_path;
	}
	
	
	public static void sendMail(String title, String content) {
		GameConstants gc = GameConstants.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<HR>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		String address_to = "";

		String[] addresses = {"3472335707@qq.com"};
//		String[] addresses = {"3472335707@qq.com","116004910@qq.com"};
		if (addresses != null) {
			for (String address : addresses) {
				address_to += address + ",";
			}
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			if(gc != null){
				args.add(title + "["+gc.getServerName()+"]");
			}else{
				args.add(title);
			}
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			//System.out.println(Arrays.toString(args.toArray(new String[0])));
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
				//System.out.println(Arrays.toString(args.toArray(new String[0])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
