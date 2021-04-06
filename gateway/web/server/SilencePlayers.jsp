<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URL"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<br><a href="./SilencePlayers.jsp">刷新此页面</a><br>

	<%
		String action = request.getParameter("action");
		if (action != null) {
			MieshiServerInfo[] serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
			String serverName = request.getParameter("serverName");
			if (serverName != null) {
				MieshiServerInfo info = null;
				for (int i = 0 ; i < serverList.length; i++) {
					if (serverList[i].getName().equals(serverName)) {
						info = serverList[i];
						break;
					}
				}
				if (info != null) {
					String pStr = request.getParameter("playerId");
					if (pStr != null && pStr.length() > 0) {
						long playerId = Long.parseLong(pStr);
						if ("checkSilence".equals(action)) {
							try{
								String args = "";
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
								args += "playerId="+playerId;
								String serverIp = info.getServerUrl();
								byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/yunying/op/checkSilence.jsp"), args.getBytes(), new HashMap(), 60000, 60000);								
								if (b != null && b.length > 0) {
									String s = new String(b);
									out.print(playerId + "  " + s + "<br><br>");
								}
							}catch(Exception e) {
								out.print(e);
							}
						}else if ("silencePlayer".equals(action)) {
							try{
								String args = "";
								int level = Integer.parseInt(request.getParameter("level"));
								String reason = request.getParameter("reason");
								String hour = request.getParameter("hour");
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
								args += "playerId="+playerId + "&hour="+ hour + "&reason="+ reason + "&level="+level;
								String serverIp = info.getServerUrl();
								byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/yunying/op/silencePlayers.jsp"), args.getBytes(), new HashMap(), 60000, 60000);								
								if (b != null && b.length > 0) {
									String s = new String(b);
									out.print(playerId + "  " + s + "<br><br>");
								}
							}catch(Exception e) {
								out.print(e);
							}
						}else if ("unSilence".equals(action)) {
							try{
								String args = "";
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&";
								args += "playerId="+playerId;
								String serverIp = info.getServerUrl();
								byte[] b = HttpUtils.webPost(new URL(serverIp+"/admin/yunying/op/unSilencePlayers.jsp"), args.getBytes(), new HashMap(), 60000, 60000);								
								if (b != null && b.length > 0) {
									String s = new String(b);
									out.print(playerId + "  " + s + "<br><br>");
								}
							}catch(Exception e) {
								out.print(e);
							}
						}
					}else {
						out.print("<br>未输入角色ID!!!!!!!<br>");
					}
				}else {
					out.print("<br>服务器不存在!!!!!!!<br>");
				}
			}
		}
	%>

	<br>
	查询某个玩家沉默情况
	<br>
	<form>
		<input type="hidden" name="action" value="checkSilence">
		服务器名字
		<input type="text" name="serverName" >
		角色ID
		<input type="text" name="playerId" >
		<input type="submit" value="提  交">
	</form>
	<br>
	<br>
	沉默某个玩家
	<br>
	<form>
		<input type="hidden" name="action" value="silencePlayer">
		服务器名字
		<input type="text" name="serverName" >
		角色ID
		<input type="text" name="playerId" >
		时间(小时)-1为永久
		<input type="text" name="hour" value="-1" >
		等级
		<input type="text" name="level" value="2" >
		原因
		<input type="text" name="reason" value="" >
		<input type="submit" value="提  交">
	</form>
	<br>
	<br>
	解沉默
	<br>
	<form>
		<input type="hidden" name="action" value="unSilence">
		服务器名字
		<input type="text" name="serverName" >
		角色ID
		<input type="text" name="playerId" >
		<input type="submit" value="提  交">
	</form>
</body>
</html>
