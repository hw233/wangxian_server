<%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.fy.boss.authorize.model.*,
				com.fy.boss.authorize.service.*,
				com.fy.boss.authorize.exception.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.finance.service.*,
				com.fy.boss.game.model.*,
				com.fy.boss.deploy.*,
				com.fy.boss.cmd.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.finance.service.*,
				com.fy.boss.game.service.* "%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
ServerManager smanager = ServerManager.getInstance();
OrderFormManager omanager = OrderFormManager.getInstance();
PassportManager pmanager = PassportManager.getInstance();
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
	<%@ include file="../../korea/translateResouce.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单查询</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
<script language="JavaScript">		
<!--
-->
function overTag(tag){
	tag.style.color = "red";	
		tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
	tag.style.backgroundColor = "white";
}
function koreatranslate(index){
	if(index=="nocommit"){
		window.location.replace("orderlist.jsp?istran=korea");
	}else if(index=="korea"){
		window.location.replace("orderlist.jsp?istran=nocommit");
	}
}
</script>
<style>
</style>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			<%=充值订单查询 %>
		</h1>
		<div id='krose'><input type='button' value='언어전환' onclick='koreatranslate("<%=istran%>")'></input></div>
	<br>
	<%
		String username = ParamUtils.getParameter(request,"username", null);
    	String orderid = ParamUtils.getParameter(request,"orderid", null);    
	%>
		<form action="orderlist.jsp" method=post name=f1>
		<%=按订单号查询 %>:<input type=text name="orderid" value="<%=orderid==null?"":orderid %>" size="25"/> <%=按玩家帐号查询 %>:<input type=text name="username" value="<%=username==null?"":username %>" size="25"/>
		<input type=submit name=sub1 value="<%=查询%>">
		</form>
		<table align="center" width="99%" cellpadding="0"
                                cellspacing="0" border="0"
                                class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
                 <tr >
                           <th align=center width=20%>
                                   <b><%=订单号 %></b>
                           </th>
                           <th align=center width=15%>
                                   <b><%=用户 %></b>
                           </th>
                           <th align=center width=10%>
                                   <b><%=服务器 %></b>
                           </th>
                           <th align=center width=10%>
                                   <b><%=额度 %></b>
                           </th>
                           <th align=center width=10%>
                                   <b><%=充值渠道 %></b>
                           </th>
                           <th align=center width=10%>
                                   <b><%=充值方式 %></b>
                           </th>
                           <th align=center width=10%>
                                   <b><%=充值卡号 %></b>
                           </th>
                           <th align=center width=15%>
                                   <b><%=当前状态 %></b>
                           </th>
                           <th align=center width=15%>
                                   <b><%=订单时间 %></b>
                           </th>
                   </tr>
                                
                 <%
                 long orderCount = 0;
                 int pageIndex = ParamUtils.getIntParameter(request,"pageIndex", 0);
                 long pagenum = 0;
                 List<OrderForm> list = null;
                 
                 if(username != null) {
                	 Passport p = pmanager.getPassport(username);
                	 if(p != null) {
                	 	list = omanager.getUserOrderForms(p.getId(), 0, 5000);
                	 }
                 } else if(orderid != null) {
                 	OrderForm order = omanager.getOrderForm(orderid);
                 	if(order != null) {
                     	list = new ArrayList<OrderForm>();
                 		list.add(order);
                 	}
                 } else {
	                 /*orderCount = omanager.getCount();
	                 int pernum = 50;
	                 pagenum = orderCount/pernum;
	                 if(orderCount % pernum != 0) {
	                 	pagenum++;
	                 }
	                 list = omanager.getOrderForms(pageIndex*pernum, pernum);*/
	                 list = null;
                 }
                 if(list == null) {
                	 list = new ArrayList<OrderForm>();
                 }
                 for(OrderForm order : list) {
                	 Passport passport = pmanager.getPassport(order.getPassportId());
                	 String minfo = order.getMediumInfo();
				 %>
				<tr onmouseover = "overTag(this);" onmouseout = "outTag(this);">
					<td align="center">
						<%=order.getOrderId() %>
					</td>
					<td align="center">
						<%=passport.getUserName() %>
					</td>
					<td align="center">
						<%=order.getServerName() %>
					</td>
					<td align="center">
						<%=order.getPayMoney()/100 %>
					</td>
					<td align="center">
						<%=order.getUserChannel() %>
					</td>
					<td align="center">
						<%=order.getSavingMedium() %>
					</td>
					<td align="center">
						<%=order.getMediumInfo() %>
					</td>

					<td align="center">
						<%=order.getStatusDesp() %>
					</td>
					<td align="center">
						<%=DateUtil.formatDate(new Date(order.getCreateTime()),"yyyy-MM-dd HH:mm:ss") %>
					</td>
				</tr>
				<%} %>
				<tr >
                           <th align=right colspan="8">
                           	<%if(pageIndex > 0) {%>
                           	<input type=button name=bt3 value="上一页" onclick="location.replace('orderlist.jsp?pageIndex=<%=pageIndex-1 %>')">&nbsp;&nbsp;&nbsp;
                           	<%} 
                           	if(pageIndex < pagenum-1) {
                           	%>
                           	<input type=button name=bt4 value="下一页" onclick="location.replace('orderlist.jsp?pageIndex=<%=pageIndex+1 %>')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           	<%} %>
                           	&nbsp;&nbsp;&nbsp;&nbsp;<%=共 %>1<%=页 %>，<%=list.size() %><%=条 %>
                           </th>
                   </tr>
          </table>     
           <br><br><Br>
	</body>
</html>
 
