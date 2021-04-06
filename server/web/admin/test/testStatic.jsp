<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveStorehouse"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.guozhan.GuozhanHistory"%>
<%@page import="com.fy.engineserver.guozhan.GuozhanOrganizer"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.farming.FarmingTaskEvent"%>
<%@page import="com.fy.engineserver.activity.farming.FarmingManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	out.print(Cave.maintenanceTD+"<br>");
	Cave.maintenanceTD = TimeDistance.MINUTE;
	out.print("修改成功："+Cave.maintenanceTD+"<br>");
// 	Player player = GamePlayerManager.getInstance().getPlayer("捷新");
// 	CaveStorehouse cs = cave.getStorehouse();
// 	cs.setFoodLevel(20);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>

<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.*"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%></html>