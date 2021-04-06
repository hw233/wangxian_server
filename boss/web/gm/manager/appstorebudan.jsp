<%@page import="com.fy.boss.authorize.model.*,com.fy.boss.authorize.service.*,java.util.*,com.xuanzhi.tools.text.*,com.fy.boss.message.appstore.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>查询</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
	</head>
	<body>
		<br>
		<br>
		<h1>
			APPSTORE充值补单
		</h1>
		<form action="appstorebudan.jsp" method=post name=f1>
			帐号:<input type=text name="username" size="15"/>
			渠道:<input type=text name="channel" size="15"/>
			服务器:<input type=text name="servername" size="15"/>
			设备号:<input type=text name="deviceCode" size="15"/><br>
			Receipt:<textarea name="receipt" rows="5" cols="80"></textarea>
			<input type=submit name=sub1 value="补单">
		</form>
	
<% 
String username = request.getParameter("username");
if(username != null) {
	long n = System.currentTimeMillis();
	String channel = request.getParameter("channel");
	String servername = request.getParameter("servername");	
	String deviceCode = request.getParameter("deviceCode");
	String receipt = request.getParameter("receipt");
	Passport passport = PassportManager.getInstance().getPassport(username);
	String orderId = null;
	if(passport != null) {
		orderId = AppStoreSavingManager.getInstance().appStoreSaving(passport,channel,servername,receipt,deviceCode);
		System.out.println("[苹果补单] [username:"+username+"] [channel:"+channel+"] [servername:"+servername+"] [deviceCode:"+deviceCode+"] [receipt:"+receipt+"] [orderId:"+orderId+"] ["+(System.currentTimeMillis()-n)+"ms]");
	}
%><br>
用户名:<%=username %>， 渠道:<%=channel %>, 服务器:<%=servername %>, deviceCode:<%=deviceCode %><br>
Receipt:<%=receipt %><br><br>
返回的订单号:<%=orderId %>
		<br>
		<Br>
<%} %>
	</body>
</html>