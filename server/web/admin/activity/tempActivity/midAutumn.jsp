<%@page import="java.util.Iterator"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Hashtable<Long,Integer> ht =(Hashtable<Long,Integer>)ActivityManager.getInstance().disk.get(ActivityManager.key_中秋国庆天天有好礼);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中秋节活动</title>
</head>
<body>
<table border="1">
<tr>
	<td>角色ID</td>
	<td>领取次数</td>
</tr>
	<%
		if (ht != null) {
			for (Iterator<Long> itor = ht.keySet().iterator();itor.hasNext();) {
				Long playerId = itor.next();
				int value = ht.get(playerId);
	%>
		<tr>
			<td><%=playerId %></td>
			<td><%=value %></td>
		</tr>
	<%
			}
		}
	%>
</table>	
</body>
</html>