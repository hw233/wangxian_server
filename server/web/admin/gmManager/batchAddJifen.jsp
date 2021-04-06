<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerIds = request.getParameter("playerIds");
	String jifens = request.getParameter("jifens");
	String pwd = request.getParameter("pwd");
	String action = request.getParameter("action");
	if ("op".equals(action)) {
		if("woyaojiajifen%^&".equals(pwd)) {
			Long [] playerIdArr = StringTool.string2Array(playerIds, "\r\n", Long.class);
			Long [] jifenArr = StringTool.string2Array(jifens, "\r\n", Long.class);
			if (playerIdArr.length == jifenArr.length) {
				for (int i = 0; i < playerIdArr.length; i++) {
					Long playerId = playerIdArr[i];
					Long jifen = jifenArr[i];
					Player player = null;
					try {
						player = GamePlayerManager.getInstance().getPlayer(playerId);
					} catch (Exception e) {
					}
					if (player == null) {
						out.print("角色不存在.请核实.所有玩家都为发放积分,角色ID:" + playerId + ",对应积分:" + jifen);
						break;
					}
				}
				for (int i = 0; i < playerIdArr.length; i++) {
					Long playerId = playerIdArr[i];
					Long jifen = jifenArr[i];
					Player player = null;
					try {
						player = GamePlayerManager.getInstance().getPlayer(playerId);
						long oldJifen = player.getChargePoints();
						player.setChargePoints(player.getChargePoints() + jifen);
						String notice = "[" + i + "]" + "[角色积分修改] " + player.getLogString() + " [积分变化:" + oldJifen + " > " + player.getChargePoints() + "] [增加值:" + jifen + "]";
						out.print(notice + "<BR/>");
						ChargeManager.logger.error(notice);
					} catch (Exception e) {
					}
				}
			} else {
				out.print("<H1>角色ID与积分数量不符</H1>");
			}
		}
	} else {
		playerIds = "请输入角色ID,一行一个";
		jifens = "请输入要增加的积分,一行一个";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量加积分</title>
</head>
<body>
	<h1><%=GameConstants.getInstance().getServerName() %></h1>
	<form action="./batchAddJifen.jsp" method="post">
		<table border="1" style="font-size: 12px;text-align: center;">
			<tr>
				<td>
					角色列表(ID)
				</td>
				<td>
					积分列表
				</td>
			</tr>
			<tr>
				<td>
					<textarea rows="30" cols="50" name="playerIds"></textarea>
				</td>
				<td>
					<textarea rows="30" cols="50" name="jifens"></textarea>
				</td>
			</tr>
			<tr>
				<td>密码:<input type="password" name="pwd"></td>
				<td><input type="hidden" name="action" value="op"> <input type="submit" value="提交"> </td>
			</tr>
		</table>
	</form>
</body>
</html>