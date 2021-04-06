<%@page import="com.fy.engineserver.activity.CheckManager"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.CheckAttribute"%>
<%@page import="java.lang.annotation.Annotation"%>
<%@page
	import="com.fy.engineserver.activity.silvercar.SilvercarManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	SilvercarManager manager = SilvercarManager.getInstance();
	String value = CheckManager.check(manager);
	out.print(value);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看押镖数据</title>
</head>
<body>

</body>
</html>