<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.LineItem"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>战场排队</title>
<% 
BattleFieldLineupService service = BattleFieldLineupService.getInstance();	
BattleFieldManager bfm = BattleFieldManager.getInstance();
	 
	
	
	
	
%>
</head>
<body>
<h2>战场情况</h2>
<a href="./list_battlefield_queue.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>编号</td>
<td>排队时间</td>
<td>战场名</td>
<td>排队玩家</td>
</tr>
<%
	BattleFieldLineupService.LineItem items[] = service.getLineItems();
	for(int i = 0 ; i < items.length ; i++){
			
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td >"+(i+1)+"</td>");
			
		out.println("<td>"+DateUtil.formatDate(new Date(items[i].lineTime),"HH:mm:ss")+"</td>");
		out.println("<td>"+items[i].battleName+"</td>");
		StringBuffer sb = new StringBuffer();
		for(Player p : items[i].players){
			sb.append(p.getName()+" , ");
		}
		out.println("<td>"+sb.toString()+"</td>");
		out.println("</tr>");
	}
%>
</table>

<br>
<h3>等待进入队列</h3>
<br>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>战场名</td>
<td>战场</td>
<td>玩家</td>
<td>等待进入时间</td>
<td>哪一方</td>
</tr>
<%

WaittingItem items2[] = service.getWaittingItems();

for(int i = 0 ; i < items2.length ; i++){
	WaittingItem wi = items2[i];
	
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td >"+(i+1)+"</td>");
	
	out.println("<td>"+wi.bf.getName()+"</td>");
	out.println("<td>"+wi.bf.getId() +"</td>");
	out.println("<td>"+wi.player.getName() +"</td>");
	out.println("<td>"+DateUtil.formatDate(new Date(wi.startTime),"HH:mm:ss")+"</td>");
	String sss[] = new String[]{"C方","A方","B方"};
	out.println("<td>"+sss[wi.side]+"</td>");
	StringBuffer sb = new StringBuffer();
	for(Player p : items[i].players){
		sb.append(p.getName());
	}
	out.println("<td>"+sb.toString()+"</td>");
	out.println("</tr>");
}
%>
</body>
</html>
