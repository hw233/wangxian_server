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
	String orderId = ParamUtils.getParameter(request, "orderid", "000");
	String username = ParamUtils.getParameter(request, "username", "").trim();
	String server = ParamUtils.getParameter(request, "server", "unkown").trim();
	String mobileos = ParamUtils .getParameter(request, "mobileos", "empty");
	String channel = ParamUtils.getParameter(request, "channel", "unkown").trim();
	String payMoney = ParamUtils.getParameter(request, "paymoney","0").trim();
	
	
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(isDo)
	{
		OrderForm orForm = OrderFormManager.getInstance().getByChannelOrderid(orderId, channel);
		PassportManager passportManager = PassportManager.getInstance();
		if(orForm != null)
		{
			out.println("[查找订单] [成功] [不补单,考虑手工补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
		
		}
		else
		{
				UCSDKSavingManager ucsdkSavingManager = UCSDKSavingManager.getInstance();
				
				Passport pp = passportManager.getPassport(username);
				if(pp == null)
				{
					out.println("[开始补偿订单] [失败] [没有找到对应用户] [用户名:"+username+"]<br>");
					return;
				}
				
				String orderid = ucsdkSavingManager.sdkSaving(pp, orderId, server, mobileos, channel);
				if(!StringUtils.isEmpty(orderid))
				{
					orForm = OrderFormManager.getInstance().getOrderForm(orderid);
					out.println("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					PlatformSavingCenter.logger.info("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					orForm.setResponseTime(System.currentTimeMillis());
					//orForm.setMemo3("通过uc补单页面进行的补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
					orForm.setSavingMedium("UCSDK充值");
					orForm.setMediumInfo("");
					orForm.setPayMoney(Long.valueOf(payMoney)*100);
					orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orForm.setResponseResult(OrderForm.RESPONSE_SUCC);
					OrderFormManager.getInstance().update(orForm);
					out.println("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					PlatformSavingCenter.logger.info("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]");
				}
				else
				{
					out.println("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [exceptionorderid:--] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"] ["+payMoney+"]<br>");
					PlatformSavingCenter.logger.info("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"] [paymoney:"+payMoney+"]");
				}
				
		}
	}
		
	
	
	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>补ucsdk订单</h2>
	<form method="post">
	<input type="hidden" name="isDo" value="true">
		UC订单号:<input type="text" name="orderid"><br>
		用户名:<input type="text" name="username"><br>
		服务器名:<input type="text" name="server"><br>
		金额:<input type="text" name="paymoney"><br>
		手机操作系统:<input type="text" name="mobileos"><br>
		渠道:<input type="text" name="channel"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
