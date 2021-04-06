<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	if (PlatformManager.getInstance().isPlatformOf(Platform.官方)||PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
		QianCengTa_Ta.isOpenHard = true;
		out.print("后台设置官方和腾讯千层卡困难模式开启为:"+QianCengTa_Ta.isOpenHard);
		QianCengTa_Ta.logger.error("后台设置官方和腾讯千层卡困难模式开启为:"+QianCengTa_Ta.isOpenHard);
	}
%>
</body>
</html>