<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.brick.BrickALLBillboard"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%
	String playerId = request.getParameter("playerId");
	String action = request.getParameter("action");
	playerId = playerId == null ? "" :playerId;
	action = action == null ? "" :action;
	if ("changecarbonstatus".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		if (player == null) {
			out.println("2未找到对应角色信息！！");
			return ;
		}
		BillboardStatDate bsd = BillboardStatDateManager.em.find(player.getId());
		if (bsd == null) {
			out.println("未找到对应角色信息！！");
			return ;
		}
		List<GameDataRecord> list = AchievementManager.gameDREm.query(GameDataRecord.class, "playerId = ? and dataType = ?", new Object[]{player.getId(), 77}, "", 1, 10);
		long num = 0;
		if (list != null && list.size() > 0) {
			num = list.get(0).getNum();
		}
		Passport port = BossClientService.getInstance().getPassportByUserName(player.getUsername());
		out.println("[角色名:"+player.getName()+"] [注册时间:"+new Timestamp(port.getRegisterDate().getTime())+"] [在线时长:"+player.getDurationOnline()+"] [大师技能:"+bsd.getSkillChongNum()+"] [刺探积分:" + bsd.getPeekNum() + "] [宝藏积分:" + bsd.getBrickNum() + "] [酒仙积分:" + bsd.getDrinkBeerNum() + "] [击杀敌国玩家数:"+num+"] <br>");
		out.println("<br<br>");
	}
%>

<form action="checkPlayerBillboardInfo.jsp" method="post">
	<input type="hidden" name="action" value="changecarbonstatus" />角色名:
	<input type="text" name="playerId" value="<%=playerId%>" />
	<input type="submit" value="查询角色信息" />
</form>
<br />
 