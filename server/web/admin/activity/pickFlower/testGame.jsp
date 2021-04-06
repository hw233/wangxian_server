<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>测试game</title>
</head>

<body>
	
	<%
	
		Game[] gs =  GameManager.getInstance().getGames();
		
		out.print(gs.length+"<br/>");
		for(Game g:gs){
			out.print(g.gi.displayName+" "+g.gi.getName() + "  "+g.country+"<br/>");
		}
	
	%>

</body>
</html>
