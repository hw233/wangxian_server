<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改公告时间</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
		<%
			for (ActivityShowInfo asi : ActivityManagers.getInstance().infos) {
				if (asi.activityTitle.equals("情缘任务加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-25 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-25 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月28日", "7月25日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("煮酒任务加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-26 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-26 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月29日", "7月26日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("刺探任务加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-27 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-27 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月25日", "7月27日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("运镖任务加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-28 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-28 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月26日", "7月28日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("偷砖任务加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-29 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-29 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月30日", "7月29日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("体力寻宝加送玄天残叶")) {
					asi.startShowTime = TimeTool.formatter.varChar19.parse("2014-07-30 00:00:00");
					asi.endShowTime = TimeTool.formatter.varChar19.parse("2014-07-30 23:59:59");
					asi.activityContent = asi.activityContent.replaceAll("7月27日", "7月30日");
					out.println(asi.activityContent + "<br>");
				}
			}
		out.println("====================");
			
		%>

	</body>
</html>
