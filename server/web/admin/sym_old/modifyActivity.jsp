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
	List<BaseActivityInstance> list = ac.allActivityMap.get(AllActivityManager.taskDeliverAct);
	list.clear();
	TaskDeliverAct ta = new TaskDeliverAct("2014-03-28 00:00:00","2014-04-03 23:59:59","sqage","","");
	ActivityProp giveProp = new ActivityProp("宠物技能升级残片",3,1,true);
	ta.setOtherVar(10, "group", "寻迹宝录", "恭喜您获得宠物技能升级残片", "请查收附件！", giveProp);
	list.add(ta);
	ac.allActivityMap.put(AllActivityManager.taskDeliverAct, list);
	ac.allActivityMap.remove(AllActivityManager.addDevilOpenTimes);
	out.println("刷新成功！！");
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