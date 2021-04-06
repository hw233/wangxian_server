<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.compose.model.ChangeColorCompose"%>
<%@page import="com.fy.engineserver.compose.ComposeManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String carbonLevel = request.getParameter("carbonLevel");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
		carbonLevel = carbonLevel== null ? "" : carbonLevel;
		//ComposeManager ci = ComposeManager.instance;
		//ChangeColorCompose cc = ci.colorComposeMap.get("炼星符_3");
		//if(cc != null) {
		//	out.println("**********1****" + cc.getNeedArticleNum());
		//	out.println("**********2****" + cc.getCostNum());
		//}
	%>
	<form action="compose.jsp" method="post">
		<input type="hidden" name="action" value="testcompose" />
		角色id:<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="测试合成" />
	</form>
	<br />
	<form action="compose.jsp" method="post">
		<input type="hidden" name="action" value="reloaddata" />
		<input type="submit" value="重新加载静态文件" />
	</form>
	<br />
	<%
	if("testcompose".equals(action)) {
		Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		long[] ids = new long[2];
		int[] nums = new int[2];
		nums[0] = 1;
		nums[1] = 1;
		ids[0] = 1101000000000868399L;
		ids[1] = 1101000000000200327L;
		//ComposeManager.instance.composeArticle(p, 2, 1, ids);
		out.println("======================");
	} else if("reloaddata".equals(action)) {
		ComposeManager.instance.init();
		out.println("*****1****" + ComposeManager.instance.colorComposeMap.size());
		out.println("*****2****" + ComposeManager.instance.compose4OtherList.size());
		out.println("加载完成");
	}
			
	%>
</body>
</html>
