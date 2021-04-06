<%@page
	import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	{
		Article ae = ArticleManager.getInstance().getArticle("絕品寵囊");
		PackageProps pps = (PackageProps) ae;
		pps.getArticleNames();
		for (ArticleProperty ap : pps.getArticleNames()) {
			if (ap.getArticleName().equals("靈獸內丹")) {
				ap.setColor(0);
				out.print(pps.getName() + " 内的 " + ap.getArticleName() + "颜色改为:" + ap.getColor() + "<BR/>");
			}
		}
	}
	{
		Article ae = ArticleManager.getInstance().getArticle("仙品寵囊");
		PackageProps pps = (PackageProps) ae;
		pps.getArticleNames();
		for (ArticleProperty ap : pps.getArticleNames()) {
			if (ap.getArticleName().equals("靈獸內丹")) {
				ap.setColor(0);
				out.print(pps.getName() + " 内的 " + ap.getArticleName() + "颜色改为:" + ap.getColor() + "<BR/>");
			}
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