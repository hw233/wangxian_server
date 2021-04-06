<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改装备持续时间</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br> 
		<%
			ArticleManager am = ArticleManager.getInstance();
			Article[] allArticles = am.getAllArticles();
			for(Article a:allArticles){
				if(a.getName().equals("光之翼(7天)")){
					if(a instanceof Equipment){
						Equipment p = (Equipment)a;
						out.print("修改前》》》要修改的物品名："+a.getName()+"--持续时间："+p.getMaxValidDays()+"<br><hr>");	
						p.setMaxValidDays(10080);
						out.print("修改后》》》要修改的物品名："+a.getName()+"--持续时间："+p.getMaxValidDays()+"<br>");
					}
				}
			}
		
		%>

	</body>
</html>
