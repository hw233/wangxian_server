<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改棉花糖的活动时间</title>
<%
	ActivityManager am = ActivityManager.getInstance();
	out.print("《修改之前》棉花糖结束时间"+TimeTool.formatter.varChar23.format(am.棉花糖结束时间));
	am.棉花糖结束时间  = TimeTool.formatter.varChar19.parse("2013-06-20 23:59:59");
	out.print("《修改之后》棉花糖结束时间"+TimeTool.formatter.varChar23.format(am.棉花糖结束时间));
%>