<%@page import="com.fy.boss.cmd.CMDService"%>
<%@page import="com.fy.boss.deploy.DeployBoundry"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>测试远程执行命令</title>
</head>
<body>
<%
	CMDService cmdService = CMDService.getInstance();
	
	cmdService.execCMD("116.213.192.242", "/home/game/resin_server/bin/httpd.sh stop");
	
	out.println("执行完毕");
%>
</body>
</html>