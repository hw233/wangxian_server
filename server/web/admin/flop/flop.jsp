<%@page import="java.util.Set"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.flop.FlopManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if(action != null) {
			if("checkP".equals(action)){
				long id = Long.parseLong(request.getParameter("pId"));
				Player p = PlayerManager.getInstance().getPlayer(id);
				Hashtable<String,Integer> tb = FlopManager.getInstance().flopCounts.get(id);
				Set<String> keys = tb.keySet();
				%>
				<table border="2">
				<tr>
					<td>物品key</td>
					<td>数目</td>
				</tr>
				<%
				for (String key : keys) {
					
					%>
					<tr>
						<td><%=key %></td>
						<td><%=tb.get(key).intValue() %></td>
					</tr>
					<%
				}
				%>
				</table>
				<%
			}
		}
		
	%>
	<br>
	<br>
	玩家数目<%=FlopManager.getInstance().flopCounts.size() %>
	<br>
	<br>
	<%
		%>
		<table border="2">
			<tr>
					<td>物品key</td>
					<td>数目</td>
				</tr>
		<%
		Set<String> keys = FlopManager.getInstance().articleFlopMaxValues.keySet();
		for (String k : keys) {
			%>
				<tr>
					<td><%=k %></td>
					<td><%=FlopManager.getInstance().articleFlopMaxValues.get(k) %></td>
				</tr>
			<%
		}
		%>
		</table>
		<%
	
	%>
	<br>
	<br>
	<form name="f2">
		查询个人掉落
		<input type="hidden" name="action" value="checkP">
		pId<input name="pId">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
</body>
</html>