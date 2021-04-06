<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.jiayuan.*,com.fy.engineserver.gang.model.*,com.fy.engineserver.gang.service.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>帮派家园</title>
<% 
	JiaYuanManager jyManager = JiaYuanManager.getInstance();
	JiaYuan jys[] = jyManager.getAllJiaYuans();

%>
</head>
<body>
<h2>所有的帮派家园：</h2>
<a href="./list_jiayuan.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>编号</td>
<td>家园ID</td>
<td>帮派名称</td>
<td>家园等级</td>
<td>家园中人数</td>
<td>家园地图</td>
<td>创建时间</td>
<td>管理线程编号</td>
</tr>

<%
	GangManager gm = GangManager.getInstance();
	for(int i = 0 ; i < jys.length ; i++){
		JiaYuan g = jys[i];
		
		long id = g.gangId;
		Gang gang = gm.getGang(id);
		if(gang != null && !gang.getName().equals(g.gangName)) {
			g.setGangName(gang.getName());
			g.setDirty(true);
		}
		
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		
		out.println("<td>"+g.getGangId()+"</td>");
		out.println("<td>"+g.getGangName()+"</td>");
		out.println("<td>"+g.getLevel()+"</td>");
		out.println("<td>"+g.getGame().getNumOfPlayer()+"</td>");
		out.println("<td>"+g.getGame().getGameInfo().getMapName()+"</td>");
		out.println("<td>"+ DateUtil.formatDate(new Date(g.getCreateTime()),"yyyy-MM-dd HH:mm:ss") +"</td>");
		out.println("<td>"+g.getThreadIndex()+"</td>");
		out.println("<tr>");
	}
	
%>
</table>


</body>
</html>
