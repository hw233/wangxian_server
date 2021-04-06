<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<%@page import="com.fy.engineserver.activity.base.AddRateActivity"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.util.config.ServerFit"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%><html>
<head>
<title>刷腾讯充值返积分页面</title>
</head>
<body>
<p>
<%
	List<AddRateActivity> jifenActivity = ActivityManager.getInstance().getJifenActivity();
	if (jifenActivity.size() > 0) {
		jifenActivity.clear();
	} else {
		out.print("无充值返双倍积分活动");
	}
	ServerFit fit = new ServerFit4Activity("tencent", "", "");
	AddRateActivity aa = new AddRateActivity("2013-12-17 00:00:00", "2013-12-25 23:59:59", 1, fit);
	jifenActivity.add(aa);
	ActivityManager.getInstance().setJifenActivity(jifenActivity);
	out.print("完成<br>");

	out.print("本服是否开放" + aa.getFit().thisServerOpen());
%>
</p>
</body>
</html>
