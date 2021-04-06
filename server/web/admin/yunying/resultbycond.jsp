<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page import="com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>批量查询角色信息 </title>
</head>
<body>
<table border="1px">

<tr>
	<td>角色ID</td>
	<td>账号名</td>
	<td>角色名</td>
</tr>
<%

	String usernames = request.getParameter("usernames");
	PlayerManager mpm = PlayerManager.getInstance();
	String playernames = request.getParameter("playernames");
	
	boolean isQueryByUserName = ParamUtils.getBooleanParameter(request, "isQueryByUserName");
	boolean isQueryByPlayerName = ParamUtils.getBooleanParameter(request, "isQueryByPlayerName");
	
	if(isQueryByUserName)
	{
		String[] userNames = usernames.split("\r*\n");
		
		for(String username : userNames)
		{
			Player players[] = mpm.getPlayerByUser(username);
			if(players == null || players.length == 0)
			{
				continue;
			}
			for(Player player : players)
			{
%>
	<tr>
		<td>
			<%=player.getId() %>
		</td>
		<td>
			<%=player.getUsername() %>
		</td>
		<td>
			<%=player.getName() %>
		</td>
	</tr>
<%		
			}
		}
	}
	
	if(isQueryByPlayerName)
	{
		String[] playerNames = playernames.split("\r*\n");
		
		for(String playername : playerNames)
		{
			Player player = mpm.getPlayer(playername);
			if(player == null)
			{
				continue;
			}
		
%>
		<tr>
		<td>
			<%=player.getId() %>
		</td>
		<td>
			<%=player.getUsername() %>
		</td>
		<td>
			<%=player.getName() %>
		</td>
	</tr>
	<% 
		}
	}
	%>
</table>
请输入账号:<br/>
	<form method="post">
		<input type="hidden" name="isQueryByUserName" value="true" />
			<textarea rows="10" cols="10" name="usernames" value="<%=usernames%>"></textarea>
		<input type="submit" value="提交">
	</form>
请输入角色名:<br/>	
	<form method="post">
		<input type="hidden" name="isQueryByPlayerName" value="true" />
			<textarea rows="10" cols="10" name="playernames" value="<%=playernames%>"></textarea>
		<input type="submit" value="提交">
	</form>

</body>
</html>
