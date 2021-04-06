<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.exchange.*,
com.fy.engineserver.exchange.concrete.*,
com.xuanzhi.boss.game.* "%>
<%@include file="../authority.jsp" %>
<%
	

	Player player = (Player)session.getAttribute("exchange.player");
	if(player == null){
		response.sendRedirect("./login.jsp");
		return;
	}
	
	DefaultExchangeService des = DefaultExchangeService.getInstance();
	ExchangeManager em = des.getExchangeManager();
	Account account = des.getAccount(player);
	
	int orderType = 0;
	int price = 0;
	int amount = 0;
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String action = request.getParameter("action");
	if(action != null && action.equals("place_order")){
		orderType = Integer.parseInt(request.getParameter("orderType"));
		price = Integer.parseInt(request.getParameter("price"));
		amount = Integer.parseInt(request.getParameter("amount"));
		
		des.playOrder(player,orderType,price,amount);
	}else if(action != null && action.equals("cancel_order")){
		int orderId = Integer.parseInt(request.getParameter("order"));
		des.cancelOrder(player,orderId);
	}
	}
%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY>
<h2>元宝交易所用户信息界面</h2><br>

<table border="0" cellpadding="0" cellspacing="1" width='100%' bgcolor="#000000" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>用户名称</b></td><td><%= player.getUsername() %></td><td><b>角色名称</b></td><td><%= player.getName() %></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td><b>角色帐户元宝</b></td><td><%= player.getTotalRmbyuanbao() %></td><td><b>角色帐户金币</b></td><td><%= player.getMoney() %></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td><b>交易所帐户元宝</b></td><td><%= account.getYuanbo() %></td><td><b>交易所帐户金币</b></td><td><%= account.getMoney() %></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td><b>交易所冻结元宝</b></td><td><%= account.getBlockedyuanbo() %></td><td><b>交易所冻结金币</b></td><td><%= account.getBlockedMoney() %></td></tr>
</table>
<%//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>

<%} %>
<h3>我的订单</h3>
<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FF88F8" align="center"><td>编号</td><td>订单类型</td><td>玩家</td><td>单价</td><td>订单数量</td><td>成交数量</td><td>状态</td><td>创建时间</td><td>最后修改时间</td><td>操作</td></tr>
	<%
		Order sos[] = des.getOrderByPlayer(player);
		for(int i = 0 ; i < sos.length ; i++){
			Order o = sos[i];
			out.println("<tr bgcolor='"+(i%2==0?"#F7AFF7":"#FFFFFF")+"' align='center'><td>"
					+ o.getId()+"</td><td>"+(o.getOrderType()== Order.ORDER_TYPE_BUY?"买单":"卖单")+"</td><td>"+o.getPlayerName()+"</td><td>"
					+ o.getPrice()+"</td><td>"
					+ o.getAmount()+"</td><td>"
					+ o.getDealAmount() +"</td><td>"
					+ Order.STATE_NAMES[o.getOrderState()]+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td><td>"
					+ StringUtil.formatDate(o.getLastModifiedTime(),"yyyy-MM-dd HH:mm")+"</td>");
			
			if(o.getOrderState() == Order.ORDER_STATE_ACTIVE
					|| o.getOrderState() == Order.ORDER_STATE_PART_DEAL){
				out.println("<td><a href='./index.jsp?order="+o.getId()+"&action=cancel_order'>取消订单</a></td>");
			}else{
				out.println("<td>--</td>");
			}
			
			out.println("</tr>");
		}
	%>
	</table>
<h3>成交的订单</h3>
<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FF88F8" align="center"><td>编号</td><td>订单类型</td><td>玩家</td><td>单价</td><td>订单数量</td><td>成交数量</td><td>状态</td><td>创建时间</td><td>最后修改时间</td><td>操作</td></tr>
	<%
		sos = des.getDealedOrderByPlayer(player);
		for(int i = 0 ; i < sos.length ; i++){
			Order o = sos[i];
			out.println("<tr bgcolor='"+(i%2==0?"#F7AFF7":"#FFFFFF")+"' align='center'><td>"
					+ o.getId()+"</td><td>"+(o.getOrderType()== Order.ORDER_TYPE_BUY?"买单":"卖单")+"</td><td>"+o.getPlayerName()+"</td><td>"
					+ o.getPrice()+"</td><td>"
					+ o.getAmount()+"</td><td>"
					+ o.getDealAmount() +"</td><td>"
					+ Order.STATE_NAMES[o.getOrderState()]+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td><td>"
					+ StringUtil.formatDate(o.getLastModifiedTime(),"yyyy-MM-dd HH:mm")+"</td>");
			
			if(o.getOrderState() == Order.ORDER_STATE_ACTIVE
					|| o.getOrderState() == Order.ORDER_STATE_PART_DEAL){
				out.println("<td><a href='./index.jsp?order="+o.getId()+"&action=cancel_order'>取消订单</a></td>");
			}else{
				out.println("<td>--</td>");
			}
			
			out.println("</tr>");
		}
	%>
	</table>
	
</BODY></html>
