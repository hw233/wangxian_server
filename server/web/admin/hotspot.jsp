<%@page import="com.fy.engineserver.hotspot.HotspotInfo"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page
	import="com.fy.engineserver.hotspot.HotspotManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	HashMap<Long, HotspotInfo[]> underRuleMap = HotspotManager.getInstance().getPlayerHotspots();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>热点</title>
</head>
<body>
<table border="1">

	<tr>
		<td>player.Id() </td>
		<td>热点打开 </td>
		<td>热点看过 </td>
		<td>热点完成 </td>
	</tr>

	<%
		for (Iterator<Long> iterator = underRuleMap.keySet()
				.iterator(); iterator.hasNext();) {

			Long id = iterator.next();
			HotspotInfo[] infos = underRuleMap.get(id);
			String open = "";
			String see = "";
			String over = "";
			for(int i = 0; i < infos.length; i++){
				if (infos[i] == null) {
					open = open + "false,";
					see = see + "false,";
					over = over + "false,";
					continue;
				}
				open = open + "true ,";
				if (infos[i].isSee()) {
					see = see+infos[i].isSee()+" ,";
				}else{
					see = see+infos[i].isSee()+",";
				}
				if (infos[i].isOver()) {
					over = over+infos[i].isOver()+" ,";
				}else {
					over = over+infos[i].isOver()+",";
				}
				
			}
	%>
	<tr>
		<td><%=id %></td>
		<td><%=open %></td>
		<td><%=see %></td>
		<td><%=over %></td>
	</tr>
	<%
		}
	%>
	</table>
</body>
</html>