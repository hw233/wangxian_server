<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%

	Field f = MemoryMonsterManager.class.getDeclaredField("monster2FlopListMap");
	f.setAccessible(true);
	LinkedHashMap<String, FlopSet[]> monster2FlopListMap = (LinkedHashMap<String, FlopSet[]>)f.get((MemoryMonsterManager)MemoryMonsterManager.getMonsterManager());
	
	ArticleManager am = ArticleManager.getInstance();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table>
	<% for (Iterator<String> itor = monster2FlopListMap.keySet().iterator();itor.hasNext();) {
		String monsterName = itor.next();
		FlopSet[] sets = monster2FlopListMap.get(monsterName);
		%>
		<tr>
			<td style="background-color: green;"><%=monsterName %></td>
		</tr>
		<tr>
		</tr>			
		<%
			for (FlopSet fs : sets) {
				StringBuffer sbf = new StringBuffer();
				for (String articleName :fs.getArticles()) {
					Article article = am.getArticle(articleName);
					if (article == null) {
						sbf.append("<font color=red>").append(articleName).append("</font>");
					} else {
						sbf.append(articleName);
					}
					sbf.append(",");
				}
		%>
		<tr>
			<td><%=sbf.toString() %></td>
		</tr>			
		<%} %>	
		<%
	} %>
</table>
</body>
</html>