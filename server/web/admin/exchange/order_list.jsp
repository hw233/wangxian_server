<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.exchange.*,
com.fy.engineserver.exchange.concrete.* "%><%
	DefaultExchangeService des = DefaultExchangeService.getInstance();
	ExchangeManager em = des.getExchangeManager();
	Order bos[] = em.getBuyingOrders();
	Order sos[] = em.getSellingOrders();
%>
<%@include file="../IPManager.jsp" %><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY>
<h2>元宝交易所实时行情</h2><br>

<table border="0" cellpadding="0" cellspacing="2" width='100%' bgcolor="#000000" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>卖单</b></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td>
	<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FFFFFF" align="center"><td>编号</td><td>玩家</td><td>单价</td><td>订单数量</td><td>成交数量</td><td>状态</td><td>创建时间</td><td>最后修改时间</td></tr>
	<%
		for(int i = 0 ; i < sos.length ; i++){
			
			Order o = sos[i];
			if(o.getOrderState() == Order.ORDER_STATE_CANCEL ||
					o.getOrderState() == Order.ORDER_STATE_TOTAL_DEAL) continue;
			out.println("<tr bgcolor='"+(i%2==0?"#F7AFF7":"#FFFFFF")+"' align='center'><td>"
					+ o.getId()+"</td><td>"+o.getPlayerName()+"</td><td>"
					+ o.getPrice()+"</td><td>"
					+ o.getAmount()+"</td><td>"
					+ o.getDealAmount() +"</td><td>"
					+ Order.STATE_NAMES[o.getOrderState()]+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td></tr>");
		}
	%>
	</table>
</td></tr>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="2" width='100%' bgcolor="#000000" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>买单</b></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td>
	<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FFFFFF" align="center"><td>编号</td><td>玩家</td><td>单价</td><td>订单数量</td><td>成交数量</td><td>状态</td><td>创建时间</td><td>最后修改时间</td></tr>
	<%
		for(int i = 0 ; i < bos.length ; i++){
			Order o = bos[i];
			if(o.getOrderState() == Order.ORDER_STATE_CANCEL ||
					o.getOrderState() == Order.ORDER_STATE_TOTAL_DEAL) continue;
			out.println("<tr bgcolor='"+(i%2==0?"#F7AFF7":"#FFFFFF")+"' align='center'><td>"
					+ o.getId()+"</td><td>"+o.getPlayerName()+"</td><td>"
					+ o.getPrice()+"</td><td>"
					+ o.getAmount()+"</td><td>"
					+ o.getDealAmount() +"</td><td>"
					+ Order.STATE_NAMES[o.getOrderState()]+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td><td>"
					+ StringUtil.formatDate(o.getCreateTime(),"yyyy-MM-dd")+"</td></tr>");
		}
	%>
	</table>
</td></tr>
</table>

</BODY></html>
