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
	String usernames = ParamUtils.getParameter(request, "usernames", "");

	if(isQuery)
	{
%>
		<table border="2px">
			<tr>
				<td>
				ID
				</td>
				<td>
				用户名
				</td>
				<td>
				第二个用户名
				</td>
				<td>
				注册渠道
				</td>
				<td>
				最近登陆渠道
				</td>
				<td>
				注册时间	
				</td>
				<td>
				最近登陆时间
				</td>
			</tr>
<% 
		String usernamess[] = usernames.split("\r*\n");
		for(String username : usernamess)
		{
			username = username.trim();
			//查询出用户名，昵称，注册渠道
			Passport p = PassportManager.getInstance().getPassport(username);
			if(p != null)
			{
		
	%>

	
			<tr>
				<td>
				<%=p.getId() %>
				</td>
				<td>
				<%=p.getUserName() %>
				</td>
				<td>
				<%=p.getNickName()%>
				</td>
				<td>
				<%=p.getRegisterChannel() %>
				</td>
				<td>
				<%=p.getLastLoginChannel() %>
				</td>
				<td>
				<%=DateUtil.formatDate(p.getRegisterDate(),"yyyy-MM-dd HH:mm:ss") %>	
				</td>
				<td>
				<%=DateUtil.formatDate(p.getLastLoginDate(),"yyyy-MM-dd HH:mm:ss") %>
				</td>
			</tr>

	<% 
			}
			else
			{
				%>
				<tr>
				<td colspan="7">
				无此用户("<%=username %>")
				</td>
				</tr>
				<%
			}
		
		}
%>
			</table>
<% 		
		
	}
	 
	
	
	
	
	
	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>根据输入的用户名获取昵称（用户有两个用户名）</h2>
	
	<form method="post">
		<input type="hidden" name="isQuery" value="true" />
<textarea rows="10" cols="10" name="usernames" value="<%=usernames%>"></textarea>
		<input type="submit" value="提交">
	</form>
	
</body>
</html>
