<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.BiaoZhiXiangQL"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.BiaoZhiXiangXW"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.BiaoZhiXiangZQ"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.BiaoZhiXiangBH"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if (TestServerConfigManager.isTestServer()) {
		long jiazuId = 1100000000002233347L;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		jiazu.setLastJiazuSilverCartime(0L);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>