<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.auction.Auction"%>
<%@page import="com.fy.engineserver.auction.service.AuctionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String action = request.getParameter("action");
	String pwd = request.getParameter("pwd");
	String addChannelName = request.getParameter("addChannelName");
	String removeChannelName = request.getParameter("removeChannelName");
	
	if (action != null && pwd.equals("5tgbnhy6")) {
		if ("add".equals(action)) {
			if(addChannelName == null || addChannelName.isEmpty()){
				out.print("请输入要增加的渠道串");
			}else{
				ChargeManager.getInstance().addCloseChannelList(addChannelName);	
				out.print("[增加的渠道:"+addChannelName+"] [限制渠道列表:"+ChargeManager.getInstance().limitChannelChargeList+"]");
			}
		}else if ("remove".equals(action)) {
			if(removeChannelName == null || removeChannelName.isEmpty()){
				out.print("请输入要删除的渠道串");
			}else{
				ChargeManager.getInstance().removeLimitChargeChannel(removeChannelName);
				out.print("[删除的渠道:"+addChannelName+"] [限制渠道列表:"+ChargeManager.getInstance().limitChannelChargeList+"]");
			}
		}
	}else{
		out.print("操作失败");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>limit</title>
</head>
<body>

添加限制渠道串
<form name='f1'>
	<input type="hidden" name="action" value="add">
	<input type="text" name="addChannelName">
	<input type="submit" value="确定">
</form>
<br>
删除限制渠道串
<form name='f1'>
	<input type="hidden" name="action" value="remove">
	<input type="text" name="removeChannelName">
	<input type="submit" value="确定">
</form>
<br>
</body>
</html>