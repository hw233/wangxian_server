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
					
				
					
					String payMoneyStr = oss[1];
					int payAmount = Integer.parseInt(payMoneyStr);
					
					
					String server = oss[2];
					if(server == null)
					{
						server = "";
					}
					else
					{
						server = server.trim();
					}
				
					OrderForm orForm = null;
					PassportManager passportManager = PassportManager.getInstance();
					if(orForm != null)
					{
						out.println("[查找订单] [成功] [不补单,考虑手工补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username)+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [channelorderid:"+orForm.getChannelOrderId()+"]<br>");
						notSuccOrList.add(orForm);
					}
					else
					{
						
							
							Passport pp = passportManager.getPassport(username);
							if(pp == null)
							{
								out.println("[开始补偿订单] [失败] [没有找到对应用户] [用户名:"+username+"]<br>");
								continue;
							}
							
							String orderid = 	PlatformSavingCenter.getInstance().cardSaving(pp, "", payAmount, "", "", server, channel, "", "", "");
							if(!StringUtils.isEmpty(orderid))
							{
								orForm = OrderFormManager.getInstance().getOrderForm(orderid);
								out.println("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
								PlatformSavingCenter.logger.info("[生成订单] [成功] [开始进行补单] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
								orForm.setResponseTime(System.currentTimeMillis());
								orForm.setMemo2("通过补单页面进行的补单，补单日期为"+DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
								orForm.setMemo3(null);
								orForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								orForm.setResponseResult(OrderForm.RESPONSE_SUCC);
								orForm.setResponseDesp("");
								OrderFormManager.getInstance().update(orForm);
								succcount++;
								out.println("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"]<br>");
								PlatformSavingCenter.logger.info("[开始补偿订单] [成功] [ok] [id:"+orForm.getId()+"] [orderid:"+orForm.getOrderId()+"] [handleresult:"+orForm.getHandleResult()+"] [responseresult:"+orForm.getResponseResult()+"] [notified:"+orForm.isNotified()+"] [nofisucc:"+orForm.isNotifySucc()+"] [channel:"+orForm.getUserChannel()+"] [server:"+orForm.getServerName()+"] [用户名:"+passportManager.getPassport(username).getUserName()+"] [充值媒介:"+orForm.getSavingMedium()+"] [金额:"+orForm.getPayMoney()+"] [成功数:"+succcount+"]<br>");
								succOrderList.add(orForm);
							}
							else
							{
								out.println("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"]<br>");
								PlatformSavingCenter.logger.info("[生成订单] [失败] [生成的订单id:"+orderid+"] [用户名:"+pp.getUserName()+"] [server:"+server+"] [mobileos:"+mobileos+"] [channel:"+channel+"]<br>");
								failcount++;
							}
							
					}
				}
			}
			
			
			out.print("<h2>成功补偿订单:"+succcount+",补偿订单失败:"+failcount+",总订单数:"+count+"</h2>");
			for(OrderForm or:succOrderList)
			{
				if(or != null)
				{
					out.println("uc orderid:"+or.getChannelOrderId()+" wangxian orderid:"+or.getOrderId()+"<br/>");
				}
			}
			
			out.print("<h2>未补偿订单</h2>");
			for(OrderForm or:notSuccOrList)
			{
				if(or != null)
				{
					out.println("uc orderid:"+or.getChannelOrderId()+" wangxian orderid:"+or.getOrderId()+"<br/>");
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
		订单信息(username  payAmount server):<textarea rows="100" cols="100" name="orderinfos" value="<%=orderInfos%>"></textarea>(用户名 金额(分) 服务器)<br>
		渠道:<input type="text" name="channel" value="<%=channel%>"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
