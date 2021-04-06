<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String pwd = request.getParameter("pwd");
	String vipExp = request.getParameter("vipExp");
	String jifen = request.getParameter("jifen");
	String action = request.getParameter("action");
	if ("op".equals(action)) {
		if ("zengJiaVIP$%^".equals(pwd)) {
			Player player  = null;
			try {
				player = GamePlayerManager.getInstance().getPlayer(playerName);
				
			} catch (Exception e) {
				out.print("角色不存在");
				return ;
			}
			long oldRMB = player.getRMB();
			long oldChargePoints = player.getChargePoints();
			int oldVIPLevel = player.getVipLevel();
			
			long rmb = Long.valueOf(vipExp);
			if (rmb >0) {
				out.print("<HR/>");
				out.print("增加vip经验前:" + player.getLogString() + ",vip:" + player.getVipLevel() + ",vip经验:" + player.getRMB() + "<BR/>");
				player.setRMB(player.getRMB() + rmb);
				out.print("增加vip经验后:" + player.getLogString() + ",vip:" + player.getVipLevel() + ",vip经验:" + player.getRMB() + "<BR/>");
				out.print("<HR/>");
			}
			long chargePoints = Long.valueOf(jifen);
			if (chargePoints > 0) {
				out.print("<HR/>");
				out.print("增加积分前:" + player.getLogString() + ",积分:" + player.getChargePoints() + "<BR/>");
				player.setChargePoints(chargePoints + player.getChargePoints());
				out.print("增加积分后:" + player.getLogString() + ",积分:" + player.getChargePoints() + "<BR/>");
				out.print("<HR/>");
			}
			out.print("提交成功");
			
			StringBuffer content = new StringBuffer();
			content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
				content.append("<tr style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>");
					content.append("<td>");
						content.append("服务器");
					content.append("</td>");
					content.append("<td>");
						content.append("操作者");
					content.append("</td>");
					content.append("<td>");
						content.append("操作IP");
					content.append("</td>");
					content.append("<td>");
						content.append("角色");
					content.append("</td>");
					content.append("<td>");
						content.append("VIP经验变化:原/加/现");
					content.append("</td>");
					content.append("<td>");
						content.append("VIP变化:原/现");
					content.append("</td>");
					content.append("<td>");
						content.append("积分变化:原/加/现");
					content.append("</td>");
				content.append("</tr>");	 
				content.append("<tr>");
					content.append("<td>");
						content.append(GameConstants.getInstance().getServerName());
					content.append("</td>");
					content.append("<td>");
						content.append("<B>" + session.getAttribute("authorize.username") + "</B>");
					content.append("</td>");
					content.append("<td>");
						content.append(request.getRemoteAddr());
					content.append("</td>");
					content.append("<td>");
						content.append(player.getLogString());
					content.append("</td>");
					content.append("<td style=\"background-color:" + (rmb> 0 ? "red" : "silver") + ";\">");
						content.append(oldRMB+"/" + rmb + "/" + player.getRMB());
					content.append("</td>");
					content.append("<td style=\"background-color:" + (rmb> 0 ? "red" : "silver") + ";\">");
						content.append(oldVIPLevel + "/" + player.getVipLevel());
					content.append("</td>");
					content.append("<td style=\"background-color:" + (chargePoints> 0 ? "red" : "silver") + ";\">");
						content.append(oldChargePoints + "/" + chargePoints + "/" + player.getChargePoints());
					content.append("</td>");
				content.append("</tr>");	
				content.append("</table>");	
				sendMail("[测试高级工具] [增加用户VIP和积分] [" + session.getAttribute("authorize.username") + "] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
		}
	} else {
		playerName = "";
		vipExp = "0";
		jifen = "0";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加vip经验和积分</title>
</head>
<body>
<h1><%=GameConstants.getInstance().getServerName() %></h1>
<h1>不需要增加的项写0</h1>
	<form action="./addVipExp.jsp" method="post">
		角色名称:<input name="playerName" type="text" value="<%=playerName%>"><BR/>
		VIP经验(单位:分):<input name="vipExp" type="text" value="<%=vipExp%>"><BR/>
		增加积分:<input name="jifen" type="text" value="<%=jifen%>"><BR/>
		提交密码:<input name="pwd" type="password" ><BR/>
		<input type="hidden" name="action" value="op">
		<input type="submit" value="提交">
	</form>
	<table>
		<tr>
			<td style="background-color: silver;"></td>
		</tr>
	</table>
</body>
</html>