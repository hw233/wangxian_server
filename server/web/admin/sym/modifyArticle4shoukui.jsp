<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.taskDeliver.TaskDeliverAct"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	Map<String, Article> articleNameMaps = new Hashtable<String, Article>();
	Map<String, Article> articleNameCnnMaps = new Hashtable<String, Article>();
	for (Article a : ArticleManager.getInstance().getAllArticles()) {
		if (a.getName().equals("45级完美橙武器包")) {
			PackageProps pp = ((PackageProps)a);
			ArticleProperty ap = new ArticleProperty();
			ap.setArticleName("天玑幻灭之镰");
			ap.setCount(1);
			ap.setColor(6);
			ArticleProperty[] aps = Arrays.copyOf(((PackageProps)a).getArticleNames(), ((PackageProps)a).getArticleNames().length + 1) ;
			aps[aps.length - 1] = ap;
			((PackageProps)a).setArticleNames(aps);
		}
		articleNameMaps.put(a.getName(), a);
		articleNameCnnMaps.put(a.getName_stat(), a);
	}
	
	Class<?> c = ArticleManager.class;
	Field f_allArticleNameMap = c.getDeclaredField("allArticleNameMap");
	f_allArticleNameMap.setAccessible(true);
	Field f_allArticleCNNameMap = c.getDeclaredField("allArticleCNNameMap");
	f_allArticleCNNameMap.setAccessible(true);
	f_allArticleNameMap.set(ArticleManager.getInstance(), articleNameMaps);
	f_allArticleCNNameMap.set(ArticleManager.getInstance(), articleNameCnnMaps);
	
	
	
	out.println("ok!!");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正白格</title>
</head>
<body>

</body>
</html>