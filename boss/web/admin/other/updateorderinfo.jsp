<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.finance.service.PlatformSavingCenter"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.fy.boss.platform.uc.UCSDKSavingManager"%>
<%@page import="com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl"%>
<%@page import="com.fy.boss.finance.model.ExceptionOrderForm"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.finance.service.OrderFormManager"%>
<%@page import="com.fy.boss.finance.model.OrderForm"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//
	String orderInfos = request.getParameter("orderinfos");
	String orders[] = null;
	if(orderInfos != null)
	{
		orderInfos.trim();
		orders = orderInfos.split("\r*\n");
	}
	String mobileos = "unknown";
	String channel = ParamUtils.getParameter(request, "channel", "");
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(orders != null && orders.length > 0)
	{
		if(isDo)
		{
			List<OrderForm> succOrderList = new ArrayList<OrderForm>();
			List<OrderForm> notSuccOrList = new ArrayList<OrderForm>();
			List<OrderForm> failList = new ArrayList<OrderForm>();
			int succcount = 0;
			int count = 0;
			int failcount = 0;
			for(String orderinfo : orders)
			{
				String oss[] = orderinfo.split("[\\s]+");
				if(oss != null && oss.length == 3)
				{
					count++;
					
					String username = oss[0];
					if(username == null)
					{
						username = "";
					}
					else
					{
						username = username.trim();
					}
					
					String orderId = oss[1];
					if(orderId == null)
					{
						orderId = "";
					}
					else
					{
						orderId = orderId.trim();
					}
					
					String server = oss[2];
					if(server == null)
					{
						server = "";
					}
					else
					{
						server = server.trim();
					}
				
					OrderForm orForm = OrderFormManager.getInstance().getOrderForm(orderId);
					PassportManager passportManager = PassportManager.getInstance();
					if(orForm != null)
					{
						String oldServerName = orForm.getServerName();
						orForm.setServerName(server);
						OrderFormManager.getInstance().update(orForm);
						
						out.println("[查找订单] [成功] [不补单,考虑手工补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"] [old servername:"+oldServerName+"] [now servername："+server+"]<br>");
						succOrderList.add(orForm);
					}
					else
					{
						out.println("[查找订单] [失败] [无对应订单] ["+orderId+"]");
					}
				}
			}
			
			
			out.print("<h2>成功更新订单:"+succcount+",补偿订单失败:"+failcount+",总订单数:"+count+"</h2>");
			for(OrderForm or:succOrderList)
			{
				if(or != null)
				{
					out.println("wangxian orderid:"+or.getOrderId()+"<br/>");
				}
			}
			
			out.print("<h2>未更新订单</h2>");
			for(OrderForm or:notSuccOrList)
			{
				if(or != null)
				{
					out.println("wangxian orderid:"+or.getOrderId()+"<br/>");
				}
			}
		}
	}
		
	
	
	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>批量更新订单服务器信息</h2>
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		订单号:<textarea rows="100" cols="100" name="orderinfos" value="<%=orderInfos%>"></textarea><br>
		渠道:<input type="text" name="channel" value="<%=channel%>"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
