<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ZongPaiManager zpm = ZongPaiManager.getInstance();
	Field f = ZongPaiManager.class.getDeclaredField("zongPaiMap");
	f.setAccessible(true);
	ConcurrentHashMap<Long, ZongPai> zongPaiMap = (ConcurrentHashMap<Long, ZongPai>) f.get(zpm);

	Field f1 = ZongPaiManager.class.getDeclaredField("citySeizeMap");
	f1.setAccessible(true);
	HashMap<String, Long> citySeizeMap = (HashMap<String, Long>) f1.get(zpm);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table style="font-size: 12px;" border="1">
	<tr>
		<td>ID</td>
		<td>名字</td>
		<td>宗长</td>
		<td>家族数量</td>
		<td>占领城市</td>
		<td>家族列表</td>
	</tr>
		<%
			for (Iterator<Long> itor = zongPaiMap.keySet().iterator(); itor.hasNext();) {
				Long id = itor.next();
				ZongPai zp = zongPaiMap.get(id);
		%>
	
		<tr>
			<td><%=zp.getId()%></td>
			<td><%=zp.getZpname()%></td>
			<td><%=zp.getMasterId()%></td>
			<td><%=zp.getJiazuNum()%></td>
			<td><%=zp.getSeizeCity()%></td>
			<td><%=Arrays.toString(zp.getJiazuIds().toArray(new Long[0]))%></td>
		</tr>

		<%
			}
		%>
</table>
<h1>被占领的城市</h1>
<% for (Iterator<String> itor = citySeizeMap.keySet().iterator();itor.hasNext();) { 
	String name = itor.next();
	out.print("地图:" + name + ",宗派ID" + citySeizeMap.get(name) + "</BR>");
} 
%>
</body>
</html>