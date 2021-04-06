<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreManager"%>


<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.ExchangeArticle"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询所有的寻宝物品</title>
</head>
<body>
	<%
		String name = request.getParameter("playerName");
		if(name == null){
			%>
			<form action="" name="beginExplore">
				随便：<input type="text" name="playerName" />
				<input type="submit" value="提交"/>
			</form>
			<%
		}else{
			
			List<ExchangeArticle> list1 = ArticleManager.getInstance().exchangeArticleList;
			for(ExchangeArticle ea: list1){
				out.print("name:"+ea.getName()+ea.isUse()+"<br/>");
			}
		}
	
	%>

	
</body>
</html>