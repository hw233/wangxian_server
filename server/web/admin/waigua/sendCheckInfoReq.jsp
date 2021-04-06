<%@page import="com.fy.engineserver.sprite.NewUserEnterServerService.ClientInfo"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.message.GET_CLIENT_INFO_1_REQ"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.message.GET_PHONE_INFO_1_REQ"%>
<%@page import="com.fy.engineserver.sprite.NewUserEnterServerService"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="java.lang.reflect.Constructor"%>
<%@page import="com.xuanzhi.tools.transport.Message"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("sendReq")) {
				String reqName = request.getParameter("reqType");
				Class c = Class.forName("com.fy.engineserver.message." + reqName);
				Class[] parType = {long.class, String[].class};
				Constructor con = c.getConstructor(parType);
				Object[] pa = {GameMessageFactory.nextSequnceNum(), NewUserEnterServerService.checkStrings};
				int pNum = 0;
				Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
				int num = ps.length;
				NewUserEnterServerService.checkInfo.clear();
				for (int i = 0; i < num; i++) {
					Message m = (Message)con.newInstance(pa);
					ps[i].addMessageToRightBag(m);
					NewUserEnterServerService.checkInfo.put(ps[i].getId(), new ClientInfo(ps[i].getId(), ps[i].getUsername(), ps[i].getName(), System.currentTimeMillis(), reqName));
				}
				out.print(c.getName() + "发送成功  玩家数目:"+num+"<br>");
			}
		}
	%>
	
	<a href="./sendCheckInfoReq.jsp">刷新次页面</a>
	
	<form name="f8">
		给所有玩家发送协议
		<input type="hidden" name="action" value="sendReq">
		输入协议名字<input type="text" name="reqType">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<br>
	<table border="2">	
		<tr>
			<td>玩家ID</td>
			<td>名字</td>
			<td>用户名</td>
			<td>等级</td>
			<td>协议返回</td>
			<td>协议是否对应</td>
			<td>返回是否正确</td>
			<td>返回内容</td>
		</tr>
	<%
		for (Long key : NewUserEnterServerService.checkInfo.keySet()) {
			ClientInfo info = NewUserEnterServerService.checkInfo.get(key);
			int level = -1;
			if (PlayerManager.getInstance().isOnline(info.pID)) {
				level = PlayerManager.getInstance().getPlayer(info.pID).getSoulLevel();
			}
			String backV = "";
			if (info.backInfo != null) {
				for (int n = 0; n< 5; n ++) {
					backV += "["+info.backInfo[n] + "],";
				}
			}
			out.print("<tr>");
			out.print("<td>"+info.pID+"</td>");
			out.print("<td>"+info.pName+"</td>");
			out.print("<td>"+info.userName+"</td>");
			out.print("<td>"+level+"</td>");
			out.print("<td>"+info.isBack+"</td>");
			out.print("<td>"+info.isMessageRight+"</td>");
			out.print("<td>"+info.isBackRight+"</td>");
			out.print("<td>"+backV+"</td>");
			out.print("</tr>");
		}
	%>
	</table>
	
</body>
</html>