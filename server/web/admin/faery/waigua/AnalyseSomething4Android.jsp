
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.engineserver.sprite.SpriteSubSystem"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgData"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgManager"%>
<%@page import="com.fy.engineserver.message.GET_SOME4ANDROID_1_RES"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.message.GET_SOME4ANDROID_RES"%>
<%@page import="com.fy.engineserver.validate.ValidateAskManager"%>
<%@page import="com.fy.engineserver.validate.DefaultValidateManager"%>
<%@page import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%!
	int showType = 0;
	int checkNum = 5;
	long rmb = 1000;
%>
<body>

	<%
	String sName = GameConstants.getInstance().getServerName();
	ArrayList<String> unServerName = new ArrayList<String>();
	unServerName.add("国内本地测试");
	unServerName.add("pan2");
	unServerName.add("ST");
	unServerName.add("化外之境");
	unServerName.add("仙尊降世");
	if (!unServerName.contains(sName)) {

	String userName = (String)session.getAttribute("username");
	if(userName == null){

		String action = request.getParameter("action");
		if (action != null) {
			if(action.equals("login")){
				String u = request.getParameter("username");
				String p = request.getParameter("password");
	
				if(u != null && p != null && u.equals("wtx") && p.equals("19840717") ){
					userName = u;
					session.setAttribute("username",userName);
				}else{
					out.println("<h3>用户名密码不对</h3>");
				}
			}
		}

		if(userName == null){
%>
	<center>
		<h1>请先登录</h1>
		<form>
		<input type="hidden" name="action" value="login">
		请输入用户名：<input type="text" name="username" value="" size=30><br/>
		请输入密码：<input type="text" name="password" value="" size=30><br/>
		<input type="submit" value="提  交">
		</form>
	</center>
<%
		return;
		}
	}
	}
