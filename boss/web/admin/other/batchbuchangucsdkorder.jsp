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
	String orderIds = ParamUtils.getParameter(request, "orderid", "000");
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	String channel = ParamUtils.getParameter(request, "channel", "");
	
	boolean isQuery = ParamUtils.getBooleanParameter(request, "isQuery");
	
	String[] orderids =  orderIds.split("\r*\n");
	
	
	
	if(isDo)
	{
		String[] oids = orderIds.split("\r*\n");
		for(String orderId : oids)
		{
			if(orderId == null)
			{
				orderId = "";
			}
			
			OrderForm orForm = null;
			if(channel.contains("UCSDK"))
				orForm =OrderFormManager.getInstance().getByChannelOrderid(orderId.trim(), channel);
			else
			{
				orForm = OrderFormManager.getInstance().getOrderForm(orderId.trim());
				if(orForm == null)
				{
					try
					{
						orForm = OrderFormManager.getInstance().getOrderForm(Long.parseLong(orderId.trim()));
					}
					catch(Exception e)
					{
						out.println(e);
					}
				}
				
			}
			PassportManager passportManager = PassportManager.getInstance();
			  /*
		  	已经在订单表中存在的 
		  
		  
		  */
		  
		  String reason = "通过批量补单页面进行补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			
			if(orForm != null)
			{
				boolean buchangSucc = false;
				out.println("[查找订单] [成功] [开始补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
				if(orForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) //已经回调过的
				{
					if(orForm.getResponseResult() == OrderForm.RESPONSE_FAILED)
					{
						out.println("[开始补单] [--] [此订单被回调过，要从失败变为成功] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
						
						if(channel.contains("UCSDK"))
							orForm.setSavingMedium("UCSDK充值");
						orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orForm.setResponseResult( OrderForm.RESPONSE_SUCC);
						orForm.setResponseDesp(reason);
						orForm.setMemo2(reason);
						orForm.setMemo3(null);
						orForm.setNotified(false);
						out.println("[开始补单] [--] [此订单被回调过，已从失败变为成功] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
						PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调过，已从失败变为成功] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
						buchangSucc = true;
					}
					else
					{
						
						if(orForm.isNotified() == true)
						{
							if(orForm.isNotifySucc() == false)
							{
								out.println("[开始补单] [--] [此订单被回调过，通知游戏服失败，需要重新通知游戏服] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
								if(channel.contains("UCSDK"))
									orForm.setSavingMedium("UCSDK充值");
								orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								orForm.setResponseDesp(reason);
								orForm.setMemo3(null);
								orForm.setNotified(false);
								out.println("[开始补单] [--] [此订单被回调过，通知游戏服失败，已经设置要重新通知游戏服] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
								PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调过，通知游戏服失败，已经设置要重新通知游戏服] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
								buchangSucc = true;
							
							}
							else
							{
								out.println("[开始补单] [--] [此订单被回调过，并且不用补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
								PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调过，并且不用补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
								buchangSucc = true;
							}
						}
						else
						{
							if(orForm.getResponseResult() == OrderForm.RESPONSE_SUCC)
							{
								orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								orForm.setResponseDesp(reason);
								orForm.setMemo3(null);
								orForm.setNotified(false);
								out.println("[开始补单] [--] [此订单被回调过，但调用接口时超时或者失败 强制补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
							}
							
							out.println("[开始补单] [--] [此订单被回调但无通知  请注意此订单状态] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
							PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调但无通知  请注意此订单状态] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
							buchangSucc = true;
						}
					}
				}
				else //没有回调过的 
				{
					boolean isCanForce = false;
					String[] unlimitips = new String[]{"106.120.222.214"};
					for(String str : unlimitips)
					{
						if(str.trim().equals(request.getRemoteAddr()))
						{
							isCanForce = true;
							break;
						}
					}
					if(isCanForce)
					{
						orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orForm.setResponseResult(OrderForm.RESPONSE_SUCC);
						orForm.setResponseDesp("");
						orForm.setMemo3(null);
						orForm.setNotified(false);
						out.println("[开始补单] [--] [此订单未被回调过，强制补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[开始补单] [--] [此订单未被回调过，强制补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						buchangSucc = true; 
					}
					else
					{
						out.println("[开始补单] [--] [此订单未被回调过，强制补单] [失败] [出现非法ip想强制补单] [非法ip:"+request.getRemoteAddr()+"] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[开始补单] [--] [此订单未被回调过，强制补单] [失败] [出现非法ip想强制补单] [非法ip:"+request.getRemoteAddr()+"] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]");
						buchangSucc = false; 
					}
				
				}
				
				
				OrderFormManager.getInstance().update(orForm);
				
				if(buchangSucc = true)
				{
					ExceptionOrderForm exceptionOrderForm = ExceptionOrderFormDAOImpl.getInstance().getByChannelOrderid(orderId, channel);
					
					if(exceptionOrderForm != null)//如果在异常订单表中查到此订单 则进行删除
					{
						ExceptionOrderFormDAOImpl.getInstance().getEm().remove(exceptionOrderForm);	
						out.println("[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [channelorderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"]<br>");
					}
					else
					{
						out.println("[未找到异常订单] [不必删除]<br>");
					}
				}
				
				
			
			}
			else
			{
				/**
				未在订单表中 需要去补偿ucsdk页面进行补单
				*/
				
				
				out.println("<h2>未在订单表中查到相关订单，请去ucsdk页面<a href=\"buchangucsdkorder.jsp\">单独补偿ucsdk订单</a>进行单独补单</h2>[订单id:"+orderId+"] [渠道:"+channel+"]<br>");
			}
	/* 		else
			{
				ExceptionOrderForm exceptionOrderForm = ExceptionOrderFormDAOImpl.getInstance().getByChannelOrderid(orderId, channel);
				if(exceptionOrderForm != null)
				{
					out.println("[查找异常订单] [成功] [可通过此页面补单] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getChannelOrderId()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [媒介:"+exceptionOrderForm.getSavingMedium()+"] [mediuminfo:"+exceptionOrderForm.getMediumInfo()+"] [订单状态:"+exceptionOrderForm.getResponseResult()+"]<br>");
					UCSDKSavingManager ucsdkSavingManager = UCSDKSavingManager.getInstance();
					
					Passport pp = passportManager.getPassport(orForm.getPassportId()).getUserName();
					if(pp == null)
					{
						out.println("[开始补偿订单] [失败] [没有找到对应用户] [用户名:"+username+"]<br>");
						return;
					}
					
					String orderid = ucsdkSavingManager.sdkSaving(pp, exceptionOrderForm.getChannelOrderId(), server, mobileos, channel);
					if(!StringUtils.isEmpty(orderid))
					{
						orForm = OrderFormManager.getInstance().getOrderForm(orderid);
						out.println("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [channelorderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						orForm.setResponseTime(exceptionOrderForm.getResponseTime());
						orForm.setMemo3("通过uc补单页面进行的补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
						orForm.setSavingMedium("UCSDK充值");
						orForm.setMediumInfo(exceptionOrderForm.getMediumInfo());
						orForm.setPayMoney(exceptionOrderForm.getPayMoney());
						orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						orForm.setResponseResult(exceptionOrderForm.getResponseResult());
						orForm.setResponseDesp(exceptionOrderForm.getResponseDesp());
						OrderFormManager.getInstance().update(orForm);
						out.println("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
						ExceptionOrderFormDAOImpl.getInstance().getEm().remove(exceptionOrderForm);	
						out.println("[[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"]<br>");
						PlatformSavingCenter.logger.info("[删除异常订单] [成功] [ok] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"]<br>");
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
			} */
		}
		
		
		
		/* out.println("线程名称:"+Thread.currentThread().getName()+"<br>");
		long waitLong = 15000l;
		out.println("[线程名称:"+Thread.currentThread().getName()+"] [等候"+waitLong+"ms]<br>");
		Thread.currentThread().sleep(waitLong);
		out.println("[线程名称:"+Thread.currentThread().getName()+"] [等候结束]<br>"); 
		
		*/
		out.println("<br><br>");
		if(oids != null)
		{
			for(String orderid:oids)
			{
				if(orderid != null)
				{
					OrderForm orForm = OrderFormManager.getInstance().getByChannelOrderid(orderid, channel);
					PassportManager passportManager = PassportManager.getInstance();
					
					if(orForm != null)
					{
						out.println("<h3>[补单结果] ["+(orForm.isNotifySucc() == true?"成功":"失败")+"] [--] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]</h3><br>");
					}
					
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
	<h2>批量补订单，请只有确认成功后的订单才可以用此页面（即已经确认失败，但是后来又确认成功的）</h2>
	
	<table border="1px">
		<tr>
			<td>
				id
			</td>
			<td>
				orderId
			</td>
			<td>
				充值平台(savingplatform)
			</td>
			<td>
				充值介质(savingmedium)
			</td>
			<td>
				充值信息(mediuminfo)
			</td>
			<td>
				充值金额(paymoney)
			</td>
			<td>
				用户名(username)
			</td>
			<td>
				创建时间(createTime)
			</td>
			<td>
				提交结果(handleresult)
			</td>
			<td>
				提交描述(handleresultdesc)
			</td>
			<td>
				响应结果(responseresult)
			</td>
			<td>
				响应描述(responsedesc)
			</td>
			<td>
				响应时间(responseteime)
			</td>
			<td>
				是否通知(notified)
			</td>
			<td>
				通知是否成功(notifysucc)
			</td>
			<td>
				手机操作系统(memo1)
			</td>
			<td>
				充值渠道(userchannel)
			</td>
			<td>
				服务器名称(createTime)
			</td>
			<td>
				附注(memo3)
			</td>
			<td>
				附注(memo2)
			</td>
			<td>
				渠道订单号(channelorderid)
			</td>
		</tr>
	<%
	
	if(isQuery)
	{
		
		for(String oiid : orderids)
		{
			OrderForm o = OrderFormManager.getInstance().getOrderForm(oiid.trim());
			if(o == null)
			{
				try
				{
					o = OrderFormManager.getInstance().getOrderForm(Long.parseLong(oiid.trim()));
				}
				catch(Exception e)
				{
					out.println(e);
				}
			}
			
			
			if(o != null)
			{
	%>
		<tr>
			<td>
				<%=o.getId() %>
			</td>	
			<td>
				<%=o.getOrderId() %>
			</td>	
			<td>
				<%=o.getSavingPlatform()%>
			</td>	
			<td>
				<%=o.getSavingMedium() %>
			</td>	
			<td>
				<%=o.getMediumInfo() %>
			</td>	
			<td>
				<%=o.getPayMoney() %>
			</td>	
			<td>
				<%=PassportManager.getInstance().getPassport(o.getPassportId()).getUserName() %>
			</td>	
			<td>
				<%=DateUtil.formatDate(new Date(o.getCreateTime()), "yyyy-MM-dd HH:mm:ss") %>
			</td>	
			<td>
				<%
					String s = "";
					if(o.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC)
					{
						s = "成功";
					}
					else if(o.getHandleResult() == OrderForm.HANDLE_RESULT_NOBACK)
					{
						s = "等待回调";
					}
					else if(o.getHandleResult() == OrderForm.HANDLE_RESULT_FAILED)
					{
						s="失败";
					}
					else
					{
						s="未知状态";
					}
				
				%>
				<%=s%>
			</td>	
			<td>
				<%=o.getHandleResultDesp() %>
			</td>	
			<td>
			<%
					s = "";
					if(o.getResponseResult() == OrderForm.RESPONSE_SUCC)
					{
						s = "成功";
					}
					else if(o.getResponseResult() == OrderForm.RESPONSE_NOBACK)
					{
						s = "等待回调";
					}
					else if(o.getResponseResult() == OrderForm.RESPONSE_FAILED)
					{
						s="失败";
					}
					else
					{
						s="未知状态";
					}
				
				%>
				<%=s%>
			</td>	
			<td>
				<%=o.getResponseDesp() %>
			</td>	
			<td>
				<%=DateUtil.formatDate(new Date(o.getResponseTime()), "yyyy-MM-dd HH:mm:ss") %>
			</td>	
			<td>
				<%=o.isNotified() %>
			</td>	
			<td>
				<%=o.isNotifySucc() %>
			</td>	
			<td>	
				<%=o.getMemo1() %>
			</td>	
			<td>
				<%=o.getUserChannel() %>
			</td>	
			<td>
				<%=o.getServerName() %>
			</td>
			<td>
				<%=o.getMemo3() %>
			</td>
			<td>
				<%=o.getMemo2() %>
			</td>
			<td>
				<%=o.getChannelOrderId() %>
			</td>		
		</tr>	
	<%
			}
		}
	}
	
	%>
		</table>
	<form method="post">
		<input type="hidden" name="isQuery" value="true" />
		输入飘渺寻仙曲订单号查询订单状态:<textarea rows="20" cols="30" name="orderid" value="<%=orderIds%>"></textarea><br>
		<input type="submit" value="提交">
	</form>
	
	
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		输入飘渺寻仙曲订单号进行补单:<textarea rows="20" cols="30" name="orderid" value="<%=orderIds%>"></textarea><br>
		<!-- 渠道:<input type="text" name="channel" value="<%=channel%>"><br> -->
		<input type="submit" value="提交">
	</form>
</body>
</html>
