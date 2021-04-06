<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="java.util.ArrayList"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.TotalXiaoFeiActivity"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改</title>
</head>
<body>
<%
	ArrayList<NewXiaoFeiActivity> xiaoFeiAcitivitys = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys;
	for (NewXiaoFeiActivity x : xiaoFeiAcitivitys) {
			if (x.getConfigID() == 1656) {
				x.setServerNames(new String[]{"客户端测试"});
				out.print(x.getName()+"//"+Arrays.toString(x.getServerNames())+"//"+Arrays.toString(x.getParameters())+"<br>");
			}
			if (x.getConfigID() == 1657) {
				x.setServerNames(new String[]{"ALL_SERVER"});
				out.print(x.getName()+"//"+Arrays.toString(x.getServerNames())+"//"+Arrays.toString(x.getParameters())+"<br>");
			}
			if (x.getConfigID() == 1658) {
				x.setServerNames(new String[]{"ALL_SERVER"});
				out.print(x.getName()+"//"+Arrays.toString(x.getServerNames())+"//"+Arrays.toString(x.getParameters())+"<br>");
			}
			if (x.getConfigID() == 1659) {
				x.setServerNames(new String[]{"客户端测试"});
				out.print(x.getName()+"//"+Arrays.toString(x.getServerNames())+"//"+Arrays.toString(x.getParameters())+"<br>");
			}
	}
%>