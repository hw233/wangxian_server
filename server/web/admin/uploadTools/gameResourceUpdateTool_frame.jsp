<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<title>资源更新</title>
</head>
	<%
	%>		
		<frameset cols="15%,80%" title="资源更新">
			<frame src="gameResourceUpdateTool_left.jsp"></frame>
			<frame src="http://119.254.154.199:12114/admin/uploadTools/gameResourceUpdateTool_right.jsp?action=" name="targetname"></frame>
		</frameset>
	<% 		
	%>
	
</html>