<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%><html>
  <head>
    <base href="<%=basePath%>">
    
    <title>根据id查询坐骑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
 
 <%
 	String horseIds = request.getParameter("horseId");
 	if(horseIds == null || horseIds.equals("")){
 		%>
 		<form action="">
 			马匹id:<input type="text" name="horseId"/>
 			<input type="submit" value="submit">
 		</form>
 		<%
 	
 		Horse horse =  HorseManager.em.find(Long.parseLong(horseIds));
 		if(horse != null){
 			out.print("主人id"+horse.getOwnerId());
 		}else{
 			out.print("horse null");
 		}
 	}
 
 %>
 
  </body>
</html>
