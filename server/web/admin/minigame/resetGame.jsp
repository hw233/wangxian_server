<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.fy.engineserver.minigame.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.minigame.MiniGameManager"%>
<%@page import="com.fy.engineserver.minigame.instance.MemoryGame"%>
<%@page import="com.fy.engineserver.message.INITDATA_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.minigame.MiniGameSystem"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntity"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntityManager"%>
<%@page import="com.fy.engineserver.message.HANDLE_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.message.END_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.message.HANDLE_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.menu.marriage.Option_Temp2_Minigame"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<%
	String action = request.getParameter("action");
	String petId = request.getParameter("petId");
	String playerId = request.getParameter("playerId");
	//
	petId = petId == null ? "" : petId;
	playerId = playerId == null ? "" : playerId;
	
%>
<h2>重置小游戏</h2>
<form action="resetGame.jsp" method="get">
<input type="hidden" name="action" value="reset"/>
人物名<input type="text" name="petId" value="<%=petId%>"/>
<input type="submit" value="重置小游戏"/>
</form>
<br/>


<%
if("reset".equals(action)){
	if(petId.isEmpty()){
		%>人物名是空<%
	} else{
		Player player = GamePlayerManager.getInstance().getPlayer(petId);
		MiniGameEntityManager.getInstance().resetMinigame(player);
		out.println("重置完成！");
	}
}

%>
</BODY></html>
