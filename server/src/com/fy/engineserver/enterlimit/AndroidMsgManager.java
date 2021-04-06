package com.fy.engineserver.enterlimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.DENY_USER_REQ;
import com.fy.engineserver.message.GET_SOME4ANDROID_1_REQ;
import com.fy.engineserver.message.GET_SOME4ANDROID_1_RES;
import com.fy.engineserver.message.GET_SOME4ANDROID_REQ;
import com.fy.engineserver.message.GET_SOME4ANDROID_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.SpriteSubSystem;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.boss.game.GameConstants;

public class AndroidMsgManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(AndroidMsgManager.class);
	
	private static AndroidMsgManager instance;
	
	public static ConcurrentHashMap<Long, AndroidMsgData> msgDatas = new ConcurrentHashMap<Long, AndroidMsgData>();
	
	public static boolean isRunAble = true;
	public static long sleepTime = 1000L*30;
	
	public static long getValueSpace = 1000L*60*1;
	
	//这个是因为客户端一次调用太多jni方法会内存出问题
	public static int getProcessMaxNum = 10;
	
	//自动封号配置参数
	public static boolean isAutoFeng = false;
	public static long lastCheckTime = 0L;					//上次检查时间
	public static long checkTimeSpace = 1000L*60*55;		//检查间隔jius
	public static int sameWIFI_Num = 6;						//相同wifi下数目   在线不在线的
	public static int rmbLimit = 10;						//充值多少的不封
	
	public static int sameIP_Num = 4;						//相同IP下数目  在线的
	public static long lastCheckTime1 = 0L;			 		//检查在线的时间
	public static long checkTimeSpace1 = 1000L*60*21;		//检查间隔
	
	public static boolean isCheckRMBFengHao = false;			//是否检查充值玩家同IP下超过一定数目在线并且电池温度为0
	public static int rmbFengHao_sameIP_Num = 10;
	public static int rmbFengHao_fenghaoTime = 1;			//1小时
	public static long lastCheckTime2 = 0L;					//检查时间
	public static long checkTimeSpace2 = 1000L*60*10;		//5分钟检查一次
	
	/**
	 * 不取android信息的服务器名字，苹果国内服不取。
	 */
	public static List<String> unGetAndroidMsgServerNames = new ArrayList<String>();
	static {
	}
	
	public void init() {
		new Thread(this, "AndroidMsgManager").start();
		
//		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
//			isAutoFeng = true;
//		}
	}

	public static AndroidMsgManager getInstance() {
		if (instance == null) {
			instance = new AndroidMsgManager();
			instance.init();
		}
		return instance;
	}
	
	//这条协议 在玩家一次进入只发一次，他会取回一些冗余信息，clientID等
	public void handle_GET_SOME4ANDROID_REQ (GET_SOME4ANDROID_REQ req, Player player) {
		try {
			String[] vs = req.getValue();
			AndroidMsgData data = msgDatas.get(player.getId());
			if (data == null) {
				data = new AndroidMsgData();
				msgDatas.put(player.getId(), data);
			}
			data.setpID(player.getId());
			for (int i = 0; i < vs.length-1; i++) {
				data.getReqInfos().add(vs[i]);
			}
			handle(player, GET_CLIENT_MAC, vs[vs.length-1]);
			logger.warn("[收到android信息] ["+player.getLogString()+"] ["+Arrays.toString(vs)+"]");
		} catch(Exception e) {
			logger.error("handle_GET_SOME4ANDROID_REQ出错:["+ player.getLogString()+"]["+Arrays.toString(req.getValue())+"]", e);
		}
	}
	
	public void handle_GET_SOME4ANDROID_1_REQ(GET_SOME4ANDROID_1_REQ req, Player player) {
		String key = req.getKeyName();
		String value = req.getValue();
		handle(player, key, value);
		logger.warn("[收到a信息] [key="+key+"] [v="+value+"]");
	}
	
	public void handle(Player player, String key, String value) {
		try {
			AndroidMsgData data = msgDatas.get(player.getId());
			if (data == null) {
				logger.warn("[接收协议出错-对象不存在] ["+player.getLogString()+"] [key="+key+"] [v="+value+"]");
				return;
			}
			data.getSendTimes().put(key, -1L);
			if (key.equals(GET_PROCESS_LENGTH_KEY)) {
				//进程数目
				int len = Integer.parseInt(value);
				data.setProcessNames(new String[len]);
				len = len > getProcessMaxNum ? getProcessMaxNum : len;
				if (len > 0) {
					for (int i = 0 ; i < len; i++) {
						sendGetProcJess(player, i);
					}
				}
			}else if (key.startsWith(GET_PROCESS_INDEX_KEY)) {
				//进程
				int index = Integer.parseInt(key.substring(GET_PROCESS_INDEX_KEY.length(), key.length()));
				data.getProcessNames()[index] = value;
			}else if (key.equals(GET_CLIENT_MAC)) {
				//mac地址
				data.setMac(value);
				//发送获取进程数目协议
				sendGetProcess_length(player);
			}else if (key.equals(GET_BATTERY_WENDU)) {
				//温度
				data.setBatteryTemperature(Integer.parseInt(value));
			}else if (key.equals(GET_BATTERY_MA)) {
				//毫安数
				data.setBatteryMA(Integer.parseInt(value));
			}else if (key.equals(GET_BATTERY_VALUE)) {
				//电量
				data.getSendTimes().put(key, System.currentTimeMillis());
				if (value.equals("") || value.length() <= 0) {
					value="-1";
				}
				data.addBatteryValue(Integer.parseInt(value));
			}else if (key.equals(GET_CLIENT_NETTYPE)) {
				//联网方式
				if (value.equals("") || value.length() <= 0) {
					value="-1";
				}
				data.setNetType(Integer.parseInt(value));
			}else if (key.equals(GET_CLIENT_NETNAME)) {
				//联网方式名字
				data.setNetName(value);
			}else if (key.equals(GET_CLIENT_WIFINAME)) {
				//wifi名字
				data.setWifiName(value);
			}else if (key.equals(GET_CLIENT_WIFIRSSI)) {
				//wifi信号
				data.getSendTimes().put(key, System.currentTimeMillis());
				if (value.equals("") || value.length() <= 0) {
					value="-1";
				}
				data.addWifiRssi(Integer.parseInt(value));
			}else if(key.equals(GET_CLIENT_RSA))
			{
				data.setAndroid_rsa(value);
			}
			logger.warn("[收到协议处理] ["+player.getLogString()+"] [key:"+key+"] ["+value+"]");
		} catch (Exception e) {
			logger.error("处理消息出错:", e);
		}
	}
	
	/**
	 * 取进程数目,取到之后再取各个进程的信息
	 * @param player
	 */
	public static String GET_PROCESS_LENGTH_KEY = "processLengthKey";
	public void sendGetProcess_length (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getRunningAppProcesses", "size"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "activity", "0", "0"};
		sendMessage(player, true, GET_PROCESS_LENGTH_KEY, cName, mNames, types, args);
	}
	
	/**
	 * 取进程信息
	 * @param player
	 */
	public static String GET_PROCESS_INDEX_KEY = "processIndexKey";
	public void sendGetProcJess(Player player, int index) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getRunningAppProcesses", "get", "!vprocessName"};
		String[] types = {"1", "java.lang.String", "0", "1", "int", "0"};
		String[] args = {"1", "activity", "0", "1", index+"", "0"};
		sendMessage(player, true, GET_PROCESS_INDEX_KEY+index, cName, mNames, types, args);
	}
	
	/**
	 * 取电池温度
	 * @param player
	 */
	public static String GET_BATTERY_WENDU = "batteryWD";
	public void sendGetBatteryTemperature (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getApplicationContext", "registerReceiver", "getIntExtra"};
		String[] types = {"0", "2", "android.content.BroadcastReceiver", "android.content.IntentFilter", "2", "java.lang.String", "int"};
		String[] args = {"0","2", "null", "!Newandroid.content.IntentFilter,java.lang.String,android.intent.action.BATTERY_CHANGED", "2", "temperature",""+0};
		sendMessage(player, true, GET_BATTERY_WENDU, cName, mNames, types, args);
	}
	
	/**
	 * 取电池毫安数
	 * @param player
	 */
	public static String GET_BATTERY_MA = "batteryMA";
	public void sendGetBatteryMA (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getApplicationContext", "registerReceiver", "getIntExtra"};
		String[] types = {"0", "2", "android.content.BroadcastReceiver", "android.content.IntentFilter", "2", "java.lang.String", "int"};
		String[] args = {"0","2", "null", "!Newandroid.content.IntentFilter,java.lang.String,android.intent.action.BATTERY_CHANGED", "2", "voltage",""+0};
		sendMessage(player, true, GET_BATTERY_MA, cName, mNames, types, args);
	}
	
	/**
	 * 取电池容量
	 * @param player
	 */
	public static String GET_BATTERY_VALUE = "batteryValue";
	public void sendGetBatteryValue(Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getApplicationContext", "registerReceiver", "getIntExtra"};
		String[] types = {"0", "2", "android.content.BroadcastReceiver", "android.content.IntentFilter", "2", "java.lang.String", "int"};
		String[] args = {"0","2", "null", "!Newandroid.content.IntentFilter,java.lang.String,android.intent.action.BATTERY_CHANGED", "2", "level",""+0};
		sendMessage(player, true, GET_BATTERY_VALUE, cName, mNames, types, args);
	}
	
	/**
	 * 取mac地址
	 * @param player
	 */
	//TODO:这个方法是开始，取了mac地址后就会自动取进程信息和其他信息
	public static String GET_CLIENT_MAC = "cMac";
	public void sendGetMac (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getConnectionInfo", "getMacAddress"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "wifi", "0", "0"};
		sendMessage(player, false, GET_CLIENT_MAC, cName, mNames, types, args);
	}
	/**
	 * 取mac地址
	 * @param player
	 */
	//TODO:这个方法是开始，取了mac地址后就会自动取进程信息和其他信息
	public static String GET_CLIENT_RSA = "cRSA";
	public void sendGetRSA (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getCertificateSHA1Fingerprint"};
		String[] types = { "0"};
		String[] args = { "0"};
		sendMessage(player, true, GET_CLIENT_RSA, cName, mNames, types, args);
	}
	
	/**
	 * 取联网方式
	 * @param player
	 */
	public static String GET_CLIENT_NETTYPE = "cNetType";
	public void sendGetNetType (Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getActiveNetworkInfo", "getType"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "connectivity", "0", "0"};
		sendMessage(player, true, GET_CLIENT_NETTYPE, cName, mNames, types, args);
	}
	
	/**
	 * 取联网方式名字
	 * @param player
	 */
	public static String GET_CLIENT_NETNAME = "cNetName";
	public void sendGetNetName(Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getActiveNetworkInfo", "getTypeName"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "connectivity", "0", "0"};
		sendMessage(player, true, GET_CLIENT_NETNAME, cName, mNames, types, args);
	}
	
	/**
	 * 取wifi的名字
	 * @param player
	 */
	public static String GET_CLIENT_WIFINAME = "cWifiName";
	public void sendGetWifiName(Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getConnectionInfo", "getSSID"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "wifi", "0", "0"};
		sendMessage(player, true, GET_CLIENT_WIFINAME, cName, mNames, types, args);
	}
	
	/**
	 * 取wifi信号强度
	 * @param player
	 */
	public static String GET_CLIENT_WIFIRSSI = "cWifiRssi";
	public void sendGetWifiRssi(Player player) {
		String cName = "org.cocos2dx.tests.TestsDemo";
		String[] mNames = {"getSystemService", "getConnectionInfo", "getRssi"};
		String[] types = {"1", "java.lang.String", "0", "0"};
		String[] args = {"1", "wifi", "0", "0"};
		sendMessage(player, true, GET_CLIENT_WIFIRSSI, cName, mNames, types, args);
	}
	
	/**
	 * 取OS arch
	 * @param player
	 */
	public static String GET_CLIENT_OSARCH = "cOsArch";
	public void sendGetOsArch(Player player) {
		String cName = "java.lang.System";
		String[] mNames = {"getProperty"};
		String[] types = {"1", "java.lang.String"};
		String[] args = {"1", "os.arch"};
		sendMessage(player, true, GET_CLIENT_OSARCH, cName, mNames, types, args);
	}
	
	public void sendMessage (Player player, boolean isSimple, String key, String cName, String[] mNames, String[] types, String[] args) {
		if (isSimple) {
			AndroidMsgData data = msgDatas.get(player.getId());
			if (data != null) {
				data.getSendTimes().put(key, System.currentTimeMillis());
			}
			GET_SOME4ANDROID_1_RES res = new GET_SOME4ANDROID_1_RES(GameMessageFactory.nextSequnceNum(), key, cName, mNames, types, args);
			if (player.isOnline()) {
				player.addMessageToRightBag(res);
			}
		}else {
			GET_SOME4ANDROID_RES res = new GET_SOME4ANDROID_RES(GameMessageFactory.nextSequnceNum(), key, cName, mNames, types, args);
			if (player.isOnline()) {
				AndroidMsgData data = new AndroidMsgData();
				data.setpID(player.getId());
				data.getSendTimes().put(key, System.currentTimeMillis());
				msgDatas.put(player.getId(), data);
				player.addMessageToRightBag(res);
			}
		}
	}

	public void run() {
		while(isRunAble) {
			try {
				Thread.sleep(sleepTime);
				logger.warn("心跳执行");
				long startTime = System.currentTimeMillis();
				for (Long id : msgDatas.keySet()) {
					AndroidMsgData data = msgDatas.get(id);
					if (!PlayerManager.getInstance().isOnline(id)) {
						continue;
					}
					Player player = PlayerManager.getInstance().getPlayer(id);
					Long mac = data.getSendTimes().get(GET_CLIENT_MAC);
					if (mac != null) {
						if (mac == -1L || startTime - mac > 30*1000) {
							Long wendu = data.getSendTimes().get(GET_BATTERY_WENDU);
							if (wendu == null) {
								sendGetBatteryTemperature(player);
							}
							Long ma = data.getSendTimes().get(GET_BATTERY_MA);
							if (ma == null) {
								sendGetBatteryMA(player);
							}
							Long bValue = data.getSendTimes().get(GET_BATTERY_VALUE);
							if (bValue == null) {
								sendGetBatteryValue(player);
							}else {
								if (System.currentTimeMillis() - bValue > getValueSpace) {
									sendGetBatteryValue(player);
								}
							}
							Long netType = data.getSendTimes().get(GET_CLIENT_NETTYPE);
							if (netType == null) {
								sendGetNetType(player);
								sendGetNetName(player);
							}else {
								if (data.getNetType() == 1) {
									//如果是wifi
									Long wifi = data.getSendTimes().get(GET_CLIENT_WIFINAME);
									if (wifi == null) {
										sendGetWifiName(player);
										sendGetWifiRssi(player);
									}else {
										Long wifiS = data.getSendTimes().get(GET_CLIENT_WIFIRSSI);
										if (System.currentTimeMillis() - wifiS > getValueSpace) {
											sendGetWifiRssi(player);
										}
									}
								}
							}
						}
					}
				}
				
				try {
					if (isAutoFeng && !unGetAndroidMsgServerNames.contains(GameConstants.getInstance().getServerName())) {
						long now = System.currentTimeMillis();
						if (lastCheckTime == 0) {
							lastCheckTime = now;
						}
						if (now - lastCheckTime > checkTimeSpace) {
							lastCheckTime = now;
							//封blueStacks
							ArrayList<AndroidMsgData> fengDatas = new ArrayList<AndroidMsgData>();
							long fengSilver = 0L;
							for (Long key : msgDatas.keySet()) {
								AndroidMsgData data = msgDatas.get(key);
								if (data.getWifiName() != null && "BlueStacks".equalsIgnoreCase(data.getWifiName())) {
									Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
									if (pp == null) {
										fengDatas.add(data);
										continue;
									}
									if (pp.getRMB() < rmbLimit) {
										fengDatas.add(data);
										String pUserName = pp.getUsername();
										if (!EnterLimitManager.getInstance().limited.contains(pUserName)) {
											EnterLimitManager.getInstance().limited.add(pUserName);
											fengSilver += pp.getSilver();
										}else {
											continue;
										}
										if (pp.getPassport() == null) {
											Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(pp.getUsername());
											pp.setPassport(passport);
										}
										if (!pp.getUsername().equals(pp.getPassport().getUserName())) {
											if (!EnterLimitManager.getInstance().limited.contains(pp.getPassport().getUserName())) {
												EnterLimitManager.getInstance().limited.add(pp.getPassport().getUserName());
											}
										}
										logger.warn("[通过自动封号] [原因:wifi名字叫BlueStacks] ["+pp.getLogString()+"] ["+data.toString()+"]");
										if (pp.isOnline()) {
											pp.getConn().close();
										}
									}
								}
							}
							for (AndroidMsgData data : fengDatas) {
								AndroidMsgManager.msgDatas.remove(data.getpID());
							}
							logger.warn("[通过自动封号] [封掉blueStacks:"+fengDatas.size()+"个] [封掉银子:"+fengSilver+"]");
							
							fengSilver = 0L;
							fengDatas.clear();
							
							HashMap<String, ArrayList<AndroidMsgData>> sameWifis = new HashMap<String, ArrayList<AndroidMsgData>>();
							for (Long key : msgDatas.keySet()) {
								AndroidMsgData data = msgDatas.get(key);
								if (data.getWifiName() != null && !"没".equals(data.getWifiName()) && data.getBatteryTemperature() == 0) {
									ArrayList<AndroidMsgData> datas = sameWifis.get(data.getWifiName());
									if (datas == null) {
										datas = new ArrayList<AndroidMsgData>();
										sameWifis.put(data.getWifiName(), datas);
									}
									datas.add(data);
								}
							}
							
							for (String key : sameWifis.keySet()) {
								ArrayList<AndroidMsgData> datas = sameWifis.get(key);
								if (datas.size() >= sameWIFI_Num) {
									int realNum = 0;
									for (int i = 0 ; i < datas.size(); i++) {
										AndroidMsgData data = datas.get(i);
										Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
										if (pp == null) {
											continue;
										}
										if (pp.getRMB() < rmbLimit) {
											realNum++;
										}
									}
									if (realNum >= sameWIFI_Num) {
										String userName = "";
										for (int i = 0; i < datas.size(); i++) {
											AndroidMsgData data = datas.get(i);
											Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
											if (pp == null) {
												fengDatas.add(data);
												continue;
											}
											if (pp.getRMB() < rmbLimit) {
												fengDatas.add(data);
												String pUserName = pp.getUsername();
												if (!EnterLimitManager.getInstance().limited.contains(pUserName)) {
													EnterLimitManager.getInstance().limited.add(pUserName);
													fengSilver += pp.getSilver();
													userName = userName + "," + pUserName;
												}else {
													continue;
												}
												if (pp.getPassport() == null) {
													Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(pp.getUsername());
													pp.setPassport(passport);
												}
												if (!pp.getUsername().equals(pp.getPassport().getUserName())) {
													if (!EnterLimitManager.getInstance().limited.contains(pp.getPassport().getUserName())) {
														EnterLimitManager.getInstance().limited.add(pp.getPassport().getUserName());
													}
												}
												logger.warn("[通过自动封号] [原因:电池温度为0并且同wifi下超过"+sameWIFI_Num+"个] ["+pp.getLogString()+"] ["+data.toString()+"]");
												if (pp.isOnline()) {
													pp.getConn().close();
												}
											}
										}
										logger.warn("[通过自动封号] [原因:电池温度为0] [用户集合] ["+userName+"]");
									}
								}
							}
							
							for (AndroidMsgData data : fengDatas) {
								AndroidMsgManager.msgDatas.remove(data.getpID());
							}
							logger.warn("[通过自动封号] [封掉wifi相同且电池温度为0:"+fengDatas.size()+"个] [封掉银子:"+fengSilver+"]");
							
						}
						
						now = System.currentTimeMillis();
						if (lastCheckTime1 == 0) {
							lastCheckTime1 = now;
						}
						if (now - lastCheckTime1 >= checkTimeSpace1 && !PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
							lastCheckTime1 = now;
							ArrayList<AndroidMsgData> fengDatas = new ArrayList<AndroidMsgData>();
							long fengYinzi = 0L;
							//封IP相同的在线玩家
							HashMap<String, ArrayList<AndroidMsgData>> sameIps = new HashMap<String, ArrayList<AndroidMsgData>>();
							for (Long key : msgDatas.keySet()) {
								AndroidMsgData data = msgDatas.get(key);
								if (PlayerManager.getInstance().isOnline(data.getpID()) && data.getBatteryTemperature() == 0) {
									Player player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
									if (player.getRMB() < rmbLimit) {
										String ipAddress = player.getConn().getRemoteAddress();
										ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
										ArrayList<AndroidMsgData> datas = sameIps.get(ipAddress);
										if (datas == null) {
											datas = new ArrayList<AndroidMsgData>();
										}
										datas.add(data);
										sameIps.put(ipAddress, datas);
									}
								}
							}
							
							for (String key : sameIps.keySet()) {
								ArrayList<AndroidMsgData> datas = sameIps.get(key);
								if (datas.size() >= sameIP_Num) {
									String userName = "";
									for (int i = 0; i < datas.size(); i++) {
										AndroidMsgData data = datas.get(i);
										fengDatas.add(data);
										Player player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
										if (player != null) {
											String pUserName = player.getUsername();
											if (!EnterLimitManager.getInstance().limited.contains(pUserName)) {
												EnterLimitManager.getInstance().limited.add(pUserName);
												fengYinzi += player.getSilver();
												userName = userName + "," + pUserName;
											}else {
												continue;
											}
											if (player.getPassport() == null) {
												Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(player.getUsername());
												player.setPassport(passport);
											}
											if (!player.getUsername().equals(player.getPassport().getUserName())) {
												if (!EnterLimitManager.getInstance().limited.contains(player.getPassport().getUserName())) {
													EnterLimitManager.getInstance().limited.add(player.getPassport().getUserName());
												}
											}
											logger.warn("[通过自动封号] [原因:电池温度为0并且同IP下超过"+sameIP_Num+"个] ["+player.getLogString()+"] ["+data.toString()+"]");
											if (player.isOnline()) {
												player.getConn().close();
											}
										}
									}
								}
							}
							for (AndroidMsgData data : fengDatas) {
								AndroidMsgManager.msgDatas.remove(data.getpID());
							}
							logger.warn("[通过自动封号] [封掉wifi相同且电池温度为0:"+fengDatas.size()+"个] [封掉银子:"+fengYinzi+"]");
						}
						
						if (isCheckRMBFengHao) {
							now = System.currentTimeMillis();
							if (lastCheckTime2 == 0L) {
								lastCheckTime2 = now;
							}
							if (now - lastCheckTime2 > checkTimeSpace2 && !PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
								//封IP相同的在线玩家
								HashMap<String, ArrayList<AndroidMsgData>> sameIps = new HashMap<String, ArrayList<AndroidMsgData>>();
								for (Long key : msgDatas.keySet()) {
									AndroidMsgData data = msgDatas.get(key);
									if (PlayerManager.getInstance().isOnline(data.getpID()) && data.getBatteryTemperature() == 0) {
										Player player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
										String ipAddress = player.getConn().getRemoteAddress();
										ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
										ArrayList<AndroidMsgData> datas = sameIps.get(ipAddress);
										if (datas == null) {
											datas = new ArrayList<AndroidMsgData>();
										}
										datas.add(data);
										sameIps.put(ipAddress, datas);
									}
								}
								
								for (String key : sameIps.keySet()) {
									ArrayList<AndroidMsgData> list = sameIps.get(key);
									if (list.size() > rmbFengHao_sameIP_Num) {
										for (AndroidMsgData data : list) {
											Player p = PlayerManager.getInstance().getPlayer(data.getpID());
											DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", p.getUsername(), Translate.您此账号存在异常情况被临时封号 , GameConstants.getInstance().getServerName() + "-外挂检测模块",
													false, true, false, 0, rmbFengHao_fenghaoTime);
											MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
											p.getConn().close();
										}
									}
								}
							}
						}
					}
				} catch(Exception e) {
					logger.error("自动封号出错:", e);
				}
				
			} catch(Exception e) {
				logger.error("心跳出错:", e);
			}
		}
	}
}
