
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
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
		if(action != null && action.equals("login")){
			String u = request.getParameter("username");
			String p = request.getParameter("password");

			if(u != null && p != null && u.equals("wtx") && p.equals("19840717") ){
				userName = u;
				session.setAttribute("username",userName);
			}else{
				out.println("<h3>用户名密码不对</h3>");
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

	<br><a href="./sendGetSomething4Android.jsp">刷新此页面</a><br>
	
	<%
		int num = 0;
		boolean isShowTemp0 = false;
		boolean isShowBlueStacks = false;
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("getMac")) {
				String pName = request.getParameter("pName");
				String[] names = pName.split(",");
				for (int i = 0; i < names.length; i++) {
					Player p = PlayerManager.getInstance().getPlayer(names[i]);
					AndroidMsgManager.getInstance().sendGetMac(p);
				}
			}else if (action.equals("getOnline")) {
				Player[] onlines = PlayerManager.getInstance().getOnlinePlayers();
				for (int i = 0; i <onlines.length; i++) {
					AndroidMsgManager.getInstance().sendGetMac(onlines[i]);
				}
			}else if (action.equals("showBMA")) {
				String selectIndex = request.getParameter("selectIndex");
				if (selectIndex.equals("1")){
					isShowTemp0 = true;
				}else if (selectIndex.equals("2")){
					isShowBlueStacks = true;
				}
			}else if (action.equals("chanagerServer")) {
				String serverName = request.getParameter("serverName");
				serverName = serverName.substring(1, serverName.length()-1);
				String[] names = serverName.split(", ");
				List<String> serverNames = new ArrayList<String>();
				for (int i = 0; i < names.length; i++) {
					serverNames.add(names[i]);
				}
				AndroidMsgManager.unGetAndroidMsgServerNames = serverNames;
			}else if (action.equals("limited")) {
				String selectIndex = request.getParameter("selectIndex");
				if (selectIndex.equals("1")){
					int limitedNum = 0;
					ArrayList<AndroidMsgData> limitedData = new ArrayList<AndroidMsgData>();
					for (Long key : AndroidMsgManager.msgDatas.keySet()) {
						AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
						if (data.getWifiName() != null) {
							if (data.getWifiName().equalsIgnoreCase("BlueStacks")) {
								try {
									Player pp = PlayerManager.getInstance().getPlayer(data.getpID());
									String userName = pp.getUsername();
									if (!EnterLimitManager.getInstance().limited.contains(userName)) {
										EnterLimitManager.getInstance().limited.add(userName);
									}
									if (pp.isOnline()) {
										pp.getConn().close();
									}
									limitedData.add(data);
									AndroidMsgManager.logger.warn("[通过页面封号] [原因:wifi名字叫BlueStacks] ["+pp.getLogString()+"] ["+data.toString()+"]");
									out.println("封号成功:"+pp.getLogString()+"<br>");
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
			}
		}
	%>
	<br>
	<form>
		<input type="hidden" name="action" value="chanagerServer">
		<input size="80" type="text" name="serverName" value="<%=Arrays.toString(AndroidMsgManager.unGetAndroidMsgServerNames.toArray(new String[0]))%>">
		<input type="submit" value="确定">
	</form>
	<br>
	按条件筛选
	<form>
		<input type="hidden" name="action" value="showBMA">
		<select name="selectIndex">
		<option value="1">筛选温度为0
		<option value="2">筛选wifiName为BlueStacks
		</select>
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		<input type="hidden" name="action" value="limited">
		<select name="selectIndex">
		<option value="1">筛选wifiName为BlueStacks
		</select>
		<input type="submit" value="封号">
	</form>
	<br>
	查询数目:<%=AndroidMsgManager.msgDatas.size() %>
	<table border="2">
		<tr>
		<td>角色ID</td>
		<td>角色名字</td>
		<td>角色等级</td>
		<td>是否在线</td>
		<td>mac地址</td>
		<td>进程</td>
		<td>电池温度</td>
		<td>毫安</td>
		<td>电池容量</td>
		<td>联网方式</td>
		<td>wifiName</td>
		<td>wifi信号强度</td>
		<td>otherInfo</td>
		</tr>
		<%
			for (Long key : AndroidMsgManager.msgDatas.keySet()) {
				AndroidMsgData data = AndroidMsgManager.msgDatas.get(key);
				if (isShowTemp0) {
					if (data.getBatteryTemperature() != 0) {
						continue;
					}
				}
				if (isShowBlueStacks) {
					if (!data.getWifiName().equalsIgnoreCase("BlueStacks")) {
						continue;
					}
				}
				num++;
				out.println("<tr>");
				out.println("<td>"+data.getpID()+"</td>");
				if (PlayerManager.getInstance().isOnline(data.getpID())) {
					Player p = PlayerManager.getInstance().getPlayer(data.getpID());
					out.println("<td>"+p.getName()+"</td>");
					out.println("<td>"+p.getSoulLevel()+"</td>");
					out.println("<td>"+p.getIPAddress()+"</td>");
				}else {
					out.println("<td>"+""+"</td>");
					out.println("<td>"+""+"</td>");
					out.println("<td>"+"false"+"</td>");
				}
				{
					Long mac = data.getSendTimes().get(AndroidMsgManager.GET_CLIENT_MAC);
					if (mac == null) {
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
						out.println("<td>"+"还没取"+"</td>");
					}else {
						if (mac != -1L) {
							out.println("<td>"+"没返回"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
							out.println("<td>"+"还没取"+"</td>");
						}else {
							out.println("<td>"+data.getMac()+"</td>");
							Long pLen = data.getSendTimes().get(AndroidMsgManager.GET_PROCESS_INDEX_KEY+"0");
							if (pLen == null || pLen != -1L) {
								out.println("<td>"+"没返回"+"</td>");
							}else {
								out.println("<td>"+data.getProcessNames().length+"</td>");
							}
							Long wendu = data.getSendTimes().get(AndroidMsgManager.GET_BATTERY_WENDU);
							if (wendu == null || wendu != -1L) {
								out.println("<td>"+"没返回"+"</td>");
							}else {
								out.println("<td>"+data.getBatteryTemperature()+"</td>");
							}
							Long ma = data.getSendTimes().get(AndroidMsgManager.GET_BATTERY_MA);
							if (ma == null || mac != -1L) {
								out.println("<td>"+"没返回"+"</td>");
							}else {
								out.println("<td>"+data.getBatteryMA()+"</td>");
							}
							//电池容量
							out.println("<td>"+Arrays.toString(data.getBatteryValue())+"</td>");
							
							Long netType = data.getSendTimes().get(AndroidMsgManager.GET_CLIENT_NETTYPE);
							if (netType == null || netType != -1L) {
								out.println("<td>"+"没返回"+"</td>");
							}else {
								out.println("<td>"+data.getNetType()+"-"+data.getNetName()+"</td>");
							}
							//wifi
							out.println("<td>"+data.getWifiName()+"</td>");
							out.println("<td>"+Arrays.toString(data.getWifiRssi())+"</td>");
							
							if (data.getReqInfos().size() > 0) {
								out.println("<td>"+data.getReqInfos().get(0)+"</td>");
							}
						}
					}
				}
				out.println("</tr>");
			}
		%>
	</table>
	<br>
	筛选后数目:<%=num %>
</body>
</html>