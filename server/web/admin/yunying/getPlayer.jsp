<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
	%>
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery" />密码：
		<input type="text" name="playerId" value="<%=playerId%>" /> 
			<input type="submit" value="查询玩家" />
	</form>
	<br />
	
	<%
	if("chakanrenwu2".equals(action) && "qweasdaZx".equals(playerId)) {
		GamePlayerManager gm = GamePlayerManager.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = format.parse("2013-11-21 00:00:00").getTime();
		gm.em.em.query(Player.class, "soulLevel>? and loginServerTime>?", new Object[] { name, time}, "loginServerTime desc", 1, 1000);
	}
	%>
</body>
</html>
