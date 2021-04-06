<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.gang.model.GangMember"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String gangName=request.getParameter("gangName");
Gang gang=null;
Player[] players = null;
if(gangName!=null&&gangName.length()>0){
	gang=GangManager.getInstance().getGang(gangName);
}
if(gang!=null){
	GangMember[] members=gang.getMembers();
	if(members!=null){
		players = new Player[members.length];
		for(int i=0;i<players.length;i++){
			players[i]=PlayerManager.getInstance().getPlayer(members[i].getPlayerid().intValue());
		}
	}
}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@include file="../IPManager.jsp" %><html>
  <head>
    <base href="<%=basePath%>">
    <title>Gang Members</title>
    
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
  	<table width="100%">
  	<tr align="center">
  	<td>
  	<%=gangName%>
  	</td>
  	</tr>
  	</table>
  	<table width="20%" border="1">
  	<tr>
  	<td align="center">姓名</td>
  	<td align="center">ID</td>
  	<td align="center">等级</td>
  	<td align="center">门派</td>
  	</tr>
  	<%
  	for(int i=0;i<players.length;i++){
		out.println("<tr>");
		out.println("<td>"+players[i].getName()+"</td>");
		out.println("<td>"+players[i].getId()+"</td>");
		out.println("<td>"+players[i].getLevel()+"</td>");
		out.println("<td>"+CareerManager.getInstance().getCareer(players[i].getCareer()).getName()+"</td>");
		out.println("</tr>");  		
  	}
  	 %>
  	</table>
	  
    <%
    out.println("<table>");
    out.println("<tr>");
    
    out.println("</tr>");
    out.println("</table>");
    
     %>
  </body>
</html>
