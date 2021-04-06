<%@page import="com.fy.engineserver.datasource.article.data.props.BrightInlayProps"%>
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
	
	String[] articleNames = new String[]{"高级橙彩宝石(一年)","高级幻彩宝石(一年)","高级幻蓝宝石(一年)","高级亮星宝石(一年)","高级曙光宝石(一年)","高级星光宝石(一年)","高级炫光宝石(一年)"
			,"高级异彩宝石(一年)","高级月光宝石(一年)","高级紫灵宝石(一年)"};
	
	
	for (Article a : ArticleManager.getInstance().getAllArticles()) {
		for (String str : articleNames) {
			if (str.equalsIgnoreCase(a.getName())) {
				if (a instanceof BrightInlayProps) {
					if (a.isHaveValidDays() && a.getMaxValidDays() == 4320) {
						a.setMaxValidDays(525600);
						out.println(a.getName() + " = " + a.getMaxValidDays() + "<br>");
					}
				}
			}
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