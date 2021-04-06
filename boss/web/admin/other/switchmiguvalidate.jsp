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
	boolean isSwitch = ParamUtils.getBooleanParameter(request, "switchMigu");	

	
	if(isSwitch)
	{
		if(MiguWorker.CURRENT_VALIDATE_REQ_TYPE == MiguWorker.VALIDATE_REQUEST_TYPE_DEVICECODE)
		{
			MiguWorker.CURRENT_VALIDATE_REQ_TYPE = MiguWorker.VALIDATE_REQUEST_TYPE_ENTERSERVER_TIME;
		}
		else
		{
			MiguWorker.CURRENT_VALIDATE_REQ_TYPE = MiguWorker.VALIDATE_REQUEST_TYPE_DEVICECODE;
		}
	}
	
%>

<form method="post">
<input type="hidden" name="switchMigu" value="true">
	<input type="submit" value="<%if(MiguWorker.CURRENT_VALIDATE_REQ_TYPE == MiguWorker.VALIDATE_REQUEST_TYPE_DEVICECODE){ %>切换到验证玩家进入时间方式<% }else{%>切换到验证玩家设备方式<%} %>" >
</form>

</body>
</html>