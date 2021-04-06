<%@page import="java.sql.Timestamp"%>
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
	long now = System.currentTimeMillis();
	for (BaseActivityInstance bi :  list) {
		if (((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleCNName().equals("宝石湛天(3级)")) {
			if (bi.isThisServerFit() == null) {
				bi.setEndTime(Timestamp.valueOf("2014-03-30 00:00:00").getTime());
				out.println("[更改活动:" + bi.getInfoShow() + "] [结束时间:" + new Timestamp(bi.getEndTime()) + "]<br<br>>");
			}
		} else if (((ArticleActivityOfUseAndGive)bi).getNeedBuyProp().getArticleCNName().equals("宝石墨轮(3级)")) {
			if (bi.isThisServerFit() == null) {
				bi.setEndTime(Timestamp.valueOf("2014-03-30 00:00:00").getTime());
				out.println("[更改活动:" + bi.getInfoShow() + "] [结束时间:" + new Timestamp(bi.getEndTime()) + "]<br><br>");
			}
		}
	}
	out.println("ok!");
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