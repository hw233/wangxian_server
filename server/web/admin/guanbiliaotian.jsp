<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关闭聊天</title>
</head>
<body>
<%
	ChatMessageService.isJianCha = false;
%>
修改成功
</body>
</html>