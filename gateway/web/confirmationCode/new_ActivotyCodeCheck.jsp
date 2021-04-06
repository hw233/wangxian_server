<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.confirm.bean.Prize"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/confirmationcode.css"/>
<script language="javascript" type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
</head>
<body>
<hr>
<form action="./seeCode.jsp" method="post" >
	输入要查询的激活码：<input name="code" type="text">
	<input type="submit" value="查询">
</form>
<hr>
</body>
</html>
