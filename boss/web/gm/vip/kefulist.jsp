<%@page import="com.fy.boss.vip.platform.CustomServicerManager"%>
<%@page import="com.fy.boss.vip.platform.model.CustomServicer"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
<%@page import="com.xuanzhi.tools.authorize.RoleManager"%>
<%@page import="com.xuanzhi.tools.authorize.Role"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.boss.vip.platform.VipPlayerInfoManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="org.apache.commons.codec.language.bm.BeiderMorseEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	 <link rel='stylesheet' type='text/css' href='../css/jquery-ui.css' />
	<script type='text/javascript' src='../js/jquery-1.9.1.min.js'></script>
	<script type='text/javascript' src='../js/jquery-ui-1.10.3.min.js'></script>

</head>
<body>
	<h1>客服资料列表</h1>
	<table border="1px">
		<tr>
			<td>
				序列号
			</td>
			<td>
				gm后台登陆账号
			</td>
			<td>
				昵称
			</td>
			<td>
				操作
			</td>
		</tr>
<%
	String sql = "";
	int sum = 0;
	
		
	List<CustomServicer> lst = CustomServicerManager.getInstance().queryForWhere("", new Object[0]);
	
	if(lst != null)
	{
		for(CustomServicer  customServicer : lst)
		{
	
	%>
					<tr>
						<td>
							<%=customServicer.getId() %>
						</td>	
						<td>
							<%=customServicer.getLoginName()%>
						</td>	
						<td>
							<%=customServicer.getNickname()%>
						</td>	
						<td>
							<a href="xiugaikefu.jsp?id=<%=customServicer.getId() %>">查看/修改</a>
						</td>	
					</tr>	
				
	<%
				sum += 1;
		}
	}
	
	%>
		<tr>
						<td>
							总计
						</td>	
						<td>
							<%=sum%>条记录
						</td>	
						<td>
							<div style="display:none;">
							</div>
						</td>	
						<td>
						</td>	
		</tr>
	
		</table>

</body>
</html>
