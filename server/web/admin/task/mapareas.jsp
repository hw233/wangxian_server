<%@page import="com.fy.engineserver.core.res.MapPolyArea"%>
<%@page import="com.fy.engineserver.core.res.MapArea"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.tune.GameMapManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Game[] games = GameManager.getInstance().getGames();
	for (int i = 0; i < games.length; i++) {
		Game g = games[i];
		MapArea [] mas = g.gi.mapAreas;
		for (MapArea ma :mas) {
			out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
			out.print("[areaName] [地图:"+g.gi.displayName+"] [区域名:"+ma.name+"]");
			out.print("</BR>");
		}
		MapPolyArea[] mpas = g.gi.mapPolyAreas;
		for (MapPolyArea mpa : mpas) {
			out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
			out.print("<font color=red>[MapPolyArea</font>] [地图:"+g.gi.displayName+"] [区域名:"+mpa.name+"]");
			out.print("</BR>");
		}
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