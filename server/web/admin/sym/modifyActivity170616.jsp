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
     AllActivityManager ac = AllActivityManager.instance;
	List<BaseActivityInstance> list = ac.allActivityMap.get(AllActivityManager.useGiveAct);
	String[] str = new String[]{"宝石竹清(6级)", "宝石墨轮(6级)", "宝石湛天(6级)", "宝石幽橘(6级)"};
	String[] str2 = new String[]{"宝石竹清(7级)", "宝石墨轮(7级)", "宝石湛天(7级)", "宝石幽橘(7级)"};
	long time = TimeTool.formatter.varChar19.parse("2017-06-16 00:00:00");
	for (BaseActivityInstance base : list) {
		if (base.getStartTime() < time) {
			continue;
		}
		if (base instanceof ArticleActivityOfUseAndGive) {
			ArticleActivityOfUseAndGive aa = (ArticleActivityOfUseAndGive)base;
			for (int k=0; k<str.length; k++) {
				if (str[k].equals(((ArticleActivityOfUseAndGive)base).getNeedBuyProp().getArticleCNName())) {
					((ArticleActivityOfUseAndGive)base).getGiveProp().setArticleCNName(str2[k]);
					out.println("[开始时间:"+(new Timestamp(aa.getStartTime()))+"] [结束时间:"+(new Timestamp(aa.getEndTime()))+"] [使用物品:"+aa.getNeedBuyProp().getArticleCNName()+"] [赠送物品:"+aa.getGiveProp().getArticleCNName()+"]<br>");
				}
			}
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