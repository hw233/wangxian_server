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
	//订单号 价格
	String orderInfos = request.getParameter("orderinfos");
	String orders[] = null;
	if(orderInfos != null)
	{
		orderInfos.trim();
		orders = orderInfos.split("\r*\n");
	}
	String mobileos = "unknown";
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(orders != null && orders.length > 0)
	{
		if(isDo)
		{
			List<OrderForm> succOrderList = new ArrayList<OrderForm>();
			List<OrderForm> notSuccOrList = new ArrayList<OrderForm>();
			List<String> failList = new ArrayList<String>();
			int succcount = 0;
			int count = 0;
			int failcount = 0;
			for(String orderinfo : orders)
			{
				
				count++;
				
				String orderId =orderinfo.trim();
				
				OrderForm orForm = OrderFormManager.getInstance().getOrderForm(orderId);
				PassportManager passportManager = PassportManager.getInstance();
				try
				{
					if(orForm != null)
					{
						orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orForm.setResponseTime(System.currentTimeMillis());
						orForm.setNotified(true);
						orForm.setNotifySucc(true);
						OrderFormManager.getInstance().update(orForm);
						String infos = "[查找订单] [成功] [开始补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]";
						out.println(infos+"<br/>");
						PlatformSavingCenter.logger.info(infos);
						succOrderList.add(orForm);
						succcount++;
					}
					else
					{
						String info = "[查找订单] [失败] [orderid:"+orderId+"]";
						out.println(info+"<br/>");
						PlatformSavingCenter.logger.info(info);
						failList.add(orderId);
						failcount++;
					}
				}
				catch(Exception e)
				{
					String infos = "[修改订单状态] [失败] [出现异常] [orderId:"+orderId+"]";
					out.print(infos+"<br/>");
					PlatformSavingCenter.logger.info(infos,e);
					failList.add(orderId);
					failcount++;
					
				}
			}
			
			
			out.print("<h2>成功补偿订单:"+succcount+",补偿订单失败:"+failcount+",总订单数:"+count+"</h2>");
			for(OrderForm or:succOrderList)
			{
				if(or != null)
				{
					out.println("orderid:"+or.getOrderId()+"<br/>");
				}
			}
			
			out.print("<h2>未补偿订单</h2>");
			for(String or:failList)
			{
				if(or != null)
				{
					out.println("orderid:"+or+"<br/>");
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
	<h2>批量补韩服订单</h2>
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		订单号:<textarea rows="100" cols="100" name="orderinfos" value="<%=orderInfos%>"></textarea><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