%>
	
	<%
		String action  = request.getParameter("action");
		if (action != null) {
			if (action.equals("checkRmb")) {
				rmb = Long.parseLong(request.getParameter("rmb"));
			}else if ("autoFeng".equals(action)) {
				AndroidMsgManager.isAutoFeng = !AndroidMsgManager.isAutoFeng;
			}else if ("checkTimeSpace".equals(action)) {
				AndroidMsgManager.checkTimeSpace = Long.parseLong(request.getParameter("num"));
			}else if (action.equals("checkNum")) {
				checkNum = Integer.parseInt(request.getParameter("num"));
			}else if (action.equals("limited")) {
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
					AndroidMsgManager.logger.warn("[通过页面封号] [原因:电池温度为0] [passName:"+pp.getPassport().getUserName()+"] ["+pp.getLogString()+"] ["+data.toString()+"]");
					out.println("封号成功: ["+pp.getPassport().getUserName()+"] "+pp.getLogString()+"<br>");
					if (pp.isOnline()) {
						pp.getConn().close();
					}
				}
				AndroidMsgManager.logger.warn("[通过页面封号] [原因:电池温度为0] [用户集合] ["+userName+"]");
				for (Long id : limitedData) {
					AndroidMsgManager.msgDatas.remove(id);
				}
				EnterLimitManager.getInstance().diskCache.put(EnterLimitManager.LIMIT_KEY, (Serializable) EnterLimitManager.getInstance().limited);
				out.println("一共封号:"+pIds.length+"<br>");
			}else if (action.equals("tiren")) {
				String p = request.getParameter("pIds");
				String[] pIds = p.split(",");
				for (int i = 0; i < pIds.length; i++) {
					Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(Long.parseLong(pIds[i]));
					if (pp == null) {
						continue;
					}
					ChatMessageService.getInstance().silencePlayer(pp.getId(), 24*365, "玩家同IP下账号过多", 2);
				}
			}else if (action.equals("chengmo")) {
				String p = request.getParameter("pIds");
				String[] pIds = p.split(",");
				for (int i = 0; i < pIds.length; i++) {
					Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(Long.parseLong(pIds[i]));
					if (pp == null) {
						continue;
					}
					if (pp.isOnline()) {
						pp.getConn().close();
					}
				}
			}else if (action.equals("showType")) {
				String selectIndex = request.getParameter("selectIndex");
				if (selectIndex.equals("1")) {
					showType = 1;
				}else if (selectIndex.equals("2")) {
					showType = 2;
				}
			}else if (action.equals("limitedBlue")) {
				String selectIndex = request.getParameter("selectIndex");
				if (selectIndex.equals("1")){
					int limitedNum = 0;
					ArrayList<AndroidMsgData> limitedData = new ArrayList<AndroidMsgData>();
					for (Long key : AndroidMsgManager.msgDatas.keySet()) {
						AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
						if (data.getWifiName() != null) {
							if (data.getWifiName().equalsIgnoreCase("BlueStacks")) {
								try {
									Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
									String pUserName = pp.getUsername();
									if (!EnterLimitManager.getInstance().limited.contains(pUserName)) {
										EnterLimitManager.getInstance().limited.add(pUserName);
									}else {
										limitedData.add(data);
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
									limitedData.add(data);
									AndroidMsgManager.logger.warn("[通过页面封号] [原因:wifi名字叫BlueStacks] ["+pp.getLogString()+"] ["+data.toString()+"]");
									out.println("封号成功:"+pp.getLogString()+"<br>");
									if (pp.isOnline()) {
										pp.getConn().close();
									}
									limitedNum++;
								}catch(Exception e) {
									out.println("封号失败:["+data.getpID()+"]<br>");
								}
							}
						}
					}
					for (AndroidMsgData data : limitedData) {
						AndroidMsgManager.msgDatas.remove(data.getpID());
					}
					EnterLimitManager.getInstance().diskCache.put(EnterLimitManager.LIMIT_KEY, (Serializable) EnterLimitManager.getInstance().limited);
					out.println("一共封号:"+limitedNum+"<br>");
				}else if (selectIndex.equals("2")) {
					int limitedNum = 0;
					ArrayList<AndroidMsgData> limitedData = new ArrayList<AndroidMsgData>();
					for (Long key : AndroidMsgManager.msgDatas.keySet()) {
						AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
						if (data.getWifiRssi() != null) {
							if (data.getWifiRssi()[0] == -9999 && data.getBatteryTemperature() == 0) {
								try {
									Player pp = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
									String pUserName = pp.getUsername();
									if (!EnterLimitManager.getInstance().limited.contains(pUserName)) {
										EnterLimitManager.getInstance().limited.add(pUserName);
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
									limitedData.add(data);
									AndroidMsgManager.logger.warn("[通过页面封号] [原因:wifi信号强度为-9999] ["+pp.getLogString()+"] ["+data.toString()+"]");
									out.println("封号成功:"+pp.getLogString()+"<br>");
									if (pp.isOnline()) {
										pp.getConn().close();
									}
									limitedNum++;
								}catch(Exception e) {
									out.println("封号失败:["+data.getpID()+"]<br>");
								}
							}
						}
					}
					for (AndroidMsgData data : limitedData) {
						AndroidMsgManager.msgDatas.remove(data.getpID());
					}
					EnterLimitManager.getInstance().diskCache.put(EnterLimitManager.LIMIT_KEY, (Serializable) EnterLimitManager.getInstance().limited);
					out.println("一共封号:"+limitedNum+"<br>");
				}
			}else if (action.equals("isCheckRMBFengHao")) {
				AndroidMsgManager.isCheckRMBFengHao = !AndroidMsgManager.isCheckRMBFengHao;
			}
		}
%>
		
	<br><a href="./AnalyseSomething4Android.jsp">刷新此页面</a><br>
	
	<form>
		<%=AndroidMsgManager.isAutoFeng ? "当前自动封" : "当前不自动封" %>
		<input type="hidden" name="action" value="autoFeng">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		修改检查时间
		<input type="hidden" name="action" value="checkTimeSpace">
		<input type="text" name="num" value="<%=AndroidMsgManager.checkTimeSpace %>">
		<input type="submit" value="确定">
	</form>
	<br>
	
	<form>
		修改检查数目
		<input type="hidden" name="action" value="checkNum">
		<input type="text" name="num" value="<%=checkNum %>">
		<input type="submit" value="确定">
	</form>
	<br>
	
	<form>
		修改检查rmb
		<input type="hidden" name="action" value="checkRmb">
		<input type="text" name="rmb" value="<%=rmb %>">
		<input type="submit" value="确定">
	</form>
	<br>
	
	<form>
		<input type="hidden" name="action" value="limitedBlue">
		<select name="selectIndex">
		<option value="1">筛选wifiName为BlueStacks
		<option value="2">筛选wifi信号强度为-9999
		</select>
		<input type="submit" value="封号">
	</form>
	<br>
	
	<form>
		<input type="hidden" name="action" value="showType">
		<select name="selectIndex">
		<option value="1">筛选温度为0
		<option value="2">按wifiName
		</select>
		<input type="submit" value="确定">
	</form>
	<br>
	
	<form>
	打开检查相同IP下玩家临时封号:<%=AndroidMsgManager.isCheckRMBFengHao %>
		<input type="hidden" name="action" value="isCheckRMBFengHao">
		<input type="submit" value="确定">
	</form>
	<br>
<%
		if (showType == 1) {
			HashMap<String, ArrayList<AndroidMsgData>> datas4IP = new HashMap<String, ArrayList<AndroidMsgData>>();
			for (Long key : AndroidMsgManager.msgDatas.keySet()) {
				AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
				if (ChatMessageService.getInstance().isSilence(data.getpID()) == 2) {
					continue;
				}
				boolean isOnline = PlayerManager.getInstance().isOnline(data.getpID());
				if (isOnline) {
					Player player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
					String ipAddress = player.getConn().getRemoteAddress();
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
					ArrayList<AndroidMsgData> datas = datas4IP.get(ipAddress);
					if (datas == null) {
						datas = new ArrayList<AndroidMsgData>();
					}
					datas.add(data);
					datas4IP.put(ipAddress, datas);
				}
			}
			
			for (String key : datas4IP.keySet()) {
				ArrayList<AndroidMsgData> datas = datas4IP.get(key);
				if (datas.size() < checkNum) {
					continue;
				}
				out.println("IP地址:"+key+"---- 数目:"+datas.size()+"<br>");
				out.println("<table border=\"2\">");
				out.println("<tr>");
				out.println("<td>角色ID</td>");
				out.println("<td>角色名字</td>");
				out.println("<td>角色等级</td>");
				out.println("<td>充值</td>");
				out.println("<td>银子</td>");
				out.println("<td>用户名</td>");
				out.println("<td>国家</td>");
				out.println("<td>机型</td>");
				out.println("<td>注册时间</td>");
				out.println("<td>wifiName</td>");
				out.println("<td>毫安</td>");
				out.println("<td>电池容量</td>");
				out.println("<td>wifi信号强度</td>");
				out.println("</tr>");
				String pids = "";
				for (int i = 0 ; i < datas.size(); i++) {
					AndroidMsgData data = datas.get(i);
					Player player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
					PlayerRecordData recordData = EnterLimitManager.getPlayerRecordData(player.getId());
					if (player.getRMB() >= rmb) {
						continue;
					}
					pids += player.getId();
					if (i != datas.size()-1) {
						pids += ",";
					}
					out.println("<tr>");
					out.println("<td>"+player.getId()+"</td>");
					out.println("<td>"+player.getName()+"</td>");
					out.println("<td>"+player.getSoulLevel()+"</td>");
					out.println("<td>"+player.getRMB()+"</td>");
					out.println("<td>"+player.getSilver()+"</td>");
					out.println("<td>"+player.getUsername()+"</td>");
					out.println("<td>"+player.getCountry()+"</td>");
					out.println("<td>"+recordData.ua+"</td>");
					out.println("<td>"+recordData.registerTime+"</td>");
					out.println("<td>"+data.getWifiName()+"</td>");
					out.println("<td>"+data.getBatteryMA()+"</td>");
					out.println("<td>"+Arrays.toString(data.getBatteryValue())+"</td>");
					out.println("<td>"+Arrays.toString(data.getWifiRssi())+"</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"action\" value=\"limited\">");
				out.println("<input type=\"hidden\" name=\"pIds\" value=\""+pids+"\">");
				out.println("<input type=\"submit\" value=\"封号\">");
				out.println("</form>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"action\" value=\"tiren\">");
				out.println("<input type=\"hidden\" name=\"pIds\" value=\""+pids+"\">");
				out.println("<input type=\"submit\" value=\"踢人\">");
				out.println("</form>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"action\" value=\"chengmo\">");
				out.println("<input type=\"hidden\" name=\"pIds\" value=\""+pids+"\">");
				out.println("<input type=\"submit\" value=\"沉默\">");
				out.println("</form>");
			}
		}else if (showType == 2) {
			try {
				HashMap<String, ArrayList<AndroidMsgData>> datas4WifiName = new HashMap<String, ArrayList<AndroidMsgData>>();
				for (Long key : AndroidMsgManager.msgDatas.keySet()) {
					AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
					if (ChatMessageService.getInstance().isSilence(data.getpID()) == 2) {
						continue;
					}
					if (!data.getWifiName().equals("没")) {
						ArrayList<AndroidMsgData> datas = datas4WifiName.get(data.getWifiName());
						if (datas == null) {
							datas = new ArrayList<AndroidMsgData>();
						}
						datas.add(data);
						datas4WifiName.put(data.getWifiName(), datas);
					}
				}
				
				ArrayList<Long> dePlayers = new ArrayList<Long>();
				for (String key : datas4WifiName.keySet()) {
					ArrayList<AndroidMsgData> datas = datas4WifiName.get(key);
					if (datas.size() < checkNum) {
						continue;
					}
					out.println("wifiName:"+key+"---- 数目:"+datas.size()+"<br>");
					out.println("<table border=\"2\">");
					out.println("<tr>");
					out.println("<td>角色ID</td>");
					out.println("<td>角色名字</td>");
					out.println("<td>角色等级</td>");
					out.println("<td>充值</td>");
					out.println("<td>银子</td>");
					out.println("<td>用户名</td>");
					out.println("<td>国家</td>");
					out.println("<td>机型</td>");
					out.println("<td>注册时间</td>");
					out.println("<td>在线</td>");
					out.println("<td>毫安</td>");
					out.println("<td>电池容量</td>");
					out.println("<td>wifi信号强度</td>");
					out.println("</tr>");
					String pids = "";
					for (int i = 0 ; i < datas.size(); i++) {
						AndroidMsgData data = datas.get(i);
						Player player = null;
						try {
							player = ((GamePlayerManager)(PlayerManager.getInstance())).em.find(data.getpID());
						} catch(Exception e) {
							
						}
						if (player != null) {
							if (player.getRMB() >= rmb) {
								continue;
							}
							PlayerRecordData recordData = EnterLimitManager.getPlayerRecordData(player.getId());
							pids += player.getId();
							if (i != datas.size()-1) {
								pids += ",";
							}
							out.println("<tr>");
							out.println("<td>"+player.getId()+"</td>");
							out.println("<td>"+player.getName()+"</td>");
							out.println("<td>"+player.getSoulLevel()+"</td>");
							out.println("<td>"+player.getRMB()+"</td>");
							out.println("<td>"+player.getSilver()+"</td>");
							out.println("<td>"+player.getUsername()+"</td>");
							out.println("<td>"+player.getCountry()+"</td>");
							if (recordData == null) {
								out.println("<td>"+"没信息"+"</td>");
								out.println("<td>"+"没信息"+"</td>");
							}else {
								out.println("<td>"+recordData.ua+"</td>");
								out.println("<td>"+recordData.registerTime+"</td>");
							}
							if (player.isOnline()) {
								String ipAddress = player.getConn().getRemoteAddress();
								ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
								out.println("<td>"+ipAddress+"</td>");
							}else {
								out.println("<td>"+"不在线"+"</td>");
							}
							out.println("<td>"+data.getBatteryMA()+"</td>");
							out.println("<td>"+Arrays.toString(data.getBatteryValue())+"</td>");
							out.println("<td>"+Arrays.toString(data.getWifiRssi())+"</td>");
							out.println("</tr>");
						}else {
							dePlayers.add(data.getpID());
						}
					}
					out.println("</table>");
					out.println("<form>");
					out.println("<input type=\"hidden\" name=\"action\" value=\"limited\">");
					out.println("<input type=\"hidden\" name=\"pIds\" value=\""+pids+"\">");
					out.println("<input type=\"submit\" value=\"封号\">");
					out.println("</form>");
				}

				for (Long pp : dePlayers) {
					AndroidMsgManager.msgDatas.remove(pp);
				}
			} catch(Exception e) {
				out.println(e);
			}
		}
	%>
	
</body>
</html>