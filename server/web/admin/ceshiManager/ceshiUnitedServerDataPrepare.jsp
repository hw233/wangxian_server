<%@page import="com.fy.engineserver.core.ExperienceManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ include file="./inc.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String pwd = request.getParameter("pwd");
	String playerNames = request.getParameter("playerNames");
	String levels = request.getParameter("levels");
	String exps = request.getParameter("exps");
	String silvers = request.getParameter("silvers");
	String bindsilvers = request.getParameter("bindsilvers");
	
	if ("hefuAjiashuju".equals(pwd)) {
		StringBuffer content = new StringBuffer();
		{
			//发送者信息:
			content.append("<table style='font-size: 12px;text-align: center;' border='1'>");

			content.append("<tr>");
			content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='2'>发送者信息</td>");
			content.append("</tr>");

			content.append("<tr>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>发送者</td>");
			content.append("<td>").append(request.getSession().getAttribute("authorize.username")).append("</td>");
			content.append("</tr>");

			content.append("<tr>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>IP</td>");
			content.append("<td>").append(request.getRemoteAddr()).append("</td>");
			content.append("</tr>");

			content.append("<tr>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>服务器</td>");
			content.append("<td>").append(GameConstants.getInstance().getServerName()).append("</td>");
			content.append("</tr>");

			content.append("</table>");
			content.append("<HR>");
		}
		String[] playerNameArr = playerNames.split("\r\n");
		String[] levelArr = levels.split("\r\n");
		String[] expsArr = exps.split("\r\n");
		String[] silverArr = silvers.split("\r\n");
		String[] bindsilversArr = bindsilvers.split("\r\n");
		if (playerNameArr.length == levelArr.length && levelArr.length == expsArr.length && levelArr.length == silverArr.length) {
			
			content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
			content.append("<tr>");
			content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='7'>接受者信息</td>");
			content.append("</td>");
			content.append("</tr>");
			
			content.append("<tr>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>用户名</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色ID</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色名</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>等级变化</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>经验变化</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>银子变化</td>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>绑银变化</td>");
			content.append("</tr>");
			for (int i = 0; i < playerNameArr.length; i++) {
				String playerName = playerNameArr[i];
				try {
					Player player = GamePlayerManager.getInstance().getPlayer(playerName);
					String levelChange = null;
					if (Integer.valueOf(levelArr[i]) > 0) {
					levelChange = "[" + player.getSoulLevel() + " > "; 
						player.setSoulLevel(Integer.valueOf(levelArr[i]));
						player.initNextLevelExpPool();
						levelChange += player.getSoulLevel() + "]";
					}
					String expChange = null;
					if (Long.valueOf(expsArr[i]) > 0) {
						expChange = "[" + player.getExp() + " > "; 					
						player.addExp(Long.valueOf(expsArr[i]),ExperienceManager.活动);
						expChange += player.getExp() + "]";
					}
					player.setClassLevel((short) (player.getLevel() / 20));
					
					String silverChange = null;
					if (Long.valueOf(silverArr[i]) != 0) {
					silverChange = "[" + BillingCenter.得到带单位的银两(player.getSilver()) + " > "; 
						BillingCenter.getInstance().playerSaving(player, Long.valueOf(silverArr[i]), CurrencyType.YINZI, SavingReasonType.后台设置属性, "合服测试添加");
						silverChange += BillingCenter.得到带单位的银两(player.getSilver()) + "]";
					}
					
					String bindSilverChange =  null;
					if (Long.valueOf(bindsilversArr[i]) != 0) {
						bindSilverChange  = "[" + BillingCenter.得到带单位的银两(player.getBindSilver()) + " > "; 
						BillingCenter.getInstance().playerSaving(player, Long.valueOf(bindsilversArr[i]), CurrencyType.BIND_YINZI, SavingReasonType.后台设置属性, "合服测试添加");
						bindSilverChange += BillingCenter.得到带单位的银两(player.getBindSilver()) + "]";
					}
					// 加入到邮件内容
					out.print("角色属性设置完毕:[" + playerName + "] [级别:" + player.getSoulLevel() + "] [expp:" + player.getExp() + "] [silver:" + player.getSilver() + "]");
					out.print("<BR/>");
					content.append("<tr>");
					content.append("<td>" + player.getUsername() + "</td>");
					content.append("<td>" + player.getId() + "</td>");
					content.append("<td>" + player.getName() + "</td>");
					content.append("<td "+(levelChange == null ? "" : "style='background-color: black;color: red;font-size: 14px;font-weight: bold;'") + ">" + levelChange + "</td>");
					content.append("<td "+(expChange == null ? "" : "style='background-color: black;color: red;font-size: 14px;font-weight: bold;'") + ">" + expChange + "</td>");
					content.append("<td "+(silverChange == null ? "" : "style='background-color: black;color: red;font-size: 14px;font-weight: bold;'") + ">" + silverChange + "</td>");
					content.append("<td "+(bindSilverChange == null ? "" : "style='background-color: black;color: red;font-size: 14px;font-weight: bold;'") + ">" + bindSilverChange + "</td>");
					content.append("</tr>");
				} catch (Exception e) {
					out.print("<font color=red>角色不存在,其他继续发放:[" + playerName + "]</font></BR>");
				}
			}
			content.append("</table>");
			sendMail("[高级测试功能] [设置等级-经验-银子] [" + session.getAttribute("authorize.username") + "] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
		} else {
			out.print("<H1>长度不符</H1>");
		}
	} else {
		playerNames = "";
		levels = "";
		exps = "";
		silvers = "";
		bindsilvers = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合服数据准备</title>
</head>
<body style="font-size: 12px;">
	<form action="./ceshiUnitedServerDataPrepare.jsp" method="post">
	<h1><%=GameConstants.getInstance().getServerName() %></h1>
		<table border="1">
			<tr>
				<td colspan="5" style="text-align: center;font-weight: bold;"><%=GameConstants.getInstance().getServerName() %></td>
			</tr>
			<tr>
				<td>角色列表</td>
				<td>设置等级(境界自动加)</td>
				<td>增加经验</td>
				<td>银子增加</td>
				<td>绑银增加</td>
			</tr>
			<tr>
				<td><textarea name="playerNames" rows="10"></textarea></td>
				<td><textarea name="levels" rows="10"></textarea></td>
				<td><textarea name="exps" rows="10"></textarea></td>
				<td><textarea name="silvers" rows="10"></textarea></td>
				<td><textarea name="bindsilvers" rows="10"></textarea></td>
			</tr>
		</table>
		密码:<input name="pwd" type="password"><input type="submit"
			name="提交"><BR />
	</form>
</body>
</html>