<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<%@page import="com.fy.engineserver.activity.base.AddRateActivity"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.util.config.ServerFit"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page import="com.fy.engineserver.util.TimeTool"%><html>
<head>
<title>刷腾讯酒贴次数加倍页面</title>
</head>
<body>
<p>
<%
	ArrayList<TimesActivity> activitys = TimesActivityManager.instacen.activitys;
	if (activitys != null) {
		int blessId=1200;
		TimesActivity ta1 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				6, 3,20, "tencent");
		ta1.setServerNames(new String[]{"神龙摆尾"});
		ta1.setUnServerNames(null);
		activitys.add(ta1);
		
		TimesActivity ta2 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				6, 2,20, "tencent");
		ta2.setServerNames(new String[]{"神龙摆尾"});
		ta2.setUnServerNames(null);
		activitys.add(ta2);
		
		TimesActivity ta3 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				15, 3,20, "tencent");
		ta3.setServerNames(new String[]{"柳暗花明"});
		ta3.setUnServerNames(null);
		activitys.add(ta3);
		
		TimesActivity ta4 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				15, 2,20, "tencent");
		ta4.setServerNames(new String[]{"柳暗花明"});
		ta4.setUnServerNames(null);
		activitys.add(ta4);
		
		TimesActivity ta5 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				6, 2,20, "tencent");
		ta5.setServerNames(new String[]{"化外之境"});
		ta5.setUnServerNames(null);
		activitys.add(ta5);
		
		TimesActivity ta6 = new TimesActivity(blessId++, TimeTool.formatter.varChar19.parse("2013-12-23 00:00:00"), TimeTool.formatter.varChar19.parse("2013-12-23 23:59:59"), 
				15, 3,20, "tencent");
		ta6.setServerNames(new String[]{"化外之境"});
		ta6.setUnServerNames(null);
		activitys.add(ta6);
		
		TimesActivityManager.instacen.activitys=activitys;
		out.print("完成<br>");

	}
%>
</p>
</body>
</html>
