<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreManager"%>


<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.ExchangeArticle"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreExpTemplate"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷新所有的寻宝物品</title>
</head>
<body>
	<%
		String name = request.getParameter("playerName");
		if(name == null || name.equals("")){
			List<ExploreExpTemplate> list = ExploreManager.getInstance().getExpList();
			for(ExploreExpTemplate eet :list){
				out.print("物品名"+eet.getArticleName()+"       任务id:"+eet.getTaskId()+"    经验id:"+eet.getExpId()+"<br/>");
			}
			
			out.print("刷新前*********************************************************<br/>");
			%>
			<form action="" name="beginExplore">
				随便：<input type="text" name="playerName" />
				<input type="submit" value="提交"/>
			</form>
			<%
		}else{
			
			List<ExploreExpTemplate> list = ExploreManager.getInstance().getExpList();
			for(ExploreExpTemplate eet :list){
				out.print("物品名"+eet.getArticleName()+"       任务id:"+eet.getTaskId()+"    经验id:"+eet.getExpId()+"<br/>");
			}
			list.clear();
			ExploreManager.getInstance().init();
			out.print("刷新后*********************************************************<br/>");
			list = ExploreManager.getInstance().getExpList();
			for(ExploreExpTemplate eet :list){
				out.print("物品名"+eet.getArticleName()+"       任务id:"+eet.getTaskId()+"    经验id:"+eet.getExpId()+"<br/>");
			}
		}
	
	%>

	
</body>
</html>