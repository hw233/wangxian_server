<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.*"%>

<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>

<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<title>设置现在所有内存中的玩家的称号列表</title>

</head>
<body>

	<%
	
		int i= 0;
	
		Player[] ps = PlayerManager.getInstance().getCachedPlayers();
		for(Player p : ps){
			List list =  p.getPlayerTitles();
			if(list != null && list.size() >0){
				i++;
				p.setPlayerTitles(list);
				out.print(p.getId()+"<br/>");
			}
		}
		out.print(i);
	
	%>

</body>
</html>
