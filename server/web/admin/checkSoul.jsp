<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String name = request.getParameter("name");
	Player player = null;
	if (name != null) {
		player = GamePlayerManager.getInstance().getPlayer(name);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./checkSoul.jsp" method="get">

		<input name="name" type="text" value="<%=name%>"> <input
			name="ok" type="submit" value="提交>">
	</form>

	<%
		if (player != null) {
			out.print("[当前元神]    hp " + player.getCurrSoul().toString() + "<br/>");
			out.print("[非当前元神]  hp " + player.getUnusedSoul().get(0).toString() + "<br/>");
			out.print("[角色]  hp " + player.getMaxHP());
			out.print("[角色] 加成 hp " + player.getMaxHPX());
		}
	%>
</body>
</html>