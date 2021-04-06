<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.enterlimit.Player_Process"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.sprite.SpriteSubSystem"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgData"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgManager"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.EnterLimitData"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int temp = 330;

	int selectType = 0;
	
	int sameValueNum = 3;

	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("limited")) {
			String p = request.getParameter("pIds");
			String[] pIds = p.split(",");
			ArrayList<Long> limitedData = new ArrayList<Long>();
			String userName = "";
			for (int i = 0; i < pIds.length; i++) {
				Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(Long.parseLong(pIds[i]));
				if (pp == null) {
					continue;
				}
				userName += pp.getUsername();
				if (pp.getPassport() == null) {
					out.println("[用户名:"+pp.getUsername()+"]<br>");
					Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(pp.getUsername());
					pp.setPassport(passport);
				}
				AndroidMsgData data = AndroidMsgManager.msgDatas.get(pp.getId());
				if (!EnterLimitManager.getInstance().limited.contains(pp.getUsername())) {
					EnterLimitManager.getInstance().limited.add(pp.getUsername());
				}
				if (!pp.getUsername().equals(pp.getPassport().getUserName())) {
					if (!EnterLimitManager.getInstance().limited.contains(pp.getPassport().getUserName())) {
						EnterLimitManager.getInstance().limited.add(pp.getPassport().getUserName());
					}
				}
				limitedData.add(pp.getId());
				AndroidMsgManager.logger.warn("[通过页面封号] [原因:温度330,容量4338] [passName:"+pp.getPassport().getUserName()+"] ["+pp.getLogString()+"] ["+data.toString()+"]");
				out.println("封号成功: ["+pp.getPassport().getUserName()+"] "+pp.getLogString()+"<br>");
				if (pp.isOnline()) {
					pp.getConn().close();
				}
			}
			AndroidMsgManager.logger.warn("[通过页面封号] [原因:温度330,容量4338] [用户集合] ["+userName+"]");
			for (Long id : limitedData) {
				AndroidMsgManager.msgDatas.remove(id);
			}
			EnterLimitManager.getInstance().diskCache.put(EnterLimitManager.LIMIT_KEY, (Serializable) EnterLimitManager.getInstance().limited);
			out.println("一共封号:"+pIds.length+"<br>");
		}else if (action.equals("limiteAll")) {
			
			HashMap<String, ArrayList<AndroidMsgData>> map = new HashMap<String, ArrayList<AndroidMsgData>>();
			int num = 0;
			for (Long key : AndroidMsgManager.msgDatas.keySet()) {
				AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
				if (data.getBatteryTemperature() == temp && data.getBatteryMA() == 4338 && 
						data.getBatteryValue()[0] == 100 && data.getWifiRssi()[0] == -47) {
					Player p = null;
					String ipAdd = "";
					long rmb = 0;
					if (PlayerManager.getInstance().isOnline(data.getpID())) {
						p = PlayerManager.getInstance().getPlayer(data.getpID());
						rmb = p.getRMB();
						ipAdd = p.getConn().getRemoteAddress();
						ipAdd = ipAdd.substring(0, ipAdd.indexOf(":"));
					}
					ArrayList<AndroidMsgData> datas = map.get(ipAdd);
					if (datas == null) {
						datas = new ArrayList<AndroidMsgData>();
						map.put(ipAdd, datas);
					}
					if (rmb < 100) {
						datas.add(data);
						num++;
					}
				}
			}
			int sameIpNum = Integer.parseInt(request.getParameter("sameIPNum"));
			for (String ipAdd : map.keySet()) {
				if (ipAdd.equals("")) {
					continue;	
				}
				ArrayList<AndroidMsgData> datas = map.get(ipAdd);
				if (datas.size() < sameIpNum) {
					continue;
				}

				ArrayList<Long> limitedData = new ArrayList<Long>();
				String userName = "";
				for (AndroidMsgData d : datas) {
					Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(d.getpID());
					if (pp == null) {
						continue;
					}
					userName += pp.getUsername();
					if (pp.getPassport() == null) {
						out.println("[用户名:"+pp.getUsername()+"]<br>");
						Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(pp.getUsername());
						pp.setPassport(passport);
					}
					AndroidMsgData data = AndroidMsgManager.msgDatas.get(pp.getId());
					if (!EnterLimitManager.getInstance().limited.contains(pp.getUsername())) {
						EnterLimitManager.getInstance().limited.add(pp.getUsername());
					}
					if (!pp.getUsername().equals(pp.getPassport().getUserName())) {
						if (!EnterLimitManager.getInstance().limited.contains(pp.getPassport().getUserName())) {
							EnterLimitManager.getInstance().limited.add(pp.getPassport().getUserName());
						}
					}
					limitedData.add(pp.getId());
					AndroidMsgManager.logger.warn("[通过页面封号] [原因:温度330,容量4338] [passName:"+pp.getPassport().getUserName()+"] ["+pp.getLogString()+"] ["+data.toString()+"]");
					out.println("封号成功: ["+pp.getPassport().getUserName()+"] "+pp.getLogString()+"<br>");
					if (pp.isOnline()) {
						pp.getConn().close();
					}
				}
				AndroidMsgManager.logger.warn("[通过页面封号] [原因:温度330,容量4338] [用户集合] ["+userName+"]");
				for (Long id : limitedData) {
					AndroidMsgManager.msgDatas.remove(id);
				}
				EnterLimitManager.getInstance().diskCache.put(EnterLimitManager.LIMIT_KEY, (Serializable) EnterLimitManager.getInstance().limited);
				out.println("一共封号:"+datas.size()+"<br>");
			}
		}else if (action.equals("chanagerSelectType")) {
			selectType = Integer.parseInt(request.getParameter("selectIndex"));
		}else if (action.equals("sendOSArch")) {
			long pId = Long.parseLong(request.getParameter("pID"));
			if (PlayerManager.getInstance().isOnline(pId)) {
				Player p = PlayerManager.getInstance().getPlayer(pId);
				String cName = "java.lang.System";
				String[] mNames = {"getProperty"};
				String[] types = {"1", "java.lang.String"};
				String[] args = {"1", "os.arch"};
				AndroidMsgManager.getInstance().sendMessage(p, true, "cOsArch", cName, mNames, types, args);
			}
		}else if (action.equals("sendAllOSArch")) {
			String proKey1 = request.getParameter("proKey1");
			String proKey2 = request.getParameter("proKey2");
			Player[] players = PlayerManager.getInstance().getOnlinePlayers();
			for (Player p : players) {
				String cName = "java.lang.System";
				String[] mNames = {"getProperty"};
				String[] types = {"1", "java.lang.String"};
				String[] args = {"1", proKey1+"."+proKey2};
				AndroidMsgManager.getInstance().sendMessage(p, true, "cOsArch", cName, mNames, types, args);
			}
		}else if (action.equals("sendSome")) {
			String pName = request.getParameter("pName");
			if (pName == null || pName.length() == 0) {
				Player[] players = PlayerManager.getInstance().getOnlinePlayers();
				for (Player p : players) {
					String cName = "org.cocos2dx.tests.TestsDemo";
					String[] mNames = {"getSystemService", "getSimCountryIso"};
					String[] types = {"1", "java.lang.String", "0"};
					String[] args = {"1", "phone", "0"};
					AndroidMsgManager.getInstance().sendMessage(p, true, "phoneMsg", cName, mNames, types, args);
				}
			}else {
				if (PlayerManager.getInstance().isOnline(pName)) {
					Player p = PlayerManager.getInstance().getPlayer(pName);
					String cName = "org.cocos2dx.tests.TestsDemo";
					String[] mNames = {"getSystemService", "getSimCountryIso"};
					String[] types = {"1", "java.lang.String", "0"};
					String[] args = {"1", "phone", "0"};
					AndroidMsgManager.getInstance().sendMessage(p, true, "phoneMsg", cName, mNames, types, args);
				}
			}
		}
	}

	HashMap<String, ArrayList<AndroidMsgData>> map = new HashMap<String, ArrayList<AndroidMsgData>>();
	int num = 0;
	if (selectType == 0) {
		for (Long key : AndroidMsgManager.msgDatas.keySet()) {
			AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
			if (data.getBatteryTemperature() == temp && data.getBatteryMA() == 4338 && 
					data.getBatteryValue()[0] == 100 && data.getWifiRssi()[0] == -47) {
				Player p = null;
				String ipAdd = "";
				long rmb = 0;
				if (PlayerManager.getInstance().isOnline(data.getpID())) {
					p = PlayerManager.getInstance().getPlayer(data.getpID());
					rmb = p.getRMB();
					ipAdd = p.getConn().getRemoteAddress();
					ipAdd = ipAdd.substring(0, ipAdd.indexOf(":"));
				}
				ArrayList<AndroidMsgData> datas = map.get(ipAdd);
				if (datas == null) {
					datas = new ArrayList<AndroidMsgData>();
					map.put(ipAdd, datas);
				}
				if (rmb < 100) {
					datas.add(data);
					num++;
				}
			}
		}
	}else if (selectType == 1) {
		for (Long key : AndroidMsgManager.msgDatas.keySet()) {
			AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
			if (data.getNetName().equalsIgnoreCase("ETHERNET")) {
				Player p = null;
				String ipAdd = "";
				long rmb = 0;
				if (PlayerManager.getInstance().isOnline(data.getpID())) {
					p = PlayerManager.getInstance().getPlayer(data.getpID());
					rmb = p.getRMB();
					ipAdd = p.getConn().getRemoteAddress();
					ipAdd = ipAdd.substring(0, ipAdd.indexOf(":"));
				}
				ArrayList<AndroidMsgData> datas = map.get(ipAdd);
				if (datas == null) {
					datas = new ArrayList<AndroidMsgData>();
					map.put(ipAdd, datas);
				}
				if (rmb < 100) {
					datas.add(data);
					num++;
				}
			}
		}
	}else if (selectType == 2) {
		HashMap<String, ArrayList<Long>> platform = new HashMap<String, ArrayList<Long>>();
		for (Long key : AndroidMsgManager.msgDatas.keySet()) {
			AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
			if (data != null) {
//				out.println("key=["+key+"]<br>");
				Player_Process po = EnterLimitManager.player_process.get(data.getpID());
				if (po != null && po.getRealPlatform() != null) {
					String pls = po.getRealPlatform();
					int len = 5;
					if (pls.length() < len){
						len = pls.length();
					}
					pls = pls.substring(0, len);
					ArrayList<Long> ids = platform.get(pls);
					if (ids == null) {
						ids = new ArrayList<Long>();
						platform.put(pls, ids);
					}
					ids.add(data.getpID());
				}
			}
		}
		
		ArrayList<Long> samePlatForm = new ArrayList<Long>();
		for (String key1 : platform.keySet()) {
			ArrayList<Long> ids = platform.get(key1);
			if (ids.size() > 1000) {
				samePlatForm.addAll(ids);
			}
		}
		
		HashMap<String, ArrayList<Long>> sameBatteryTemp = new HashMap<String, ArrayList<Long>>();
		for (Long id : samePlatForm) {
			AndroidMsgData data = AndroidMsgManager.msgDatas.get(id);
			Player_Process po = EnterLimitManager.player_process.get(data.getpID());
			if (data.getBatteryTemperature() != -1000) {
				ArrayList<Long> ids = sameBatteryTemp.get("" + data.getBatteryTemperature());
				if (ids == null) {
					ids = new ArrayList<Long>();
					sameBatteryTemp.put("" + data.getBatteryTemperature(), ids);
				}
				ids.add(id);
			}
		}
		
		out.println("sameBatteryTemp数目:["+sameBatteryTemp.size()+"]<br>");
		for (String key : sameBatteryTemp.keySet()) {
			ArrayList<Long> ids = sameBatteryTemp.get(key);
			if (ids.size() > 30) {
				ArrayList<AndroidMsgData> ds = new ArrayList<AndroidMsgData>();
				map.put(key, ds);
				for (int i = 0; i < ids.size(); i++) {
					ds.add(AndroidMsgManager.msgDatas.get(ids.get(i)));
				}
//				out.println("------------:["+key+"]-----["+sameBatteryTemp.get(key).size()+"]<br>");
			}
		}
		
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<br><a href="./ShowAndroidMsg.jsp">刷新此页面</a><br>
	按条件筛选
	<form>
		<input type="hidden" name="action" value="chanagerSelectType">
		<select name="selectIndex">
		<option value="0">温度330毫安4338
		<option value="1">联网方式
		<option value="2">自动比较 3个属性相同
		</select>
		<input type="submit" value="确定">
	</form>
	<form>
		<input type="hidden" name="action" value="limiteAll">
		<input type="text" name="sameIPNum" value="8">
		<input type="submit" value="全封">
	</form>
	<br>
	发送OSArch
	<br>
	<form>
		<input type="hidden" name="action" value="sendOSArch">
		<input type="text" name="pID" value="">
		<input type="submit" value="发送">
	</form>
	<br>
	发送给所有在线玩家OSArch
	<br>
	<form>
		<input type="hidden" name="action" value="sendAllOSArch">
		<input type="text" name="proKey1" value="os">
		<input type="text" name="proKey2" value="arch">
		<input type="submit" value="发送">
	</form>
	<br>
	发送别的
	<br>
	<form>
		<input type="hidden" name="action" value="sendSome">
		<input type="text" name="pName" value="">
		<input type="submit" value="发送">
	</form>
	<%
		out.println("总数目:"+num+"<br>");
		for (String ipAdd : map.keySet()) {
			ArrayList<AndroidMsgData> datas = map.get(ipAdd);
			out.println("IP地址:"+ipAdd+"---- 数目:"+datas.size()+"<br>");
			out.println("<table border=\"2\">");
			out.println("<tr>");
			out.println("<td>角色ID</td>");
			out.println("<td>毫安</td>");
			out.println("<td>温度</td>");
			out.println("<td>进程数目</td>");
			out.println("<td>进程</td>");
			out.println("<td>UA</td>");
			out.println("</tr>");
			String pids = "";
			for (int i = 0; i < datas.size(); i++) {
				AndroidMsgData data = datas.get(i);
				Player_Process po = EnterLimitManager.player_process.get(data.getpID());
				pids += data.getpID();
				if (i != datas.size()-1) {
					pids += ",";
				}
				out.println("<tr>");
				out.println("<td>"+data.getpID()+"</td>");
				out.println("<td>"+data.getBatteryMA()+"</td>");
				out.println("<td>"+data.getBatteryTemperature()+"</td>");
				out.println("<td>"+data.getProcessNames().length+"</td>");
				if (po == null) {
					out.println("<td>"+""+"</td>");
					out.println("<td>"+""+"</td>");
				}else {
					out.println("<td>"+Arrays.toString(po.getAndroidProcesss())+"</td>");
					out.println("<td>"+po.getRealPlatform()+"</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("<form>");
			out.println("<input type=\"hidden\" name=\"action\" value=\"limited\">");
			out.println("<input type=\"hidden\" name=\"pIds\" value=\""+pids+"\">");
			out.println("<input type=\"submit\" value=\"封号\">");
			out.println("</form>");
		}
	%>
</body>
</html>