<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.exchange.*,
com.fy.engineserver.exchange.concrete.* "%><%
	
	GamePlayerManager pm = (GamePlayerManager)GamePlayerManager.getInstance();
	DefaultExchangeService des = DefaultExchangeService.getInstance();
	ExchangeManager em = des.getExchangeManager();
	DealItem items[] = em.getDealItemsForAll();
%>
<%@include file="../IPManager.jsp" %><html><head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY>
<h2>元宝交易所当天成交记录</h2><br>


	<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FFFFFF" align="center"><td>编号</td><td>买家</td><td>卖家</td><td>成交价格</td><td>成交数量</td><td>时间</td></tr>
	<%
		for(int i = 0 ; i < items.length ; i++){
			DealItem o = items[i];
			String bname = ""+o.getBuyOrderId();
			try{
				Player b = pm.getPlayer(o.getBuyPlayId());
				bname = b.getName();
			}catch(Exception e){}
			
			String sname = ""+o.getSellPlayId();
			try{
				Player b = pm.getPlayer(o.getSellPlayId());
				sname = b.getName();
			}catch(Exception e){}
			
			out.println("<tr bgcolor='"+(i%2==0?"#F7AFF7":"#FFFFFF")+"' align='center'><td>"
					+ o.getId()+"</td><td>"+bname+"</td><td>"+sname+"</td><td>"
					+ o.getDealPrice()+"</td><td>"
					+ o.getDealAmount()+"</td><td>"
					+ StringUtil.formatDate(o.getDealTime(),"yyyy-MM-dd HH:mm:ss")+"</td></tr>");
		}
	%>
	</table>


</BODY></html>
