<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager.SimplePlayerInfoWrapper"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
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
		if ("feng".equals(action)) {
			String user = request.getParameter("user");
			EnterLimitManager.getInstance().putTolimit("", new String[] {user});
			out.println("封" + user);
		}
	}
%>
	<table border="1">
		<tr>
			<td>角色ID</td>
			<td>用户名</td>
			<td>次数</td>
			<td>角色名字</td>
			<td>level</td>
			<td>-------</td>
		</tr>
	<%
		for (Long key : EnterLimitManager.playerSendErrorWorldChat.keySet()) {
			int num = EnterLimitManager.playerSendErrorWorldChat.get(key);
			String name = "";
			String user = "";
			int level = 0;
			
			Player[] onlinePs = PlayerManager.getInstance().getOnlinePlayers();
			for (Player p : onlinePs) {
				if (p.getId() == key.longValue()) {
					name = p.getName();
					user = p.getUsername();
					level = p.getLevel();
					break;
				}
			}
			if (name.equals("")) {
				PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(key);
				name = info.getName();
				user = info.getUsername();
				level = info.getLevel();
			}
	%>
		<tr>
			<td><%=key %></td>
			<td><%=user %></td>
			<td><%=num %></td>
			<td><%=name %></td>
			<td><%=level %></td>
			<td><input type='hidden' name='action' value="feng"><input type='hidden' name='user' value=<%=user %>><input type='submit' value='封'></td>
		</tr>
	<%
		}
	%>
	</table>
</body>
</html>
