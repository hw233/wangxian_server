<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive"%>
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
    AllActivityManager ac = AllActivityManager.instance;
	List<BaseActivityInstance> list = ac.allActivityMap.get(AllActivityManager.useGiveAct);
	for (BaseActivityInstance bi :  list) {
		if (((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleColor() < 0) {
			Article a = ArticleManager.getInstance().getArticle(((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleCNName());
			((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().setArticleColor(a.getColorType());
			out.println("[" + ((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleCNName() + "_" + ((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleColor() + "]<br>");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正活动</title>
</head>
<body>

</body>
</html>