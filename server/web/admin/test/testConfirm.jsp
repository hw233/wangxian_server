<%@page import="com.xuanzhi.confirmation.bean.GameActivity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.xuanzhi.confirmation.service.client.ClientService"%>
<%@page import="com.xuanzhi.confirmation.service.message.QUERY_ALL_ACTIVITY_RES"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
Player player = GamePlayerManager.getInstance().getPlayer("Candy");
GameConstants	gameConstants = GameConstants.getInstance();
QUERY_ALL_ACTIVITY_RES bossRes = (QUERY_ALL_ACTIVITY_RES) ClientService.getInstance().doQueryActivity("", gameConstants.getGameName(), gameConstants.getServerName());
GameActivity[]  gas = bossRes .getActivitys();
out.print(gas.length);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>