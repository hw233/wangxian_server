<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="com.fy.engineserver.economic.ExpenseReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!String pwd = "xiaoxincaozuo@#$"; 
	String action_remove = "remove";
%>
<%
	AuthorizeManager am = AuthorizeManager.getInstance();
	User user = am.getUserManager().getUser(String.valueOf(request.getSession().getAttribute("authorize.username")));
	
	out.print("hello:" + user.getRealName() + "<BR/>");
	String action = request.getParameter("action");
	if (action_remove.equals(action)) {//移除银子
		String playerIdS = request.getParameter("playerId");
		String silverS = request.getParameter("silver");
		Long playerId = Long.valueOf(playerIdS);
		Long silver = Long.valueOf(silverS);
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		if (player != null) {
			long playerHasSilver = player.getSilver();
			if (playerHasSilver > silver) {
				BillingCenter.getInstance().playerExpense(player,silver,CurrencyType.YINZI,ExpenseReasonType.活动,"扣除玩家银子-撤单" + user.getRealName());
				String notice = user.getRealName() + "扣除玩家银子成功:" + player.getLogString()+ "  [银子:" + silver + "/" + BillingCenter.得到带单位的银两(silver) + "] [剩余银两:" + player.getSilver() + "/" + BillingCenter.得到带单位的银两(player.getSilver()) + "]";
				out.print("<H3><font color=red>" + notice + "</font></H3>");
				System.out.println(notice);
			} else {
				out.print("<H2>角色银子不足:"+player.getLogString()+"</H2>");
			}
		} else {
			out.print("<h2>输入的角色不存在:" + playerIdS + "</H2>");
		}
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>移除玩家银子:<%=GameConstants.getInstance().getServerName()%></title>
</head>
<body>
	<form action="./removeSilver.jsp" method="post">
		角色ID：<input name="playerId" type="text"><BR/>
		要移除的银子:<input name="silver" type="text"><BR/>
		移除密码:<input name="pwd" type="password"><BR/>
		<input type="hidden" name="action" value="<%=action_remove%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>