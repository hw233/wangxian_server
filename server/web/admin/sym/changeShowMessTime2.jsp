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
				if (asi.activityTitle.equals("中秋福利狂欢月")) {
					asi.activityContent = asi.activityContent.replaceAll("7月25日", "9月1日");
					asi.activityContent = asi.activityContent.replaceAll("7月31日", "9月15日");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("情缘任务加送酒票")) {
					asi.activityContent = asi.activityContent.replaceAll("8月22日00:00", "9月1日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月22日23:59", "9月3日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("煮酒任务加送帖券")) {
					asi.activityContent = asi.activityContent.replaceAll("8月23日00:00", "9月4日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月23日23:59", "9月6日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("刺探任务加送酒票")) {
					asi.activityContent = asi.activityContent.replaceAll("8月24日00:00", "9月7日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月24日23:59", "9月9日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("运镖任务加送帖券")) {
					asi.activityContent = asi.activityContent.replaceAll("8月25日00:00", "9月10日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月25日23:59", "9月12日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("偷砖任务加送酒票")) {
					asi.activityContent = asi.activityContent.replaceAll("8月26日00:00", "9月13日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月26日23:59", "9月15日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("体力寻宝加送帖券")) {
					asi.activityContent = asi.activityContent.replaceAll("8月27日00:00", "9月16日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月27日23:59", "9月18日23:59");
					out.println(asi.activityContent + "<br>");
				}
				if (asi.activityTitle.equals("中秋积分抢购")) {
					asi.activityContent = asi.activityContent.replaceAll("8月8日00:00", "9月1日00:00");
					asi.activityContent = asi.activityContent.replaceAll("8月14日23:59", "9月11日23:59");
					out.println(asi.activityContent + "<br>");
				}
			}
		out.println("====================");
			
		%>

	</body>
</html>
