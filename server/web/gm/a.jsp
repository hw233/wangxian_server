<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>潜龙GM邮箱</title>
<link rel="stylesheet" href="../style.css"/>
<script type='text/javascript' src='/game_server/dwr/interface/getmes.js'></script>
<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
<script type="text/javascript" src="/game_server/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/game_server/dwr/util.js"></script>
<script type="text/javascript">
 
</script>
</head>
<body>
  <%
    
     	String username = session.getAttribute("username").toString();
     	String gmid = session.getAttribute("gmid").toString();
     	out.print("欢迎 username is "+username+" gmid is "+gmid);
     	out.print("<hr/>session id is "+session.getId()+"<br/>");
     	
     
  %>
</body>
