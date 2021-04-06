<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
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
	boolean isQuery = ParamUtils.getBooleanParameter(request, "isQuery");
	boolean isContain = ParamUtils.getBooleanParameter(request, "isQuery");
	String queryDay = ParamUtils.getParameter(request, "queryTime", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
	
	Date queryDate = DateUtil.parseDate(queryDay, "yyyy-MM-dd");
	long queryTime = queryDate.getTime();
	
	//获取指定日期的前一天时间
	 Calendar c = Calendar.getInstance();
	 c.setTime(queryDate);
	 int day =  c.get(Calendar.DATE);
	 c.set(Calendar.DATE,day+1);
	 long timeAfter = c.getTime().getTime();
	
	 
	
	
	
	
	
	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>查询订单</h2>
	
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
				渠道订单号(channelorderid)
			</td>
		</tr>
	<%
	int count = 0;
	long sumMoney= 0l;
	if(isQuery)
	{
		out.println(isContain);
		long[] oids = isContain?OrderFormManager.getInstance().getEm().queryIds(OrderForm.class, "responseResult <> "+OrderForm.RESPONSE_SUCC+" and createTime >= " + queryTime + " and createTime < " + timeAfter  ):OrderFormManager.getInstance().getEm().queryIds(OrderForm.class, "responseResult="+OrderForm.RESPONSE_FAILED +" and createTime >= " + queryTime + " and createTime < " + timeAfter);
		
		
		for(long oiid : oids)
		{
			OrderForm o = OrderFormManager.getInstance().getOrderForm(oiid);
			if(o != null)
			{
				count++;
				sumMoney += o.getPayMoney();
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
				<%=o.getChannelOrderId() %>
			</td>		
		</tr>	
	<%
			}
		}
	}
	
	%>
		<tr>
			<td>
				总数:<%=count%>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
				总金额(paymoney):<%=sumMoney %>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
		</tr>	
		</table>
	<form method="post">
		<input type="hidden" name="isQuery" value="true" />
		日期:<input type="text" name="queryTime" value=""><br>
		<input type="checkbox" name="isContain" value="true" />是否包含未响应订单
		<input type="submit" value="提交">
	</form>
	
</body>
</html>
