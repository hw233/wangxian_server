<%@page import="org.apache.commons.lang.StringUtils"%>
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
	long startTime = System.currentTimeMillis();
	ServerManager smanager = ServerManager.getInstance();
	OrderFormManager omanager = OrderFormManager.getInstance();
	PassportManager pmanager = PassportManager.getInstance();
	 OrderForm order =  null;
	 Passport  p = null;
%>

 <%
 	long orderCount = 0;
	 boolean isCommit = ParamUtils.getBooleanParameter(request, "isCommit");
	 if(isCommit)
	 {
		 String horderid = ParamUtils.getParameter(request, "horderid", "");
		 int handleresult = ParamUtils.getIntParameter(request, "handleresult", -2);
		 int responseresult = ParamUtils.getIntParameter(request, "responseresult", -2);
		 String memo3 = ParamUtils.getParameter(request, "memo3", "");
		 String isnotified = ParamUtils.getParameter(request, "isnotified","T");
		
		 
	 	 if(!StringUtils.isEmpty(horderid))
	 	 {
	 		order = omanager.getOrderForm(horderid);
	 		 if(order != null)
	 		 {
	 			 p = pmanager.getPassport(order.getPassportId());
	 		 
		 		 if(handleresult == 1)
		 		 {
		 		 	order.setHandleResult(handleresult);
		 		 }
		 		 
		 		 if(responseresult == 1)
		 		 {
		 		 	order.setResponseResult(responseresult);
		 		 }
		 		 
		 		 if(isnotified == "F")
		 		 {
		 		 	order.setNotified(false);
		 		 }
		 		 
		 		 order.setMemo3("通过后台jsp修改，商务已经确认此订单充值成功！");
		 		 omanager.update(order);
		 		 if(PlatformSavingCenter.logger.isInfoEnabled())
		 			 PlatformSavingCenter.logger.info("[通过jsp为订单补单] [成功] [OK] [订单 id:"+order.getId()+"] [订单号:"+order.getOrderId()+"] [handleresult:"+order.getHandleResult()+"] [responseresult:"+order.getResponseResult()+"] [isnotified:"+order.isNotified()+"] [notifiedsucc:"+order.isNotifySucc()+"] [渠道订单id:"+order.getChannelOrderId()+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
	 	 		orderCount = 1;
	 		 }
	 	 }
	 }
	 
	 
	 

	
%>



<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单状态修改</title>
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
		<% if(isCommit)
				{
		%>
			修改订单成功数为<%= %>个
			<%
				}
			%>
		</h1>
	<br>
		<form action="updateorderstatus.jsp" method=post name=f1>
		按订单号查询:<input type=text name="orderid" size="25"/> <!--  按玩家帐号查询:<input type=text name="username" size="25"/>-->
		<input type=submit name=sub1 value="查询">
		</form>
		<%
			if(order != null)
			{
			%>
			<form  action="updateorderstatus.jsp" method=post name=f2  onsubmit="return tips()">
			<input type="hidden"  name="isCommit" id="isCommit" value="true" />
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
	                                   <b>调用接口状态</b>
	                           </th>
								<th align=center width=15%>
	                                   <b>最终确认充值状态</b>
	                           </th>
								<th align=center width=15%>
	                                   <b>是否通知游戏服</b>
	                           </th>
								<th align=center width=15%>
	                                   <b>通知是否成功</b>
	                           </th>
	                          
	                           <th align=center width=15%>
	                                   <b>订单时间</b>
	                           </th>
	                   </tr>
	                                
	
					<tr onmouseover = "overTag(this);" onmouseout = "outTag(this);">
						<td align="center" >
							<%=order.getOrderId() %>
							<input type="hidden" name="horderid" value="<%=order.getOrderId() %>">
						</td>
						<td align="center">
							<%=p.getUserName() %>
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
							<input type="text"  name="handleresult"  id="handleresult" value="<%=order.getHandleResult()%>">
						</td>
						<td align="center">
							<input type="text"  name="responseresult"  id="responseresult"" value="<%=order.getResponseResult()%>">
						</td>
						<td align="center">
							<input type="text"  name="isnotified"  id="isnotified"  value="<%=order.isNotified()%>">
						</td>
						<td align="center">
							<input type="text"  name="isnotifiedsucc"  id="isnotifiedsucc"  value="<%=order.isNotifySucc()%>">
						</td>
						
						<td align="center">
							<%=DateUtil.formatDate(new Date(order.getCreateTime()),"yyyy-MM-dd HH:mm:ss") %>
						</td>
					</tr>
	          </table> 
	          <input type="submit" value="确认">
	          </form>  
	     <%}%>
           <br><br><Br>
	</body>
</html>


 
