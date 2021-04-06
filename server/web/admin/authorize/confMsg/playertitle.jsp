<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>玩家title</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="4%">类型</th>
		    <th width="7%">类型ID</th>
		    <th width="4%">具体的称号</th>
		</thead>
		
		<%
		PlayerTitlesManager ptm  =  PlayerTitlesManager.getInstance();
		Map<String, Integer> keyMap = ptm.getTitleKeyMap();
		Map<String, String> valueMap =  ptm.getTitleValueMap();
		Iterator<Entry<String,Integer>> itor = keyMap.entrySet().iterator();
		Iterator<Entry<String,String>> itor1 = valueMap.entrySet().iterator();
		while(itor.hasNext()){
			Entry<String, Integer> entry = itor.next();
			String name = entry.getKey();
			int id = entry.getValue();
			String type = ""; 
			if(itor1.hasNext()){
				 type = itor1.next().getValue();
			}
			%>
			<tr>
			<td><%=name%></td>
			<td><%=id %></td>
			<td><%=type %></td>
			</tr>
			<%
		}
		%>
		
	</table>
</body>
</html>