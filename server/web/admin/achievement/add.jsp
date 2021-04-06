	<%@page import="com.fy.engineserver.achievement.RecordAction"%>
	<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
	<%@page
		import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
	<%@page import="com.fy.engineserver.sprite.Player"%>
	<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%
		String playerIdS = request.getParameter("playerId");
		String achS = request.getParameter("ach");
		System.out.println("playerIdS:"+playerIdS );
		System.out.println("achS:"+achS );
		if (playerIdS != null && !"".equals(playerIdS.trim()) && achS != null && !"".equals(achS.trim())) {
			long playerId = Long.valueOf(playerIdS);
			int achType = Integer.valueOf(achS);
			RecordAction ra = getRecordAction(achType);
			
			int num = Integer.valueOf(request.getParameter("num"));
			if (ra == null) {
				out.print("<H1>成就不存在</H1>");
			} else {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				if (player == null) {
					out.print("<H1>角色不存在</H1>");
				} else {
					AchievementManager.getInstance().record(player,ra,num);
					out.print("<H2>给角色:"+player.getLogString()+" 增加数据:"+ra.getName()+"[数量:"+num+"]成功</H2>");
				}
			}
		} else {
			playerIdS = "";
		}
	%>
	<%!RecordAction getRecordAction(int achType) {
			for (RecordAction action : RecordAction.values()) {
				if (action.getType() == achType) {
					return action;
				}
			}
			return null;
		}%>
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	</head>
	<body>
	
		<form action="" method="post">
		角色ID:<input name="playerId" value="<%=playerIdS%>">
		数据类型:<select	name="ach">
				<option value="-1">--</option>
				<%
					for (RecordAction action : RecordAction.values()) {
				%>
				<option value="<%=action.getType()%>"><%=action.getName()%>[<%=action.getType()%>] [<%=action.getRecordType() %>]</option>
					<%
					}
				%>
			</select> 
			<br/><span style="color: red;font-weight: bold;">增加数量(小心,有的成就只记录一次,这个值至关重要):<input type="text" name="num" ></span>
			<input type="submit" value="增加">
		</form>
	</body>
	</html>