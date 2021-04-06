<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.trade.requestbuy.RequestBuy"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.trade.requestbuy.RequestBuyRule"%>
<%@page import="java.util.HashMap"%>
<%@page
	import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	RequestBuyManager requestM = RequestBuyManager.getInstance();
	Hashtable<RequestBuyRule, List<RequestBuy>> underRuleMap = requestM
			.getUnderRuleMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<tr>
			<td><%="服务启动时库里剩余"%></td>
			<td><%="现在剩余"%></td>
			<td><%="发布"%></td>
			<td><%="出售"%></td>
			<td><%="取消"%></td>
			<td><%="到期"%></td>
			<td><%="税"%></td>
		</tr>
		<tr>
			<td><%=BillingCenter.得到带单位的银两(requestM.startMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.allMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.requestMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.saleMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.cancleMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.timeOverMoney) %></td>
			<td><%=BillingCenter.得到带单位的银两(requestM.taxMoney) %></td>
		</tr>
	</table>

	<table border="1">

		<tr>
			<td>rb.getId()</td>
			<td>rb.getReleaserId()</td>
			<td>rb.getReleaserName()</td>
			<td>rb.getSize()</td>
			<td>rb.getItemName</td>
			<td>rb.getItemPrice</td>
			<td>rb.getStartNum</td>
			<td>rb.getItemNum</td>
			<td>rb.getRule().getArticleName()</td>
			<td>rb.getRule().getColor()</td>
			<td>rb.getRule().getOutshowName()</td>
			<td>rb.getRule().getMainType()</td>
			<td>rb.getRule().getSubType()</td>
		</tr>

		<%
			for (Iterator<RequestBuyRule> iterator = underRuleMap.keySet()
					.iterator(); iterator.hasNext();) {

				RequestBuyRule rr = iterator.next();
				for (RequestBuy rb : underRuleMap.get(rr)) {
		%>
		<tr>
			<td><%=rb.getId()%></td>
			<td><%=rb.getReleaserId()%></td>
			<td><%=rb.getReleaserName()%></td>
			<td><%=rb.getSize()%></td>
			<td><%=rb.getItem().getEntityName()%></td>
			<td><%=rb.getItem().getPerPrice()%></td>
			<td><%=rb.getItem().getStartNum()%></td>
			<td><%=rb.getItem().getEntityNum()%></td>
			<td><%=rb.getRule().getArticleName()%></td>
			<td><%=rb.getRule().getColor()%></td>
			<td><%=rb.getRule().getOutshowName()%></td>
			<td><%=rb.getRule().getMainType()%></td>
			<td><%=rb.getRule().getSubType()%></td>
		</tr>
		<%
			}
			}
		%>
	</table>
</body>
</html>