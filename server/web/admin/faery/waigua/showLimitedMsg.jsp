<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	public interface LimitedPlayerInfo {
	
		public long getId();
		public String getUsername();
		public long getSilver();
		public int getLevel();
		public long getLoginServerTime();
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
查询封号情况
<BR/>
<hr>
<%
String action = request.getParameter("action");
if (action != null) {
	if (action.equals("create")) {
		File f1 = new File("./webapps/game_server/admin/faery/waigua/一月"+GameConstants.getInstance().getServerName()+".t");
		FileOutputStream fop1 = new FileOutputStream(f1);
		DataOutputStream dos1 = new DataOutputStream(fop1);
		File f2 = new File("./webapps/game_server/admin/faery/waigua/十月"+GameConstants.getInstance().getServerName()+".t");
		FileOutputStream fop2 = new FileOutputStream(f2);
		DataOutputStream dos2 = new DataOutputStream(fop2);
		
		SimpleEntityManager<Player> em = ((GamePlayerManager)GamePlayerManager.getInstance()).em;
		List<String> us = EnterLimitManager.getInstance().limited;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long startTime1 = format.parse("2016-01-01 00:00:00").getTime();
		long endTime1 = format.parse("2016-12-31 23:59:59").getTime();
		long startTime2 = format.parse("2016-10-01 00:00:00").getTime();
		long endTime2 = format.parse("2016-12-31 23:59:59").getTime();
		for (String username : us) {
			long[] ids = em.queryIds(Player.class, "username=?", new Object[] {username}, "", 1, 100);
			List<LimitedPlayerInfo> sims = em.queryFields(LimitedPlayerInfo.class, ids);
			for (LimitedPlayerInfo info : sims) {
				out.print(username + " " + info.getId() + " "+ info.getSilver() + " " + info.getLoginServerTime() + " " + (info.getLoginServerTime() > startTime1) + " " + (info.getLoginServerTime() < endTime1) +"<br>");
				out.print(username + " " + info.getId() + " "+ info.getSilver() + " " + info.getLoginServerTime() + " " + (info.getLoginServerTime() > startTime2) + " " + (info.getLoginServerTime() < endTime2) +"<br>");
				if (info.getLoginServerTime() > startTime1 && info.getLoginServerTime() < endTime1) {					
					String s = info.getId()+"@"+info.getUsername()+"@"+info.getLevel()+"@"+info.getSilver()+"\n";
					dos1.write(s.getBytes("UTF-8"));
				}
				if (info.getLoginServerTime() > startTime2 && info.getLoginServerTime() < endTime2) {
					String s = info.getId()+"@"+info.getUsername()+"@"+info.getLevel()+"@"+info.getSilver()+"\n";
					dos2.write(s.getBytes("UTF-8"));
				}
			}
		}
		dos1.close();
		dos2.close();
	}
}
%>
<form>
	<input type="hidden" value="create" name="action">
	<input type="submit" value="生成数据">
</form>

<br>
<br>
<a href="./一月<%=GameConstants.getInstance().getServerName()%>.t">点击下载2016一月</a>
<br>
<br>
<a href="./十月<%=GameConstants.getInstance().getServerName()%>.t">点击下载2016十月</a>
</body>
</html>