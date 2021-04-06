<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>删除通行证</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>删除通行证</h1>
	<%
	PassportManager pmanager = PassportManager.getInstance();
	String idstr = request.getParameter("id");
	pmanager.deletePassport(Long.parseLong(idstr));
	response.sendRedirect("passport_list.jsp");
	%>
	
</body>
</html>
