<%@page import="com.fy.engineserver.core.ExperienceManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String pwd = request.getParameter("pwd");
	String playerNames = request.getParameter("playerNames");
	String levels = request.getParameter("levels");
	String exps = request.getParameter("exps");
	String silvers = request.getParameter("silvers");
	if ("hefuAjiashuju".equals(pwd)) {
		String[] playerNameArr = playerNames.split("\r\n");
		String[] levelArr = levels.split("\r\n");
		String[] expsArr = exps.split("\r\n");
		String[] silverArr = silvers.split("\r\n");
		if (playerNameArr.length == levelArr.length && levelArr.length == expsArr.length && levelArr.length == silverArr.length) {
			for (int i = 0; i < playerNameArr.length; i++) {
				String playerName = playerNameArr[i];
				try {
					Player player = GamePlayerManager.getInstance().getPlayer(playerName);
					if (Integer.valueOf(levelArr[i]) > 0) {
						player.setSoulLevel(Integer.valueOf(levelArr[i]));
					}
					if (Long.valueOf(expsArr[i]) > 0) {
						
						player.addExp(Long.valueOf(expsArr[i]),ExperienceManager.活动);
					}
					player.setClassLevel((short) (player.getLevel() / 20));
					if (Long.valueOf(silverArr[i]) != 0) {
						BillingCenter.getInstance().playerSaving(player, Long.valueOf(silverArr[i]), CurrencyType.YINZI, SavingReasonType.后台设置属性, "合服测试添加");
					}
					out.print("角色属性设置完毕:[" + playerName + "] [级别:" + player.getSoulLevel() + "] [expp:" + player.getExp() + "] [silver:" + player.getSilver() + "]");
					out.print("<BR/>");
				} catch (Exception e) {
					out.print("<font color=red>角色不存在,其他继续发放:[" + playerName + "]</font></BR>");
				}
			}
		} else {
			out.print("<H1>长度不符</H1>");
		}
	} else {
		playerNames = "";
		levels = "";
		exps = "";
		silvers = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合服数据准备</title>
</head>
<body style="font-size: 12px;">
	<form action="./UnitedServerDataPrepare.jsp" method="post">
		<table border="1">
			<tr>
				<td colspan="4" style="text-align: center;font-weight: bold;"><%=GameConstants.getInstance().getServerName() %></td>
			</tr>
			<tr>
				<td>角色列表</td>
				<td>等级(境界自动加)</td>
				<td>增加经验</td>
				<td>银子增加</td>
			</tr>
			<tr>
				<td><textarea name="playerNames" rows="10"></textarea></td>
				<td><textarea name="levels" rows="10"></textarea></td>
				<td><textarea name="exps" rows="10"></textarea></td>
				<td><textarea name="silvers" rows="10"></textarea></td>
			</tr>
		</table>
		密码:<input name="pwd" type="password"><input type="submit"
			name="提交"><BR />
	</form>
</body>
</html>