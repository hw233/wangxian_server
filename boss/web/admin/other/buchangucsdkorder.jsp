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
			ExceptionOrderForm exceptionOrderForm = ExceptionOrderFormDAOImpl.getInstance().getByChannelOrderid(orderId, channel);
			if(exceptionOrderForm != null)
			{
				out.println("[查找异常订单] [成功] [可通过此页面补单] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getChannelOrderId()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [用户名:"+passportManager.getPassport(username)+"] [媒介:"+exceptionOrderForm.getSavingMedium()+"] [mediuminfo:"+exceptionOrderForm.getMediumInfo()+"] [订单状态:"+exceptionOrderForm.getResponseResult()+"]<br>");
				UCSDKSavingManager ucsdkSavingManager = UCSDKSavingManager.getInstance();
				
				Passport pp = passportManager.getPassport(username);
				if(pp == null)
				{
					out.println("[开始补偿订单] [失败] [没有找到对应用户] [用户名:"+username+"]<br>");
					return;
				}
				
				String orderid = ucsdkSavingManager.sdkSaving(pp, exceptionOrderForm.getChannelOrderId(), server, mobileos, channel);
				if(!StringUtils.isEmpty(orderid))
				{
					orForm = OrderFormManager.getInstance().getOrderForm(orderid);
					out.println("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					PlatformSavingCenter.logger.info("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					orForm.setResponseTime(exceptionOrderForm.getResponseTime());
					orForm.setMemo3("通过uc补单页面进行的补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
					orForm.setSavingMedium("UCSDK充值");
					orForm.setMediumInfo(exceptionOrderForm.getMediumInfo());
					orForm.setPayMoney(exceptionOrderForm.getPayMoney());
					orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					orForm.setResponseResult(exceptionOrderForm.getResponseResult());
					orForm.setResponseDesp(exceptionOrderForm.getResponseDesp());
					OrderFormManager.getInstance().update(orForm);
					out.println("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					PlatformSavingCenter.logger.info("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
					ExceptionOrderFormDAOImpl.getInstance().getEm().remove(exceptionOrderForm);	
					out.println("[[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"]<br>");
					PlatformSavingCenter.logger.info("[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"] [channelorderid:"+exceptionOrderForm.getChannelOrderId()+"]<br>");
				}
				else
				{
					out.println("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [exceptionorderid:"+exceptionOrderForm.getChannelOrderId()+"] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"] [exceptionorderresult:"+exceptionOrderForm.getResponseResult()+"]<br>");
					PlatformSavingCenter.logger.info("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [exceptionorderid:"+exceptionOrderForm.getChannelOrderId()+"] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"] [exceptionorderresult:"+exceptionOrderForm.getResponseResult()+"]<br>");
				}
				
			}
			else
			{
				out.println("[查找异常订单] [失败] [订单id:"+orderId+"] [渠道:"+channel+"]<br>");
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
		手机操作系统:<input type="text" name="mobileos"><br>
		渠道:<input type="text" name="channel"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
