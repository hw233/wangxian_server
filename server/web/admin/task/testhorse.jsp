<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String horseId = request.getParameter("horseId");
	out.print("horseId:" + horseId + "<br/>");
	if (horseId != null) {
		long id = Long.valueOf(horseId);
		Player player = GamePlayerManager.getInstance().getPlayer("打电话");
		Horse h = HorseManager.getInstance().getHorseById(id, player);
		h.setEnergy(0);
		player.setLastFeedTime("1990-01-01");
		player.setFeedNum(0);
		out.print(player.getName() + "[" + h + "]设置完成");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./testhorse.jsp">
	<input type="text" name="horseId" value="<%=horseId%>">
	<input type="submit">
	</form>
</body>
</html>