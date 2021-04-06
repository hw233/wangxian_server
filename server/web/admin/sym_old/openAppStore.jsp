<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>国际服炼星和元神问题</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
		<%
			String servername = GameConstants.getInstance().getServerName();
			if(!servername.equals("幽恋蝶谷")&&!servername.equals("巍巍昆仑")){
				out.print("操作错误，只有国际服可以改动!");
				return;
			}
			
			out.print("修改前,总的幸运基数："+ArticleManager.TOTAL_LUCK_VALUE+"--最大星："+ArticleManager.starMaxValue+"<br>");
			ArticleManager.TOTAL_LUCK_VALUE = 10000;
			ArticleManager.starMaxValue = 20;
			out.print("修改后,总的幸运基数："+ArticleManager.TOTAL_LUCK_VALUE+"--最大星："+ArticleManager.starMaxValue);
		%>

	</body>
</html>
