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
// 	TournamentManager tm = TournamentManager.getInstance();
// 	out.print(GameConstants.getInstance().getServerName()+"<br>");
// 	out.print(tm.上周比武按名次排序后的id.length+"<br>");
// 	if (tm.上周比武按名次排序后的id != null && tm.上周比武按名次排序后的id.length > 0) {
// 	out.print(tm.上周比武按名次排序后的id.length+"<br>");
// 	for (int i = 0; i < tm.上周比武按名次排序后的id.length; i++) {
// 		Player p = PlayerManager.getInstance().getPlayer(tm.上周比武按名次排序后的id[i]);
// 		if(p!=null){
// 			out.print("【"+(++i)+"】"+p.getLogString()+"<br>");
// 		}
// 	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	long lasttime = sdf.parse("2014-03-31 00:00:00").getTime();
	
	
	int lastWeek = TournamentManager.得到一年中的第几个星期_周日并到上星期(lasttime - 7 * 1l * 24 * 3600 * 1000);
	try {
		long ids [] = TournamentManager.getInstance().emPlayer.queryIds(OneTournamentData.class, "currentWeek=?", new Object[] { lastWeek }, "currentTournamentPoint desc", 1, 500);
		if(ids!=null && ids.length>0){
			for(long id:ids){
				out.print(id+"<br>");
			}
		}else{
			out.print("没有数据"+lastWeek);
		}
	} catch (Exception ex) {
		out.print("异常"+ex);
	}
// }

%>
</body>
</html>
