<%@page import="com.fy.boss.platform.anzhi.AnZhiSavingManager"%>
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
				if(oss != null)
				{
					count++;
					

					String orderId = oss[0];
					if(orderId == null)
					{
						orderId = "";
					}
					else
					{
						orderId = orderId.trim();
					}
					
					String payMoneyStr = oss[1];
					int payAmount = Integer.parseInt(payMoneyStr);
					
					String channelOrderId = oss[2];
					
					String username = oss[3];
					String server = oss[4];
				
					OrderForm orForm = null;
					orForm = OrderFormManager.getInstance().getByChannelOrderid(channelOrderId, channel);
					
					if(orForm != null)
					{
						out.println("[生成订单] [失败] [已经补过单了] [生成的订单id:"+orderId+"] [mobileos:"+mobileos+"] [channel:"+channel+"]<br>");
						return;
					}
					
					PassportManager passportManager = PassportManager.getInstance();
					if(StringUtils.isEmpty(orderId))
					{
						out.print("[补偿订单] [失败] [无订单id] ["+orderinfo+"]<br/>");
						return;
					}
					
					orForm = OrderFormManager.getInstance().getOrderForm(orderId);
					if(orForm != null)
					{
						
							Passport passport = passportManager.getPassport(orForm.getPassportId());						
							
							if(passport == null)
							{
								out.print("[补偿订单] [失败] [订单的passportid没有对应的passport] ["+orderinfo+"]<br/>");
								return;
							}
							
							if(!passport.getUserName().equals(username))
							{
								out.print("[补偿订单] [失败] [订单的username和输入的username不匹配] [passportusername:"+passport.getUserName()+"] [username:"+username+"] ["+orderinfo+"]<br/>");
								return;
							}
							
							if(!orForm.getServerName().equals(server))
							{
								out.print("[补偿订单] [失败] [订单的username和输入的username不匹配] [orformserver:"+orForm.getServerName()+"] [server:"+server+"] ["+orderinfo+"]<br/>");
								return;
							}
							
							out.println("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
							PlatformSavingCenter.logger.info("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"]  [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
							orForm.setResponseTime(System.currentTimeMillis());
							orForm.setMemo2("通过补单页面进行的补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
							orForm.setMemo3(null);
							if(orForm.getPayMoney() == 0)
							{
								orForm.setPayMoney(payAmount);
							}
							
							orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							orForm.setResponseDesp("");
							orForm.setChannelOrderId(channelOrderId);
							OrderFormManager.getInstance().update(orForm);
							succcount++;
							out.println("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"]  [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
							PlatformSavingCenter.logger.info("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"]  [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [成功数:"+succcount+"]<br>");
							succOrderList.add(orForm);
						
						
					}
					
					else
					{
						
						out.println("[查找订单] [成功] [不补单,考虑手工补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
						notSuccOrList.add(orForm);
						
							
							
							
					}
				}
			}
			
			
			out.print("<h2>成功补偿订单:"+succcount+",补偿订单失败:"+failcount+",总订单数:"+count+"</h2>");
			for(OrderForm or:succOrderList)
			{
				if(or != null)
				{
					out.println("channel orderid:"+or.getChannelOrderId()+" wangxian orderid:"+or.getOrderId()+"<br/>");
				}
			}
			
			out.print("<h2>未补偿订单</h2>");
			for(OrderForm or:notSuccOrList)
			{
				if(or != null)
				{
					out.println("channel orderid:"+or.getChannelOrderId()+" wangxian orderid:"+or.getOrderId()+"<br/>");
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
	<h2>批量补ucsdk订单</h2>
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		订单信息(username  payAmount server):<textarea rows="100" cols="100" name="orderinfos" value="<%=orderInfos%>"></textarea>(orderId 金额(分) 渠道订单id)<br>
		渠道:<input type="text" name="channel" value="<%=channel%>"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
