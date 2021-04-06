<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.exchange.*,
com.fy.engineserver.exchange.concrete.* "%><%
	DefaultExchangeService des = DefaultExchangeService.getInstance();
	ExchangeManager em = des.getExchangeManager();
	long buying[] = des.getBuyingMarket();
	long selling[] = des.getSellingMarket();
%>
<%@include file="../IPManager.jsp" %><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Refresh" content="10">
</HEAD>
<BODY>
<h2>元宝交易所实时行情</h2><br>

<table border="0" cellpadding="0" cellspacing="2"  width='50%' bgcolor="#000000" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>卖单</b></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td>
	<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FFFFFF" align="center"><td>单价</td><td>数量</td></tr>
	<%
		for(int i = selling.length/2 -1 ; i >= 0 ; i--){
			out.println("<tr bgcolor='"+(i%2==0?"#A7AFF7":"#FFFFFF")+"' align='center'><td>"+ selling[2*i]+"</td><td>"+selling[2*i+1]+"</td></tr>");
		}
	%>
	</table>
</td></tr>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="2" width='50%' bgcolor="#000000" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>买单</b></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td>
	<table border="0" cellpadding="0" cellspacing="0" width='100%' bgcolor="#FFFFFF" align="center">
	<tr bgcolor="#FFFFFF" align="center"><td>单价</td><td>数量</td></tr>
	<%
		for(int i = 0 ; i < buying.length/2 ; i++){
			out.println("<tr bgcolor='"+(i%2==0?"#A7AFF7":"#FFFFFF")+"' align='center'><td>"+ buying[2*i]+"</td><td>"+buying[2*i+1]+"</td></tr>");
		}
	%>	
	</table>
</td></tr>
</table>

</BODY></html>
