<%@page import="com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker"%>
<%@page import="com.fy.boss.platform.migu.MiguWorker"%>
<%@page import="com.fy.boss.message.appstore.AppStoreKunLunSavingManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.boss.message.appstore.AppStoreSavingManager"%>
<%@page import="com.fy.boss.message.appstore.AppStoreGuojiSavingManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	boolean isSwitchLimitChannel = ParamUtils.getBooleanParameter(request, "switchLimitChannel");	
	boolean isSwitchLimitServer = ParamUtils.getBooleanParameter(request, "switchLimitServer");	
	boolean isSwitchLimitMigu = ParamUtils.getBooleanParameter(request, "switchLimitMigu");
	
	if(isSwitchLimitMigu)
	{
		MiGuTradeServiceWorker.isLimitMigu = !MiGuTradeServiceWorker.isLimitMigu;
	}
	
	if(isSwitchLimitChannel)
	{
		MiGuTradeServiceWorker.是否限制渠道 = !MiGuTradeServiceWorker.是否限制渠道;
	}
	
	if(isSwitchLimitServer)
	{
		MiGuTradeServiceWorker.是否限制服务器 = !MiGuTradeServiceWorker.是否限制服务器;
	}
%>

<form method="post">
<input type="hidden" name="switchLimitMigu" value="true">
	<input type="submit" value="<%if(MiGuTradeServiceWorker.isLimitMigu){ %>不限制打开米谷助手<% }else{%>限制打开米谷助手<%} %>" >
</form>

<form method="post">
<input type="hidden" name="switchLimitChannel" value="true">
	<input type="submit" value="<%if(MiGuTradeServiceWorker.是否限制渠道){ %>不限制打开米谷助手的渠道<% }else{%>启用打开米谷助手的渠道限制<%} %>" >
</form>

<form method="post">
<input type="hidden" name="switchLimitServer" value="true">
	<input type="submit" value="<%if(MiGuTradeServiceWorker.是否限制服务器){ %>不限制打开米谷助手的服务器<% }else{%>启用米谷助手的服务器限制<%} %>" >
</form>

</body>
</html>