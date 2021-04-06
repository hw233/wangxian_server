<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.economic.*,com.xuanzhi.boss.authorize.exception.*,com.fy.engineserver.mail.service.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消费测试</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<body>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){
String message = null;
int playerId = -1;
Object obj = session.getAttribute("playerid");
if(obj != null){
	playerId = Integer.parseInt(obj.toString());
}
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
if (playerId != -1) {
	player = pm.getPlayer(playerId);
}
if(player != null) {
	String amountStr = request.getParameter("amount");
	if(amountStr != null) {
		long expenseAmount = Long.parseLong(request.getParameter("amount"));
		BillingCenter center = BillingCenter.getInstance();
		int currencyType = Integer.parseInt(request.getParameter("ctype"));
		try {
			center.playerExpense(player, expenseAmount, currencyType, ExpenseReasonType.SHOP_BUY, new Object[0]);
			message = "成功消费";
		} catch(NoEnoughMoneyException e) {
			e.printStackTrace();
			message = "钱不够花";
		} catch(BillFailedException e) {
			e.printStackTrace();
			message = "计费失败";
		}
	}
}
if(message != null) {
%>
<h2><%=message %></h2>
<%} %>

<div style="padding:40px 0 40px 30px;">
<h3><%=(player==null?"未知玩家":player.getName()) %></h3>
<form action="" name=f1>
	消费类型:
	<select name="ctype">
		<option value="<%=CurrencyType.RMB_YUANBAO %>"><%=CurrencyType.getCurrencyDesp(CurrencyType.RMB_YUANBAO) %></option>
		<option value="<%=CurrencyType.BIND_YUANBAO %>"><%=CurrencyType.getCurrencyDesp(CurrencyType.BIND_YUANBAO) %></option>
		<option value="<%=CurrencyType.GAME_MONEY %>"><%=CurrencyType.getCurrencyDesp(CurrencyType.GAME_MONEY) %></option>
	</select><br>
	消费数量:<input type=text name=amount size=10><br>
	<input type=submit name=sub1 value="确定">
</form>
</div>
<%} %>
</body>
</html>
