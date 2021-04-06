
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
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
	System.out.println("playerIdS:" + playerIdS);
	System.out.println("achS:" + achS);
	if (playerIdS != null && !"".equals(playerIdS.trim()) && achS != null && !"".equals(achS.trim())) {
		long playerId = Long.valueOf(playerIdS);
		int achType = Integer.valueOf(achS);
		RecordAction ra = getRecordAction(achType);

		if (!"pwd".equals(request.getParameter("pwd"))) {
			out.print("<H1>没密码别得瑟</H1>");
		} else {
			if (ra == null) {
				out.print("<H1>成就不存在</H1>");
			} else {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				if (player == null) {
					out.print("<H1>角色不存在</H1>");
				} else {
					GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, ra);
					if (gdr == null) {
						out.print("<H1>角色还没有此类数据记录</H1>");
					} else {
						player.gdrMap.remove(ra.getType());
						AchievementManager.gameDREm.remove(gdr);
						out.print("<H1>移除记录成功:" + gdr.toString() + "</H1>");
					}
				}
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
<title>移除统计项</title>
</head>
<body>

	<form action="" method="post">
		角色ID:<input name="playerId" value="<%=playerIdS%>"> 数据类型:<select
			name="ach">
			<option value="-1">--</option>
			<%
				for (RecordAction action : RecordAction.values()) {
			%>
			<option value="<%=action.getType()%>"><%=action.getName()%>[<%=action.getType()%>]
				[<%=action.getRecordType()%>]
			</option>
			<%
				}
			%>
		</select> 输入移除密码:<input name="pwd" type="password"> <input
			type="submit" value="移除">
	</form>
</body>
</html>