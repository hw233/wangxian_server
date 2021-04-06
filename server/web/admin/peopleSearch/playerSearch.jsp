<%@page import="com.fy.engineserver.activity.peoplesearch.CountryNpc"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleSearch"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String op = request.getParameter("op");
	PeopleSearch peopleSearch = null;
	if (playerName != null && !"".equals(playerName) && op != null) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		if (player == null) {
			out.print("角色[" + playerName + "]不存在");
		} else {
			if ("del".equals(op)) {
				//player.setPeopleSearch(null);
				//player.setLastSearchPeopleTime(0L);
				//out.print("清除完毕" + player.getPeopleSearch());
				//peopleSearch = null;
			} else if ("sel".equals(op)) {
				out.print(player.getPeopleSearch());
				out.print("<BR/>");
				//System.out.print(player.getPeopleSearch() == null ? "" : player.getPeopleSearch().getNotice());
				peopleSearch = player.getPeopleSearch();
			}
		}
	} else {
		playerName = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./playerSearch.jsp" method="post">
		<input type="text" name="playerName" value="<%=playerName%>">
		<input type="submit" value="查询"> <input type="hidden"
			name="op" value="sel">
	</form>
	<form action="./playerSearch.jsp" method="post">
		<input type="text" name="playerName" value="<%=playerName%>">
		<input type="submit" value="移除"> <input type="hidden"
			name="op" value="del">
	</form>
	<% if (peopleSearch != null) { %>
		<table border="1" style="font-size: 12px;">
			<tr>
				<td>目标</td>
				<td><%=peopleSearch.getPeopleTemplet().getTarget().toString() %></td>
				<td>模板ID</td>
				<td><%=peopleSearch.getTempletId() %></td>
				<td>当前分数</td>
				<td><%=peopleSearch.getScore() %></td>
				<td>是否找到了目标</td>
				<td><%=peopleSearch.isFound() %></td>
				
			</tr>
			<tr>
				<td>消息提供NPC</td>
				<td colspan="3"><%
						for (CountryNpc countryNpc: peopleSearch.getMessageNpc()) {
							out.print(countryNpc.toString() + "<BR/>");
						}
						%></td>
				<td>已打探到的消息</td>
				<td colspan="3"><% for (int i = 0; i < peopleSearch.getSnooped().length;i++){
						boolean visited = peopleSearch.getSnooped()[i];
						if (visited) {
							int messageIndex = peopleSearch.getMessageIndex()[i];
							String message = peopleSearch.getPeopleTemplet().getDes()[i][messageIndex];
							out.print("第"+i+"个:" + message + "<BR/>");
						} else {
							out.print("第"+i+"个: 还未打探<BR/>");
						}
					
				} %></td>
			</tr>
		</table>
	
	<%} %>
</body>
</html>