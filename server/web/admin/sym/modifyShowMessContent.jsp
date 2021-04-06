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
	for (ActivityShowInfo asi : ActivityManagers.getInstance().infos) {
		if (asi.activityId == 100) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
		if (asi.activityId == 101) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
		if (asi.activityId == 102) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
		if (asi.activityId == 103) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
		if (asi.activityId == 104) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
		if (asi.activityId == 105) {
			asi.activityContent = asi.activityContent.replaceAll("紫色法宝兑换券！", "深渊级橙色气血器灵（鞋部）！");
			out.println(asi.activityContent + "<br>");
		}
	}
	
	
	out.println("ok!!");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正热点</title>
</head>
<body>

</body>
</html>