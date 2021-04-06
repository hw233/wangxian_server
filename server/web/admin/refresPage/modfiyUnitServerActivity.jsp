<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="com.fy.engineserver.notice.NoticeForever"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="com.fy.engineserver.uniteserver.UnitServerActivity"%><html>
<head>
<title>刷新合服活动</title>
</head>
<body>
<p>
<% 
  		List<String> serverNameList1=new LinkedList<String>();
  		serverNameList1.add("天火重生");
  		serverNameList1.add("万劫魔神");
  		serverNameList1.add("踏天之道");
  		serverNameList1.add("傲世转生");
  		
  		String serverNames1="天火重生,万劫魔神,踏天之道,傲世转生";
  		
  		String startTime1="2013-12-03 00:00:00";
  		String endTime1="2013-12-15 23:59:59";
  		
  		String serverName=GameConstants.getInstance().getServerName();
  		if(serverNameList1.contains(serverName)){
  			out.print(serverName+"需要更新<br>");
  			List<UnitServerActivity> usActivities= UnitedServerManager.getInstance().getUnitServerActivityByName("武圣争夺奖励翻倍");
  			if(usActivities!=null){
  				usActivities.get(0).setOpenServers(serverNames1);
  				usActivities.get(0).setStartTime(TimeTool.formatter.varChar19.parse(startTime1));
  				usActivities.get(0).setStartTime(TimeTool.formatter.varChar19.parse(endTime1));
  				out.print(serverName+"活动"+usActivities.get(0).getActivityName()+"更新完毕<br>");
  			}
  		}
  		%>
</p>
</body>
</html>
