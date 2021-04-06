<%@page import="com.xuanzhi.tools.text.StringUtil"%>
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
	
	
	boolean buchangSucc = false;
	
	if(isDo)
	{
		String sec = ParamUtils.getParameter(request, "sec");
		String key = "@#SEWRdssq";
		String mysec = StringUtil.hash(orderIds+key);
		
		if(!mysec.equals(sec))
		{
			PlatformSavingCenter.logger.info("[开始补单] [失败] [出现非法的签名信息] ["+sec+"] ["+mysec+"]");
			return;
		}
		
		
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
			}
			PassportManager passportManager = PassportManager.getInstance();
			  /*
		  	已经在订单表中存在的 
		  
		  
		  */
		  
		  String reason = "通过批量补单页面进行补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			
			if(orForm != null)
			{
				
				if(orForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) //已经回调过的
				{
					if(orForm.getResponseResult() == OrderForm.RESPONSE_FAILED)
					{
					}
					else
					{
						
						if(orForm.isNotified() == true)
						{
							if(orForm.isNotifySucc() == false)
							{
								if(channel.contains("UCSDK"))
									orForm.setSavingMedium("UCSDK充值");
								orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								orForm.setResponseDesp(reason);
								orForm.setMemo3(null);
								orForm.setNotified(false);
								PlatformSavingCenter.logger.info("[开始补单] [--] [此订单被回调过，通知游戏服失败，已经设置要重新通知游戏服] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(orForm.getPassportId()).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]");
								buchangSucc = true;
							}
							else
							{
							}
						}
						else
						{
						}
					}
				}
				else //没有回调过的 
				{
				
				}
				
				
				OrderFormManager.getInstance().update(orForm);
				
			
			}
		}
		
		if(buchangSucc)
		{
			out.print("success");
		}
		else
		{
			out.print("fail");
		}
	}
	else
	{
		
	}
		
	
	

	
	
	
	
	

%>


