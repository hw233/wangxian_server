<%@page import="com.fy.engineserver.activity.RefreshSpriteManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	GameConstants gc = GameConstants.getInstance();
	RefreshSpriteManager.getInstance().getConfigedDatas().clear();
	out.print(gc.getServerName() + "[ok]");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>