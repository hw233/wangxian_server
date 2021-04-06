<%@page import="com.fy.engineserver.tournament.data.OneTournamentData"%>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("qiehuan")) {
				TournamentManager.getInstance().每周的竞技列表切换();
			}else if (action.equals("gaiku")) {
				long count = TournamentManager.getInstance().emPlayer.count();
				long[] ids = TournamentManager.getInstance().emPlayer.queryIds(OneTournamentData.class, "lastWeek=?", new Object[] { 25 }, "currentTournamentPoint desc", 1, count + 1);
				for (long i : ids) {
					OneTournamentData data = TournamentManager.getInstance().emPlayer.find(i);
					data.setLastWeek(24);
					TournamentManager.getInstance().emPlayer.save(data);
				}
				TournamentManager.getInstance().上周比武按名次排序后的id = TournamentManager.getInstance().得到上周竞技的人的id();
			}
		}
	%>
	
	<br>
	<form name="f1">
		<input type="hidden" name="action" value="qiehuan">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f2">
		<input type="hidden" name="action" value="gaiku">
		<input type="submit" value=<%="确定"%>>
	</form>
	
	<%
		for (long id : TournamentManager.getInstance().上周比武按名次排序后的id) {
			out.print("---" + id);
			out.print("<br>");
	%>
	
	<%
		}
	%>
</body>
</html>
