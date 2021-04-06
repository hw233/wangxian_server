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
	boolean isGuoji = ParamUtils.getBooleanParameter(request, "switchGuoji");	
	boolean isApp = ParamUtils.getBooleanParameter(request, "switchAppStore");	
	boolean isTaiwan = ParamUtils.getBooleanParameter(request, "switchTaiwan");	
	
	if(isGuoji)
	{
		AppStoreGuojiSavingManager.isGoSandBox = !AppStoreGuojiSavingManager.isGoSandBox;
	}
	
	if(isApp)
	{
		AppStoreSavingManager.isGoSandBox = !AppStoreSavingManager.isGoSandBox;
	}
	
	if(isTaiwan)
	{
		AppStoreKunLunSavingManager.isGoSandBox = !AppStoreKunLunSavingManager.isGoSandBox;
	}
	
%>
<form method="post">
<input type="hidden" name="switchGuoji" value="true">
	<input type="submit" value="<%if(AppStoreGuojiSavingManager.isGoSandBox){ %>关闭国际版沙盒<% }else{%>开启国际版沙盒<%} %>" >
</form>

<form method="post">
<input type="hidden" name="switchAppStore" value="true">
	<input type="submit" value="<%if(AppStoreSavingManager.isGoSandBox){ %>关闭国内版沙盒<% }else{%>开启国内版沙盒<%} %>" >
</form>
<form method="post">
<input type="hidden" name="switchTaiwan" value="true">
	<input type="submit" value="<%if(AppStoreKunLunSavingManager.isGoSandBox){ %>关闭台湾版沙盒<% }else{%>开启台湾版沙盒<%} %>" >
</form>

</body>
</html>