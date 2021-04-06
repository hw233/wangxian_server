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
		if (a.getName().equals("干蓝秘籍残卷一")) {
			a.setName("乾蓝秘籍残卷一");
			a.setName_stat("乾蓝秘籍残卷一");
		} else if (a.getName().equals("干蓝秘籍残卷二")) {
			a.setName("乾蓝秘籍残卷二");
			a.setName_stat("乾蓝秘籍残卷二");
		} else if (a.getName().equals("干蓝秘籍残卷三")) {
			a.setName("乾蓝秘籍残卷三");
			a.setName_stat("乾蓝秘籍残卷三");
		} else if (a.getName().equals("干蓝秘籍残卷四")) {
			a.setName("乾蓝秘籍残卷四");
			a.setName_stat("乾蓝秘籍残卷四");
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
	
	for (ActivityShowInfo asi : ActivityManagers.getInstance().infos) {
		if (asi.activityId == 107) {
			asi.activityContent = asi.activityContent.split("活动期间内")[0] + "活动期间内，每日登陆领取10活跃度礼包即送神秘的—嫦娥赐福券（收集嫦娥赐福券，将在下个周期活动中兑换神秘物品），完成100活跃度领取礼包将加送中秋福利月饼*1，开启福利月饼可以获得各种宝石、炼星、灵兽内丹及各类稀有道具！";
			out.println(asi.activityContent + "<br>");
		}
	}
	
	
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