<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.economic.*,com.xuanzhi.boss.authorize.exception.*,com.fy.engineserver.mail.service.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>角色战场清理</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<body>
<%
String message = null;
int playerId = -1;
Object obj = session.getAttribute("playerid");
if(obj != null){
	playerId = Integer.parseInt(obj.toString());
}
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
if (playerId != -1) {
	player = pm.getPlayer(playerId);
}
if(player != null) {
	String doset = request.getParameter("doset");
	if(doset != null) {
		player.setBattleField(null);
		player.setInBattleField(false);
		message = "已设置玩家身上的battleField为null";
	} else {
		if(player.getBattleField() != null) {
			message = "玩家("+player.getName()+")在战场中:" + player.getBattleField().getName();
		} else {
			message = "玩家("+player.getName()+")不在战场中";
		}
	}
}
if(message != null) {
%>
<h2><%=message %></h2>
<%} %>

<div style="padding:40px 0 40px 30px;">
<form action="" name=f1>
	是否确认清理战场信息：<input type=submit name=sub1 value="确定">
	<input type=hidden name=doset value="true">
</form>
</div>
</body>
</html>
