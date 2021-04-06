<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Player[] ps = GamePlayerManager.getInstance().getCachedPlayers();
	Field f = Player.class.getDeclaredField("initializing");
	f.setAccessible(true);
	for (Player p : ps) {
		if (p.getHp() > 0) {
			continue;
		}
		f.set(p, false);
		p.init();
		out.print(p.getName() + "<BR/>");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>