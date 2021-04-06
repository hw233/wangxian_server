<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.BeatHeartThread"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.core.BeatHeartThreadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	BeatHeartThreadManager manager = BeatHeartThreadManager.getInstance();
	Field f = BeatHeartThreadManager.class.getDeclaredField("bhtList");
	f.setAccessible(true);
	List<BeatHeartThread> list = (List<BeatHeartThread>) f.get(manager);
	DecimalFormat df1 = new DecimalFormat("##.0000%");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table style="font-size: 12px;text-align: center;" border="1">
		<tr>
			<td>名称</td>
			<td>地图</td>
			<td>心跳次数</td>
			<td>超时次数</td>
			<td>超时比例</td>
		</tr>
		<%
			for (BeatHeartThread bt : list) {
				Game[] games = bt.getGames();
				StringBuffer sbf = new StringBuffer();
				for (Game g : games) {
					sbf.append(g.gi.displayName).append(",");
				}
		%>
		<tr>
			<td><%=bt.getName()%></td>
			<td><%=sbf.toString()%></td>
			<td><%=bt.getAmountOfBeatheart()%></td>
			<td><%=bt.getAmountOfOverflow()%></td>
			<td><%=df1.format(((double) bt.getAmountOfOverflow()) / bt.getAmountOfBeatheart())%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>