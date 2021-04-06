<%@page import="com.fy.boss.vip.platform.CustomServicerManager"%>
<%@page import="com.fy.boss.vip.platform.model.CustomServicer"%>
<%@page import="com.fy.boss.vip.platform.VipPlayerInfoManager"%>
<%@page import="com.fy.boss.transport.BossServerService"%>
<%@page import="com.fy.boss.vip.platform.model.Descript"%>
<%@page import="com.fy.boss.vip.platform.model.VipPlayerInfoRecord"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="priv.jsp"%>     

<%
	if(level != 1)
	{
		out.println("<script>window.alert('无权限!');</script>");
		return;
	}


%>     
    
    
<% 
	String id = ParamUtils.getParameter(request, "id");
	boolean isDone = ParamUtils.getBooleanParameter(request, "isdone");
 	if(id != null)
	{
 		CustomServicer customServicer = CustomServicerManager.getInstance().find(Long.parseLong(id));
 		
 		if(isDone)
 		{
 			String loginName = ParamUtils.getParameter(request, "loginname", "");
 			String nickname = ParamUtils.getParameter(request, "nickname", "");
 			
 			boolean hasErr = false;
 			
 			if(loginName == null || loginName.trim().length() == 0)
 			{
 				out.println("<script>alert(登陆账号不能为空);</script>");
 				hasErr = true;
 			}
 			else if(nickname == null || nickname.trim().length() == 0)
 			{
 				out.println("<script>alert(昵称不能为空);</script>");
 				hasErr = true;
 			}
 			CustomServicerManager customServicerManager =  CustomServicerManager.getInstance();
 			//查找是否有登录名称重复的
 			if(!hasErr)
 			{
 				
 				customServicer.setLoginName(loginName);		
 				customServicer.setNickname(nickname);		
 				
 				customServicerManager.saveOrUpdate(customServicer);
 				
 				response.sendRedirect("kefulist.jsp");
 				return;
 			}
 			
 			

 		}
 		
 		
		
		if(customServicer != null)
		{
%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<title></title>
	<link rel='stylesheet' type='text/css' href='../css/jquery-ui.css' />
	<script type='text/javascript' src='../js/jquery-1.9.1.min.js'></script>
	<script type='text/javascript' src='../js/jquery-ui-1.10.3.min.js'></script>
	<style type="text/css">
	<!--
	body,td,th {
		font-size: 12px;
		font-family: Arial, Helvetica, sans-serif;
		font-weight: bold;
	}
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	.bai {color: #FFFFFF}
	-->
	</style>
	</head>
	<body>
		<div style="height:20px;clear:both" align="left">
			<h3>修改:</h3>
		</div>
		<div style="text-align: center;">
		<form method="post" action="modifyvipinfo.jsp">
		<input type="hidden" name="isdone" value="true"/>
		<input type="hidden" name="id" value="<%=customServicer.getId()%>"/>
			<div style="font-size: 15px;font-family: Arial, Helvetica, sans-serif;font-weight: bold; text-align: center;">客服信息修改</div>
			<div style="border:1px solid black;height:20px;width:600px;margin-left: auto; margin-right: auto;text-align: left">
				客服信息
			</div>
			gm后台登陆账号:<input type="text" name="loginname" value="<%=customServicer.getLoginName()%>" />&nbsp;&nbsp;&nbsp;&nbsp;
			昵称:<input type="text" name="nickname" value="<%=customServicer.getNickname()%>"  /><br/>
			<input type="submit" name="submit" value="修改" />
		</form>
		</div>
	</body>
	</html>

<%
		}
	}
%>