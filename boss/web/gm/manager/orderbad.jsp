<%@ page contentType="text/html;charset=utf-8"%><%@page import="java.util.*,java.io.*,
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
				com.fy.boss.finance.service.*,com.fy.boss.game.service.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%
ServerManager smanager = ServerManager.getInstance();
OrderFormManager omanager = OrderFormManager.getInstance();
PassportManager pmanager = PassportManager.getInstance();
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单查询</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
<script language="JavaScript">		
<!--
-->
</script>
<style>
</style>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			当日失败的订单
		</h1>
	<br>
		<form action="orderbad.jsp" method=post name=f1>
			按订单号查询:<input type=text name="orderid" size="25"/>
			<input type=submit name=sub1 value="查询">
		</form>
		<table align="center" width="99%" cellpadding="0"
                                cellspacing="0" border="0"
                                class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
                 <tr >
                           <th align=center width=20%>
                                   <b>订单号</b>
                           </th>
                           <th align=center width=15%>
                                   <b>用户</b>
                           </th>
                           <th align=center width=10%>
                                   <b>服务器</b>
                           </th>
                           <th align=center width=10%>
                                   <b>额度(元)</b>
                           </th>
                           <th align=center width=10%>
                                   <b>充值渠道</b>
                           </th>
                           <th align=center width=10%>
                                   <b>充值方式</b>
                           </th>
                           <th align=center width=15%>
                                   <b>当前状态</b>
                           </th>
                           <th align=center width=15%>
                                   <b>订单时间</b>
                           </th>
                           <th align=center width=15%>
                                   <b>操作</b>
                           </th>
                   </tr>
                                
                 <%
                 long orderCount = 0;
                 int pernum = 50;
                 long pagenum = 0;
                 int pageIndex = 0;
                 List<OrderForm> list = null;
                 long orderid = ParamUtils.getLongParameter(request,"orderid", -1);
                 if(orderid != -1 && ParamUtils.getParameter(request,"act","").equals("resync")) {
                	 //同步补单
                	 OrderForm order = omanager.getOrderForm(orderid);
                	 if(order != null) {
                		 if(order.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC && order.getResponseResult() == OrderForm.RESPONSE_SUCC && 
 								order.isNotified() && !order.isNotifySucc() && order.getMemo3()!=null && order.getMemo3().equals("同步失败")) {
                		 	order.setNotified(false);
                		 	order.setMemo3("");
                		 	omanager.update(order);
                		 }
                	 }
                 } else if(orderid != -1 && ParamUtils.getParameter(request,"act","").equals("reback")) { 
                	 //回调补单
                	 OrderForm order = omanager.getOrderForm(orderid);
                	 if(order != null) {
                		 if(order.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC && order.getResponseResult() == OrderForm.RESPONSE_FAILED && 
 								!order.isNotified() && !order.isNotifySucc()) {
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
                		 	omanager.update(order);
                		 }
                	 }
                 } else if(orderid != -1) {
                 	OrderForm order = omanager.getOrderForm(orderid);
                 	if(order != null) {
                     	list = new ArrayList<OrderForm>();
                 		list.add(order);
                 	}
                 } 
                 if(list == null) {
                	 list = new ArrayList<OrderForm>();
                	 /*
	                 orderCount = omanager.getFailedOrderFormsCount();
	                 pagenum = orderCount/pernum;
	                 if(orderCount % pernum != 0) {
	                 	pagenum++;
	                 }
	                 pageIndex = ParamUtils.getIntParameter(request,"pageIndex", 0);
	                 list = omanager.getFailedOrderForms(pageIndex*pernum, pernum);
	                 */
                 }
                 for(OrderForm order : list) {
                	 Passport passport = pmanager.getPassport(order.getPassportId());
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
						<%=order.getStatusDesp() %>
					</td>
					<td align="center">
						<%=DateUtil.formatDate(new Date(order.getCreateTime()),"yyyy-MM-dd HH:mm:ss") %>
					</td>
					<td align="center">
						<%if(order.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC && order.getResponseResult() == OrderForm.RESPONSE_SUCC && 
								order.isNotified() && !order.isNotifySucc() && order.getMemo3()!=null && order.getMemo3().equals("同步失败")) {%>
						<input type="button" name=bt1 value="同步补单" onclick="location.replace('orderbad.jsp?orderid=<%=order.getId() %>&act=resync')">
						<%} else if(order.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC && order.getResponseResult() == OrderForm.RESPONSE_FAILED && !order.isNotified() && !order.isNotifySucc()) {%>
						<input type="button" name=bt1 value="回调补单" onclick="location.replace('orderbad.jsp?orderid=<%=order.getId() %>&act=reback')">
						<%} %>
					</td>
				</tr>
				<%} %>
				<tr >
                           <th align=right colspan="9">
                           	<%if(pageIndex > 0) {%>
                           	<input type=button name=bt3 value="上一页" onclick="location.replace('orderlist.jsp?pageIndex=<%=pageIndex-1 %>')">&nbsp;&nbsp;&nbsp;
                           	<%} 
                           	if(pageIndex < pagenum-1) {
                           	%>
                           	<input type=button name=bt4 value="下一页" onclick="location.replace('orderlist.jsp?pageIndex=<%=pageIndex+1 %>')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           	<%} %>
                           	&nbsp;&nbsp;&nbsp;&nbsp;共<%=pagenum %>页，<%=orderCount %>条
                           </th>
                   </tr>
          </table>     
          <br><br><Br>
	</body>
</html>
 
