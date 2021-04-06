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
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(orders != null && orders.length > 0)
	{
		if(isDo)
		{
			for(String orderinfo : orders)
			{
				String oss[] = orderinfo.split("[\\s]+");
				//1 orderid 2 充值金额
				ExceptionOrderForm eorderForm = new ExceptionOrderForm();
				String orderId = oss[0];
				eorderForm.setChannelOrderId(orderId);
				eorderForm.setChannel("UCSDK_MIESHI");
				eorderForm.setUserChannel("UCSDK_MIESHI");
				//设置金额
				long payAmount = Long.parseLong(oss[1]);
				eorderForm.setPayMoney(payAmount);
				//设置充值时间
				eorderForm.setResponseTime(System.currentTimeMillis());
				//设置充值结果
				eorderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
				//设置充值媒介
				eorderForm.setSavingMedium("UCSDK充值");
				//设置充值平台
				eorderForm.setSavingPlatform("UCSDK");
				
				ExceptionOrderFormDAOImpl daoImpl = ExceptionOrderFormDAOImpl.getInstance();
				daoImpl.saveNew(eorderForm);
				out.println("[开始构造ucsdk订单] [--] [id:"+eorderForm.getId()+"] [orderid:"+eorderForm.getOrderId()+"] [handleresult:"+eorderForm.getHandleResult()+"] [responseresult:"+eorderForm.getResponseResult()+"] [notified:"+eorderForm.isNotified()+"] [nofisucc:"+eorderForm.isNotifySucc()+"] [channel:"+eorderForm.getUserChannel()+"] [server:"+eorderForm.getServerName()+"] [充值媒介:"+eorderForm.getSavingMedium()+"] [金额:"+eorderForm.getPayMoney()+"] [channelorderid:"+eorderForm.getChannelOrderId()+"]<br>");
				PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调过，通知游戏服失败，已经设置要重新通知游戏服] [id:"+eorderForm.getId()+"] [orderid:"+eorderForm.getOrderId()+"] [handleresult:"+eorderForm.getHandleResult()+"] [responseresult:"+eorderForm.getResponseResult()+"] [notified:"+eorderForm.isNotified()+"] [nofisucc:"+eorderForm.isNotifySucc()+"] [channel:"+eorderForm.getUserChannel()+"] [server:"+eorderForm.getServerName()+"] [充值媒介:"+eorderForm.getSavingMedium()+"] [金额:"+eorderForm.getPayMoney()+"] [channelorderid:"+eorderForm.getChannelOrderId()+"]<br>");
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
		订单信息:<textarea rows="100" cols="100" name="orderinfos" value="<%=orderInfos%>"></textarea><br>
		渠道:<input type="text" name="channel" value=""><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
