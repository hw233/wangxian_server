<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Article[] articles = ArticleManager.getInstance().getAllArticles();
	Map<Class<?>,List<Article>> classArtiles = new HashMap<Class<?>,List<Article>>();
	for(Article article : articles) {
		Class<?> clazz = article.getClass();
		if (!classArtiles.containsKey(clazz)) {
			classArtiles.put(clazz, new ArrayList<Article>());
		}
		classArtiles.get(clazz).add(article);
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品查看</title>
</head>
<body>

</body>
</html>