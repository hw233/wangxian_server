<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.SessionManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientWaiGuaProcessManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>

<%!
	public boolean isLoad = false;
	public static HashSet<String> noFengUsers = new HashSet<String>();

	public static long CLEAR_SPACE_TIME = 1000L*60*60*5;
	public static long AUTO_LIMITED_SPACETIME = 1000L*60*30;
	public static long AUTO_GETDATA_SPACETIME = 1000L*60*5;

	public static boolean isAutoLimited = false;
	public static long lastAutoTime = 0L;
	public static boolean isAutoLimitedNoMsg = false;
	public static int autoNoMsgNum = 10;
	public static boolean isLimitedByIps = true;
	public static int autoIpNum = 30;
	
	public static long autoRMB = 1;
	public static int autoRunNum = 0;
	
	public static boolean isShowUnLimitedIps = true;
	public static int checkNum = 15;
	public static long checkRmb = 500;
	public static int showState = -1;
	public static String ShowChannel = "";
	public static String ShowServerName = "";
	public static String checkServerName = "";
	public static HashMap<String, HashMap<String, PlayerData>> serverPlayerDatas = new HashMap<String, HashMap<String, PlayerData>>();
	public static HashMap<String, Integer> limitedIPs = new HashMap<String, Integer>();
	public static HashMap<String, ArrayList<PlayerData>> ipDatas = new HashMap<String, ArrayList<PlayerData>>();
	public static HashMap<String, Integer> channelLimitNum = new HashMap<String, Integer>();
	public static HashMap<String, Long> channelLimitSilver = new HashMap<String, Long>();
	public static long lastChcekTime = 0;
	public static class PlayerData implements Serializable{
		private static final long serialVersionUID = 511751396093343420L;
		
		public long pId;
		public String userName;
		public String pName;
		public String pChannel;
		public String ipAdd;
		public int level;
		public long rmb;
		public long silver;
		public String macAdd;
		public String ua;
		public String wifiName;
		public boolean isOnline;
		public String serverName;
		
		public String toString() {
			return "["+pId+"] ["+pName+"] ["+pChannel+"] ["+ipAdd+"] ["+level+"] ["+macAdd+"] ["+ua+"] ["+wifiName+"] ["+isOnline+"]";
		}
	}
	
	public static class LimitedMsg implements Serializable {
		private static final long serialVersionUID = 211750106096743425L;
		
		public LimitedMsg(PlayerData da) {
			this.userName = da.userName;
			this.serverName = da.serverName;
			this.pId = da.pId;
		}
		
		public String userName;
		public String serverName;
		public long pId;
		
		public String toString(){
			return ""+userName+",qas,"+serverName+",qas,"+pId;
		}
	}
	
	public static long lastGetDataTime = 0L;
	public static Thread thr = null;
	public static boolean isRun = false;
	public static class GetMsgRunable implements Runnable {
		public void run() {
			while(isRun) {
				try{
					Thread.sleep(1000*60);
				}catch(Exception e) {
					e.printStackTrace();
				}
				if (lastChcekTime == 0) {
					lastChcekTime = System.currentTimeMillis();
				}
				
				if (System.currentTimeMillis() - lastChcekTime > CLEAR_SPACE_TIME) {
					serverPlayerDatas.clear();
					ipDatas.clear();
					lastChcekTime = System.currentTimeMillis();
				}
				
				if (isAutoLimited) {
					if (lastAutoTime == 0L) {
						lastAutoTime = System.currentTimeMillis();
					}
					if (System.currentTimeMillis() - lastAutoTime > AUTO_LIMITED_SPACETIME) {
						HashMap<String, ArrayList<PlayerData>> ip_Datas = new HashMap<String, ArrayList<PlayerData>>();
						for (String key : serverPlayerDatas.keySet()) {
							HashMap<String, PlayerData> datas = serverPlayerDatas.get(key);
							for (String userName : datas.keySet()) {
								PlayerData ddd = datas.get(userName);
								ddd.serverName = key;
								if (ddd.rmb > autoRMB) {
									continue;
								}
								String ipA = ddd.ipAdd;
								ArrayList<PlayerData> list = ip_Datas.get(ipA);
								if (list == null) {
									list = new ArrayList<PlayerData>();
									ip_Datas.put(ipA, list);
								}
								list.add(ddd);
							}
						}
						
						for (String ip : ip_Datas.keySet()) {
							if (isLimitedByIps) {
								if (!limitedIPs.containsKey(ip)) {
									continue;
								}
							}
							ArrayList<PlayerData> datas = ip_Datas.get(ip);
							int limiNum = autoIpNum;
							if (limitedIPs.containsKey(ip)) {
								limiNum = autoIpNum - limitedIPs.get(ip)/5;
								if (limiNum < 6) {
									limiNum = 6;
								}
							}
							if (datas.size() < limiNum) {
								continue;
							}
							try{
								int ipNum = 0;
								if (limitedIPs.containsKey(ip)) {
									ipNum = limitedIPs.get(ip).intValue();
								}
								limitedIPs.put(ip, ipNum + 1);
								LimitedMsg[] msgs = new LimitedMsg[datas.size()];
								long allSilver = 0;
								for (int i = 0; i < msgs.length; i++) {
									allSilver += datas.get(i).silver;
									msgs[i] = new LimitedMsg(datas.get(i));
									{
										Integer cNum = channelLimitNum.get(datas.get(i).pChannel);
										if (cNum == null) {
											cNum = 0;
										}
										channelLimitNum.put(datas.get(i).pChannel, cNum+1);
									}
									{
										Long sNum = channelLimitSilver.get(datas.get(i).pChannel);
										if (sNum == null) {
											sNum = 0L;
										}
										channelLimitSilver.put(datas.get(i).pChannel, sNum+datas.get(i).silver);
									}
								}
								HashSet<String> serverNames = new HashSet<String>();
								for (int i = 0; i < msgs.length; i++) {
									LimitedMsg m = msgs[i];
									serverPlayerDatas.get(m.serverName).remove(m.userName);
									serverNames.add(m.serverName);
								}
								MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
								String args = "";
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
								args = args+"action=limited&" +
								"uInfo="+JsonUtil.jsonFromObject(msgs);
								for (int i = 0 ; i < serverList.length; i++) {
									MieshiServerInfo info = serverList[i];
									
									if (serverNames.contains(info.getName())) {
										String serverIp = info.getServerUrl();
										byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
										if(b != null && b.length > 0){
											String json = new String(b);
										}
									}
								}
							}catch(Exception e) {
								System.out.print(e);
							}
						}
						
						if (isAutoLimitedNoMsg) {
							ip_Datas = new HashMap<String, ArrayList<PlayerData>>();
							for (String key : serverPlayerDatas.keySet()) {
								HashMap<String, PlayerData> datas = serverPlayerDatas.get(key);
								for (String userName : datas.keySet()) {
									PlayerData ddd = datas.get(userName);
									ddd.serverName = key;
									if (ddd.rmb > autoRMB || !ddd.wifiName.equals("未收到协议")) {
										continue;
									}
									String ipA = ddd.ipAdd;
									ArrayList<PlayerData> list = ip_Datas.get(ipA);
									if (list == null) {
										list = new ArrayList<PlayerData>();
										ip_Datas.put(ipA, list);
									}
									list.add(ddd);
								}
							}
							
							for (String ip : ip_Datas.keySet()) {
								ArrayList<PlayerData> datas = ip_Datas.get(ip);
								if (datas.size() > autoNoMsgNum) {
									try{
										int ipNum = 0;
										if (limitedIPs.containsKey(ip)) {
											ipNum = limitedIPs.get(ip).intValue();
										}
										limitedIPs.put(ip, ipNum + 1);
										LimitedMsg[] msgs = new LimitedMsg[datas.size()];
										long allSilver = 0;
										for (int i = 0; i < msgs.length; i++) {
											allSilver += datas.get(i).silver;
											msgs[i] = new LimitedMsg(datas.get(i));
											{
												Integer cNum = channelLimitNum.get(datas.get(i).pChannel);
												if (cNum == null) {
													cNum = 0;
												}
												channelLimitNum.put(datas.get(i).pChannel, cNum+1);
											}
											{
												Long sNum = channelLimitSilver.get(datas.get(i).pChannel);
												if (sNum == null) {
													sNum = 0L;
												}
												channelLimitSilver.put(datas.get(i).pChannel, sNum+datas.get(i).silver);
											}
										}
										HashSet<String> serverNames = new HashSet<String>();
										for (int i = 0; i < msgs.length; i++) {
											LimitedMsg m = msgs[i];
											serverPlayerDatas.get(m.serverName).remove(m.userName);
											serverNames.add(m.serverName);
										}
										MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
										String args = "";
										args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
										args = args+"action=limited&" +
										"uInfo="+JsonUtil.jsonFromObject(msgs);
										for (int i = 0 ; i < serverList.length; i++) {
											MieshiServerInfo info = serverList[i];
											
											if (serverNames.contains(info.getName())) {
												String serverIp = info.getServerUrl();
												byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
												if(b != null && b.length > 0){
													String json = new String(b);
												}
											}
										}
									}catch(Exception e) {
										System.out.print(e);
									}
								}
							}
						}
						
						lastAutoTime = System.currentTimeMillis();
						try {
							File f = new File(System.getProperty("user.dir") + "/webapps/game_gateway/WEB-INF/diskCacheFileRoot/limitedIPs.data");
							FileOutputStream fo = new FileOutputStream(f);
							DataOutputStream dos = new DataOutputStream(fo);
							if (autoRunNum > 10) {
								List<String> removes = new ArrayList<String>();
								for(String key : limitedIPs.keySet()) {
									int num = limitedIPs.get(key).intValue();
									if (num <= 2) {
										removes.add(key);
									}
								}
								for (String key : removes) {
									limitedIPs.remove(key);
								}
								autoRunNum = 0;
							}else {
								autoRunNum++;
							}
							dos.writeInt(limitedIPs.size());
							for (String ip : limitedIPs.keySet()) {
								int num = limitedIPs.get(ip);
								dos.writeUTF(ip);
								dos.writeInt(num);
							}
							dos.close();
							fo.close();
						} catch(Exception e) {
							System.out.println(e);
						}
					}
				}
				
				if (lastGetDataTime == 0L) {
					lastGetDataTime = System.currentTimeMillis();
				}
				
				if (System.currentTimeMillis() - lastGetDataTime > AUTO_GETDATA_SPACETIME) {
					lastGetDataTime = System.currentTimeMillis();
					int levelMin = 25;
					int levelMax = 300;
					MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
					String args = "";
					args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
					args = args+"action=AndroidMsg&levelMin="+levelMin+
					"&levelMax="+levelMax+
					"&rmb="+1500+
					"&online="+"true"+"";
					for (int i = 0 ; i < serverList.length; i++) {
						MieshiServerInfo info = serverList[i];
						if (true) {
							if (checkServerName.length() == 0) {
								if (info.getStatus() == MieshiServerInfo.STATUS_OFF || 
										info.getStatus() == MieshiServerInfo.STATUS_WEIHU || 
										info.getStatus() == MieshiServerInfo.INNER_TEST) {
									continue;
								}
								if (info.getServerType() == MieshiServerInfo.SERVERTYPE_WIN8专用服务器 || 
										info.getServerType() == MieshiServerInfo.SERVERTYPE_内部测试服务器 ||
										info.getServerType() == MieshiServerInfo.SERVERTYPE_对外测试的服务器 ||
										info.getServerType() == MieshiServerInfo.SERVERTYPE_文化部测试服务器 ||
										info.getServerType() == MieshiServerInfo.SERVERTYPE_移动专服) {
									continue;
								}
							}else {
								if (checkServerName.indexOf(info.getName()) < 0) {
									continue;
								}
							}
						}
	//					out.println("["+info.getName()+"] ["+info.getHttpPort()+"]");
	//					out.println("<br>");
						try {
							String serverIp = info.getServerUrl();
							byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
							if(b != null && b.length > 0){
								String json = new String(b);
								PlayerData[] datas = JsonUtil.objectFromJson(json,PlayerData[].class);//好像这里不能用泛型，如果无法用的话，就用其他结构吧
								HashMap<String, PlayerData> map = serverPlayerDatas.get(info.getName());
								if (map == null) {
									map = new HashMap<String, PlayerData>();
									serverPlayerDatas.put(info.getName(), map);
								}
								for (PlayerData ddd : datas) {
									if (noFengUsers.contains(ddd.userName)) {
										continue;
									}
									ddd.serverName = info.getName();
									if (map.containsKey(ddd.userName)) {
										if (map.get(ddd.userName).rmb < ddd.rmb) {
											map.put(ddd.userName, ddd);
										}
									}else {										
										map.put(ddd.userName, ddd);
									}
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%

	if (!isLoad) {
		FileInputStream fo = null;
		try{
			File f = new File(System.getProperty("user.dir") + "/webapps/game_gateway/WEB-INF/diskCacheFileRoot/NoFengUsers.txt");
			fo = new FileInputStream(f);
			int l = fo.available();
			byte[] bs = new byte[l];
			fo.read(bs);
			String s = new String(bs);
			fo.close();
			String[] ss = s.split(",");
			for (int i = 0; i < ss.length; i++) {
				noFengUsers.add(ss[i]);
			}
			isLoad = true;
		}catch(Exception e) {
			out.print(e);
			if (fo != null) {
				fo.close();
			}
		}
	}

	String ac = request.getParameter("action");
	if (ac != null) {
		if (ac.equals("chanagCheck")) {
			checkNum = Integer.parseInt(request.getParameter("checkNum"));
			checkRmb = Long.parseLong(request.getParameter("rmb"));
			ShowChannel = request.getParameter("channel");
			if (ShowChannel == null) {
				ShowChannel = "";
			}
			ShowServerName = request.getParameter("ShowServerName");
			if (ShowServerName == null) {
				ShowServerName = "";
			}
			checkServerName = request.getParameter("checkServerName");
			if (checkServerName == null) {
				checkServerName = "";
			}
			isShowUnLimitedIps = Boolean.valueOf(request.getParameter("isShowUnLimitedIps"));
		}else if (ac.equals("threadStart")) {
			if (thr == null) {
				thr = new Thread(new GetMsgRunable(), "GetMsgRunable");
				isRun = true;
				thr.start();
			}else {
				isRun = false;
				thr = null;
			}
		}else if (ac.equals("autoLimited")) {
			if (isRun) {
				boolean isAuto = Boolean.parseBoolean(request.getParameter("isAuto"));
				boolean isIps = Boolean.parseBoolean(request.getParameter("byIps"));
				int ipNum = Integer.parseInt(request.getParameter("autoNum"));
				isAutoLimitedNoMsg = Boolean.parseBoolean(request.getParameter("noMsg"));
				autoNoMsgNum = Integer.parseInt(request.getParameter("noMsgNum"));
				if (autoNoMsgNum < 6) {
					autoNoMsgNum = 6;
				}
				isAutoLimited = isAuto;
				isLimitedByIps = isIps;
				autoIpNum = ipNum;
				if (autoIpNum < 8) {
					autoIpNum = 8;
				}
				File f = new File(System.getProperty("user.dir") + "/webapps/game_gateway/WEB-INF/diskCacheFileRoot/limitedIPs.data");
				if (f.exists()) {
					DataInputStream dis = new DataInputStream(new FileInputStream(f));
					int num = dis.readInt();
					for (int i = 0; i < num; i++) {
						limitedIPs.put(dis.readUTF(), dis.readInt());
					}
					
					dis.close();
				}
			}else {
				out.println("启动失败，自动查询未开启");
			}
		}else if (ac.equals("showLimitedIps")) {
			for(String ip : limitedIPs.keySet()) {
				out.println(ip+"-"+limitedIPs.get(ip)+"<br>");
			}
			String clear = request.getParameter("isClear");
			if (Boolean.valueOf(clear)) {
				limitedIPs.clear();
			}
			try{
				File f = new File(System.getProperty("user.dir") + "/webapps/game_gateway/WEB-INF/diskCacheFileRoot/limitedIPs.data");
				FileOutputStream fo = new FileOutputStream(f);
				DataOutputStream dos = new DataOutputStream(fo);
				dos.writeInt(limitedIPs.size());
				dos.close();
			}catch(Exception e) {
				System.out.println(e);
			}
		}else if (ac.equals("SpaceTime")) {
			CLEAR_SPACE_TIME = Long.parseLong(request.getParameter("clearTime"));
			AUTO_LIMITED_SPACETIME = Long.parseLong(request.getParameter("autoLimitedTime"));
			AUTO_GETDATA_SPACETIME = Long.parseLong(request.getParameter("autoGetDataTime"));
		}
	}
%>
	<br><a href="./ShowPlayerData.jsp">刷新此页面</a><br>
	<br>
	修改检查情况
	<form>
		<input type="hidden" name="action" value="chanagCheck">
		显示数目<input type="text" name="checkNum" value="<%=checkNum%>">
		显示充值<input type="text" name="rmb" value="<%=checkRmb%>">
		显示渠道<input type="text" name="channel" value="<%=ShowChannel%>">
		显示服务器<input type="text" name="ShowServerName" value="<%=ShowServerName%>">
		<br>
		查询服务器<input type="text" name="checkServerName" value="<%=checkServerName%>">
		是否不显示封过的IP<input type="text" name="isShowUnLimitedIps" value="<%=isShowUnLimitedIps %>"> 
		<input type="submit" value="提  交">
	</form>
	<br>
	自动封号相关时间
	<form>
		<input type="hidden" name="action" value="SpaceTime">
		清除数据间隔<input type="text" name="clearTime" value="<%=CLEAR_SPACE_TIME%>">下次执行时间:[<%=(lastChcekTime + CLEAR_SPACE_TIME - System.currentTimeMillis())/1000/60 %>分钟]<br>
		自动封号间隔<input type="text" name="autoLimitedTime" value="<%=AUTO_LIMITED_SPACETIME%>">下次执行时间:[<%=(lastAutoTime + AUTO_LIMITED_SPACETIME - System.currentTimeMillis())/1000/60 %>分钟]<br>
		查询数据间隔<input type="text" name="autoGetDataTime" value="<%=AUTO_GETDATA_SPACETIME%>">下次执行时间:[<%=(lastGetDataTime + AUTO_GETDATA_SPACETIME - System.currentTimeMillis())/1000/60 %>分钟]<br>
		<input type="submit" value="提  交">
	</form>
	<br>
	封之前封过的IP，如果角色数目超过输入数目
	<form>
		<input type="hidden" name="action" value="limitedIPS">
		数目<input type="text" name="pNum" value="40">
		<input type="submit" value="封之前封的IP">
	</form>
	<br>
	封多少数目以上的，和某个渠道占比高的
	<form>
		<input type="hidden" name="action" value="limitedNums">
		数目<input type="text" name="pNum" value="40">
		渠道<input type="text" name="pChannel" value="">
		<input type="submit" value="封号">
	</form>
	<br>
	开启自动查数据线程:当前[<%=(thr==null||(!thr.isAlive())) ? "未开启" : "开启" %>]
	<form>
		<input type="hidden" name="action" value="threadStart">
		<input type="submit" value="提  交">
	</form>
	<br>
	<br>
	开启自动封号，必须先开启自动查询
	<form>
		<input type="hidden" name="action" value="autoLimited">
		当前是[<%=isAutoLimited ? "开启" : "未开启" %>]<input type="text" name="isAuto" value="<%=isAutoLimited %>">
		是否按手动封号IP封号<input type="text" name="byIps" value="<%=isLimitedByIps %>" >
		自动封号数目<input type="text" name="autoNum" value="<%=autoIpNum %>">
		是否自动封无协议<input type="text" name="noMsg" value="<%=isAutoLimitedNoMsg %>" >
		数目<input type="text" name="noMsgNum" value="<%=autoNoMsgNum %>" >
		 自动封号RMB<input type="text" name="autoRMB" value="<%=autoRMB %>" >
		<input type="submit" value="提  交">
	</form>
	<br>
	尝试解封
	<form>
		<input type="hidden" name="action" value="removeLimited">
		服务器名字<input type="text" name="serverName">
		用户名<input type="text" name="uName">
		<input type="submit" value="提  交">
	</form>
	<br>
	向服务器查询数据
	<br>
	<form>
		<input type="hidden" name="action" value="AndroidMsg">
		等级<input type="text" name="levelMin" value="30">
		-<input type="text" name="levelMax" value="50">
		充值<input type="text" name="rmb" value="1500">
		<input type="text" name="online" value="true">
		<input type="submit" value="提  交">
	</form>
	<br>
	对现有数据IP进行分析
	<form>
		<input type="hidden" name="action" value="fenxiIP">
		是否去除IP后一个点<input type="text" name="isSub" value="false">
		<input type="submit" value="提  交">
	</form>
	<%
	
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("AndroidMsg")) {
				int levelMin = Integer.parseInt(request.getParameter("levelMin"));
				int levelMax = Integer.parseInt(request.getParameter("levelMax"));
				MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
				String args = "";
				args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
				args = args+"action=AndroidMsg&levelMin="+levelMin+
				"&levelMax="+levelMax+
				"&rmb="+request.getParameter("rmb")+
				"&online="+request.getParameter("online")+"";
				for (int i = 0 ; i < serverList.length; i++) {
					MieshiServerInfo info = serverList[i];
					if (true) {
						if (checkServerName.length() == 0) {
							if (info.getStatus() == MieshiServerInfo.STATUS_OFF || 
									info.getStatus() == MieshiServerInfo.STATUS_WEIHU || 
									info.getStatus() == MieshiServerInfo.INNER_TEST) {
								continue;
							}
							if (info.getServerType() == MieshiServerInfo.SERVERTYPE_WIN8专用服务器 || 
									info.getServerType() == MieshiServerInfo.SERVERTYPE_内部测试服务器 ||
									info.getServerType() == MieshiServerInfo.SERVERTYPE_对外测试的服务器 ||
									info.getServerType() == MieshiServerInfo.SERVERTYPE_文化部测试服务器 ||
									info.getServerType() == MieshiServerInfo.SERVERTYPE_移动专服) {
								continue;
							}
						}else {
							if (checkServerName.indexOf(info.getName()) < 0) {
								continue;
							}
						}
					}
//					out.println("["+info.getName()+"] ["+info.getHttpPort()+"]");
//					out.println("<br>");
					String json = "";
					try {
						String serverIp = info.getServerUrl();
						byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
						if(b != null && b.length > 0){
							json = new String(b);
							PlayerData[] datas = JsonUtil.objectFromJson(json,PlayerData[].class);//好像这里不能用泛型，如果无法用的话，就用其他结构吧
							HashMap<String, PlayerData> map = serverPlayerDatas.get(info.getName());
							if (map == null) {
								map = new HashMap<String, PlayerData>();
								serverPlayerDatas.put(info.getName(), map);
							}
							for (PlayerData ddd : datas) {
								if (noFengUsers.contains(ddd.userName)) {
									continue;
								}
								ddd.serverName = info.getName();
								if (map.containsKey(ddd.userName)) {
									if (map.get(ddd.userName).rmb < ddd.rmb) {
										map.put(ddd.userName, ddd);
									}
								}else {									
									map.put(ddd.userName, ddd);
								}
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
						out.println(info.getName() + "  " + info.getServerUrl() + e);
						out.println("<br>");
						out.println("json = ["+json+"]");
						out.println("<br>");
					}
				}
				out.println(levelMin + "级到" + levelMax+"级的数据查询完毕。");
			}else if (action.equals("removeLimited")) {
				String serverName = request.getParameter("serverName");
				String uName = request.getParameter("uName");
				
				MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
				MieshiServerInfo info = null;
				for (int i = 0 ; i < serverList.length; i++) {
					if (serverList[i].getName().equals(serverName)) {
						info = serverList[i];
						break;
					}
				}
				if (info != null) {
					String args = "";
					args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
					args = args+"action=removeLimited&uName="+uName;
					try {
						String serverIp = info.getServerUrl();
						byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
						if(b != null && b.length > 0){
							String json = new String(b);
							out.println("<br>");
							out.println(json);
							out.println("<br>");
						}
					} catch(Exception e) {
						e.printStackTrace();
						out.println(e);
						out.println("<br>");
					}
				}else {
					out.println("服务器["+serverName+"]不存在<br>");
				}
			}else if (action.equals("fenxiIP")) {
				boolean is = Boolean.parseBoolean(request.getParameter("isSub"));
				if (is) {
					showState = 2;
				}else {
					showState = 1;
				}
			}else if (action.equals("cleanData")) {
				serverPlayerDatas.clear();
			}else if (action.equals("cleanChannelData")) {
				channelLimitNum.clear();
				channelLimitSilver.clear();
			}else if (action.equals("limited")) {
				String v = request.getParameter("pIds");
				if (true) {
					ArrayList<PlayerData> datas = ipDatas.get(v);
					if (limitedIPs.containsKey(v)) {
						limitedIPs.put(v, limitedIPs.get(v).intValue() + 1);
					}else {
						limitedIPs.put(v, 1);
					}
					LimitedMsg[] msgs = new LimitedMsg[datas.size()];
					long allSilver = 0;
					for (int i = 0; i < msgs.length; i++) {
						allSilver += datas.get(i).silver;
						msgs[i] = new LimitedMsg(datas.get(i));
						{
							Integer cNum = channelLimitNum.get(datas.get(i).pChannel);
							if (cNum == null) {
								cNum = 0;
							}
							channelLimitNum.put(datas.get(i).pChannel, cNum+1);
						}
						{
							Long sNum = channelLimitSilver.get(datas.get(i).pChannel);
							if (sNum == null) {
								sNum = 0L;
							}
							channelLimitSilver.put(datas.get(i).pChannel, sNum+datas.get(i).silver);
						}
					}
					HashSet<String> serverNames = new HashSet<String>();
					for (int i = 0; i < msgs.length; i++) {
						LimitedMsg m = msgs[i];
						serverPlayerDatas.get(m.serverName).remove(m.userName);
						serverNames.add(m.serverName);
					}
					MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
					String args = "";
					args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
					args = args+"action=limited&" +
					"uInfo="+JsonUtil.jsonFromObject(msgs);
					out.println("下列用户在某服务器被封-----一共封账号:["+msgs.length+"]银子["+allSilver+"]");
					out.println("<br>");
					for (int i = 0 ; i < serverList.length; i++) {
						MieshiServerInfo info = serverList[i];
						
						if (serverNames.contains(info.getName())) {
							String serverIp = info.getServerUrl();
							byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
							if(b != null && b.length > 0){
								String json = new String(b);
								out.println("<br>");
								out.println(info.getName());
								out.println("<br>");
								out.println(json);
								out.println("<br>");
							}
						}
					}
				}
			}else if (action.equals("limitedIPS")) {
				int pNum = Integer.parseInt(request.getParameter("pNum"));
				if (pNum > 15) {
					for (String key : ipDatas.keySet()) {
						ArrayList<PlayerData> datas = ipDatas.get(key);
						if (datas.size() >= pNum) {
							LimitedMsg[] msgs = new LimitedMsg[datas.size()];
							long allSilver = 0;
							for (int i = 0; i < msgs.length; i++) {
								allSilver += datas.get(i).silver;
								msgs[i] = new LimitedMsg(datas.get(i));
								{
									Integer cNum = channelLimitNum.get(datas.get(i).pChannel);
									if (cNum == null) {
										cNum = 0;
									}
									channelLimitNum.put(datas.get(i).pChannel, cNum+1);
								}
								{
									Long sNum = channelLimitSilver.get(datas.get(i).pChannel);
									if (sNum == null) {
										sNum = 0L;
									}
									channelLimitSilver.put(datas.get(i).pChannel, sNum+datas.get(i).silver);
								}
							}
							HashSet<String> serverNames = new HashSet<String>();
							for (int i = 0; i < msgs.length; i++) {
								LimitedMsg m = msgs[i];
								serverPlayerDatas.get(m.serverName).remove(m.userName);
								serverNames.add(m.serverName);
							}
							MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
							String args = "";
							args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
							args = args+"action=limited&" +
							"uInfo="+JsonUtil.jsonFromObject(msgs);
							out.println("下列用户在某服务器被封-----一共封账号:["+msgs.length+"]银子["+allSilver+"]");
							out.println("<br>");
							for (int i = 0 ; i < serverList.length; i++) {
								MieshiServerInfo info = serverList[i];
								
								if (serverNames.contains(info.getName())) {
									String serverIp = info.getServerUrl();
									byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
									if(b != null && b.length > 0){
										String json = new String(b);
										out.println("<br>");
										out.println(info.getName());
										out.println("<br>");
										out.println(json);
										out.println("<br>");
									}
								}
							}
						}
					}
				}
			}else if (action.equals("limitedNums")) {
				int pNum = Integer.parseInt(request.getParameter("pNum"));
				if (pNum > 15) {
					String pChannel = request.getParameter("pChannel");
					for (String key : ipDatas.keySet()) {
						ArrayList<PlayerData> datas = ipDatas.get(key);
						if (datas.size() >= pNum) {
							int channelNum = 0;
							if (pChannel == null || pChannel.equals("")) {
								channelNum = datas.size();
							}else {
								for (PlayerData dd : datas) {
									if (dd.pChannel.equals(pChannel)) {
										channelNum++;
									}
								}
							}
							if (channelNum + 10 > datas.size()) {
								LimitedMsg[] msgs = new LimitedMsg[datas.size()];
								if (limitedIPs.containsKey(key)) {
									limitedIPs.put(key, limitedIPs.get(key).intValue() + 1);
								}else {
									limitedIPs.put(key, 1);
								}
								long allSilver = 0;
								for (int i = 0; i < msgs.length; i++) {
									allSilver += datas.get(i).silver;
									msgs[i] = new LimitedMsg(datas.get(i));
									{
										Integer cNum = channelLimitNum.get(datas.get(i).pChannel);
										if (cNum == null) {
											cNum = 0;
										}
										channelLimitNum.put(datas.get(i).pChannel, cNum+1);
									}
									{
										Long sNum = channelLimitSilver.get(datas.get(i).pChannel);
										if (sNum == null) {
											sNum = 0L;
										}
										channelLimitSilver.put(datas.get(i).pChannel, sNum+datas.get(i).silver);
									}
								}
								HashSet<String> serverNames = new HashSet<String>();
								for (int i = 0; i < msgs.length; i++) {
									LimitedMsg m = msgs[i];
									serverPlayerDatas.get(m.serverName).remove(m.userName);
									serverNames.add(m.serverName);
								}
								MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
								String args = "";
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
								args = args+"action=limited&" +
								"uInfo="+JsonUtil.jsonFromObject(msgs);
								out.println("下列用户在某服务器被封-----一共封账号:["+msgs.length+"]银子["+allSilver+"]");
								out.println("<br>");
								for (int i = 0 ; i < serverList.length; i++) {
									MieshiServerInfo info = serverList[i];
									
									if (serverNames.contains(info.getName())) {
										String serverIp = info.getServerUrl();
										byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/waigua/AndroidMsg.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
										if(b != null && b.length > 0){
											String json = new String(b);
											out.println("<br>");
											out.println(info.getName());
											out.println("<br>");
											out.println(json);
											out.println("<br>");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (showState == 1) {
			out.println("当前显示为按IP分<br>");
			ipDatas.clear();
			for (String key : serverPlayerDatas.keySet()) {
				HashMap<String, PlayerData> datas = serverPlayerDatas.get(key);
				for (String userName : datas.keySet()) {
					PlayerData ddd = datas.get(userName);
					ddd.serverName = key;
					if (ShowChannel.length() > 0) {
						if (!ddd.pChannel.equals(ShowChannel)) {
							continue;
						}
					}
					if (ShowServerName.length() > 0) {
						if (ShowServerName.indexOf(ddd.serverName) < 0) {
							continue;
						}
					}
					if (ddd.rmb > checkRmb) {
						continue;
					}
					ArrayList<PlayerData> list = ipDatas.get(ddd.ipAdd);
					if (list == null) {
						list = new ArrayList<PlayerData>();
						ipDatas.put(ddd.ipAdd, list);
					}
					list.add(ddd);
				}
			}
			
		}else if (showState == 2) {
			out.println("当前显示为按IP切割分<br>");
			ipDatas.clear();
			for (String key : serverPlayerDatas.keySet()) {
				HashMap<String, PlayerData> datas = serverPlayerDatas.get(key);
				for (String userName : datas.keySet()) {
					PlayerData ddd = datas.get(userName);
					ddd.serverName = key;
					if (ShowChannel.length() > 0) {
						if (!ddd.pChannel.equals(ShowChannel)) {
							continue;
						}
					}
					if (ShowServerName.length() > 0) {
						if (ShowServerName.indexOf(ddd.serverName) < 0) {
							continue;
						}
					}
					if (ddd.rmb > checkRmb) {
						continue;
					}
					String ipA = ddd.ipAdd;
					if (ipA.lastIndexOf(".") > 0) {
						ipA = ipA.substring(0, ipA.lastIndexOf("."));
					}
					ArrayList<PlayerData> list = ipDatas.get(ipA);
					if (list == null) {
						list = new ArrayList<PlayerData>();
						ipDatas.put(ipA, list);
					}
					list.add(ddd);
				}
			}
		}
		for (String ipKey : ipDatas.keySet()) {
			ArrayList<PlayerData> list = ipDatas.get(ipKey);
			if (list.size() >= checkNum) {
				if (limitedIPs.containsKey(ipKey)) {
					if (!isShowUnLimitedIps) {
						continue;
					}
					out.println(ipKey + ":"+list.size() + "个-----[IP已经封过"+limitedIPs.get(ipKey)+"]");
				}else {
					out.println(ipKey + ":"+list.size() + "个");
				}
				out.println("<br>");
				out.println("<table border=\"2\">");
				out.println("<tr>");
				out.println("<td>用户</td>");
				out.println("<td>角色ID</td>");
				out.println("<td>服务器名字</td>");
				out.println("<td>角色名字</td>");
				out.println("<td>渠道</td>");
				out.println("<td>角色等级</td>");
				out.println("<td>rmb</td>");
				out.println("<td>银子</td>");
				out.println("<td>mac</td>");
				out.println("<td>机型</td>");
				out.println("<td>wifiName</td>");
				out.println("<td>在线</td>");
				out.println("</tr>");
//				ArrayList<LimitedMsg> lMsg = new ArrayList<LimitedMsg>();
				String vv = "";
				for (int i = 0; i < list.size(); i++) {
					PlayerData pd = list.get(i);
//					lMsg.add(new LimitedMsg(pd));
					out.println("<tr>");
					out.println("<td>"+pd.userName+"</td>");
					out.println("<td>"+pd.pId+"</td>");
					out.println("<td>"+pd.serverName+"</td>");
					out.println("<td>"+pd.pName+"</td>");
					out.println("<td>"+pd.pChannel+"</td>");
					out.println("<td>"+pd.level+"</td>");
					out.println("<td>"+pd.rmb+"</td>");
					out.println("<td>"+pd.silver+"</td>");
					out.println("<td>"+pd.macAdd+"</td>");
					out.println("<td>"+pd.ua+"</td>");
					out.println("<td>"+pd.wifiName+"</td>");
					out.println("<td>"+pd.isOnline+"</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"action\" value=\"limited\">");
				out.println("<input type=\"hidden\" name=\"pIds\" value=\""+ipKey+"\">");
				out.println("<input type=\"submit\" value=\"封号\">");
				out.println("</form>");
			}
		}
	%>
	
	<br>
	清除所有查询数据
	<form>
		<input type="hidden" name="action" value="cleanData">
		<input type="submit" value="提  交">
	</form>
	<br>
	<br>
	显示所有封IP的情况
	<form>
		<input type="hidden" name="action" value="showLimitedIps">
		<input type="text" name="isClear" value="false">
		<input type="submit" value="提  交">
	</form>
	<br>
	
	<br>
	封号情况:
	<%
		int fNum = 0;
		for (String channel : channelLimitNum.keySet()) {
			fNum += channelLimitNum.get(channel);
		}
		long silverNum = 0;
		for (String channel : channelLimitSilver.keySet()) {
			silverNum += channelLimitSilver.get(channel);
		}
		out.println("总数目["+fNum+"]总银子:["+silverNum+"]");
		out.println("<table border=\"2\">");
		out.println("<tr>");
		out.println("<td>渠道</td>");
		out.println("<td>数目</td>");
		out.println("<td>银子</td>");
		out.println("</tr>");
		for (String channel : channelLimitNum.keySet()) {
			out.println("<tr>");
			out.println("<td>"+channel+"</td>");
			out.println("<td>"+channelLimitNum.get(channel)+"</td>");
			out.println("<td>"+channelLimitSilver.get(channel)+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
	%>
	清除所有封号数目
	<form>
		<input type="hidden" name="action" value="cleanChannelData">
		<input type="submit" value="提  交">
	</form>
</body>
</html>
