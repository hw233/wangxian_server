<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.fy.engineserver.newBillboard.date.charm.ChargeMonthBillboard"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>更新充值排行榜</title>
		<link rel="stylesheet" href="gm/style.css" />
		<style type="text/css">
</style>
	</head>
	<body>
		<%
			Class c = ChargeMonthBillboard.class;
			
			ChargeMonthBillboard cm = (ChargeMonthBillboard)c.newInstance();
// 			
// 			out.print(cm.getCount()+"--"+cm.getBegintime());
			Field f = c.getDeclaredField("endtime");
			f.setAccessible(true);
			out.print("开始时间："+cm.getBegintime());
			f.setLong(cm, TimeTool.formatter.varChar19.parse("2013-08-10 00:00:00"));
			out.print("改变后的开始时间："+cm.getBegintime());
		%>
	</body>
</html>
