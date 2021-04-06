<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%><html>
<head>
<title>test</title>
</head>
<body>

	<%
	
		Player[] ps =  PlayerManager.getInstance().getOnlinePlayers();
		for(Player p :ps){
			p.init();
		}
		
		out.print("刷新坐骑");
		
	%>

</body>

</html>
