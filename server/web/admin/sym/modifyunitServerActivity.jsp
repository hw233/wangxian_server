<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="com.fy.engineserver.activity.unitedserver.DailyLoginActivity"%>
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
	List<DailyLoginActivity> list = UnitedServerManager.getInstance().dailyLoginActivities;

	for (DailyLoginActivity da : list) {
		if (da.getId() == 130109323) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第3日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第3日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109324) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第4日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第4日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109325) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第5日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第5日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109326) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第6日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第6日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109327) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第7日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第7日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109328) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第8日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第8日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109329) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第9日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第9日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109330) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第10日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第10日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109331) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第11日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第11日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109332) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第12日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第12日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109333) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第13日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第13日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109334) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第14日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第14日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109335) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第15日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第15日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109336) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第16日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第16日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109337) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第17日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第17日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109338) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第18日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第18日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109339) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第19日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第19日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109340) {
			da.setMailContent("亲爱的玩家，恭喜您获得合服第20日奖励，连续登陆将会获得更多奖励");
			da.setMailTitle("合服后登陆第20日奖励");
			out.println(da.getId() + " - " + da.getDay() + " - " + da.getMailTitle() + " - " + da.getMailContent() + "<br>");
		} else if (da.getId() == 130109341) {
			da.getActivityProp().setArticleCNName("紫色玉液锦囊");
			da.getActivityProp().setArticleColor(3);
			da.getActivityProp().setArticleNum(4);
			out.println(da.getDay() + "_" + da.getActivityProp() + "<br>");
		} else if (da.getId() == 130109342) {
			da.getActivityProp().setArticleCNName("橙色玉液锦囊");
			da.getActivityProp().setArticleColor(4);
			da.getActivityProp().setArticleNum(2);
			out.println(da.getDay() + "_" + da.getActivityProp() + "<br>");
		} else if (da.getId() == 130109343) {
			da.getActivityProp().setArticleCNName("橙色玉液锦囊");
			da.getActivityProp().setArticleColor(2);
			da.getActivityProp().setArticleNum(5);
			out.println(da.getDay() + "_" + da.getActivityProp() + "<br>");
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正合服活动</title>
</head>
<body>

</body>
</html>