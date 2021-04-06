<%@page import="com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
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
	boolean limitMigu = ParamUtils.getBooleanParameter(request, "switchMigu");	
	if(limitMigu)
	{
		MiGuTradeServiceWorker.isLimitMigu = !MiGuTradeServiceWorker.isLimitMigu;
	}
	
	
%>
<form method="post">
<input type="hidden" name="switchMigu" value="true">
	<input type="submit" value="<%if(MiGuTradeServiceWorker.isLimitMigu){ %>显示米谷助手<% }else{%>关闭米谷助手<%} %>" >
</form>

</body>
</html>