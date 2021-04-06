<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	DiskCache dc = UnitedServerManager.getInstance().getSameNamePlayerCache();
	if (dc == null) {
		out.print("diskCache不存在");
		return;
	}
	Object o =	dc.get(UnitedServerManager.changeNameListKey);
	if (o == null) {
		out.print(UnitedServerManager.changeNameListKey + "不存在");
		return;
	}
	List<Long> list = (List<Long>)o;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合服角色改名</title>
</head>
<body>
	<table style="font-size: 12px;border: 1">
		<tr style="color: white;font-weight: bold;background-color: black;">
			<td>索引</td>
			<td>ID</td>
			<td>当前名字</td>
			<td>是否已经改名</td>
		</tr>
		<% 
		int index = 0;
		for(Long id : list) { 
			String name = String.valueOf(dc.get(id));
			if ("".equals(name)) {
				continue;
			}
		%>
			<tr>
				<td><%=++index %></td>
				<td><%=id %></td>
				<td><%=name==null ? "--" : name %></td>
				<td><%=name==null %></td>
			</tr>
		<%} %>
	</table>
</body>
</html>