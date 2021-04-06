<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.res.MapPolyArea"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.core.GameInfo"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Game[] games = GameManager.getInstance().getGames();//("piaomiaowangcheng");
	List<Game> gameList = new ArrayList<Game>();

	String name = "piaomiaowangcheng";
	for (Game game : games) {
		if (game.getGameInfo().name.equals(name)) {
			gameList.add(game);
			out.print(game.gi.mapPolyAreas);
		}
	}
	out.print(gameList.size() + "<BR/>");
	for (Game g : gameList) {
			File f = new File(request.getRealPath("piaomiaowangcheng.xmd"));
			GameInfo a = new GameInfo();
			a.load(f,"");
			out.print(a.mapPolyAreas + "XXX<BR>");
			g.gi.mapPolyAreas = a.mapPolyAreas;
			out.print(g.gi.mapPolyAreas + "sss<BR>");
			out.print(g.country + ",的:" + g.gi.displayName + "," + g.getLivingObjects().length + "[OK]<BR/>");
	}
	//out.print(request.getRealPath("piaomiaowangcheng.xmd"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看某地图的生物数量</title>
</head>
<body>

</body>
</html>