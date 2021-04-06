<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String carbonLevel = request.getParameter("carbonLevel");
	%>
	<table
		style="text-align: right; font-size: 12px; float: left; border-width: 20%;"
		border="1">
		<tr>
			<th>角色id</th>
			<th>角色名</th>
			<th>最后登录时间</th>
		</tr>
		<%
			if ("enterCarbon".equals(action)) {
				long ppId = Long.parseLong(playerId);
				Player p = ((GamePlayerManager) (GamePlayerManager.getInstance())).em.find(ppId);
				long[] ids = ((GamePlayerManager) (GamePlayerManager.getInstance())).em.queryIds(Player.class, "username=?", new Object[] { p.getUsername() }, "enterGameTime desc", 1, 100);
				if (ids.length <=  ((GamePlayerManager) (GamePlayerManager.getInstance())).最大可查询的角色数量) {
					out.println("不允许修改！！！！");
					return ;
				}
				long time = Timestamp.valueOf(carbonLevel).getTime();
				p.setEnterGameTime(time);
				((GamePlayerManager) (GamePlayerManager.getInstance())).em.flush(p);
				out.println("修改成功");
			} else if ("queryAllPlayers".equals(action)) {
				long[] ids = ((GamePlayerManager) (GamePlayerManager.getInstance())).em.queryIds(Player.class, "username=?", new Object[] { playerId }, "enterGameTime desc", 1, 100);
				for (int i = 0; i < ids.length; i++) {
					Player p = ((GamePlayerManager) (GamePlayerManager.getInstance())).em.find(ids[i]);
		%>
		<tr bgcolor="8CACE8">
			<td><%=p.getId()%></td>
			<td><%=p.getName()%></td>
			<td><%=(new Timestamp(p.getEnterGameTime()))%></td>
		</tr>
		<%
			}
			}
		%>
	</table>
	<table>
		<form action="modifyPlayerTime.jsp" method="post">
			<input type="hidden" name="action" value="queryAllPlayers" />角色 名 :
			<input type="text" name="playerId" value="<%=playerId%>" /><input
				type="submit" value="查询账号所有角色" />
		</form>
		<br />

		<form action="modifyPlayerTime.jsp" method="post">
			<input type="hidden" name="action" value="enterCarbon" />角色 id : <input
				type="text" name="playerId" value="<%=playerId%>" />时间: <input
				type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
				type="submit" value="修改最后登录时间" />
		</form>
		<br />
	</table>
</body>
</html>
