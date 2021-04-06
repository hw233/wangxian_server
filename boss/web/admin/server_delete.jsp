<%@ page contentType="text/html;charset=utf-8"%><%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.fy.boss.authorize.model.*,
				com.fy.boss.authorize.service.*,
				com.fy.boss.authorize.exception.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.finance.service.*,
				com.fy.boss.game.model.*,
				com.fy.boss.deploy.*,
				com.fy.boss.cmd.*,com.fy.boss.game.service.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>删除服务器</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>删除服务器</h1>
	<%
	ServerManager smanager = ServerManager.getInstance();
	String idstr = request.getParameter("id");
	smanager.deleteServer(Long.parseLong(idstr));
	response.sendRedirect("server_list.jsp");
	%>
	
</body>
</html>
