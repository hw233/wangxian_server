<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String[] rightCNNames = new String[] { "终极造化丹" };
	String[] rightNames = new String[] { "终极造化丹" };
	String[] wrongCNNames = new String[] { "终级造化丹" };
	String[] wrongNames = new String[] { "终级造化丹" };

	if (rightCNNames.length != rightNames.length || rightNames.length != wrongCNNames.length || wrongCNNames.length != wrongNames.length) {
		out.print("<h1 style='color=red'>输入错误,列表长度不同!</h1>");
		return;
	}
	
	Class<?> c = ArticleManager.class;
	Field f_allArticleNameMap = c.getDeclaredField("allArticleNameMap");
	f_allArticleNameMap.setAccessible(true);
	Field f_allArticleCNNameMap = c.getDeclaredField("allArticleCNNameMap");
	f_allArticleCNNameMap.setAccessible(true);
	f_allArticleNameMap.set(ArticleManager.getInstance(), value);

	Map<String, Article> allArticleNameMap = (Map<String, Article>) f_allArticleNameMap.get(ArticleManager.getInstance());
	Map<String, Article> allArticleCNNameMap = (Map<String, Article>) f_allArticleCNNameMap.get(ArticleManager.getInstance());
	
	for(int i = 0 ; i < rightCNNames.length; i ++) {
		String rightCNName =  rightCNNames[i];
		String rightName =  rightNames[i];
		String wrongCNName =  wrongCNNames[i];
		String wrongName =  wrongNames[i];
		Article article = ArticleManager.getInstance().getArticle(wrongName);
		if (article != null) {
			article.setName(rightName);
			article.setName_stat(rightName);

			allArticleNameMap.remove(wrongName);
			allArticleNameMap.put(rightName, article);
			
			allArticleCNNameMap.remove(wrongCNName);
			allArticleCNNameMap.put(rightCNName, article);
			
			out.print("OK,设置完毕:[" + wrongName + "]>[" + rightName + "]<BR/>");
		} else {
			out.print("<H1>物品不存在:" + wrongName + "</H1>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>