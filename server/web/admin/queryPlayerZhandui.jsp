<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="java.util.Arrays"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.StatueForFairyBuddha"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.gaiming.GaiMingManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle"%>
<%@page
	import="com.fy.engineserver.battlefield.jjc.JJCBillboardData"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCManager"%>
<%@page import="com.fy.engineserver.battlefield.jjc.BattleTeam"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询某个玩家战队信息</title>
</head>
<%
	String playerId = request.getParameter("playerId");
	if (playerId != null && !"".equals(playerId)) {

		Player p = PlayerManager.getInstance().getPlayer(Long.valueOf(playerId));
		if(p==null){
			out.print("p=null");
			return;
		}
		BattleTeam team = JJCManager.getInstance().getTeam(p, 1, "后台查询");
		if (team != null) {
			out.print("战队名称:" + team.getTeamName() + "<br>");
			long[] memIds = team.getMemberIds();
			if (memIds != null && memIds.length > 0) {
				for (long id : memIds) {
					if(id<=0){
						continue;
					}
					Player pp = PlayerManager.getInstance().getPlayer(id);
					if (pp != null) {
						out.print(pp.getLogString() + "<br>");
					} else {
						out.print("获取不到玩家，id:" + id + "<br>");
					}
				}
			}
		} else {
			out.print("没有查询到战队信息<br>");
		}
	}
%>
<body>
<form action=""><input type="text" name="playerId"> <input
	type="submit" value="提交"></form>
</body>
</html>