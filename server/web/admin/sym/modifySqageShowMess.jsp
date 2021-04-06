<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive"%>
<%@page import="java.sql.Timestamp"%>
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

	String[] notOpen = new String[]{"神游四海","皓腕柔情","神锤憾玄","冰霜之心","断魂憶梦","纵酒狂欢","六界仙尊","玉露仙池","太玄仙遁","寿鹿仙狐","祥云罩顶","轻舞仙凡", "国内本地测试"};
	String[] old = null;
	for (ActivityShowInfo asi : ActivityManagers.getInstance().infos) {
		if (asi.activityId == 319) {
			old = asi.limitservers;
			asi.limitservers = notOpen;
			out.println("[活动名:"+asi.activityName+"]<br>");
			out.println("*****************************************************<br>");
			out.println("原来不开启服务器:" + Arrays.toString(old) + "<br>");
			out.println("*****************************************************<br><br>");
			
			out.println("*****************************************************<br>");
			out.println("修改后不开启服务器:" +Arrays.toString(asi.limitservers) + "<br>");
			out.println("*****************************************************<br>");
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